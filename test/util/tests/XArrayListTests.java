package util.tests;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.ncsu.dlf.refactoring.precondition.util.XArrayList;
import edu.ncsu.dlf.refactoring.precondition.util.interfaces.IMapper;
import edu.ncsu.dlf.refactoring.precondition.util.interfaces.IPredicate;

public class XArrayListTests {

	private XArrayList<Integer> list;
	
	@Before
	public void prepare()
	{
		list = new XArrayList<Integer>();
		list.add(0);
		list.add(3);
		list.add(5);
		list.add(4);
	}
	
	@Test
	public void method1()
	{
		list = list.orderBy(new Comparator<Integer>(){
			@Override
			public int compare(Integer arg0, Integer arg1) {
				return arg0 - arg1;
			}});
		Assert.isTrue(list.size() == 4);
		Assert.isTrue(list.get(0) == 0);
		Assert.isTrue(list.get(1) == 3);
		Assert.isTrue(list.get(2) == 4);
		Assert.isTrue(list.get(3) == 5);
	}
	
	@Test
	public void method2() throws Exception
	{
		list = list.where(new IPredicate<Integer>(){
			@Override
			public boolean IsTrue(Integer t) throws Exception {
				return t < 3;
			}});
		
		Assert.isTrue(list.size() == 1);
		Assert.isTrue(list.get(0) == 0);
	}
	
	@Test
	public void method3() throws Exception
	{
		 XArrayList<String> list2 = list.select(new IMapper<Integer, String>(){
			@Override
			public List<String> map(Integer t) throws Exception {
				List<String> l = new ArrayList<String>();
				l.add(t.toString());
				return l;
			}});
		 Assert.isTrue(list2.size() == 4);
		 Assert.isTrue(list2.contains("0"));
		 Assert.isTrue(list2.contains("3"));
		 Assert.isTrue(list2.contains("4"));
		 Assert.isTrue(list2.contains("5"));
	}
	
	@Test
	public void method4() throws Exception 
	{
		Assert.isTrue(list.all(new IPredicate<Integer>(){
			@Override
			public boolean IsTrue(Integer t) throws Exception {
				return t > -1;
			}}));
		
		Assert.isTrue(list.exist(new IPredicate<Integer>(){
			@Override
			public boolean IsTrue(Integer t) throws Exception {
				return t > 3;
			}}));
	}
	
}
