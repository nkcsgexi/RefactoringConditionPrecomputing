package dlf.refactoring.precondition.JavaModelAnalyzers;

import org.eclipse.jdt.core.IJavaElement;

public class IMemberAnalyzer {

	public static IJavaElement getContainingType(IJavaElement member) throws Exception {
		return IJavaElementAnalyzer.getAncestorsByType(member, IJavaElement.TYPE).get(0);
	}
	
}
