package JavaModelAnalyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IParent;
import org.eclipse.jdt.core.JavaModelException;

import util.ExpandCollection;
import util.IMapper;
import util.IPredicate;
import util.ListOperations;

public class IJavaElementUtils {

	public static List<IJavaElement> convertArray2List(IJavaElement[] elements)
	{
		ArrayList<IJavaElement> list = new ArrayList<IJavaElement>();
		list.addAll(Arrays.asList(elements));
		return list;
	}
	
	public static List<IJavaElement> expandJavaElement(List<IJavaElement> input, 
			IMapper<IJavaElement, IJavaElement> mapper) throws Exception
	{
		ExpandCollection<IJavaElement, IJavaElement> expand = new ExpandCollection<IJavaElement, 
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
	
}
