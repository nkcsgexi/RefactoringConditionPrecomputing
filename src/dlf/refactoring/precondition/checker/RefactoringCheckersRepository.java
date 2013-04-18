package dlf.refactoring.precondition.checker;

import java.util.List;

import org.apache.log4j.Logger;

import javaEventing.interfaces.Event;
import javaEventing.interfaces.GenericEventListener;

import dlf.refactoring.precondition.checker.environments.IRefactoringEnvironment;
import dlf.refactoring.precondition.checker.environments.RefactoringContext;
import dlf.refactoring.precondition.checker.result.ICheckingResult;
import dlf.refactoring.precondition.checker.result.RefactoringEnvironmentResults;
import dlf.refactoring.precondition.util.XArrayList;
import dlf.refactoring.precondition.util.XLoggerFactory;
import dlf.refactoring.precondition.util.interfaces.IConvertor;
import dlf.refactoring.precondition.util.interfaces.IMapper;
import dlf.refactoring.precondition.util.interfaces.IOperation;


public class RefactoringCheckersRepository {
	private final XArrayList<RefactoringCheckerSet> checkerSets;
	private final Logger logger;
	
	private RefactoringCheckersRepository()
	{
		this.checkerSets = new XArrayList<RefactoringCheckerSet>();
		this.logger = XLoggerFactory.GetLogger(this.getClass());

		// add new refactoring checker set here.
		checkerSets.add(addRefactoringCheckerSetListner(new RenameClassCheckerSet()));
		checkerSets.add(addRefactoringCheckerSetListner(new PushDownMethodCheckerSet()));
		checkerSets.add(addRefactoringCheckerSetListner(new RenameMethodCheckerSet()));
	}
	
	public RefactoringCheckerSet addRefactoringCheckerSetListner(RefactoringCheckerSet set)
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
	
	
	private static RefactoringCheckersRepository instance;
	
	public static RefactoringCheckersRepository getInstance()
	{
		if(instance == null)
			instance = new RefactoringCheckersRepository();
		return instance;
	}
	
	public XArrayList<RefactoringEnvironmentResults> performChecking(final RefactoringContext 
			context) throws Exception
	{
		return checkerSets.select(new IMapper<RefactoringCheckerSet,RefactoringEnvironmentResults>()
		{
			@Override
			public List<RefactoringEnvironmentResults> map(final RefactoringCheckerSet checkers)
					throws Exception {
				return checkers.checkingRefactoringContext(context);
		}});
	}
	
	
}
