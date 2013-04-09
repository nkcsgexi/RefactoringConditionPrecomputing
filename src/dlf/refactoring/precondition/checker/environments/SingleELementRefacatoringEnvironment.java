package dlf.refactoring.precondition.checker.environments;

import org.eclipse.jdt.core.IJavaElement;

import dlf.refactoring.enums.RefactoringType;

public abstract class SingleELementRefacatoringEnvironment implements IRefactoringEnvironment {

	private final IJavaElement element;
	
	public SingleELementRefacatoringEnvironment(IJavaElement element)
	{
		this.element = element;
	}
	
	public IJavaElement getJavaElement()
	{
		return this.element;
	}
	
	@Override
	public boolean equals(Object other)
	{
		if(other instanceof SingleELementRefacatoringEnvironment)
		{
			SingleELementRefacatoringEnvironment otherEnv = (SingleELementRefacatoringEnvironment) 
					other;
			return otherEnv.getJavaElement() == this.getJavaElement() && otherEnv.
					getRefactoringType() == this.getRefactoringType();
		}
		return false;
	}
	

	@Override
	public abstract RefactoringType getRefactoringType();

}
