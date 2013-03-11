package util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class XSelector<T> {
	
	public List<T> Select(List<T> array, IPredicate<T> predicate) throws Exception
	{
		List<T> selected = new ArrayList<T>();
		for(T t : array)
		{
			if(predicate.IsTrue(t))
			{
				selected.add(t);
			}
		}	
		return selected;
	}
}
