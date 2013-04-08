package edu.ncsu.dlf.refactoring.precondition.checker.environments;

import edu.ncsu.dlf.refactoring.ConditionType;
import edu.ncsu.dlf.refactoring.RefactoringType;

public class RefactoringInputFactory {
	
	private static abstract class NewNameInput extends IRefactoringInput
	{
		private final String newName;
		
		protected NewNameInput(String newName)
		{
			this.newName = newName;
		}
		
		@Override
		public ConditionType getConditionType() {
			return ConditionType.NAME_COLLISION;
		}

		@Override
		protected boolean isInputInforSame(IRefactoringInput input) {
			if(input instanceof NewNameInput)
			{
				String newName = ((NewNameInput)input).newName;
				return newName == this.newName;
			}
			return false;
		}
	}
	
	public static IRefactoringInput createRenameClassNewNameInput(final String newName)
	{
		return new NewNameInput(newName){
			@Override
			public RefactoringType getRefactoringType() {
				return RefactoringType.RENAME_CLASS;
			}};
	}
	
	
	public static IRefactoringInput createRenameMethodNewNameInput(final String newName)
	{
		return new NewNameInput(newName){
			@Override
			public RefactoringType getRefactoringType() {
				return RefactoringType.RENAME_METHOD;
			}};
	}
	
	
}
