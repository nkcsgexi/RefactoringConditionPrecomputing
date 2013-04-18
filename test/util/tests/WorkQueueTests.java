package util.tests;

import javaEventing.EventManager;
import javaEventing.EventObject;
import javaEventing.interfaces.Event;
import javaEventing.interfaces.GenericEventListener;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Assert;
import org.junit.Test;

import dlf.refactoring.precondition.util.XBox;
import dlf.refactoring.precondition.util.XLoggerFactory;
import dlf.refactoring.precondition.util.XWorkItemListener;
import dlf.refactoring.precondition.util.XWorkQueue;
import dlf.refactoring.precondition.util.interfaces.IRunnable;

public class WorkQueueTests {

	private final Logger logger = XLoggerFactory.GetLogger(this.getClass());
	
	@Test
	public void method1() throws InterruptedException
	{
		XWorkQueue queue = XWorkQueue.createSingleThreadWorkQueue(Thread.MIN_PRIORITY);
		final XBox<Boolean> b = new XBox<Boolean>();
		b.stuff = false;
		queue.execute(new IRunnable(){
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
		queue.execute(new IRunnable() {
			@Override
			public void run() {
				
		}});
		Thread.sleep(1000);
	}
	
	
	private class TestRunnable implements IRunnable
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
	
	private class WorkFinishEvent extends EventObject{}
	
	@Test
	public void method3() throws Exception
	{
		XWorkQueue queue = XWorkQueue.createSingleThreadWorkQueue(Thread.MIN_PRIORITY);
		final Thread current = Thread.currentThread();
		
		queue.addEmptyQueueEventListener(new GenericEventListener(){
			@Override
			public void eventTriggered(Object arg0, Event arg1) {
				logger.info("Work queue is empty.");
				EventManager.triggerEvent(this, new WorkFinishEvent());
			}});
		queue.addWorkItemListener(new XWorkItemListener(){
			@Override
			public void runnableFinished(IRunnable runnable) {
				int number = ((TestRunnable)runnable).number;
				logger.info("After:" + number);
			}});
		for(int i= 0; i<100; i++) {
			queue.execute(new TestRunnable(i));
		}
		EventManager.waitUntilTriggered(WorkFinishEvent.class, Integer.MAX_VALUE);
	}
	
	private class event1 extends EventObject{}
	private class event2 extends EventObject{}
	
	@Test
	public void method4() throws Exception
	{
		logger.info("Start thread is:" + Thread.currentThread().getId());
		
		EventManager.registerEventListener(new GenericEventListener(){
			@Override
			public void eventTriggered(Object arg0, Event arg1) {
				logger.info("Handling event 1 thread:" + Thread.currentThread().getId());
			}}, event1.class);
		EventManager.registerEventListener(new GenericEventListener(){
			@Override
			public void eventTriggered(Object arg0, Event arg1) {
				logger.info("Handling event 2 thread:" + Thread.currentThread().getId());
			}}, event2.class);
		for(int i = 0; i< 100; i++) {
			EventManager.triggerEvent(this, new event1());
			EventManager.triggerEvent(this, new event2());
		}
	}
	
}
