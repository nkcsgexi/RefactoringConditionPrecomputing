package dlf.refactoring.precondition.checker.environments;

import dlf.refactoring.ConditionType;
import dlf.refactoring.RefactoringType;

public class RefactoringInputFactory {
	
	private static abstract class NewNameInput extends RefactoringInput
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
		public int compareTo(Object input) {
			if(input instanceof NewNameInput)
			{
				String newName = ((NewNameInput)input).newName;
				return this.newName.compareTo(newName);
			}
			return Integer.MIN_VALUE;
		}
	}
	
	public static RefactoringInput createRenameClassNewNameInput(final String newName)
	{
		return new NewNameInput(newName){
			@Override
			public RefactoringType getRefactoringType() {
				return RefactoringType.RENAME_CLASS;
			}};
	}
	
	public static RefactoringInput createRenameMethodNewNameInput(final String newName)
	{
		return new NewNameInput(newName){
			@Override
			public RefactoringType getRefactoringType() {
				return RefactoringType.RENAME_METHOD;
			}};
	}
}
