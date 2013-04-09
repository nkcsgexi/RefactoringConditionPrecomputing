package dlf.refactoring.precondition.checker.result;

import java.util.ArrayList;
import java.util.List;

import dlf.refactoring.precondition.checker.environments.IRefactoringInput;


public abstract class C2CheckingResult implements ICheckingResult {
	
	private final List<IRefactoringInput> illegalInputs;
	
	public C2CheckingResult()
	{
		this.illegalInputs = new ArrayList<IRefactoringInput>();
	}
	
	public void addIllegalInput(IRefactoringInput input)
	{
		for (int i = 0; i < illegalInputs.size(); i++) 
		 {
	        if (illegalInputs.get(i).compareTo(input) < 0) 
	        	continue;
	        if (illegalInputs.get(i).compareTo(input) == 0) 
	        	return;
	        illegalInputs.add(i, input);
	        return;
	     }
		this.illegalInputs.add(input);
	}
	
	public boolean isIllegalInput(IRefactoringInput input)
	{
		return this.illegalInputs.contains(input);
	}
}
