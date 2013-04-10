package dlf.refactoring.precondition.JavaModelAnalyzers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;

import dlf.refactoring.precondition.util.ListOperations;
import dlf.refactoring.precondition.util.interfaces.IPredicate;



public class IPackageFragmentRootAnalyzer {
	
	/*
	 * Return whether this is a source package.
	 */
	public static boolean isSourceFragmentRoot(IPackageFragmentRoot root) throws JavaModelException
	{
		return (root.getKind() == IPackageFragmentRoot.K_SOURCE);
	}
	
	public static IJavaElement getPackageFragmentByName(IPackageFragmentRoot root, String name)
	{
		return root.getPackageFragment(name);
	}
	
	/*
	 * Get all the compilation unit element in a given package root.
	 */
	public static List<IJavaElement> getICompilationUnits(IPackageFragmentRoot root) throws 
		Exception
	{
		return getChildrenByType(root, IJavaElement.COMPILATION_UNIT);
	}
	
	
	public static List<IJavaElement> getSourcePackages(IPackageFragmentRoot root) throws 
		Exception
	{
		return getChildrenByType(root, IJavaElement.PACKAGE_FRAGMENT);
	}
	
	private static List<IJavaElement> getChildrenByType(IPackageFragmentRoot root, final int type) 
			throws Exception {
		ListOperations<IJavaElement> selector = new ListOperations<IJavaElement>();
		return selector.Select(IJavaElementAnalyzer.convertArray2XArrayList(root.getChildren()), new 
			IPredicate<IJavaElement>(){
				@Override
				public boolean IsTrue(IJavaElement t) {
					return t.getElementType() == type;
			}});
	}
	
	
	
}
