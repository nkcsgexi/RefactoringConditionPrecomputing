package util.tests;

import org.eclipse.core.runtime.Assert;
import org.junit.Test;

import util.XBox;
import util.XWorkQueue;


public class WorkQueueTests {

	@Test
	public void method1() throws InterruptedException
	{
		XWorkQueue queue = XWorkQueue.GetSingleThreadWorkQueue();
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
		XWorkQueue queue = XWorkQueue.GetSingleThreadWorkQueue();
		queue.execute(new Runnable() {
			@Override
			public void run() {
				
		}});
		Thread.sleep(1000);
	
	}
}
