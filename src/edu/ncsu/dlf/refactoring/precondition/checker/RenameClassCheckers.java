package edu.ncsu.dlf.refactoring.precondition.checker;

import edu.ncsu.dlf.refactoring.ConditionType;
import edu.ncsu.dlf.refactoring.RefactoringType;
import edu.ncsu.dlf.refactoring.precondition.checker.environments.RefactoringContext;
import edu.ncsu.dlf.refactoring.precondition.checker.environments.IRefactoringEnvironment;
import edu.ncsu.dlf.refactoring.precondition.checker.result.C2CheckingResult;
import edu.ncsu.dlf.refactoring.precondition.checker.result.ICheckingResult;
import edu.ncsu.dlf.refactoring.precondition.util.XArrayList;

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
