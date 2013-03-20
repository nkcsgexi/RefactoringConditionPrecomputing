package javamodel.tests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.junit.Before;
import org.junit.BeforeClass;

import edu.ncsu.dlf.refactoring.precondition.JavaModelAnalyzers.ICompilationUnitAnalyzer;
import edu.ncsu.dlf.refactoring.precondition.JavaModelAnalyzers.IJavaElementAnalyzers;
import edu.ncsu.dlf.refactoring.precondition.JavaModelAnalyzers.IJavaModelAnalyzer;
import edu.ncsu.dlf.refactoring.precondition.JavaModelAnalyzers.IPackageFragmentAnalyzer;
import edu.ncsu.dlf.refactoring.precondition.JavaModelAnalyzers.IPackageFragmentRootAnalyzer;
import edu.ncsu.dlf.refactoring.precondition.JavaModelAnalyzers.IProjectAnalyzer;
import edu.ncsu.dlf.refactoring.precondition.util.ExpandCollection;
import edu.ncsu.dlf.refactoring.precondition.util.interfaces.IMapper;


public class RefactoringExperiment {

	protected Logger logger;
	protected int projectIndex;
	protected IJavaElement project;
	protected List<IJavaElement> sourcePackageRoots;
	protected List<IJavaElement> sourcePackages;
	protected List<IJavaElement> compilationUnits;
	protected List<IJavaElement> types;
	
	@Before
	public void createEnvironment() throws Exception
	{
		this.sourcePackages = new ArrayList<IJavaElement>();
		this.project = IJavaModelAnalyzer.getCurrentJavaProjects()[projectIndex];
		IPackageFragmentRoot[] packageRoots = IProjectAnalyzer.getPackageFragmentRoots
				((IJavaProject) project);
		this.sourcePackageRoots = TestUtils.getSourcePackageRoots(IJavaElementAnalyzers.
				convertArray2List(packageRoots));
		for(IJavaElement fragment : sourcePackageRoots)
		{
			Collection<IJavaElement> packages = IPackageFragmentRootAnalyzer.getSourcePackages 
					((IPackageFragmentRoot) fragment);
			this.sourcePackages.addAll(packages);
		}
		
		compilationUnits = (new ExpandCollection<IJavaElement, IJavaElement>()).expand
			(sourcePackages, new IMapper<IJavaElement, IJavaElement>(){
				@Override
				public List<IJavaElement> map(IJavaElement t) throws Exception {
					return IPackageFragmentAnalyzer.getICompilationUnits(t);
				}
		});
		this.types = getAllTypes(this.compilationUnits);
	}
	
	
	protected List<IJavaElement> getAllTypes(List<IJavaElement> units) throws Exception
	{
		return IJavaElementAnalyzers.expandJavaElement(units, new IMapper<IJavaElement, IJavaElement>(){
			@Override
			public List<IJavaElement> map(IJavaElement t)
					throws Exception {
				return ICompilationUnitAnalyzer.getTypes(t);
			}});
	}
	
}
