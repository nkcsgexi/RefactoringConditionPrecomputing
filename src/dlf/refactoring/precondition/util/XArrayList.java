package dlf.refactoring.precondition.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import dlf.refactoring.precondition.util.interfaces.IConvertor;
import dlf.refactoring.precondition.util.interfaces.IIndexedIteration;
import dlf.refactoring.precondition.util.interfaces.IMapper;
import dlf.refactoring.precondition.util.interfaces.IOperation;
import dlf.refactoring.precondition.util.interfaces.IPredicate;

public class XArrayList<T> extends ArrayList<T> {

	private final ListOperations<T> listOp;

	public XArrayList() {
		super();
		this.listOp = new ListOperations<T>();
	}

	public XArrayList(Collection<? extends T> list) {
		super();
		this.listOp = new ListOperations<T>();
		this.addAll(list);
	}

	public XArrayList(T[] elements) {
		super();
		this.listOp = new ListOperations<T>();
		this.addAll(Arrays.asList(elements));
	}

	public XArrayList<T> where(IPredicate<T> predicate) throws Exception {
		return new XArrayList<T>(listOp.Select(this, predicate));
	}

	public boolean exist(IPredicate<T> predicate) throws Exception {
		return listOp.exist(this, predicate);
	}

	public boolean all(final IPredicate<T> predicate) throws Exception {
		return !listOp.exist(this, new IPredicate<T>() {
			@Override
			public boolean IsTrue(T t) throws Exception {
				return !predicate.IsTrue(t);
			}
		});
	}

	public void operateOnElement(IOperation<T> op) throws Exception {
		listOp.operationOnElements(this, op);
	}

	public <S> XArrayList<S> select(IMapper<T, S> mapper) throws Exception {
		List<S> tempList = new ArrayList<S>();
		for (T t : this) {
			tempList.addAll(mapper.map(t));
		}
		return new XArrayList<S>(tempList);
	}

	public <S> XArrayList<S> convert(IConvertor<T, S> convertor)
			throws Exception {
		List<S> tempList = new ArrayList<S>();
		for (T t : this) {
			tempList.add(convertor.convert(t));
		}
		return new XArrayList<S>(tempList);
	}

	public XArrayList<T> orderBy(Comparator<T> comparator) {
		ArrayList<T> tempList = new ArrayList<T>();
		tempList.addAll(this);
		Collections.sort(tempList, comparator);
		return new XArrayList<T>(tempList);
	}

	public T max(Comparator<T> comparator) {
		return Collections.max(this, comparator);
	}

	public T min(Comparator<T> comparator) {
		return Collections.min(this, comparator);
	}

	public T first(IPredicate<T> predicate) throws Exception {
		List<T> tempList = this.where(predicate);
		if (tempList.size() > 0) {
			return tempList.get(0);
		}
		return null;
	}

	public T last(IPredicate<T> predicate) throws Exception {
		List<T> tempList = this.where(predicate);
		if (tempList.size() > 0) {
			return tempList.get(tempList.size() - 1);
		}
		return null;
	}

	public boolean empty() {
		return this.size() == 0;
	}

	public boolean any() {
		return this.size() > 0;
	}

	public int count(IPredicate<T> pre) throws Exception {
		int count = 0;
		for (T t : this) {
			if (pre.IsTrue(t)) {
				count++;
			}
		}
		return count;
	}

	public Double sum(IConvertor<T, Double> convertor) throws Exception {
		Double all = 0.0;
		for (T t : this) {
			all += convertor.convert(t);
		}
		return all;
	}
	
	public void indexedLoop(IIndexedIteration<T> iteration) {
		for(int i = 0; i < this.size(); i ++) {
			iteration.iterate(this.get(i), i);
		}
	}
	
	public String toString(String delimit) {
		StringBuilder sb = new StringBuilder();
		for(T t : this) {
			sb.append(t.toString());
			sb.append(delimit);
		}
		return sb.toString();
	}

	public String toString() {
		return toString("\r\n");
	}
	
	
	
}
