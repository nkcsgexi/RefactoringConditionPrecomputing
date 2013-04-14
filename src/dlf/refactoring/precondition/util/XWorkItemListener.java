package dlf.refactoring.precondition.util;

public interface XWorkItemListener {
	void beforeRunning(Runnable runnable);
	void afterRunning(Runnable runnable);
}
