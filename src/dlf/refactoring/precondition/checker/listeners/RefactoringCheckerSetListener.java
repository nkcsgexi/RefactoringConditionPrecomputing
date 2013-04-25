package dlf.refactoring.precondition.checker.listeners;

import dlf.refactoring.precondition.checker.RefactoringCheckerSet;
import dlf.refactoring.precondition.util.TimedEventObject;

public interface RefactoringCheckerSetListener {
	public void performCheckingStart(TimedEventObject<RefactoringCheckerSet> event);
	public void performCheckingEnd(TimedEventObject<RefactoringCheckerSet> event);
	public void calculateEnvironmentStart(TimedEventObject<RefactoringCheckerSet> event);
	public void calculateEnvironmentEnd(TimedEventObject<RefactoringCheckerSet> event);
}
