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
import dlf.refactoring.precondition.checker.environments.IRefactoringInput;
import dlf.refactoring.precondition.checker.environments.RefactoringContext;
import dlf.refactoring.precondition.checker.environments.SingleELementRefacatoringEnvironment;
import dlf.refactoring.precondition.checker.environments.StringRefactoringInput;
import dlf.refactoring.precondition.checker.result.C2CheckingResult;
import dlf.refactoring.precondition.checker.result.ICheckingResult;
import dlf.refactoring.precondition.util.XArrayList;
import dlf.refactoring.precondition.util.interfaces.IConvertor;
import dlf.refactoring.precondition.util.interfaces.IMapper;
import dlf.refactoring.precondition.util.interfaces.IOperation;
import dlf.refactoring.precondition.util.interfaces.IPredicate;

public class RenameMethodCheckerSet extends RefactoringCheckerSet{

	@Override
	public RefactoringType getRefactoringType() {
		return RefactoringType.RENAME_METHOD;
	}

	@Override
	protected XArrayList<IConditionChecker> getAllConditionCheckers() {
		XArrayList<IConditionChecker> checkerSetList = new XArrayList<IConditionChecker>();
		checkerSetList.add(new MethodNameCollisionChecker());
		return checkerSetList;
	}

	@Override
	protected XArrayList<IRefactoringEnvironment> getAllRefactoringEnvironments(RefactoringContext 
			context) throws Exception {
		Iterator<IJavaElement> it = context.getCompilationUnits();
		XArrayList<IRefactoringEnvironment> allEnvironments = new XArrayList
				<IRefactoringEnvironment>();
		
		while(it.hasNext())
		{
			IJavaElement unit = it.next();
			allEnvironments.addAll(ICompilationUnitAnalyzer.getTypes(unit).select(new IMapper
					<IJavaElement, IJavaElement>(){
				@Override
				public List<IJavaElement> map(IJavaElement t) throws Exception {
					return ITypeAnalyzer.getMethods(t);
				}}).where(new IPredicate<IJavaElement>(){
					@Override
					public boolean IsTrue(IJavaElement t) throws Exception {
						return !IMethodAnalyzer.isConstructor(t);
					}}).convert(new IConvertor<IJavaElement, IRefactoringEnvironment>(){
						@Override
						public IRefactoringEnvironment convert(IJavaElement t) throws Exception {
							return new RenameMethodEnvironment(t);
						}}));
		}
		
		return allEnvironments;
	}
	
	private class RenameMethodEnvironment extends SingleELementRefacatoringEnvironment
	{

		public RenameMethodEnvironment(IJavaElement element) {
			super(element);
		}

		@Override
		public RefactoringType getRefactoringType() {
			return RefactoringType.RENAME_METHOD;
		}
	}
	
	
	private class MethodNameCollisionChecker implements IConditionChecker
	{
		@Override
		public ICheckingResult performChecking(IRefactoringEnvironment environment) throws 
			Exception {
			IJavaElement method = ((RenameMethodEnvironment)environment).getJavaElement();
			IJavaElement type = method.getAncestor(IJavaElement.TYPE);
			XArrayList<IJavaElement> typeTree = new XArrayList<IJavaElement>();
			
			typeTree.addAll(ITypeAnalyzer.getSubTypes(type));
			typeTree.addAll(ITypeAnalyzer.getSuperTypes(type));
			typeTree.addAll(ITypeAnalyzer.getSuperInterfaces(type));
			typeTree.add(type);
			
			final C2CheckingResult result = new MethodNameCollisionCheckingResult();
		
			typeTree.select(new IMapper<IJavaElement, IJavaElement>(){
				@Override
				public List<IJavaElement> map(IJavaElement t) throws Exception {
					return ITypeAnalyzer.getMethods(t);
				}}).convert(new IConvertor<IJavaElement, IRefactoringInput>(){
					@Override
					public IRefactoringInput convert(IJavaElement t)throws Exception {
						return new MethodNameInput(t.getElementName());
					}}).operateOnElement(new IOperation<IRefactoringInput>(){
						@Override
						public void perform(IRefactoringInput t) throws Exception {
							result.addIllegalInput(t);
						}});
			return result;
		}
		
		private class MethodNameCollisionCheckingResult extends C2CheckingResult
		{
			@Override
			public ConditionType getConditionType() {
				return ConditionType.NAME_COLLISION;
			}
		}
		
		private class MethodNameInput extends StringRefactoringInput {

			public MethodNameInput(String input) {
				super(input);
			}

			@Override
			public InputType getInputType() {
				return InputType.METHOD_NAME;
			}
			
		}
	
	}
	

}
