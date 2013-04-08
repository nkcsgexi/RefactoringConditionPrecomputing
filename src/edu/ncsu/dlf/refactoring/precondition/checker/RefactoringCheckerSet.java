package edu.ncsu.dlf.refactoring.precondition.checker;

import java.util.List;

import edu.ncsu.dlf.refactoring.precondition.checker.environments.RefactoringContext;
import edu.ncsu.dlf.refactoring.precondition.checker.environments.IRefactoringEnvironment;
import edu.ncsu.dlf.refactoring.precondition.checker.result.ICheckingResult;
import edu.ncsu.dlf.refactoring.precondition.util.XArrayList;
import edu.ncsu.dlf.refactoring.precondition.util.interfaces.IConvertor;
import edu.ncsu.dlf.refactoring.precondition.util.interfaces.IMapper;

public abstract class RefactoringCheckerSet{
		
	protected abstract XArrayList<IConditionChecker> getAllConditionCheckers();
	
	public abstract XArrayList<IRefactoringEnvironment> getAllRefactoringEnvironments(
			RefactoringContext context);
	
	
	public final XArrayList<ICheckingResult> performAllChecking(final IRefactoringEnvironment 
			environment) throws Exception
	{
		final XArrayList<IConditionChecker> checkers = getAllConditionCheckers();
		return checkers.convert(new IConvertor<IConditionChecker, ICheckingResult>(){
			@Override
			public ICheckingResult convert(final IConditionChecker c)throws Exception {
				return c.performChecking(environment);
			}});
	}
}
