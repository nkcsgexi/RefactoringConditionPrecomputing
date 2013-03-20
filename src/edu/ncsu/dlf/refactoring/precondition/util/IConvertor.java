package edu.ncsu.dlf.refactoring.precondition.util;

public interface IConvertor<T, I> {
	I convert(T t);
}
