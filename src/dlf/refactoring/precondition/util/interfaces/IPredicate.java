package dlf.refactoring.precondition.util.interfaces;

public interface IPredicate<T> {
	boolean IsTrue(T t) throws Exception;
}