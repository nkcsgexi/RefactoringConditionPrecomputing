package edu.ncsu.dlf.refactoring.precondition.checker;

import edu.ncsu.dlf.refactoring.ConditionType;
import edu.ncsu.dlf.refactoring.RefactoringType;
import edu.ncsu.dlf.refactoring.precondition.checker.environments.RefactoringEnvironment;

public abstract class C1CheckingResult implements ICheckingResult {
	
	private final boolean isOK;
	private final RefactoringEnvironment environment;
	
	public C1CheckingResult(boolean isOK, RefactoringEnvironment environment)
	{
		this.isOK = isOK;
		this.environment = environment;
	}
	
	public boolean IsOK()
	{
		return isOK;
	}
}
