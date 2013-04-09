package dlf.refactoring.precondition.checker.environments;

import java.util.Iterator;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;

import dlf.refactoring.precondition.JavaModelAnalyzers.ICompilationUnitAnalyzer;
import dlf.refactoring.precondition.util.XArrayList;

public class RefactoringContext {
	private final XArrayList<IJavaElement> units;
	
	public RefactoringContext()
	{
		units = new XArrayList<IJavaElement>();
	}
	
	public void AddCompilationUnit(ICompilationUnit unit)
	{
		units.add(unit);
	}
	
	public Iterator<IJavaElement> getCompilationUnits()
	{
		return units.iterator();
	}
	
	public boolean isEmpty()
	{
		return units.size() == 0;
	}
	
	public XArrayList<IJavaElement> getFragmentPackages() throws Exception
	{
		XArrayList<IJavaElement> packages = new XArrayList<IJavaElement>();
		for(IJavaElement u : units)
		{
			IJavaElement p = ICompilationUnitAnalyzer.getPackageFragment(u);
			if(!packages.contains(p))
			{
				packages.add(p);
			}
		}
		return packages;
	}
	
	public IJavaElement getJavaProject()
	{
		return null;
	}
}
