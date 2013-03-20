package edu.ncsu.dlf.refactoring.precondition.JavaModelAnalyzers;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

public class ICompilationAnalyzer {
	
	public static IJavaElement getProject(ICompilationUnit iu)
	{
		return iu.getJavaProject();
	}
	
	public static IType[] getClasses(ICompilationUnit iu) throws JavaModelException
	{
		return iu.getAllTypes();
	}
	
}
