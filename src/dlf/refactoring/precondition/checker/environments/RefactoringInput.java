package dlf.refactoring.precondition.checker.environments;

import dlf.refactoring.interfaces.IHasConditionType;
import dlf.refactoring.interfaces.IHasRefactoringType;

public abstract class RefactoringInput implements IHasRefactoringType, IHasConditionType, 
		Comparable{
	public final boolean equals(Object obj)
	{
		if(obj instanceof RefactoringInput)
		{
			RefactoringInput input = (RefactoringInput) obj;
			if(input.getRefactoringType() == this.getRefactoringType() && input.getConditionType() 
					== this.getConditionType())
			{
				return compareTo(input) == 0;
			}
		}
		return false;
	}
	
	@Override
	public abstract int compareTo(Object other);
}
