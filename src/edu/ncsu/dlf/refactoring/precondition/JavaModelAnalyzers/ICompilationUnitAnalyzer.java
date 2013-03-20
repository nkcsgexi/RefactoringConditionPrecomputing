package edu.ncsu.dlf.refactoring.precondition.JavaModelAnalyzers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;

import edu.ncsu.dlf.refactoring.precondition.util.Parser;


public class ICompilationUnitAnalyzer {

	public static List<IJavaElement> getTypes(IJavaElement cu) throws Exception
	{
		ICompilationUnit unit = (ICompilationUnit) cu;
		ArrayList<IJavaElement> list = new ArrayList<IJavaElement>();
		list.addAll(Arrays.asList(unit.getTypes()));
		return list;
	}
	
	public static ASTNode parser(IJavaElement cu)
	{
		return Parser.Parse2ComilationUnit((ICompilationUnit)cu);
	}
}
