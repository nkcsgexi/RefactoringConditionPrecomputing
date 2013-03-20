package util.tests;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.ncsu.dlf.refactoring.precondition.util.ListOperations;
import edu.ncsu.dlf.refactoring.precondition.util.interfaces.IOperation;

public class ListOperationTests {

	private final ListOperations<Integer> operations = new ListOperations<Integer>();
	private final List<Integer> list = new ArrayList<Integer>();

	
	@Test
	public void method1()
	{
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		
		List<List<Integer>> lists = operations.getAllSublists(list);
		
		Assert.isTrue(lists.size() > 0);
		
		for(List<Integer> l : lists)
		{
			operations.operationOnElements(l, new IOperation<Integer>(){
				@Override
				public void perform(Integer t) {
					System.out.print(t);
				}});
			System.out.println();
		}
	}
}
