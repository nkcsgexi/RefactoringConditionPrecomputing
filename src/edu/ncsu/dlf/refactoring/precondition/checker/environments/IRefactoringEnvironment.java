package edu.ncsu.dlf.refactoring.precondition.checker.environments;

import org.eclipse.jdt.core.IJavaElement;

import edu.ncsu.dlf.refactoring.RefactoringType;
import edu.ncsu.dlf.refactoring.interfaces.IHasRefactoringType;

public interface IRefactoringEnvironment extends IHasRefactoringType{
	@Override
	boolean equals(Object other);
}
