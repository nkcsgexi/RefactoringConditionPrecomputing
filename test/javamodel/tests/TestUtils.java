package javamodel.tests;
import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;

import util.IPredicate;
import util.ListOperations;
import JavaModelAnalyzer.IPackageFragmentRootAnalyzer;


public class TestUtils {
	
	
	public static List<IJavaElement> getPackagesWithCompilationUnits(List<IJavaElement> 
			allPacks) throws Exception
	{
		ListOperations<IJavaElement> selector = new ListOperations<IJavaElement>();
		return selector.Select(allPacks, new IPredicate<IJavaElement>(){
			@Override
			public boolean IsTrue(IJavaElement t) throws Exception {
				return ((IPackageFragment) t).getCompilationUnits().length > 0;
			}});
	}
	
	
	public static List<IJavaElement> getSourcePackageRoots(List<IJavaElement> packages) 
			throws Exception
	{
		ListOperations<IJavaElement> selector = new ListOperations<IJavaElement>();
		return selector.Select(packages, new IPredicate<IJavaElement>(){
			@Override
			public boolean IsTrue(IJavaElement t) throws Exception {
				return IPackageFragmentRootAnalyzer.isSourceFragmentRoot((IPackageFragmentRoot) t);
			}});
	}
}
