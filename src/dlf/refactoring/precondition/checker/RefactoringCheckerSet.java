package dlf.refactoring.precondition.checker;

import java.util.List;

import javaEventing.EventManager;
import javaEventing.EventObject;
import javaEventing.interfaces.Event;
import javaEventing.interfaces.GenericEventListener;

import dlf.refactoring.enums.interfaces.IHasRefactoringType;
import dlf.refactoring.precondition.checker.environments.IRefactoringEnvironment;
import dlf.refactoring.precondition.checker.environments.RefactoringContext;
import dlf.refactoring.precondition.checker.listeners.RefactoringCheckerSetListener;
import dlf.refactoring.precondition.checker.result.ICheckingResult;
import dlf.refactoring.precondition.checker.result.RefactoringEnvironmentResults;
import dlf.refactoring.precondition.util.TimedEventObject;
import dlf.refactoring.precondition.util.XArrayList;
import dlf.refactoring.precondition.util.interfaces.IConvertor;
import dlf.refactoring.precondition.util.interfaces.IMapper;


public abstract class RefactoringCheckerSet implements IHasRefactoringType{
	
	protected abstract XArrayList<IConditionChecker> getAllConditionCheckers();
	protected abstract XArrayList<IRefactoringEnvironment> getAllRefactoringEnvironments(
			RefactoringContext context) throws Exception;
	
	
	private final class StartComputeEnvironmentsEvent extends TimedEventObject<RefactoringCheckerSet>{
		public StartComputeEnvironmentsEvent(RefactoringCheckerSet information) {
			super(information);
		}}
	private final class EndComputeEnvironmentsEvent extends TimedEventObject<RefactoringCheckerSet>{
		public EndComputeEnvironmentsEvent(RefactoringCheckerSet information) {
			super(information);
		}}
	private final class StartCheckingEvent extends TimedEventObject<RefactoringCheckerSet> {
		public StartCheckingEvent(RefactoringCheckerSet information) {
			super(information);
		}}
	private final class EndCheckingEvent extends TimedEventObject<RefactoringCheckerSet> {
		public EndCheckingEvent(RefactoringCheckerSet information) {
			super(information);
		}}
	
	
	
	
	private final XArrayList<IRefactoringEnvironment> computeRefactoringEnvironments(
			RefactoringContext context) throws Exception
	{
		EventManager.triggerEvent(this, new StartComputeEnvironmentsEvent(this));
		XArrayList<IRefactoringEnvironment> result = getAllRefactoringEnvironments(context);
		EventManager.triggerEvent(this, new EndComputeEnvironmentsEvent(this));
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
		EventManager.triggerEvent(this, new StartCheckingEvent(this));
		
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
		EventManager.triggerEvent(this, new EndCheckingEvent(this));
		return results;
	}
	
	
	
	public final void addCheckingSetListener(final RefactoringCheckerSetListener lis)
	{	
		final RefactoringCheckerSet thisChecker = this;
		
		EventManager.registerEventListener(new GenericEventListener(){
			@Override
			public void eventTriggered(Object arg0, Event arg1) {
				if(arg0 == thisChecker) {
					lis.calculateEnvironmentStart((TimedEventObject<RefactoringCheckerSet>) arg1);
				}
			}}, StartComputeEnvironmentsEvent.class);
		
		EventManager.registerEventListener(new GenericEventListener(){
			@Override
			public void eventTriggered(Object arg0, Event arg1) {
				if(arg0 == thisChecker) {
					lis.calculateEnvironmentEnd((TimedEventObject<RefactoringCheckerSet>) arg1);
				}
			}}, EndComputeEnvironmentsEvent.class);
		
		EventManager.registerEventListener(new GenericEventListener(){
			@Override
			public void eventTriggered(Object arg0, Event arg1) {
				if(arg0 == thisChecker) {
					lis.performCheckingStart((TimedEventObject<RefactoringCheckerSet>) arg1);
				}
			}}, StartCheckingEvent.class);
		
		EventManager.registerEventListener(new GenericEventListener(){
			@Override
			public void eventTriggered(Object arg0, Event arg1) {
				if(arg0 == thisChecker) {
					lis.performCheckingEnd((TimedEventObject<RefactoringCheckerSet>) arg1);
				}
			}}, EndCheckingEvent.class);
	}
	
}
