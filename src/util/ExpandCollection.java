package util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ExpandCollection<I, T> {
	public List<T> expand(List<I> input, IMapper<I,T> mapper) throws Exception
	{
		List<T> results = new ArrayList<T>();
		for(I i : input)
		{
			results.addAll(mapper.map(i));
		}
		return results;
	}
	
	public List<T> convert(List<I> input, IConvertor<I, T> convertor)
	{
		List<T> results = new ArrayList<T>();
		for(I i : input)
		{
			results.add(convertor.convert(i));
		}
		return results;
	}
}
