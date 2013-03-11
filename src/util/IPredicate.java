package util;

public interface IPredicate<T> {
	boolean IsTrue(T t) throws Exception;
}