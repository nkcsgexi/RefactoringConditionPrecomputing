package edu.ncsu.dlf.refactoring.precondition.util;

public interface IPredicate<T> {
	boolean IsTrue(T t) throws Exception;
}