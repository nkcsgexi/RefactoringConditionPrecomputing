package dlf.refactoring.precondition.checker.result;

import java.util.ArrayList;
import java.util.List;

import dlf.refactoring.precondition.checker.environments.IRefactoringEnvironment;
import dlf.refactoring.precondition.checker.environments.IRefactoringInput;
import dlf.refactoring.precondition.util.ListOperations;
import dlf.refactoring.precondition.util.XArrayList;
import dlf.refactoring.precondition.util.interfaces.IConvertor;
import dlf.refactoring.precondition.util.interfaces.IPredicate;


public class CheckingResultsRepository {
	
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
	
	private RefactoringEnvironmentResults getCheckingResults(final IRefactoringEnvironment 
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
	public void addCheckingResult(IRefactoringEnvironment environment, ICheckingResult result) 
			throws Exception
	{
		RefactoringEnvironmentResults envResults = getCheckingResults(environment);
		if(envResults == null)
		{
			envResults = new RefactoringEnvironmentResults(environment);
			environmentResultsRepo.add(envResults);
		}
		envResults.addCheckingResult(result);
	}
	
	
	/* Given a refactoring environment, whether there is available results. */
	public boolean isResultsAvailable(final IRefactoringEnvironment environment) throws Exception
	{
		return getCheckingResults(environment) != null;
	}

	/* Whether all the C1 conditions of the given environment are in the OK state.*/
	public boolean isC1ConditionOK(final IRefactoringEnvironment environment) throws Exception
	{
		
		RefactoringEnvironmentResults results = getCheckingResults(environment);
		if(results != null)
		{
			XArrayList<C1CheckingResult> c1Results = results.getC1Results();
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
	public boolean isC2ConditionOK(IRefactoringEnvironment environment, final IRefactoringInput 
		input) throws Exception
	{
		RefactoringEnvironmentResults envResults = getCheckingResults(environment);
		if(envResults != null)
		{
			envResults.getC2ResultByInputType(input.getInputType()).all(new IPredicate
					<C2CheckingResult>(){
				@Override
				public boolean IsTrue(C2CheckingResult t) throws Exception {
					return !t.isIllegalInput(input);
				}});
		}
		return false;
	}
}
