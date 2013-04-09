package dlf.refactoring.precondition.checker;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;

import dlf.refactoring.enums.ConditionType;
import dlf.refactoring.enums.RefactoringType;
import dlf.refactoring.precondition.JavaModelAnalyzers.ICompilationUnitAnalyzer;
import dlf.refactoring.precondition.checker.environments.IRefactoringEnvironment;
import dlf.refactoring.precondition.checker.environments.RefactoringContext;
import dlf.refactoring.precondition.checker.environments.SingleELementRefacatoringEnvironment;
import dlf.refactoring.precondition.checker.result.C2CheckingResult;
import dlf.refactoring.precondition.checker.result.ICheckingResult;
import dlf.refactoring.precondition.util.XArrayList;
import dlf.refactoring.precondition.util.interfaces.IConvertor;
import dlf.refactoring.precondition.util.interfaces.IMapper;

public class RenameClassCheckers extends RefactoringCheckerSet{
	
	@Override
	protected XArrayList<IConditionChecker> getAllConditionCheckers() {
		XArrayList<IConditionChecker> list = new XArrayList<IConditionChecker>();
		list.add(new NameCollisionChecker());
		return list;
	}

	@Override
	public XArrayList<IRefactoringEnvironment> getAllRefactoringEnvironments(
		RefactoringContext context) throws Exception 
	{
		XArrayList<IRefactoringEnvironment> environments = new XArrayList<IRefactoringEnvironment>(); 
		Iterator<ICompilationUnit> it = context.getCompilationUnits();
		for(;it.hasNext();)
		{
			IJavaElement unit = it.next();
			environments.addAll(ICompilationUnitAnalyzer.getTypes(unit).convert(new IConvertor
					<IJavaElement, RenameClassEnvironment>(){
				@Override
				public RenameClassEnvironment convert(IJavaElement t)throws Exception {
					return new RenameClassEnvironment(t);
				}}));
			
		}
		return environments;
	}
	
	private class RenameClassEnvironment extends SingleELementRefacatoringEnvironment
	{
		public RenameClassEnvironment(IJavaElement element) {
			super(element);
		}

		@Override
		public RefactoringType getRefactoringType() {
			return RefactoringType.RENAME_CLASS;
		}
	}
	
	
	private class NameCollisionChecker implements IConditionChecker
	{
		@Override
		public ICheckingResult performChecking(IRefactoringEnvironment environment) {
			C2CheckingResult result = new NameCollisionResult();
			return result;
		}
		
		private class NameCollisionResult extends C2CheckingResult
		{
			@Override
			public ConditionType getConditionType() {
				return ConditionType.NAME_COLLISION;
			}
		}
	}
}
