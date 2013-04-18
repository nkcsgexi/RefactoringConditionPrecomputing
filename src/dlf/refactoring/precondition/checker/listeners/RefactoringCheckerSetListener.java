package dlf.refactoring.precondition.checker.listeners;

import dlf.refactoring.precondition.checker.RefactoringCheckerSet;

public interface RefactoringCheckerSetListener {
	public void performCheckingStart(RefactoringCheckerSet set);
	public void performCheckingEnd(RefactoringCheckerSet set);
	public void calculateEnvironmentStart(RefactoringCheckerSet set);
	public void calculateEnvironmentEnd(RefactoringCheckerSet set);
}
