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



public class ICompilationUnitAnalyzer {

	public static XArrayList<IJavaElement> getTypes(IJavaElement cu) throws Exception
	{
		ICompilationUnit unit = (ICompilationUnit) cu;
		XArrayList<IJavaElement> list = new XArrayList<IJavaElement>();
		list.addAll(Arrays.asList(unit.getTypes()));
		return list;
	}
	
	public static ASTNode parser(IJavaElement cu)
	{
		return Parser.Parse2ComilationUnit((ICompilationUnit)cu);
	}
}
