package edu.ncsu.dlf.refactoring.precondition.checker;

import java.util.ArrayList;
import java.util.List;

import edu.ncsu.dlf.refactoring.precondition.checker.environments.IRefactoringInput;
import edu.ncsu.dlf.refactoring.precondition.checker.environments.RefactoringEnvironment;
import edu.ncsu.dlf.refactoring.precondition.util.ListOperations;
import edu.ncsu.dlf.refactoring.precondition.util.interfaces.IPredicate;

public class CheckingResultsRepository {

	private class RefactoringEnvironmentResults {

		private final RefactoringEnvironment environment;
		private final List<ICheckingResult> results;
		
		protected RefactoringEnvironmentResults(RefactoringEnvironment environment)
		{
			this.environment = environment;
			results = new ArrayList<ICheckingResult>();
		}
		
		protected void addCheckingResult(ICheckingResult result)
		{
			results.add(result);
		}

		protected boolean isEnvironmentCorrect(RefactoringEnvironment environment)
		{
			return this.environment.equals(environment);
		}
	}
	
	
	private final List<RefactoringEnvironmentResults> results;
	private final ListOperations<RefactoringEnvironmentResults> resultOperations;
	
	private CheckingResultsRepository()
	{
		this.results = new ArrayList<RefactoringEnvironmentResults>();
		this.resultOperations = new ListOperations<RefactoringEnvironmentResults>();
	}
	
	private List<RefactoringEnvironmentResults> getCheckingResults(final RefactoringEnvironment 
			environment) throws Exception
	{
		return resultOperations.Select(results, new IPredicate<RefactoringEnvironmentResults>(){
			@Override
			public boolean IsTrue(RefactoringEnvironmentResults t) throws Exception {
				return t.isEnvironmentCorrect(environment);
			}});
	}
	
	
	public boolean isResultsAvailable(final RefactoringEnvironment environment) throws Exception
	{
		return getCheckingResults(environment).size() > 0;
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
