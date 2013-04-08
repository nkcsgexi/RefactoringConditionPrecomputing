package dlf.refactoring.precondition.checker;

import java.util.List;

import dlf.refactoring.precondition.checker.environments.IRefactoringEnvironment;
import dlf.refactoring.precondition.checker.environments.RefactoringContext;
import dlf.refactoring.precondition.checker.result.ICheckingResult;
import dlf.refactoring.precondition.util.XArrayList;
import dlf.refactoring.precondition.util.interfaces.IConvertor;
import dlf.refactoring.precondition.util.interfaces.IMapper;


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
