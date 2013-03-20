package javamodel.tests;


import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.refactoring.rename.JavaRenameProcessor;
import org.eclipse.jdt.internal.corext.refactoring.structure.ExtractInterfaceProcessor;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.ncsu.dlf.refactoring.RenameAPIs;
import edu.ncsu.dlf.refactoring.StructuralRefactoringAPIs;
import edu.ncsu.dlf.refactoring.precondition.JavaModelAnalyzers.IJavaElementAnalyzers;
import edu.ncsu.dlf.refactoring.precondition.JavaModelAnalyzers.IJavaModelAnalyzer;
import edu.ncsu.dlf.refactoring.precondition.JavaModelAnalyzers.IPackageFragmentAnalyzer;
import edu.ncsu.dlf.refactoring.precondition.JavaModelAnalyzers.IPackageFragmentRootAnalyzer;
import edu.ncsu.dlf.refactoring.precondition.JavaModelAnalyzers.IProjectAnalyzer;
import edu.ncsu.dlf.refactoring.precondition.JavaModelAnalyzers.ITypeAnalyzer;
import edu.ncsu.dlf.refactoring.precondition.util.ListOperations;
import edu.ncsu.dlf.refactoring.precondition.util.XLoggerFactory;
import edu.ncsu.dlf.refactoring.precondition.util.interfaces.IPredicate;



public class JavaModelTests {

	private final Logger logger;
	
	private ICompilationUnit[] units;
	private List<IJavaElement> packages;
	private List<IJavaElement> sourcePackages;
	
	public JavaModelTests()
	{
		this.logger = XLoggerFactory.GetLogger(getClass());
	}
	
	
	@Before
	public void prepareCompilationUnit() throws Exception
	{
		IJavaProject project = IJavaModelAnalyzer.getCurrentJavaProjects()[0];
		IPackageFragmentRoot[] packageRoots = IProjectAnalyzer.getPackageFragmentRoots(project);
		Assert.isNotNull(packageRoots);
		Assert.isTrue(packageRoots.length > 0);
		
		// Get the packages for source code and assert there is at least one.
		List<IJavaElement> sourcePackageRoots = TestUtils.getSourcePackageRoots
				(IJavaElementAnalyzers.convertArray2List(packageRoots));
		Assert.isTrue(sourcePackageRoots.size() > 0);
		
		
		// Get the first package for source code and asserts it contains at least one instance of
		// ICompilationUnit.
		this.packages = IPackageFragmentRootAnalyzer.getSourcePackages
			((IPackageFragmentRoot) sourcePackageRoots.get(1));
		Assert.isTrue(packages.size() > 0);
		
		// Get the packages containing source files.
		this.sourcePackages = TestUtils.getPackagesWithCompilationUnits (packages);
		Assert.isTrue(sourcePackages.size() == 1);
		units = ((IPackageFragment) sourcePackages.get(0)).getCompilationUnits();
	}
	
	
	@Test
	public void method1() throws JavaModelException
	{
		IJavaProject[] projects = IJavaModelAnalyzer.getCurrentJavaProjects();
		Assert.isNotNull(projects);
		logger.info(projects.length);
		Assert.isTrue(projects.length == 1);
	}
	

	@Test
	public void method2() throws Exception
	{
		Assert.isNotNull(units);
		Assert.isTrue(units.length > 0);
		ICompilationUnit unit = units[0];
		IType[] types = unit.getTypes();
		Assert.isNotNull(types);
		Assert.isTrue(types.length > 0);
		IType type = types[0];
		Assert.isTrue(ITypeAnalyzer.getFields(type).size() > 0);
		Assert.isTrue(ITypeAnalyzer.getMethods(type).size() > 0);
		Assert.isTrue(ITypeAnalyzer.getSuperTypes(type).size() > 0);
	}
	
	
	@Test
	public void method3() throws Exception
	{
		String newElementName = "RenamedEntityWhyNot";
		IType type = units[0].getTypes()[0];
		Assert.isNotNull(type);
		JavaRenameProcessor processor = RenameAPIs.getRenameProcessor(type);
		Assert.isNotNull(processor);
		RefactoringStatus result = processor.checkNewElementName(newElementName);
		Assert.isTrue(result.isOK());
		result = processor.checkInitialConditions(new NullProgressMonitor());
		Assert.isTrue(result.isOK());
		processor.setNewElementName(newElementName);
	}
	
	@Test
	public void method4() throws Exception
	{
		IType type = units[0].getTypes()[0];
		Assert.isNotNull(type);
		ExtractInterfaceProcessor processor = StructuralRefactoringAPIs.
				createExtractInterfaceProcessor(type);
		Assert.isNotNull(processor);
		RefactoringStatus results = processor.checkInitialConditions(new NullProgressMonitor());
		Assert.isTrue(results.isOK());
		results = processor.checkFinalConditions(new NullProgressMonitor(), new 
				CheckConditionsContext());
		Assert.isTrue(results.hasFatalError());
		String message = results.getMessageMatchingSeverity(RefactoringStatus.FATAL);
		logger.fatal(message);
	}
}
