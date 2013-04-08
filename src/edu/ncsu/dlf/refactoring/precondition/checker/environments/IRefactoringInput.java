package edu.ncsu.dlf.refactoring.precondition.checker.environments;

import edu.ncsu.dlf.refactoring.interfaces.IHasConditionType;
import edu.ncsu.dlf.refactoring.interfaces.IHasRefactoringType;

public abstract class IRefactoringInput implements IHasRefactoringType, IHasConditionType {
	
	public final boolean equals(Object obj)
	{
		if(obj instanceof IRefactoringInput)
		{
			IRefactoringInput input = (IRefactoringInput) obj;
			if(input.getRefactoringType() == this.getRefactoringType() && input.getConditionType() 
					== this.getConditionType())
			{
				return isInputInforSame(input);
			}
		}
		return false;
	}
	
	protected abstract boolean isInputInforSame(IRefactoringInput input);
}
