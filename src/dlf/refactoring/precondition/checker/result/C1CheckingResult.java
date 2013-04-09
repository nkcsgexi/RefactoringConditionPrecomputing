package dlf.refactoring.precondition.checker.result;

import dlf.refactoring.enums.ConditionType;
import dlf.refactoring.enums.RefactoringType;
import dlf.refactoring.precondition.checker.environments.IRefactoringEnvironment;

public abstract class C1CheckingResult implements ICheckingResult {
	
	private final boolean isOK;
	private final IRefactoringEnvironment environment;
	
	public C1CheckingResult(boolean isOK, IRefactoringEnvironment environment)
	{
		this.isOK = isOK;
		this.environment = environment;
	}
	
	public boolean IsOK()
	{
		return isOK;
	}
}
