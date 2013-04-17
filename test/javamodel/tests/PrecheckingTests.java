package javamodel.tests;

import java.util.Collection;
import java.util.List;

import javaEventing.EventManager;
import javaEventing.EventObject;
import javaEventing.interfaces.Event;
import javaEventing.interfaces.GenericEventListener;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.junit.Before;
import org.junit.Test;

import util.tests.WorkQueueTests.WorkFinishEvent;

import dlf.git.GitProject;
import dlf.git.IVisitRevisionDiffStrategy;
import dlf.refactoring.enums.InputType;
import dlf.refactoring.precondition.checker.RefactoringCheckersRepository;
import dlf.refactoring.precondition.checker.environments.RefactoringContext;
import dlf.refactoring.precondition.checker.result.C2CheckingResult;
import dlf.refactoring.precondition.checker.result.RefactoringEnvironmentResults;
import dlf.refactoring.precondition.util.XArrayList;
import dlf.refactoring.precondition.util.XLoggerFactory;
import dlf.refactoring.precondition.util.XWorkQueue;
import dlf.refactoring.precondition.util.interfaces.IOperation;
import dlf.refactoring.precondition.util.interfaces.IPredicate;

public class PrecheckingTests extends RefactoringExperiment{

	private static String directory = "C:\\Users\\xige\\Desktop\\test";
	private final XWorkQueue queue = XWorkQueue.createSingleThreadWorkQueue(Thread.MIN_PRIORITY);
	
	private class DiffVisitor implements IVisitRevisionDiffStrategy
	{
		private final Logger logger = XLoggerFactory.GetLogger(this.getClass());
		private int revisionNumber = 0;
		
		private XArrayList<IJavaElement> getICompilationUnitsByNames(final Collection<String> names) 
				throws Exception {
			return compilationUnits.where(new IPredicate<IJavaElement>(){
				@Override
				public boolean IsTrue(IJavaElement t) throws Exception {
					//logger.info(t.getElementName());
					return names.contains(t.getElementName());
				}});	
		}
		
		@Override
		public void visitDiffTrees(TreeWalk tree) throws Exception {
			XArrayList<String> nameList = new XArrayList<String>();
			while(tree.next())
			{
				if(tree.getNameString().endsWith(".java")) {
					nameList.add(tree.getNameString());
				}
			}
			RefactoringContext context = new RefactoringContext();
			context.AddMultiCompilationUnits(getICompilationUnitsByNames(nameList));
			queue.execute(new RefactoringCheckingRunnable(context));
		}	
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
				logger.info("==================================================");
			}catch(Exception e)
			{
				logger.fatal(e);
			}
		}		
	}
	
	private class FinishWorkEvent extends EventObject{}
	
	@Test
	public void method1() throws Exception
	{
		GitProject project = new GitProject(directory, "prechecking");
		project.walkRevisionDiffs(new DiffVisitor());
		queue.addEmptyQueueEventListener(new GenericEventListener(){
			@Override
			public void eventTriggered(Object arg0, Event arg1) {
				EventManager.triggerEvent(arg0, new FinishWorkEvent());
			}});
		EventManager.waitUntilTriggered(FinishWorkEvent.class, Integer.MAX_VALUE);
	}
}
