package dlf.refactoring.precondition.JavaModelAnalyzers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;

import dlf.refactoring.precondition.util.Parser;
import dlf.refactoring.precondition.util.XArrayList;
import dlf.refactoring.precondition.util.interfaces.IPredicate;



public class ICompilationUnitAnalyzer {

	public static XArrayList<IJavaElement> getTypes(IJavaElement iu) throws Exception
	{
		ICompilationUnit unit = (ICompilationUnit) iu;
		XArrayList<IJavaElement> list = new XArrayList<IJavaElement>();
		list.addAll(Arrays.asList(unit.getTypes()));
		return list;
	}
	
	public static ASTNode parser(IJavaElement iu)
	{
		return Parser.Parse2ComilationUnit((ICompilationUnit)iu);
	}
	
	public static IJavaElement getPackageFragment(IJavaElement iu) throws Exception
	{
		List<IJavaElement> packs = IJavaElementAnalyzer.getAncestorsByType(iu, IJavaElement.
				PACKAGE_FRAGMENT);
		return packs.get(0);
	}
	
	public static IJavaElement getProject(IJavaElement iu)
	{
		return iu.getJavaProject();
	}
}
