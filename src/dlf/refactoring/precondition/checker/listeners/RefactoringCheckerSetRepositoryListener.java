package dlf.refactoring.precondition.checker.listeners;

import dlf.refactoring.precondition.util.TimedEventObject;
import javaEventing.interfaces.Event;

public interface RefactoringCheckerSetRepositoryListener {
	void startCheckingContext(TimedEventObject event);
	void endCheckingContext(TimedEventObject event);
}
