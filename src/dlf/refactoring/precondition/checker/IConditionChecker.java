package dlf.refactoring.precondition.checker;
import dlf.refactoring.precondition.checker.environments.IRefactoringEnvironment;
import dlf.refactoring.precondition.checker.result.ICheckingResult;

public interface IConditionChecker {
	public ICheckingResult performChecking(IRefactoringEnvironment environment);
}