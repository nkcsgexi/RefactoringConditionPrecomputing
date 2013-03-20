package edu.ncsu.dlf.refactoring.precondition.util;

import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import edu.ncsu.dlf.refactoring.precondition.util.interfaces.IPredicate;


public class TreeBuilder<T> {
	
	/* 
	 * Whether the first element is the ancestor of the second element.
	 * */
	private final IPredicate<Pair<T,T>> isAncestor;
	private final List<T> elements;
	private final ListOperations<T> operations;
	
	public TreeBuilder(List<T> elements, IPredicate<Pair<T,T>> isAncestor)
	{
		this.elements = elements;
		this.isAncestor = isAncestor;
		this.operations = new ListOperations<T>();
	}
	
	public Tree<T> createTree() throws Exception
	{
		List<T> roots = getRoots(elements);
		Tree<T> tree = initializeTree(roots);
		
		for(elements.removeAll(roots); elements.size() != 0; elements.removeAll(roots))
		{
			List<T> leafs = operations.clone(roots);
			roots = getRoots(elements);
			for(T node : roots)
			{
				addNode2Leaves(tree, leafs, node);
			}
		}
		return tree;
	}

	private Tree<T> initializeTree(List<T> roots)
			throws Exception {
		Tree<T> tree;
		if(roots.size() == 1)
		{
			 tree = new Tree<T>(roots.get(0));
		}
		else
		{
			throw new Exception("Tree has multiple roots.");
		}
		return tree;
	}

	private void addNode2Leaves(Tree<T> tree, List<T> leafs, T node) throws Exception 
	{
		List<T> parents = getAncestors(node, leafs);
		if(parents.size() == 1)
		{
			tree.addLeaf(parents.get(0), node);
		}
		else
		{
			throw new Exception("Multiple parents exist.");
		}
	}
	
	
	private List<T> getRoots(final List<T> list) throws Exception
	{
		return operations.Select(list, new IPredicate<T>(){
			@Override
			public boolean IsTrue(T t) throws Exception {
				List<T> others = ExceptElement(list, t);
				return !hasAncestors(t, others);
			}});
	}
	
	private boolean hasAncestors(final T t, List<T> list) throws Exception
	{
		return operations.Has(list, new IPredicate<T>(){
			@Override
			public boolean IsTrue(T l) throws Exception {
				return isAncestor.IsTrue(new Pair<T,T>(l, t));
			}});
	}
	
	private List<T> getAncestors(final T t, List<T> list) throws Exception
	{
		return operations.Select(list, new IPredicate<T>(){
			@Override
			public boolean IsTrue(T l) throws Exception {
				return isAncestor.IsTrue(new Pair<T,T>(l, t));
			}});
	}
	
	private List<T> ExceptElement(List<T> list, final T t) throws Exception
	{
		return operations.Select(list, new IPredicate<T>(){
			@Override
			public boolean IsTrue(T l) throws Exception {
				return l != t;
			}});
	}
}
