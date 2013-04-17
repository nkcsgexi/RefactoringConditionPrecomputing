package util.tests;

import javaEventing.EventManager;
import javaEventing.interfaces.Event;
import javaEventing.interfaces.GenericEventListener;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Assert;
import org.junit.Test;

import dlf.refactoring.precondition.util.XBox;
import dlf.refactoring.precondition.util.XLoggerFactory;
import dlf.refactoring.precondition.util.XWorkItemListener;
import dlf.refactoring.precondition.util.XWorkQueue;
import dlf.refactoring.precondition.util.XWorkQueue.EmptyWorkQueueEvent;

public class WorkQueueTests {

	private final Logger logger = XLoggerFactory.GetLogger(this.getClass());
	
	@Test
	public void method1() throws InterruptedException
	{
		XWorkQueue queue = XWorkQueue.createSingleThreadWorkQueue(Thread.MIN_PRIORITY);
		final XBox<Boolean> b = new XBox<Boolean>();
		b.stuff = false;
		queue.execute(new Runnable(){
			@Override
			public void run() {
				b.stuff = true;
			}
		});
		Thread.sleep(1000);
		Assert.isTrue(b.stuff);
	}
	
	@Test
	public void method2() throws InterruptedException
	{
		XWorkQueue queue = XWorkQueue.createSingleThreadWorkQueue(Thread.MIN_PRIORITY);
		queue.execute(new Runnable() {
			@Override
			public void run() {
				
		}});
		Thread.sleep(1000);
	}
	
	
	private class TestRunnable implements Runnable
	{	
		public final int number;
		
		protected TestRunnable(int number)
		{
			this.number = number;
		}
		
		@Override
		public void run() {
			logger.info(number);
		}
	}
	
	@Test
	public void method3() throws Exception
	{
		XWorkQueue queue = XWorkQueue.createSingleThreadWorkQueue(Thread.MIN_PRIORITY);
		final Thread current = Thread.currentThread();
		queue.addWorkItemListener(new XWorkItemListener(){
			@Override
			public void runnableFinished(Runnable runnable) {
				int number = ((TestRunnable)runnable).number;
				logger.info("After:" + number);
			}});
		for(int i= 0; i<100; i++) {
			queue.execute( new TestRunnable(i));
		}
		EventManager.waitUntilTriggered(EmptyWorkQueueEvent.class, Integer.MAX_VALUE);
	}
}
