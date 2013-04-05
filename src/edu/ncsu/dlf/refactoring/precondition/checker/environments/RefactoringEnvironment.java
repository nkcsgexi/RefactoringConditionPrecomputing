package edu.ncsu.dlf.refactoring.precondition.checker.environments;

import org.eclipse.jdt.core.IJavaElement;

import edu.ncsu.dlf.refactoring.RefactoringType;

public class RefactoringEnvironment {
	
	private final RefactoringType refactoringType;
	private final IJavaElement element;
	
	public RefactoringEnvironment(IJavaElement element, RefactoringType refactoringType)
	{
		this.refactoringType = refactoringType;
		this.element = element;
	}
	
	public IJavaElement getJavaElement()
	{
		return this.element;
	}
	public RefactoringType getRefactoringType()
	{
		return this.refactoringType;
	}
}
