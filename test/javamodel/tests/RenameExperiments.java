package javamodel.tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.refactoring.rename.JavaRenameProcessor;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.RenameRefactoring;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.ncsu.dlf.refactoring.RenameAPIs;
import edu.ncsu.dlf.refactoring.precondition.JavaModelAnalyzers.ICompilationUnitAnalyzer;
import edu.ncsu.dlf.refactoring.precondition.JavaModelAnalyzers.IJavaElementAnalyzers;
import edu.ncsu.dlf.refactoring.precondition.JavaModelAnalyzers.IJavaModelAnalyzer;
import edu.ncsu.dlf.refactoring.precondition.JavaModelAnalyzers.IMethodAnalyzer;
import edu.ncsu.dlf.refactoring.precondition.JavaModelAnalyzers.IPackageFragmentAnalyzer;
import edu.ncsu.dlf.refactoring.precondition.JavaModelAnalyzers.IPackageFragmentRootAnalyzer;
import edu.ncsu.dlf.refactoring.precondition.JavaModelAnalyzers.IProjectAnalyzer;
import edu.ncsu.dlf.refactoring.precondition.JavaModelAnalyzers.ITypeAnalyzer;
import edu.ncsu.dlf.refactoring.precondition.util.ExpandCollection;
import edu.ncsu.dlf.refactoring.precondition.util.XLoggerFactory;
import edu.ncsu.dlf.refactoring.precondition.util.interfaces.IMapper;
import edu.ncsu.dlf.refactoring.precondition.util.interfaces.IPredicate;



public class RenameExperiments {

	private final Logger logger;
	private final int projectIndex = 0;
	private IJavaElement project;
	private List<IJavaElement> sourcePackageRoots;
	private List<IJavaElement> sourcePackages;
	private List<IJavaElement> compilationUnits;
	
	public RenameExperiments()
	{
		this.logger = XLoggerFactory.GetLogger(this.getClass());
		this.sourcePackages = new ArrayList<IJavaElement>();
	}
	

	@Before
	public void createEnvironment() throws Exception
	{
		this.project = IJavaModelAnalyzer.getCurrentJavaProjects()[projectIndex];
		IPackageFragmentRoot[] packageRoots = IProjectAnalyzer.getPackageFragmentRoots
				((IJavaProject) project);
		sourcePackageRoots = TestUtils.getSourcePackageRoots(IJavaElementAnalyzers.convertArray2List
				(packageRoots));
		for(IJavaElement fragment : sourcePackageRoots)
		{
			Collection<IJavaElement> packages = IPackageFragmentRootAnalyzer.getSourcePackages 
					((IPackageFragmentRoot) fragment);
			sourcePackages.addAll(packages);
		}
		
		compilationUnits = (new ExpandCollection<IJavaElement, IJavaElement>()).expand
			(sourcePackages, new IMapper<IJavaElement, IJavaElement>(){
				@Override
				public List<IJavaElement> map(IJavaElement t) throws Exception {
					return IPackageFragmentAnalyzer.getICompilationUnits(t);
				}
		}); 
	}
	
	@Test
	public void method1()
	{
		Assert.isNotNull(project);
		Assert.isNotNull(sourcePackageRoots);
		Assert.isNotNull(sourcePackages);
		Assert.isNotNull(compilationUnits);
		
		Assert.isTrue(!sourcePackageRoots.isEmpty());
		Assert.isTrue(!sourcePackages.isEmpty());
		Assert.isTrue(!compilationUnits.isEmpty());
	}
	
	@SuppressWarnings("unchecked")
	private void RenameOnElements(Collection<IJavaElement> elements, String newName, int maxCount) 
			throws Exception
	{
		PriorityQueue<RenamableEntity> queue = new PriorityQueue<RenamableEntity>(maxCount, new 
				Comparator<RenamableEntity>(){
			@Override
			public int compare(RenamableEntity arg0, RenamableEntity arg1) {
				return (int)(arg0.timeInMilli - arg1.timeInMilli);
			}});
		
		
		for(IJavaElement element : elements)
		{
			// Measure the time of 
			long startTime = System.currentTimeMillis();
			JavaRenameProcessor processor = RenameAPIs.getRenameProcessor(element);
			processor.setNewElementName(newName);
			RenameRefactoring refactoring = RenameAPIs.getRenameRefactoring(processor);
			RefactoringStatus results = refactoring.checkAllConditions(new NullProgressMonitor());
			long endTime = System.currentTimeMillis();
			long time = endTime - startTime;
			
			// Create a renamable enetity to memorize related information.
			RenamableEntity entity = new RenamableEntity();
			entity.element = element;
			entity.timeInMilli = time;
			
			// Add the enetity to the queue and remove the currently smallest one if the size is 
			// larger than maximum.
			queue.add(entity);
			if(queue.size() == maxCount)
			{
				queue.remove();
			}	
		}
		
		logRenamableEntities(queue);
	}
	
