package dlf.refactoring.precondition.checker;

import dlf.refactoring.ConditionType;
import dlf.refactoring.RefactoringType;
import dlf.refactoring.precondition.checker.environments.IRefactoringEnvironment;
import dlf.refactoring.precondition.checker.environments.RefactoringContext;
import dlf.refactoring.precondition.checker.result.C2CheckingResult;
import dlf.refactoring.precondition.checker.result.ICheckingResult;
import dlf.refactoring.precondition.util.XArrayList;

public class RenameClassCheckers extends RefactoringCheckerSet{
	
	@Override
	protected XArrayList<IConditionChecker> getAllConditionCheckers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XArrayList<IRefactoringEnvironment> getAllRefactoringEnvironments(
			RefactoringContext context) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private class NameCollisionChecker implements IConditionChecker
	{
		@Override
		public ICheckingResult performChecking(IRefactoringEnvironment environment) {
			ICheckingResult result = new NameCollisionResult();
			
			return result;
		}
		
		private class NameCollisionResult extends C2CheckingResult
		{
			@Override
			public RefactoringType getRefactoringType() {
				return RefactoringType.RENAME_CLASS;
			}

			@Override
			public ConditionType getConditionType() {
				return ConditionType.NAME_COLLISION;
			}
		}
	}
}
