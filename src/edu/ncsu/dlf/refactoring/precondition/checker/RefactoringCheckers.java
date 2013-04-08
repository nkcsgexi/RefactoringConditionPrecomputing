package edu.ncsu.dlf.refactoring.precondition.checker;

import java.util.List;

import edu.ncsu.dlf.refactoring.precondition.checker.environments.RefactoringContext;
import edu.ncsu.dlf.refactoring.precondition.checker.environments.RefactoringEnvironment;
import edu.ncsu.dlf.refactoring.precondition.util.XArrayList;
import edu.ncsu.dlf.refactoring.precondition.util.interfaces.IConvertor;
import edu.ncsu.dlf.refactoring.precondition.util.interfaces.IMapper;

public abstract class RefactoringCheckers {
		
	protected abstract XArrayList<IConditionChecker> getAllConditionCheckers();
	protected abstract XArrayList<RefactoringEnvironment> getAllRefactoringEnvironments(
			RefactoringContext context);
	
	
	public final XArrayList<ICheckingResult> performChecking(RefactoringContext context) throws 
		Exception
	{
		final XArrayList<IConditionChecker> checkers = getAllConditionCheckers();
		final XArrayList<RefactoringEnvironment> environments = getAllRefactoringEnvironments
				(context);
		return checkers.select(new IMapper<IConditionChecker, ICheckingResult>(){
			@Override
			public List<ICheckingResult> map(final IConditionChecker c)
					throws Exception {
				return environments.convert(new IConvertor<RefactoringEnvironment, 
						ICheckingResult>(){
					@Override
					public ICheckingResult convert(RefactoringEnvironment e)
							throws Exception {
						return c.performChecking(e);
					}});
			}});
	}

	
}
