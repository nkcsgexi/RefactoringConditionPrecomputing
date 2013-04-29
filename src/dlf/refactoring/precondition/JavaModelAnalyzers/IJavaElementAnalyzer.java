package dlf.refactoring.precondition.JavaModelAnalyzers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Queue;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IParent;
import org.eclipse.jdt.core.JavaModelException;

import dlf.refactoring.precondition.util.ExpandOperations;
import dlf.refactoring.precondition.util.ListOperations;
import dlf.refactoring.precondition.util.XArrayList;
import dlf.refactoring.precondition.util.interfaces.IMapper;
import dlf.refactoring.precondition.util.interfaces.IPredicate;



public class IJavaElementAnalyzer {

	public static XArrayList<IJavaElement> convertArray2XArrayList(IJavaElement[] elements) {
		XArrayList<IJavaElement> list = new XArrayList<IJavaElement>();
		list.addAll(Arrays.asList(elements));
		return list;
	}
	
	public static List<IJavaElement> expandJavaElement(List<IJavaElement> input, 
			IMapper<IJavaElement, IJavaElement> mapper) throws Exception {
		ExpandOperations<IJavaElement, IJavaElement> expand = new ExpandOperations<IJavaElement, 
				IJavaElement>();
		return expand.expand(input, mapper);
	}
	
	public static List<IJavaElement> selectJavaElement(List<IJavaElement> elements, IPredicate
			<IJavaElement> predicate) throws Exception {
		 ListOperations<IJavaElement> selector = new ListOperations<IJavaElement>();
		 return selector.Select(elements, predicate);
	}
	
	
	public static XArrayList<IJavaElement> getAncestors(IJavaElement element, IPredicate<IJavaElement> 
		predicate) throws Exception {
		XArrayList<IJavaElement> results = new XArrayList<IJavaElement>();
		for(IJavaElement parent = element.getParent(); parent != null ;parent = parent.getParent())
		{
			if(predicate.IsTrue(parent)) {
				results.add(parent);
			}
		}
		return results;
	}
	
	public static XArrayList<IJavaElement> getDecendents(IJavaElement ancestor, 
			IPredicate<IJavaElement> lookChildren) throws Exception {
		XArrayList<IJavaElement> allChildren = new XArrayList<IJavaElement>();
		XArrayList<IJavaElement> needChildren = new XArrayList<IJavaElement>(new IJavaElement[]
				{ancestor}).where(lookChildren);
		while(!needChildren.empty()) {
			XArrayList<IJavaElement> newChildren = needChildren.select(new IMapper<IJavaElement, 
					IJavaElement>() {
				@Override
				public List<IJavaElement> map(IJavaElement t) throws Exception {
					return getChildren(t);
				}});
			allChildren.addAll(newChildren);
			needChildren = newChildren.where(lookChildren);
		}
		return allChildren;
	}
	
	public static XArrayList<IJavaElement> getDecendents(IJavaElement ancestor) throws Exception {
		return getDecendents(ancestor, new IPredicate<IJavaElement>(){
			@Override
			public boolean IsTrue(IJavaElement t) throws Exception {
				return true;
			}});
	}
	

	public static XArrayList<IJavaElement> getChildren(IJavaElement element) throws Exception {
		Object parent = element.getAdapter(IParent.class);
		return parent == null ? new XArrayList<IJavaElement>() : new XArrayList<IJavaElement>
			(((IParent) parent).getChildren()); 
	}
	
	
	public static XArrayList<IJavaElement> getAncestorsByType(IJavaElement element, final int type) 
			throws Exception {
		return getAncestors(element, new IPredicate<IJavaElement>() {
			@Override
			public boolean IsTrue(IJavaElement t) throws Exception {
				return type == t.getElementType();
			}});
	}
}
