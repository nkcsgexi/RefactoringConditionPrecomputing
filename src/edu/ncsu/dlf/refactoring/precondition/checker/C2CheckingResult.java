package edu.ncsu.dlf.refactoring.precondition.checker;

import java.util.ArrayList;
import java.util.List;
import edu.ncsu.dlf.refactoring.precondition.checker.environments.IRefactoringInput;

public class C2CheckingResult implements ICheckingResult {
	
	private final List<IRefactoringInput> illegalInputs;
	
	public C2CheckingResult()
	{
		this.illegalInputs = new ArrayList<IRefactoringInput>();
	}
	
	public void addIllegalInput(IRefactoringInput input)
	{
		this.illegalInputs.add(input);
	}
	
	public boolean isIllegalInput(IRefactoringInput input)
	{
		return this.illegalInputs.contains(input);
	}
}
