package javamodel.tests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;

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
import dlf.refactoring.precondition.util.XArrayList;
import dlf.refactoring.precondition.util.XLoggerFactory;
import dlf.refactoring.precondition.util.interfaces.IMapper;


public class RefactoringExperiment {
	
	protected final NullProgressMonitor monitor;
	protected final Logger logger;
	protected int projectIndex;
	protected IJavaElement project;
	protected XArrayList<IJavaElement> sourcePackageRoots;
	protected XArrayList<IJavaElement> sourcePackages;
	protected XArrayList<IJavaElement> compilationUnits;
	protected XArrayList<IJavaElement> typesElement;
	
	public RefactoringExperiment()
	{
		this.monitor = new NullProgressMonitor();
		this.logger = XLoggerFactory.GetLogger(this.getClass());
	}
	
	
	@Before
	public void createEnvironment() throws Exception
	{
		this.project = IJavaModelAnalyzer.getCurrentJavaProjects()[projectIndex];
		this.sourcePackageRoots = IProjectAnalyzer.getSourcePackageFragmentRoots(project);
		this.sourcePackages = this.sourcePackageRoots.select(new IMapper<IJavaElement, 
				IJavaElement>(){
			@Override
			public List<IJavaElement> map(IJavaElement t) throws Exception {
				return IPackageFragmentRootAnalyzer.getSourcePackages ((IPackageFragmentRoot) t);
			}});
		
		this.compilationUnits = this.sourcePackages.select(new IMapper<IJavaElement, 
				IJavaElement>(){
				@Override
				public List<IJavaElement> map(IJavaElement t) throws Exception {
					return IPackageFragmentAnalyzer.getICompilationUnits(t);
				}
		});
		this.typesElement = this.compilationUnits.select(new IMapper<IJavaElement, IJavaElement>(){
			@Override
			public List<IJavaElement> map(IJavaElement t) throws Exception {
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
