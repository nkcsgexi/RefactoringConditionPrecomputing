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

public class PullupMethodCheckerSet extends RefactoringCheckerSet{

	@Override
	public RefactoringType getRefactoringType() {
		return RefactoringType.PULL_UP_METHOD;
	}

	@Override
	protected XArrayList<IConditionChecker> getAllConditionCheckers() {
		XArrayList<IConditionChecker> checkerList = new XArrayList<IConditionChecker>();
		checkerList.add(new PullableMethodChecker());
		return checkerList;
	}

	@Override
	protected XArrayList<IRefactoringEnvironment> getAllRefactoringEnvironments(RefactoringContext 
			context) throws Exception {
		XArrayList<IRefactoringEnvironment> environments = new XArrayList<IRefactoringEnvironment>();
		Iterator<IJavaElement> it = context.getCompilationUnits();
		while(it.hasNext())
		{
			environments.addAll(ICompilationUnitAnalyzer.getTypes(it.next()).select(new IMapper
					<IJavaElement, IJavaElement>(){
				@Override
				public List<IJavaElement> map(IJavaElement t) throws Exception {
					return ITypeAnalyzer.getMethods(t);
				}}).convert(new IConvertor<IJavaElement, IRefactoringEnvironment>(){
					@Override
					public IRefactoringEnvironment convert(IJavaElement t) throws Exception {
						return new PullupMethodEnvironment(t);
					}}));
		}
		
		return environments;
	}
	
	
	private class PullableMethodChecker implements IConditionChecker
	{

		@Override
		public ICheckingResult performChecking(IRefactoringEnvironment environment) throws Exception {
			IMember member = (IMember) ((PullupMethodEnvironment)environment).getJavaElement();
			Refactoring refactoring = StructuralRefactoringAPIs.createPullUpRefactoring(new 
					IMember[]{member});
			RefactoringStatus result = refactoring.checkAllConditions(new NullProgressMonitor());
			return new C1CheckingResult(result.isOK(), environment){
				@Override
				public ConditionType getConditionType() {
					return ConditionType.PULLABLE_METHOD;
				}};
		}
	}
	
	private class PullupMethodEnvironment extends SingleELementRefacatoringEnvironment
	{

		public PullupMethodEnvironment(IJavaElement element) {
			super(element);
		}

		@Override
		public RefactoringType getRefactoringType() {
			return RefactoringType.PULL_UP_METHOD;
		}
		
	}

}
