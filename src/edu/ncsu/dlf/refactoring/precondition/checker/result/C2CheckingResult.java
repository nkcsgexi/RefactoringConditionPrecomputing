package edu.ncsu.dlf.refactoring.precondition.checker.result;

import java.util.ArrayList;
import java.util.List;

import edu.ncsu.dlf.refactoring.precondition.checker.environments.RefactoringInput;

public abstract class C2CheckingResult implements ICheckingResult {
	
	private final List<RefactoringInput> illegalInputs;
	
	public C2CheckingResult()
	{
		this.illegalInputs = new ArrayList<RefactoringInput>();
	}
	
	public void addIllegalInput(RefactoringInput input)
	{
		this.illegalInputs.add(input);
	}
	
	public boolean isIllegalInput(RefactoringInput input)
	{
		return this.illegalInputs.contains(input);
	}
}
