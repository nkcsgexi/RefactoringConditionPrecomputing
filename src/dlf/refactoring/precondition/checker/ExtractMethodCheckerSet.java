package dlf.refactoring.precondition.checker;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.IJavaElement;

import dlf.refactoring.enums.ConditionType;
import dlf.refactoring.enums.InputType;
import dlf.refactoring.enums.RefactoringType;
import dlf.refactoring.precondition.JavaModelAnalyzers.ICompilationUnitAnalyzer;
import dlf.refactoring.precondition.JavaModelAnalyzers.IMethodAnalyzer;
import dlf.refactoring.precondition.JavaModelAnalyzers.ITypeAnalyzer;
import dlf.refactoring.precondition.checker.environments.IRefactoringEnvironment;
import dlf.refactoring.precondition.checker.environments.RefactoringContext;
import dlf.refactoring.precondition.checker.environments.SingleELementRefacatoringEnvironment;
import dlf.refactoring.precondition.checker.environments.StringRefactoringInput;
import dlf.refactoring.precondition.checker.result.C2CheckingResult;
import dlf.refactoring.precondition.checker.result.ICheckingResult;
import dlf.refactoring.precondition.checker.result.IRefactoringInput;
import dlf.refactoring.precondition.util.XArrayList;
import dlf.refactoring.precondition.util.interfaces.IConvertor;
import dlf.refactoring.precondition.util.interfaces.IMapper;
import dlf.refactoring.precondition.util.interfaces.IPredicate;

public class ExtractMethodCheckerSet extends RefactoringCheckerSet {

	@Override
	protected XArrayList<IConditionChecker> getAllConditionCheckers() {
		XArrayList<IConditionChecker> list = new XArrayList<IConditionChecker>();
		list.add(new ExtractableStatementsChecker());
		list.add(new AvailableMethodNameChecker());
		return list;
	}

	@Override
	protected XArrayList<IRefactoringEnvironment> getAllRefactoringEnvironments(RefactoringContext 
			context) throws Exception {
		XArrayList<IRefactoringEnvironment> allEnvironments = new XArrayList
				<IRefactoringEnvironment>();
		Iterator<IJavaElement> it = context.getCompilationUnits();
		for( ;it.hasNext(); ) {
			allEnvironments.addAll(ICompilationUnitAnalyzer.getTypes(it.next()).select(new 
					IMapper<IJavaElement, IJavaElement>(){
				@Override
				public List<IJavaElement> map(IJavaElement t) throws Exception {
					return ITypeAnalyzer.getMethods(t).where(new IPredicate<IJavaElement>(){
						@Override
						public boolean IsTrue(IJavaElement t) throws Exception {
							return !IMethodAnalyzer.isConstructor(t);
						}});
				}}).convert(new IConvertor<IJavaElement, IRefactoringEnvironment>(){
					@Override
					public IRefactoringEnvironment convert(IJavaElement t)throws Exception {
						return new ExtractMethodEnvironment(t);
					}}));
		}
		return allEnvironments;
	}
	
	@Override
	public RefactoringType getRefactoringType() {
		return RefactoringType.EXTRACT_METHOD;
	}
	
	private class ExtractMethodEnvironment extends SingleELementRefacatoringEnvironment {
		
		public ExtractMethodEnvironment(IJavaElement element) {
			super(element);
		}

		@Override
		public RefactoringType getRefactoringType() {
			return RefactoringType.EXTRACT_METHOD;
		}
	}
	
	
	
	private class ExtractableStatementsChecker implements IConditionChecker {
		@Override
		public ICheckingResult performChecking(IRefactoringEnvironment environment) throws Exception 
		{
			IJavaElement method = ((SingleELementRefacatoringEnvironment) environment).
					getJavaElement();
			
			
			return null;
		}
		
		private class Statement2Extract extends C2CheckingResult
		{
			@Override
			public ConditionType getConditionType() {
				return ConditionType.EXTRACTABLE_STATEMENTS;
			}	
		}
		
		private class SelectionInput implements IRefactoringInput
		{
			int start;
			int end;
			
			protected SelectionInput(int start, int end)
			{
				this.start = start;
				this.end = end;
			}
			
			@Override
			public int compareTo(Object arg) {		
				int otherStart = ((SelectionInput)arg).start;
				int otherEnd = ((SelectionInput)arg).end;
				if(start != otherStart)
					return start - otherStart;
				return end - otherEnd;
			}

			@Override
			public InputType getInputType() {
				return InputType.SELECTION;
			}	
		}
	}
	
	
	private class AvailableMethodNameChecker implements IConditionChecker{
		@Override
		public ICheckingResult performChecking(IRefactoringEnvironment environment) throws Exception 
		{
			return null;
		}
		
		private class MethodNameChecker extends C2CheckingResult
		{
			@Override
			public ConditionType getConditionType() {
				return ConditionType.NAME_COLLISION;
			}	
		}
		
		private class MethodNameInput extends StringRefactoringInput
		{
			public MethodNameInput(String input) {
				super(input);
			}

			@Override
			public InputType getInputType() {
				return InputType.NEW_NAME;
			}	
		}
	}




}
