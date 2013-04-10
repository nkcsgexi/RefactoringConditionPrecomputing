package dlf.refactoring.precondition.JavaModelAnalyzers;

import java.util.List;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

public class IMethodAnalyzer {

	public static List<IJavaElement> getParameters(IJavaElement m) throws Exception
	{
		IMethod method = (IMethod)m;
		return IJavaElementAnalyzer.convertArray2XArrayList(method.getParameters());
	}

	public static boolean isConstructor(IJavaElement t) throws Exception {
		IMethod method = (IMethod)t;
		return method.isConstructor();
	}
}
