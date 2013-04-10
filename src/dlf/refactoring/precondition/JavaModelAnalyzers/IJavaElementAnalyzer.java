package dlf.refactoring.precondition.JavaModelAnalyzers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IParent;
import org.eclipse.jdt.core.JavaModelException;

import dlf.refactoring.precondition.util.ExpandOperations;
import dlf.refactoring.precondition.util.ListOperations;
import dlf.refactoring.precondition.util.XArrayList;
import dlf.refactoring.precondition.util.interfaces.IMapper;
import dlf.refactoring.precondition.util.interfaces.IPredicate;



public class IJavaElementAnalyzer {

	public static XArrayList<IJavaElement> convertArray2XArrayList(IJavaElement[] elements)
	{
		XArrayList<IJavaElement> list = new XArrayList<IJavaElement>();
		list.addAll(Arrays.asList(elements));
		return list;
	}
	
	public static List<IJavaElement> expandJavaElement(List<IJavaElement> input, 
			IMapper<IJavaElement, IJavaElement> mapper) throws Exception
	{
		ExpandOperations<IJavaElement, IJavaElement> expand = new ExpandOperations<IJavaElement, 
				IJavaElement>();
		return expand.expand(input, mapper);
	}
	
	public static List<IJavaElement> selectJavaElement(List<IJavaElement> elements, IPredicate
			<IJavaElement> predicate) throws Exception
	{
		 ListOperations<IJavaElement> selector = new ListOperations<IJavaElement>();
		 return selector.Select(elements, predicate);
	}
	
	
	public static List<IJavaElement> getAncestors(IJavaElement element, IPredicate<IJavaElement> 
		predicate) throws Exception
	{
		ArrayList<IJavaElement> results = new ArrayList<IJavaElement>();
		for(IJavaElement parent = element.getParent(); parent != null ;parent = parent.getParent())
		{
			if(predicate.IsTrue(parent))
			{
				results.add(parent);
			}
		}
		return results;
	}
	
	public static List<IJavaElement> getAncestorsByType(IJavaElement element, final int type) throws 
		Exception
	{
		return getAncestors(element, new IPredicate<IJavaElement>(){
			@Override
			public boolean IsTrue(IJavaElement t) throws Exception {
				return type == t.getElementType();
			}});
	}
	
	
	
}
