package dlf.refactoring.precondition.checker.result;

import java.util.ArrayList;
import java.util.List;

import dlf.refactoring.precondition.checker.environments.IRefactoringInput;
import dlf.refactoring.precondition.util.XArrayList;
import dlf.refactoring.precondition.util.interfaces.IPredicate;


public abstract class C2CheckingResult implements ICheckingResult {
	
	private final XArrayList<IRefactoringInput> illegalInputs;
	
	protected C2CheckingResult()
	{
		illegalInputs = new XArrayList<IRefactoringInput>();
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
		illegalInputs.add(input);
	}
	
	public boolean isIllegalInput(final IRefactoringInput input) throws Exception
	{
		return illegalInputs.exist(new IPredicate<IRefactoringInput>(){
			@Override
			public boolean IsTrue(IRefactoringInput t) throws Exception {
				return input.compareTo(t) == 0;
			}});
	}
}
