package javamodel.tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import util.ExpandCollection;
import util.IMapper;
import util.XLoggerFactory;

import JDTRefactoring.RenameAPIs;
import JavaModelAnalyzer.ICompilationUnitAnalyzer;
import JavaModelAnalyzer.IJavaElementUtils;
import JavaModelAnalyzer.IJavaModelAnalyzer;
import JavaModelAnalyzer.IMethodAnalyzer;
import JavaModelAnalyzer.IPackageFragmentAnalyzer;
import JavaModelAnalyzer.IPackageFragmentRootAnalyzer;
import JavaModelAnalyzer.IProjectAnalyzer;
import JavaModelAnalyzer.ITypeAnalyzer;

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
		sourcePackageRoots = TestUtils.getSourcePackageRoots(IJavaElementUtils.convertArray2List
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
	
	private void RenameOnElements(Collection<IJavaElement> elements, String newName) throws 
		Exception
	{
		long longestTime = 0;
		for(IJavaElement element : elements)
		{
			long startTime = System.currentTimeMillis();
			JavaRenameProcessor processor = RenameAPIs.getRenameProcessor(element);
			processor.setNewElementName(newName);
			// processor.isApplicable();
			RefactoringStatus results = processor.checkNewElementName(newName);
			results = processor.checkInitialConditions(new NullProgressMonitor());
			long endTime = System.currentTimeMillis();
			long time = endTime - startTime;
			if(time > longestTime)
			{
				longestTime = time;
			}
			logger.info("Checking " + element.getElementName() + ":" + time + "/" + longestTime);
		}
		logger.info("Element count:" + elements.size());
	}
	
	private List<IJavaElement> getAllTypes(List<IJavaElement> units) throws Exception
	{
		return IJavaElementUtils.expandJavaElement(units, new IMapper<IJavaElement, IJavaElement>(){
			@Override
			public List<IJavaElement> map(IJavaElement t)
					throws Exception {
				return ICompilationUnitAnalyzer.getTypes(t);
			}});
	}
	
	private List<IJavaElement> getAllFields(List<IJavaElement> types) throws Exception
	{
		return IJavaElementUtils.expandJavaElement(types, new IMapper<IJavaElement, IJavaElement>(){
			@Override
			public List<IJavaElement> map(IJavaElement t)
					throws Exception {
				return ITypeAnalyzer.getFields(t);
			}});
	}
	
	
	private List<IJavaElement> getAllMethods(List<IJavaElement> types) throws Exception
	{
		return IJavaElementUtils.expandJavaElement(types, new IMapper<IJavaElement, IJavaElement>(){
			@Override
			public List<IJavaElement> map(IJavaElement t)
					throws Exception {
				return ITypeAnalyzer.getMethods(t);
			}});
	}
	

	private List<IJavaElement> getAllParameters(List<IJavaElement> methods) throws Exception {
		return IJavaElementUtils.expandJavaElement(methods, new IMapper<IJavaElement, 
				IJavaElement>(){
			@Override
			public List<IJavaElement> map(IJavaElement t) throws Exception {
				return IMethodAnalyzer.getParameters(t);
			}});
	}
	
	
	@Test
	public void renameTypes() throws Exception
	{
		List<IJavaElement> allTypes = getAllTypes(this.compilationUnits);
		RenameOnElements(allTypes, "RandomTypeName");	
	}
	
	@Test
	public void renameFields() throws Exception
	{
		List<IJavaElement> allTypes = getAllTypes(this.compilationUnits);
		List<IJavaElement> allFields = getAllFields(allTypes);
		RenameOnElements(allFields, "RandomFieldName");
	}
	
	@Test
	public void renameMethods() throws Exception
	{
		List<IJavaElement> allTypes = getAllTypes(this.compilationUnits);
		List<IJavaElement> allMethods = getAllMethods(allTypes);
		RenameOnElements(allMethods, "RandomMethodName");
	}
	
	@Test
	public void renameParameters() throws Exception
	{
		List<IJavaElement> allTypes = getAllTypes(compilationUnits);
		List<IJavaElement> allMethods = getAllMethods(allTypes);
		List<IJavaElement> allParameters = getAllParameters(allMethods);
		RenameOnElements(allParameters, "RandomParameterName");
	}


	
}
