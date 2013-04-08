package dlf.refactoring.precondition.checker.result;

import java.util.ArrayList;
import java.util.List;

import dlf.refactoring.precondition.checker.environments.RefactoringInput;


public abstract class C2CheckingResult implements ICheckingResult {
	
	private final List<RefactoringInput> illegalInputs;
	
	public C2CheckingResult()
	{
		this.illegalInputs = new ArrayList<RefactoringInput>();
	}
	
	public void addIllegalInput(RefactoringInput input)
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
	
	public boolean isIllegalInput(RefactoringInput input)
	{
		return this.illegalInputs.contains(input);
	}
}
