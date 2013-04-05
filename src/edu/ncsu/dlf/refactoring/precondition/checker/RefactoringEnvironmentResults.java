package edu.ncsu.dlf.refactoring.precondition.checker;

import java.util.ArrayList;
import java.util.List;

import edu.ncsu.dlf.refactoring.precondition.checker.environments.RefactoringEnvironment;

public class RefactoringEnvironmentResults {

	private final RefactoringEnvironment environment;
	private final List<ICheckingResult> results;
	
	public RefactoringEnvironmentResults(RefactoringEnvironment environment)
	{
		this.environment = environment;
		results = new ArrayList<ICheckingResult>();
	}
	
	public void addCheckingResult(ICheckingResult result)
	{
		results.add(result);
	}

	public boolean isEnvironmentCorrect(RefactoringEnvironment environment)
	{
		return this.environment.equals(environment);
	}
}
