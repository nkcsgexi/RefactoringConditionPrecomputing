package dlf.refactoring.precondition.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import dlf.refactoring.precondition.util.interfaces.IOperation;
import dlf.refactoring.precondition.util.interfaces.IPredicate;


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
	
	public boolean exist(List<T> list, IPredicate<T> isIt) throws Exception
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
	
	public List<List<T>> getAllSublists(List<T> list)
	{
		ArrayList<List<T>> result = new ArrayList<List<T>>();
		for(int sublistLen = 1; sublistLen <= list.size() ; sublistLen ++)
		{
			for(int start = 0; start <= list.size() - sublistLen ; start ++)
			{
				// the toindex in the following method is exclusive.
				result.add(list.subList(start, start + sublistLen));
			}
		}
		return result;
	}
	
	public void operationOnElements(List<T> list, IOperation<T> d) throws Exception
	{
		for(T t : list)
		{
			d.perform(t);
		}
	}
}
