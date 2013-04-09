package dlf.refactoring.precondition.checker.environments;

import java.util.Iterator;
import org.eclipse.jdt.core.ICompilationUnit;
import dlf.refactoring.precondition.util.XArrayList;

public class RefactoringContext {
	private final XArrayList<ICompilationUnit> units;
	
	public RefactoringContext()
	{
		units = new XArrayList<ICompilationUnit>();
	}
	
	public void AddCompilationUnit(ICompilationUnit unit)
	{
		units.add(unit);
	}
	
	public Iterator<ICompilationUnit> getCompilationUnits()
	{
		return units.iterator();
	}
	
}
