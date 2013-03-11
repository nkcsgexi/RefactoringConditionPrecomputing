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
import util.XSelector;

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
		 XSelector<IJavaElement> selector = new XSelector<IJavaElement>();
		 return selector.Select(elements, predicate);
	}
	
}