	private void logRenamableEntities(PriorityQueue<RenamableEntity> queue) throws Exception
	{
		for(;!queue.isEmpty();)
		{
			RenamableEntity entity = queue.remove();
			String packageName = getPackageName(entity.element);
			String compilationUnitName = getCompilationUnitName(entity.element);
			logger.info("Check " + entity.element.getElementName() + " in " + packageName + " " + 
					compilationUnitName + ":" + entity.timeInMilli);
		}
	}
	

	private class RenamableEntity
	{
		IJavaElement element;
		long timeInMilli;
	}
	
	
	private String getPackageName(IJavaElement element) throws Exception
	{
		List<IJavaElement> packs = IJavaElementAnalyzers.getAncestors(element, new IPredicate
				<IJavaElement>(){
			@Override
			public boolean IsTrue(IJavaElement t) throws Exception {
				return t.getElementType() == IJavaElement.PACKAGE_FRAGMENT;
			}});
		return packs.get(0).getElementName();
	}
	
	
	private String getCompilationUnitName(IJavaElement element) throws Exception {
		List<IJavaElement> units = IJavaElementAnalyzers.getAncestors(element, new IPredicate
				<IJavaElement>(){
			@Override
			public boolean IsTrue(IJavaElement t) throws Exception {
				return t.getElementType() == IJavaElement.COMPILATION_UNIT;
			}});
		return units.get(0).getElementName();
	}

	
	
	private List<IJavaElement> getAllTypes(List<IJavaElement> units) throws Exception
	{
		return IJavaElementAnalyzers.expandJavaElement(units, new IMapper<IJavaElement, IJavaElement>(){
			@Override
			public List<IJavaElement> map(IJavaElement t)
					throws Exception {
				return ICompilationUnitAnalyzer.getTypes(t);
			}});
	}
	
	private List<IJavaElement> getAllFields(List<IJavaElement> types) throws Exception
	{
		return IJavaElementAnalyzers.expandJavaElement(types, new IMapper<IJavaElement, IJavaElement>(){
			@Override
			public List<IJavaElement> map(IJavaElement t)
					throws Exception {
				return ITypeAnalyzer.getFields(t);
			}});
	}
	
	
	private List<IJavaElement> getAllMethods(List<IJavaElement> types) throws Exception
	{
		return IJavaElementAnalyzers.expandJavaElement(types, new IMapper<IJavaElement, IJavaElement>(){
			@Override
			public List<IJavaElement> map(IJavaElement t)
					throws Exception {
				return ITypeAnalyzer.getMethods(t);
			}});
	}
	

	private List<IJavaElement> getAllParameters(List<IJavaElement> methods) throws Exception {
		return IJavaElementAnalyzers.expandJavaElement(methods, new IMapper<IJavaElement, 
				IJavaElement>(){
			@Override
			public List<IJavaElement> map(IJavaElement t) throws Exception {
				return IMethodAnalyzer.getParameters(t);
			}});
	}
	
	
	//@Test
	public void renameTypes() throws Exception
	{
		logger.info("Starting type names");
		List<IJavaElement> allTypes = getAllTypes(this.compilationUnits);
		RenameOnElements(allTypes, "RandomTypeName", 30);
		logger.info("Finishing type names");
	}
	
	//@Test
	public void renameFields() throws Exception
	{
		logger.info("Starting field names");
		List<IJavaElement> allTypes = getAllTypes(this.compilationUnits);
		List<IJavaElement> allFields = getAllFields(allTypes);
		RenameOnElements(allFields, "RandomFieldName", 30);
		logger.info("Finishing field names");
	}
	
	@Test
	public void renameMethods() throws Exception
	{
		logger.info("Starting method name.");
		List<IJavaElement> allTypes = getAllTypes(this.compilationUnits);
		List<IJavaElement> allMethods = getAllMethods(allTypes);
		RenameOnElements(allMethods, "RandomMethodName", 30);
		logger.info("Finishing method name");
	}
	
	//@Test
	public void renameParameters() throws Exception
	{
		logger.info("Starting parameter names.");
		List<IJavaElement> allTypes = getAllTypes(compilationUnits);
		List<IJavaElement> allMethods = getAllMethods(allTypes);
		List<IJavaElement> allParameters = getAllParameters(allMethods);
		RenameOnElements(allParameters, "RandomParameterName", 30);
		logger.info("Finishing parameter names.");
	}


	
}
