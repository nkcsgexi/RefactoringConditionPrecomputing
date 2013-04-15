package dlf.refactoring.precondition.checker;

import java.util.List;

import javaEventing.EventManager;
import javaEventing.EventObject;
import javaEventing.interfaces.Event;
import javaEventing.interfaces.GenericEventListener;

import dlf.refactoring.enums.interfaces.IHasRefactoringType;
import dlf.refactoring.precondition.checker.environments.IRefactoringEnvironment;
import dlf.refactoring.precondition.checker.environments.RefactoringContext;
import dlf.refactoring.precondition.checker.result.ICheckingResult;
import dlf.refactoring.precondition.checker.result.RefactoringEnvironmentResults;
import dlf.refactoring.precondition.util.XArrayList;
import dlf.refactoring.precondition.util.interfaces.IConvertor;
import dlf.refactoring.precondition.util.interfaces.IMapper;


public abstract class RefactoringCheckerSet implements IHasRefactoringType{
	
	protected abstract XArrayList<IConditionChecker> getAllConditionCheckers();
	protected abstract XArrayList<IRefactoringEnvironment> getAllRefactoringEnvironments(
			RefactoringContext context) throws Exception;
	
	
	private final XArrayList<IRefactoringEnvironment> computeRefactoringEnvironments(
			RefactoringContext context) throws Exception
	{
		EventManager.triggerEvent(this, new calculateEnvironmentsStart());
		XArrayList<IRefactoringEnvironment> result = getAllRefactoringEnvironments(context);
		EventManager.triggerEvent(this, new calculateEnvironmentsEnd());
		return result;
	}
	
	private final XArrayList<ICheckingResult> checkingRefactoringEnvironment(final 
			IRefactoringEnvironment environment) throws Exception
	{
		final XArrayList<IConditionChecker> checkers = getAllConditionCheckers();
		XArrayList<ICheckingResult> results = checkers.convert(new IConvertor<IConditionChecker, 
				ICheckingResult>(){
			@Override
			public ICheckingResult convert(final IConditionChecker c)throws Exception {
				return c.performChecking(environment);
			}});
		return results;
	}
	
	
	public final XArrayList<RefactoringEnvironmentResults> checkingRefactoringContext(
			RefactoringContext context) throws Exception
	{
		EventManager.triggerEvent(this, new checkingStartEvent());
		
		// Get the environment first
		XArrayList<IRefactoringEnvironment> environments = this.computeRefactoringEnvironments
				(context);
		
		// For each environment, calculate the checking results associated with it.
		XArrayList<RefactoringEnvironmentResults> results = environments.convert(new IConvertor
				<IRefactoringEnvironment, RefactoringEnvironmentResults>(){
			@Override
			public RefactoringEnvironmentResults convert(IRefactoringEnvironment env) throws 
					Exception {
				RefactoringEnvironmentResults envRes = new RefactoringEnvironmentResults (env);
				envRes.addMultiCheckingResults(checkingRefactoringEnvironment(env));
				return envRes;
			}});
		EventManager.triggerEvent(this, new checkingEndEvent());
		return results;
	}
	
	
	private class calculateEnvironmentsStart extends EventObject{}
	private class calculateEnvironmentsEnd extends EventObject{}
	
	private class checkingStartEvent extends EventObject{}
	private class checkingEndEvent extends EventObject{}
	
	public final void addCheckingSetListener(final RefactoringCheckerSetListener lis)
	{
		final RefactoringCheckerSet set = this;
		
		EventManager.registerEventListener(new GenericEventListener(){
			@Override
			public void eventTriggered(Object arg0, Event arg1) {
				lis.calculateEnvironmentStart(set);
			}}, calculateEnvironmentsStart.class);
		
		EventManager.registerEventListener(new GenericEventListener(){
			@Override
			public void eventTriggered(Object arg0, Event arg1) {
				lis.calculateEnvironmentEnd(set);
			}}, calculateEnvironmentsEnd.class);
		
		EventManager.registerEventListener(new GenericEventListener(){
			@Override
			public void eventTriggered(Object arg0, Event arg1) {
				lis.performCheckingStart(set);
			}}, checkingStartEvent.class);
		
		EventManager.registerEventListener(new GenericEventListener(){
			@Override
			public void eventTriggered(Object arg0, Event arg1) {
				lis.performCheckingEnd(set);
			}}, checkingEndEvent.class);
	}
	
}
