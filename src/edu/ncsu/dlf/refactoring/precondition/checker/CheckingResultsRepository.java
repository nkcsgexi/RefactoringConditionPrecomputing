package edu.ncsu.dlf.refactoring.precondition.checker;

import java.util.ArrayList;
import java.util.List;

import edu.ncsu.dlf.refactoring.precondition.checker.environments.IRefactoringInput;
import edu.ncsu.dlf.refactoring.precondition.checker.environments.RefactoringEnvironment;
import edu.ncsu.dlf.refactoring.precondition.util.ListOperations;
import edu.ncsu.dlf.refactoring.precondition.util.XArrayList;
import edu.ncsu.dlf.refactoring.precondition.util.interfaces.IConvertor;
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
	
	/* All of the pre-checking results. */
	private final XArrayList<RefactoringEnvironmentResults> environmentResultsRepo;
	
	private CheckingResultsRepository()
	{
		this.environmentResultsRepo = new XArrayList<RefactoringEnvironmentResults>();
	}
	
	
	private static CheckingResultsRepository instance;
	
	public static CheckingResultsRepository getInstance()
	{
		if(instance == null)
			instance = new CheckingResultsRepository();
		return instance;
	}
	

	private RefactoringEnvironmentResults getCheckingResults(final RefactoringEnvironment 
			environment) throws Exception
	{
		return environmentResultsRepo.first(new IPredicate<RefactoringEnvironmentResults>(){
			@Override
			public boolean IsTrue(RefactoringEnvironmentResults t)
					throws Exception {
				return t.isEnvironmentCorrect(environment);
			}});
	}
	
	/* Add a new checking result for a specific refactoring environment.*/
	public void addCheckingResult(RefactoringEnvironment environment, ICheckingResult result) 
			throws Exception
	{
		RefactoringEnvironmentResults envResults = getCheckingResults(environment);
		if(envResults == null)
		{
			envResults = new RefactoringEnvironmentResults(environment);
			envResults.addCheckingResult(result);
		}
		environmentResultsRepo.add(envResults);
	}
	
	
	/* Given a refactoring environment, whether there is available results. */
	public boolean isResultsAvailable(final RefactoringEnvironment environment) throws Exception
	{
		return getCheckingResults(environment) != null;
	}

	/* Whether all the C1 conditions of the given environment are in the OK state.*/
	public boolean isC1ConditionOK(final RefactoringEnvironment environment) throws Exception
	{
		if(isResultsAvailable(environment))
		{
			 XArrayList<C1CheckingResult> c1Results = extractCheckingResults(getCheckingResults
					 (environment).results, C1CheckingResult.class);
			if(!c1Results.empty())
			{
				if(c1Results.all(new IPredicate<C1CheckingResult>(){
					@Override
					public boolean IsTrue(C1CheckingResult t) throws Exception {
						return t.IsOK();
					}}));
			}
		}
		return false;
	}
	
	
	/* 
	 * Given an environment, check its category II conditions and return whether the refactoring
	 * is doable.
	 * */
	public boolean isC2ConditionOK(RefactoringEnvironment environment, final IRefactoringInput 
			input) throws Exception
	{
		RefactoringEnvironmentResults envResults = getCheckingResults(environment);
		if(envResults != null)
		{
			XArrayList<C2CheckingResult> c2results = extractCheckingResults(envResults.results, 
					C2CheckingResult.class);
			if(c2results.any()){
				return !c2results.exist(new IPredicate<C2CheckingResult>(){
					@Override
					public boolean IsTrue(C2CheckingResult t) throws Exception {
						return t.isIllegalInput(input);
				}});
			}
		}
		return false;
	}

	private <C> XArrayList<C> extractCheckingResults(final XArrayList<ICheckingResult> 
		results, final Class C) throws Exception {
		return results.where (new IPredicate<ICheckingResult>(){
		@Override
		public boolean IsTrue(ICheckingResult t) throws Exception {
			return t.equals(C);
		}}).convert(new IConvertor<ICheckingResult, C>(){
			@Override
			public C convert(ICheckingResult m) throws Exception {
				return (C)m;
			}});
	}
}
