package edu.ncsu.dlf.refactoring.precondition.checker.result;

import edu.ncsu.dlf.refactoring.ConditionType;
import edu.ncsu.dlf.refactoring.RefactoringType;
import edu.ncsu.dlf.refactoring.precondition.checker.environments.IRefactoringEnvironment;

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
