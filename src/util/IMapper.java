package util;

import java.util.List;

public interface IMapper<T1, T2> {
	List<T2> map(T1 t) throws Exception;
}
