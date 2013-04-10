package dlf.refactoring.precondition.checker.environments;

public abstract class StringRefactoringInput implements IRefactoringInput{

	String input;
	
	public StringRefactoringInput(String input)
	{
		this.input = input;
	}
	
	@Override
	public int compareTo(Object arg) {
		String other = ((StringRefactoringInput)arg).input;
		return this.input.compareTo(other);
	}
	
	@Override
	public String toString()
	{
		return "String input: " + input;
	}

}
