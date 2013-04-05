package edu.ncsu.dlf.refactoring.precondition.checker;
import edu.ncsu.dlf.refactoring.precondition.checker.environments.RefactoringEnvironment;

public interface IConditionChecker {
	public ICheckingResult performChecking(RefactoringEnvironment environment);
}