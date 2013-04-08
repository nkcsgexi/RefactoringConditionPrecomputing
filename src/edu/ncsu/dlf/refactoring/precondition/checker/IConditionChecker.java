package edu.ncsu.dlf.refactoring.precondition.checker;
import edu.ncsu.dlf.refactoring.precondition.checker.environments.IRefactoringEnvironment;
import edu.ncsu.dlf.refactoring.precondition.checker.result.ICheckingResult;

public interface IConditionChecker {
	public ICheckingResult performChecking(IRefactoringEnvironment environment);
}