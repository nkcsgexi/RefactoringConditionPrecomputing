package javamodel.tests;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.IJavaElement;
import org.junit.Before;
import org.junit.Test;

import dlf.refactoring.enums.InputType;
import dlf.refactoring.precondition.checker.RefactoringCheckersRepository;
import dlf.refactoring.precondition.checker.environments.RefactoringContext;
import dlf.refactoring.precondition.checker.result.C2CheckingResult;
import dlf.refactoring.precondition.checker.result.RefactoringEnvironmentResults;
import dlf.refactoring.precondition.util.XArrayList;
import dlf.refactoring.precondition.util.XLoggerFactory;
import dlf.refactoring.precondition.util.XWorkQueue;
import dlf.refactoring.precondition.util.interfaces.IOperation;

public class PrecheckingTests extends RefactoringExperiment{

	private RefactoringContext context;
	private final XWorkQueue queue = XWorkQueue.createSingleThreadWorkQueue(Thread.MIN_PRIORITY);
	
	
	@Before
	public void setUp()
	{
		List<IJavaElement> contextUnits = this.getRandomUnits(10);
		this.context = new RefactoringContext();
		context.AddMultiCompilationUnits(contextUnits);
	}
	
	private class RefactoringCheckingRunnable implements Runnable
	{
		private final Logger logger;
		private final RefactoringContext context;

		protected RefactoringCheckingRunnable(RefactoringContext context)
		{
			this.context = context;
			this.logger = XLoggerFactory.GetLogger(this.getClass());
		}
		
		@Override
		public void run() {
			try{
				RefactoringCheckersRepository repository = RefactoringCheckersRepository.
						getInstance();
				XArrayList<RefactoringEnvironmentResults> results = repository.performChecking
						(context);
				logger.info("Count of results: " + results.size());
				results.operateOnElement(new IOperation<RefactoringEnvironmentResults>(){
					@Override
					public void perform(RefactoringEnvironmentResults t) throws Exception {
						logger.info("Working on refactoring environment");
						Assert.isTrue(t.getReusltsCount() > 0);
						Assert.isTrue(t.getC1Results().empty());
						XArrayList<C2CheckingResult> c2Results = t.getC2ResultByInputType
								(InputType.NEW_NAME);
						Assert.isTrue(c2Results.size() > 0);
						C2CheckingResult result = c2Results.get(0);
						logger.info("A result: " + result.toString());
					}});
			}catch(Exception e)
			{
				logger.fatal(e);
			}
		}		
	}
	
	@Test
	public void method1() throws Exception
	{
		this.queue.execute(new RefactoringCheckingRunnable(context));
		Thread.sleep(Integer.MAX_VALUE);
	}
}
