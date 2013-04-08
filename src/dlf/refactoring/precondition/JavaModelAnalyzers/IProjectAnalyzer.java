package dlf.refactoring.precondition.JavaModelAnalyzers;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;

public class IProjectAnalyzer {

	public static IPackageFragmentRoot[] getPackageFragmentRoots(IJavaProject project) throws 
		JavaModelException
	{
		return project.getPackageFragmentRoots();
	}
}
