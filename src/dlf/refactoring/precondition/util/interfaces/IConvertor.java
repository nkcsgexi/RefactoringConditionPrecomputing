package dlf.refactoring.precondition.util.interfaces;

public interface IConvertor<T, I> {
	I convert(T t) throws Exception;
}
