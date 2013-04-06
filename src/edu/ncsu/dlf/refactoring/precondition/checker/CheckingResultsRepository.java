package edu.ncsu.dlf.refactoring.precondition.checker;

import java.util.ArrayList;
import java.util.List;

import edu.ncsu.dlf.refactoring.precondition.checker.environments.IRefactoringInput;
import edu.ncsu.dlf.refactoring.precondition.checker.environments.RefactoringEnvironment;
import edu.ncsu.dlf.refactoring.precondition.util.ListOperations;
import edu.ncsu.dlf.refactoring.precondition.util.XArrayList;
import edu.ncsu.dlf.refactoring.precondition.util.interfaces.IPredicate;

public class CheckingResultsRepository {

	private class RefactoringEnvironmentResults {

		private final RefactoringEnvironment environment;
		private final XArrayList<ICheckingResult> results;
		
		protected RefactoringEnvironmentResults(RefactoringEnvironment environment)
		{
			this.environment = environment;
			results = new XArrayList<ICheckingResult>();
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
	
	
	private final XArrayList<RefactoringEnvironmentResults> results;
	
	private CheckingResultsRepository()
	{
		this.results = new XArrayList<RefactoringEnvironmentResults>();
	}
	
	private RefactoringEnvironmentResults getCheckingResults(final RefactoringEnvironment 
			environment) throws Exception
	{
		return results.first(new IPredicate<RefactoringEnvironmentResults>(){
			@Override
			public boolean IsTrue(RefactoringEnvironmentResults t)
					throws Exception {
				return t.isEnvironmentCorrect(environment);
			}});
	}
	
	
	public boolean isResultsAvailable(final RefactoringEnvironment environment) throws Exception
	{
		return getCheckingResults(environment) != null;
	}

	public boolean isC1ConditionOK(final RefactoringEnvironment environment) throws Exception
	{
		if(isResultsAvailable(environment))
		{
			getCheckingResults(environment).results.where(new IPredicate<ICheckingResult>(){
			@Override
			public boolean IsTrue(ICheckingResult t) throws Exception {
				return t instanceof C1CheckingResult;
			}});
		
		}
		
		return false;
	}
	
	public boolean isC2ConditionOK(IRefactoringInput input)
	{
		
		
		return false;
	}
}
