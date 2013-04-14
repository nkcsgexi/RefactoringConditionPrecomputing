package dlf.refactoring.precondition.checker;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;

import dlf.refactoring.enums.ConditionType;
import dlf.refactoring.enums.InputType;
import dlf.refactoring.enums.RefactoringType;
import dlf.refactoring.precondition.JavaModelAnalyzers.ICompilationUnitAnalyzer;
import dlf.refactoring.precondition.JavaModelAnalyzers.IJavaElementAnalyzer;
import dlf.refactoring.precondition.JavaModelAnalyzers.IPackageFragmentAnalyzer;
import dlf.refactoring.precondition.checker.environments.IRefactoringEnvironment;
import dlf.refactoring.precondition.checker.environments.IRefactoringInput;
import dlf.refactoring.precondition.checker.environments.RefactoringContext;
import dlf.refactoring.precondition.checker.environments.SingleELementRefacatoringEnvironment;
import dlf.refactoring.precondition.checker.environments.StringRefactoringInput;
import dlf.refactoring.precondition.checker.result.C2CheckingResult;
import dlf.refactoring.precondition.checker.result.ICheckingResult;
import dlf.refactoring.precondition.util.XArrayList;
import dlf.refactoring.precondition.util.XLoggerFactory;
import dlf.refactoring.precondition.util.interfaces.IConvertor;
import dlf.refactoring.precondition.util.interfaces.IMapper;
import dlf.refactoring.precondition.util.interfaces.IOperation;

public class RenameClassCheckerSet extends RefactoringCheckerSet{
	
	@Override
	protected XArrayList<IConditionChecker> getAllConditionCheckers() {
		XArrayList<IConditionChecker> list = new XArrayList<IConditionChecker>();
		list.add(new ClassNameCollisionChecker());
		return list;
	}

	@Override
	protected XArrayList<IRefactoringEnvironment> getAllRefactoringEnvironments(
		RefactoringContext context) throws Exception 
	{
		XArrayList<IRefactoringEnvironment> environments = new XArrayList<IRefactoringEnvironment>(); 
		Iterator<IJavaElement> it = context.getCompilationUnits();
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
	
	@Override
	public RefactoringType getRefactoringType() {
		return RefactoringType.RENAME_CLASS;
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
	
	
	private class ClassNameCollisionChecker implements IConditionChecker
	{
		
		Logger logger = XLoggerFactory.GetLogger(this.getClass());
		
		@Override
		public ICheckingResult performChecking(IRefactoringEnvironment environment) throws Exception {
			final C2CheckingResult result = new ClassNameCollisionResult();
			IJavaElement classUnderRename = ((RenameClassEnvironment) environment).getJavaElement();	
			IJavaElement pack = IJavaElementAnalyzer.getAncestorsByType(classUnderRename, 
					IJavaElement.PACKAGE_FRAGMENT).get(0);
			
			IPackageFragmentAnalyzer.getICompilationUnits(pack).select(
				new IMapper<IJavaElement, IJavaElement>(){
					@Override
					public List<IJavaElement> map(IJavaElement t)
							throws Exception {
						return ICompilationUnitAnalyzer.getTypes(t);
					}}).operateOnElement(new IOperation<IJavaElement>(){
						@Override
						public void perform(IJavaElement t){
							result.addIllegalInput(createNewNameInput(t.getElementName()));
						}});
			return result;
		}
		
		private class ClassNameCollisionResult extends C2CheckingResult
		{			
			@Override
			public ConditionType getConditionType() {
				return ConditionType.NAME_COLLISION;
			}
		}
		
		private IRefactoringInput createNewNameInput(String s)
		{
			return new StringRefactoringInput(s){
				@Override
				public InputType getInputType() {
					return InputType.NEW_NAME;
				}};
		}
	}


	
}
