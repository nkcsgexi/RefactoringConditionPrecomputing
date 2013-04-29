package dlf.refactoring.precondition.checker;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.ltk.core.refactoring.Refactoring;

import dlf.refactoring.StructuralRefactoringAPIs;
import dlf.refactoring.enums.ConditionType;
import dlf.refactoring.enums.InputType;
import dlf.refactoring.enums.RefactoringType;
import dlf.refactoring.precondition.JavaModelAnalyzers.ICompilationUnitAnalyzer;
import dlf.refactoring.precondition.JavaModelAnalyzers.IJavaElementAnalyzer;
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
import dlf.refactoring.precondition.util.interfaces.IOperation;
import dlf.refactoring.precondition.util.interfaces.IPredicate;
import dlf.refactoring.precondition.util.interfaces.Pair;

public class ExtractInterfaceCheckerSet extends RefactoringCheckerSet{

	@Override
	public RefactoringType getRefactoringType() {
		return RefactoringType.EXTRACT_INTERFACE;
	}

	@Override
	protected XArrayList<IConditionChecker> getAllConditionCheckers() {
		XArrayList<IConditionChecker> checkerList = new XArrayList<IConditionChecker>(); 
		checkerList.add(new IConditionChecker(){			
			@Override
			public ICheckingResult performChecking(IRefactoringEnvironment environment) throws 
					Exception {
				final IJavaElement type = ((SingleELementRefacatoringEnvironment)environment).
						getJavaElement();
				final C2CheckingResult results = new C2CheckingResult(){
					@Override
					public ConditionType getConditionType() {
						return ConditionType.METHODD_EXTRACTABLE_TO_INTERFACE;
					}};
				ITypeAnalyzer.getMethods(type).convert(new IConvertor<IJavaElement, Pair<
						IJavaElement, Refactoring>>(){
					@Override
					public Pair<IJavaElement, Refactoring> convert(IJavaElement t) throws Exception {
						return new Pair<IJavaElement, Refactoring>(t, StructuralRefactoringAPIs.
								createExtractInterfaceRefactoring(type, new IMember[]{(IMember) t}));
					}}).where(new IPredicate<Pair<IJavaElement, Refactoring>>(){
						@Override
						public boolean IsTrue(Pair<IJavaElement, Refactoring> t) throws Exception {
							return !t.getSecond().checkAllConditions(new NullProgressMonitor()).isOK();
						}}).convert(new IConvertor<Pair<IJavaElement, Refactoring>, IRefactoringInput>(){
							@Override
							public IRefactoringInput convert(final Pair<IJavaElement, Refactoring> t) 
									throws Exception {
								return new ExtractInterfaceMemberNameInput(t.getFirst().
										getElementName());
							}}).operateOnElement(new IOperation<IRefactoringInput>(){
								@Override
								public void perform(IRefactoringInput t) throws Exception {
									results.addIllegalInput(t);
								}});
				return results;
			}});
		return checkerList;
	}
	
	private class ExtractInterfaceMemberNameInput extends StringRefactoringInput {
				
		public ExtractInterfaceMemberNameInput(String input) {
			super(input);
		}

		@Override
		public InputType getInputType() {
			return InputType.MEMBER_NAME;
		}
		
	}

	@Override
	protected XArrayList<IRefactoringEnvironment> getAllRefactoringEnvironments(RefactoringContext 
			context) throws Exception {
		XArrayList<IRefactoringEnvironment> allEnvironments = new XArrayList
				<IRefactoringEnvironment>();
		Iterator<IJavaElement> it = context.getCompilationUnits();
		while(it.hasNext()) {
			IJavaElement unit = it.next();
			allEnvironments.addAll(ICompilationUnitAnalyzer.getTypes(unit).convert(new IConvertor
					<IJavaElement, IRefactoringEnvironment>() {
						@Override
						public IRefactoringEnvironment convert(IJavaElement t) throws Exception {
							return new SingleELementRefacatoringEnvironment(t){
								@Override
								public RefactoringType getRefactoringType() {
									return RefactoringType.EXTRACT_INTERFACE;
								}};
						}}));
		}
		return allEnvironments;
	}
	

	

}
