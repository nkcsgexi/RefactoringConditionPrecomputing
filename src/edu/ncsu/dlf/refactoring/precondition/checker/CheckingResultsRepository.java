package edu.ncsu.dlf.refactoring.precondition.checker;

import java.util.ArrayList;
import java.util.List;

import edu.ncsu.dlf.refactoring.precondition.checker.environments.IRefactoringInput;
import edu.ncsu.dlf.refactoring.precondition.checker.environments.RefactoringEnvironment;
import edu.ncsu.dlf.refactoring.precondition.util.ListOperations;

public class CheckingResultsRepository {

	private final List<ICheckingResult> results;
	private final ListOperations<ICheckingResult> resultOperations;
	
	private CheckingResultsRepository()
	{
		this.results = new ArrayList<ICheckingResult>();
		this.resultOperations = new ListOperations<ICheckingResult>();
	}
	
	public boolean isResultsAvailable(RefactoringEnvironment environemnt)
	{

		return false;
	}
	
	public boolean isC1ConditionOK()
	{
		return false;
	}
	
	public boolean isC2ConditionOK(IRefactoringInput input)
	{
		
		
		return false;
	}
}
