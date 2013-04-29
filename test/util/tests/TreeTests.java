package util.tests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Assert;
import org.junit.Test;

import dlf.refactoring.precondition.util.Tree;
import dlf.refactoring.precondition.util.TreeBuilder;
import dlf.refactoring.precondition.util.XLoggerFactory;
import dlf.refactoring.precondition.util.interfaces.IPredicate;
import dlf.refactoring.precondition.util.interfaces.Pair;



public class TreeTests {

	private Logger logger = XLoggerFactory.GetLogger(this.getClass());
	
	
	@Test
	public void method1()
	{
		Tree<Integer> tree = new Tree<Integer>(1);
		tree.addLeaf(1, 2);
		tree.addLeaf(1, 3);
		tree.addLeaf(2, 4);
		
		Tree<Integer> tree2 = new Tree<Integer>(4);
		tree2 = tree2.setAsParent(2);
		tree2 = tree2.setAsParent(1);
		tree2.addLeaf(3);

		Assert.isTrue(tree.getHead() == 1);
		Assert.isTrue(tree2.getHead() == 1);
		Collection<Integer> successors = tree.getSuccessors(1);
		Assert.isTrue(successors.contains(2));
		Assert.isTrue(successors.contains(3));
		Assert.isTrue(successors.size() == 2);
	}
	
	@Test
	public void method2() throws Exception
	{
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);
		TreeBuilder<Integer> builder = new TreeBuilder<Integer>(list, new IPredicate<Pair
				<Integer, Integer>>(){
			@Override
			public boolean IsTrue(Pair<Integer, Integer> t) throws Exception {
				return t.getFirst() > t.getSecond();
			}});
		Tree<Integer> tree = builder.createTree();
		System.out.println(tree);
	}
}
