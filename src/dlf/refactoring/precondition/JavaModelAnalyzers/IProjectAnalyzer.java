package dlf.refactoring.precondition.JavaModelAnalyzers;

import java.util.Arrays;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;

import dlf.refactoring.precondition.util.XArrayList;
import dlf.refactoring.precondition.util.interfaces.IPredicate;

public class IProjectAnalyzer {

	public static XArrayList<IJavaElement> getPackageFragmentRoots(IJavaElement project) throws 
		Exception
	{
		IPackageFragmentRoot[] packages = ((IJavaProject)project).getPackageFragmentRoots();
		XArrayList<IJavaElement> list = new XArrayList<IJavaElement>();
		list.addAll(Arrays.asList(packages));
		return list;
	}
	
	public static XArrayList<IJavaElement> getSourcePackageFragmentRoots(IJavaElement project) 
		throws Exception
	{
		return getPackageFragmentRoots(project).where(new IPredicate<IJavaElement>(){
			@Override
			public boolean IsTrue(IJavaElement t) throws Exception {
				return IPackageFragmentRootAnalyzer.isSourceFragmentRoot((IPackageFragmentRoot) t);
			}});
	}
	
}
