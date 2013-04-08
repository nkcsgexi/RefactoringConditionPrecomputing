package dlf.refactoring.precondition.checker.environments;

import org.eclipse.jdt.core.IJavaElement;

import dlf.refactoring.RefactoringType;
import dlf.refactoring.interfaces.IHasRefactoringType;


public interface IRefactoringEnvironment extends IHasRefactoringType{
	@Override
	boolean equals(Object other);
}
