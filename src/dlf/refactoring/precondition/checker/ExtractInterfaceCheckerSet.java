package dlf.refactoring.precondition.checker;

import java.util.Iterator;

import org.eclipse.jdt.core.IJavaElement;

import dlf.refactoring.enums.RefactoringType;
import dlf.refactoring.precondition.checker.environments.IRefactoringEnvironment;
import dlf.refactoring.precondition.checker.environments.RefactoringContext;
import dlf.refactoring.precondition.util.XArrayList;

public class ExtractInterfaceCheckerSet extends RefactoringCheckerSet{

	@Override
	public RefactoringType getRefactoringType() {
		return RefactoringType.EXTRACT_INTERFACE;
	}

	@Override
	protected XArrayList<IConditionChecker> getAllConditionCheckers() {
		XArrayList<IConditionChecker> checkerList = new XArrayList<IConditionChecker>(); 
		
		return checkerList;
	}

	@Override
	protected XArrayList<IRefactoringEnvironment> getAllRefactoringEnvironments(RefactoringContext 
			context) throws Exception {
		Iterator<IJavaElement> it = context.getCompilationUnits();
		
		return null;
	}

}
