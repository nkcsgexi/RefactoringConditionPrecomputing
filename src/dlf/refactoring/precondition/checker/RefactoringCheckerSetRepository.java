package dlf.refactoring.precondition.checker;

import java.util.List;

import org.apache.log4j.Logger;

import javaEventing.EventManager;
import javaEventing.EventObject;
import javaEventing.interfaces.Event;
import javaEventing.interfaces.GenericEventListener;

import dlf.refactoring.precondition.checker.environments.IRefactoringEnvironment;
import dlf.refactoring.precondition.checker.environments.RefactoringContext;
import dlf.refactoring.precondition.checker.listeners.RefactoringCheckerSetListener;
import dlf.refactoring.precondition.checker.listeners.RefactoringCheckerSetRepositoryListener;
import dlf.refactoring.precondition.checker.result.ICheckingResult;
import dlf.refactoring.precondition.checker.result.RefactoringEnvironmentResults;
import dlf.refactoring.precondition.util.TimedEventObject;
import dlf.refactoring.precondition.util.XArrayList;
import dlf.refactoring.precondition.util.XLoggerFactory;
import dlf.refactoring.precondition.util.interfaces.IConvertor;
import dlf.refactoring.precondition.util.interfaces.IMapper;
import dlf.refactoring.precondition.util.interfaces.IOperation;


public class RefactoringCheckerSetRepository {
	private final XArrayList<RefactoringCheckerSet> checkerSets;
	private final Logger logger;
	
	private RefactoringCheckerSetRepository()
	{
		this.checkerSets = new XArrayList<RefactoringCheckerSet>();
		this.logger = XLoggerFactory.GetLogger(this.getClass());

		// add new refactoring checker set here.
		checkerSets.add(addRefactoringCheckerSetListner(new RenameClassCheckerSet()));
		checkerSets.add(addRefactoringCheckerSetListner(new PushDownMethodCheckerSet()));
		checkerSets.add(addRefactoringCheckerSetListner(new RenameMethodCheckerSet()));
		checkerSets.add(addRefactoringCheckerSetListner(new PullupMethodCheckerSet()));
	}
	
	private RefactoringCheckerSet addRefactoringCheckerSetListner(RefactoringCheckerSet set)
	{
		set.addCheckingSetListener(new RefactoringCheckerSetListener(){
			private long checkingStart;
			private long checkingDuration;
			private long calculatingStart;
			private long calculatingDuration;
			
			@Override
			public void performCheckingStart(RefactoringCheckerSet set) {
				this.checkingStart = System.currentTimeMillis();
			}

			@Override
			public void performCheckingEnd(RefactoringCheckerSet set) {
				this.checkingDuration = System.currentTimeMillis() - this.checkingStart;
				logger.info("Checking conditions for " + set.getRefactoringType().name() + ":" + 
						this.checkingDuration);
			}

			@Override
			public void calculateEnvironmentStart(RefactoringCheckerSet set) {
				this.calculatingStart = System.currentTimeMillis();
			}

			@Override
			public void calculateEnvironmentEnd(RefactoringCheckerSet set) {
				this.calculatingDuration = System.currentTimeMillis() - this.calculatingStart;
			//	logger.info("Calculating environments for " + set.getRefactoringType().name() + ":" 
			//			+ this.calculatingDuration);
			}});
		return set;
	}
	
	
	private static RefactoringCheckerSetRepository instance;
	
	public static RefactoringCheckerSetRepository getInstance()
	{
		if(instance == null)
			instance = new RefactoringCheckerSetRepository();
		return instance;
	}
	
	private final class CheckingContextStartEvent extends TimedEventObject<RefactoringContext>{
		public CheckingContextStartEvent(RefactoringContext information) {
			super(information);
		}}
	private final class CheckingContextEndEvent extends TimedEventObject<RefactoringContext>{
		public CheckingContextEndEvent(RefactoringContext information) {
			super(information);
		}}
	
	public XArrayList<RefactoringEnvironmentResults> performChecking(final RefactoringContext 
			context) throws Exception
	{
		EventManager.triggerEvent(this, new CheckingContextStartEvent(context));
		XArrayList<RefactoringEnvironmentResults> results = checkerSets.select(new IMapper
				<RefactoringCheckerSet,RefactoringEnvironmentResults>()
		{
			@Override
			public List<RefactoringEnvironmentResults> map(final RefactoringCheckerSet checkers)
					throws Exception {
				return checkers.checkingRefactoringContext(context);
		}});
		EventManager.triggerEvent(this, new CheckingContextEndEvent(context));
		return results;
	}
	
	
	public void addRefactoringCheckerSetRepositoryListener(final 
			RefactoringCheckerSetRepositoryListener listener)
	{
		EventManager.registerEventListener(new GenericEventListener() {
			@Override
			public void eventTriggered(Object arg0, Event arg1) {
				listener.startCheckingContext((TimedEventObject) arg1);
			}}, CheckingContextStartEvent.class);
		
		EventManager.registerEventListener(new GenericEventListener() {
			@Override
			public void eventTriggered(Object arg0, Event arg1) {
				listener.endCheckingContext((TimedEventObject) arg1);
			}}, CheckingContextEndEvent.class);
	}
}
