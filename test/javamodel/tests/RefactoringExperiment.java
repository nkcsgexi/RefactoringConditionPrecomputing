package javamodel.tests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.junit.Before;
import org.junit.BeforeClass;

import dlf.refactoring.precondition.JavaModelAnalyzers.ICompilationUnitAnalyzer;
import dlf.refactoring.precondition.JavaModelAnalyzers.IJavaElementAnalyzer;
import dlf.refactoring.precondition.JavaModelAnalyzers.IJavaModelAnalyzer;
import dlf.refactoring.precondition.JavaModelAnalyzers.IPackageFragmentAnalyzer;
import dlf.refactoring.precondition.JavaModelAnalyzers.IPackageFragmentRootAnalyzer;
import dlf.refactoring.precondition.JavaModelAnalyzers.IProjectAnalyzer;
import dlf.refactoring.precondition.util.ExpandOperations;
import dlf.refactoring.precondition.util.MathUtils;
import dlf.refactoring.precondition.util.interfaces.IMapper;



public class RefactoringExperiment {
	
	protected final NullProgressMonitor monitor;
	
	protected Logger logger;
	protected int projectIndex;
	protected IJavaElement project;
	protected List<IJavaElement> sourcePackageRoots;
	protected List<IJavaElement> sourcePackages;
	protected List<IJavaElement> compilationUnits;
	protected List<IJavaElement> types;
	
	public RefactoringExperiment()
	{
		this.monitor = new NullProgressMonitor();
	}
	
	
	@Before
	public void createEnvironment() throws Exception
	{
		this.sourcePackages = new ArrayList<IJavaElement>();
		this.project = IJavaModelAnalyzer.getCurrentJavaProjects()[projectIndex];
		IPackageFragmentRoot[] packageRoots = IProjectAnalyzer.getPackageFragmentRoots
				((IJavaProject) project);
		this.sourcePackageRoots = TestUtils.getSourcePackageRoots(IJavaElementAnalyzer.
				convertArray2List(packageRoots));
		for(IJavaElement fragment : sourcePackageRoots)
		{
			Collection<IJavaElement> packages = IPackageFragmentRootAnalyzer.getSourcePackages 
					((IPackageFragmentRoot) fragment);
			this.sourcePackages.addAll(packages);
		}
		
		compilationUnits = (new ExpandOperations<IJavaElement, IJavaElement>()).expand
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
		return IJavaElementAnalyzer.expandJavaElement(units, new IMapper<IJavaElement, IJavaElement>(){
			@Override
			public List<IJavaElement> map(IJavaElement t)
					throws Exception {
				return ICompilationUnitAnalyzer.getTypes(t);
			}});
	}
	
	protected List<IJavaElement> getRandomUnits(int count)
	{
		int start = MathUtils.getRondomInteger(0, this.compilationUnits.size() - count);
		return this.compilationUnits.subList(start, start + count);
	}
	
	protected long timeRefactoringChecking(Refactoring refactoring) throws Exception
	{
		long start = System.currentTimeMillis();
		refactoring.checkAllConditions(monitor);
		refactoring.createChange(monitor);
		return System.currentTimeMillis() - start;
	}
	
	
	
	
}
