package util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListOperations<T> {
	
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
	
	public boolean Has(List<T> list, IPredicate<T> isIt) throws Exception
	{
		for(T t : list)
		{
			if(isIt.IsTrue(t))
			{
				return true;
			}
		}
		return false;	
	}
	
	public List<T> clone(List<T> list)
	{
		List<T> newList = new ArrayList<T>();
		newList.addAll(list);
		return newList;
	}
}
