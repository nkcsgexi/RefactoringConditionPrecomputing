package dlf.refactoring.precondition.checker;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;

import dlf.refactoring.StructuralRefactoringAPIs;
import dlf.refactoring.enums.ConditionType;
import dlf.refactoring.enums.RefactoringType;
import dlf.refactoring.precondition.JavaModelAnalyzers.ICompilationUnitAnalyzer;
import dlf.refactoring.precondition.JavaModelAnalyzers.ITypeAnalyzer;
import dlf.refactoring.precondition.checker.environments.IRefactoringEnvironment;
import dlf.refactoring.precondition.checker.environments.RefactoringContext;
import dlf.refactoring.precondition.checker.environments.SingleELementRefacatoringEnvironment;
import dlf.refactoring.precondition.checker.result.C1CheckingResult;
import dlf.refactoring.precondition.checker.result.ICheckingResult;
import dlf.refactoring.precondition.util.XArrayList;
import dlf.refactoring.precondition.util.interfaces.IConvertor;
import dlf.refactoring.precondition.util.interfaces.IMapper;

public class PushDownMethodCheckerSet extends RefactoringCheckerSet{

	@Override
	public RefactoringType getRefactoringType() {
		return RefactoringType.PUSH_DOWN_METHOD;
	}

	@Override
	protected XArrayList<IConditionChecker> getAllConditionCheckers() {
		XArrayList<IConditionChecker> checkers = new XArrayList<IConditionChecker>();
		checkers.add(new PushableMethodChecker());
		return checkers;
	}

	@Override
	protected XArrayList<IRefactoringEnvironment> getAllRefactoringEnvironments(RefactoringContext 
			context) throws Exception {
		XArrayList<IRefactoringEnvironment> allEnvs = new XArrayList<IRefactoringEnvironment>();
		Iterator<IJavaElement> iterator = context.getCompilationUnits();
		while(iterator.hasNext()) {
			IJavaElement unit = iterator.next();
			allEnvs.addAll(ICompilationUnitAnalyzer.getTypes(unit).select(new IMapper<IJavaElement,
					IJavaElement>() {
				@Override
				public List<IJavaElement> map(IJavaElement t) throws Exception {
					return ITypeAnalyzer.getMethods(t);
				}}).convert(new IConvertor<IJavaElement, IRefactoringEnvironment>() {
					@Override
					public PushDownMethodEnvironment convert(IJavaElement t) throws Exception {
						return new PushDownMethodEnvironment(t);
					}}));
		}
		return allEnvs;
	}
	
	private class PushDownMethodEnvironment extends SingleELementRefacatoringEnvironment {

		public PushDownMethodEnvironment(IJavaElement element) {
			super(element);
		}

		@Override
		public RefactoringType getRefactoringType() {
			return RefactoringType.PUSH_DOWN_METHOD;
		}
		
	}
	
	
	private class PushableMethodChecker implements IConditionChecker {
		@Override
		public ICheckingResult performChecking(IRefactoringEnvironment environment) throws 
			Exception {
			IJavaElement method = ((PushDownMethodEnvironment) environment).getJavaElement();
			Refactoring refactoring = StructuralRefactoringAPIs.createPushDownRefactoring(new 
					IMember[]{(IMember) method});
			RefactoringStatus result = refactoring.checkAllConditions(new NullProgressMonitor());
			return new PushableMethodResult(result.isOK(), environment);
		}
		
		private class PushableMethodResult extends C1CheckingResult {
			public PushableMethodResult(boolean isOK, IRefactoringEnvironment environment) {
				super(isOK, environment);
			}

			@Override
			public ConditionType getConditionType() {
				return ConditionType.PUSHABLE_METHOD;
			}
		}
	}
}
