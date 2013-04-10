package dlf.refactoring.precondition.JavaModelAnalyzers;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;

import dlf.refactoring.precondition.util.XArrayList;


public class ITypeAnalyzer {
	
	public static XArrayList<IJavaElement> getMethods(IJavaElement type) throws Exception
	{
		return IJavaElementAnalyzer.convertArray2XArrayList(((IType) type).getMethods());
	}
	
	public static XArrayList<IJavaElement> getFields(IJavaElement type) throws Exception
	{
		
		return IJavaElementAnalyzer.convertArray2XArrayList(((IType) type).getFields());
	}

	public static XArrayList<IJavaElement> getSubTypes(IJavaElement type) throws Exception
	{
		ITypeHierarchy h = ((IType) type).newTypeHierarchy(null);
		return IJavaElementAnalyzer.convertArray2XArrayList(h.getSubclasses((IType) type));
	}
	
	public static XArrayList<IJavaElement> getSuperTypes(IJavaElement type) throws Exception
	{
		ITypeHierarchy h = ((IType) type).newTypeHierarchy(null);
		return IJavaElementAnalyzer.convertArray2XArrayList(h.getSupertypes((IType) type));
	}

	public static XArrayList<IJavaElement> getSuperInterfaces(IJavaElement type) throws Exception
	{	
		ITypeHierarchy h = ((IType) type).newTypeHierarchy(null);
		return IJavaElementAnalyzer.convertArray2XArrayList(h.getSuperInterfaces((IType) type));
	}
}
