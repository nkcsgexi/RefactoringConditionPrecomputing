package JavaModelAnalyzer;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;


public class ITypeAnalyzer {
	
	public static List<IJavaElement> getMethods(IJavaElement type) throws JavaModelException
	{
		return IJavaElementUtils.convertArray2List(((IType) type).getMethods());
	}
	
	public static List<IJavaElement> getFields(IJavaElement type) throws JavaModelException
	{
		
		return IJavaElementUtils.convertArray2List(((IType) type).getFields());
	}

	public static List<IJavaElement> getSubTypes(IJavaElement type) throws JavaModelException
	{
		ITypeHierarchy h = ((IType) type).newTypeHierarchy(null);
		return IJavaElementUtils.convertArray2List(h.getSubclasses((IType) type));
	}
	
	public static List<IJavaElement> getSuperTypes(IJavaElement type) throws JavaModelException
	{
		ITypeHierarchy h = ((IType) type).newTypeHierarchy(null);
		return IJavaElementUtils.convertArray2List(h.getSupertypes((IType) type));
	}

	public static List<IJavaElement> getSuperInterfaces(IJavaElement type) throws JavaModelException
	{	
		ITypeHierarchy h = ((IType) type).newTypeHierarchy(null);
		return IJavaElementUtils.convertArray2List(h.getSuperInterfaces((IType) type));
	}
}
