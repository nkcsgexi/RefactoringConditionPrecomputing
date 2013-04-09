package dlf.refactoring.precondition.JavaModelAnalyzers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.JavaElement;

import dlf.refactoring.precondition.util.XArrayList;

public class IPackageFragmentAnalyzer {

	public static XArrayList<IJavaElement> getICompilationUnits(IJavaElement pack) throws 
		JavaModelException
	{	
		XArrayList<IJavaElement> elements = new XArrayList<IJavaElement>();
		ICompilationUnit[] units = ((IPackageFragment) pack).getCompilationUnits(); 		
		for(IJavaElement unit : units)
		{
			elements.add(unit);
		}
		return elements;
	}
	
	public static String getName(IJavaElement p)
	{
		return p.getElementName();
	}
}
