package util.tests;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.junit.Test;

import util.IPredicate;
import util.ListOperations;

public class SelectorTests {

	
	@Test
	public void method1() throws Exception
	{
		List<Integer> num = new ArrayList<Integer>();
		num.add(1);
		num.add(2);
		num.add(3);
		ListOperations<Integer> selector = new ListOperations<Integer>();
		List<Integer> selected = selector.Select(num, new IPredicate<Integer>() {
			@Override
			public boolean IsTrue(Integer t) {
				return t < 2;
		}});
		Assert.isNotNull(selected);
		Assert.isTrue(selected.size() == 1);
		Assert.isTrue(selected.get(0) == 1);
	}
}

