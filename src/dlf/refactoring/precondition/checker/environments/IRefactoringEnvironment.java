package dlf.refactoring.precondition.checker.environments;

import org.eclipse.jdt.core.IJavaElement;

import dlf.refactoring.enums.RefactoringType;
import dlf.refactoring.enums.interfaces.IHasRefactoringType;


public interface IRefactoringEnvironment extends IHasRefactoringType{
	@Override
	boolean equals(Object other);
}