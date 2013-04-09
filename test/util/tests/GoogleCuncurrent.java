package util.tests;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

import org.eclipse.core.runtime.Assert;
import org.junit.Test;

import com.google.common.util.concurrent.AbstractExecutionThreadService;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.Service.State;
import com.google.common.util.concurrent.ServiceManager;
import com.google.common.util.concurrent.AbstractService;
import dlf.refactoring.precondition.util.WorkQueue;

public class GoogleCuncurrent {

	
	@Test
	public void method1()
	{
		Service s = new  AbstractExecutionThreadService(){
			@Override
			protected void run() throws Exception {
				for(int i = 0;; i++)
				{
					System.out.println(i);
				}
			}};
		s.startAndWait();
		
		Assert.isTrue(s.state() == State.RUNNING);
	}
	
	@Test
	public void method2()
	{
		WorkQueue q = new WorkQueue(1);
		q.execute(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
			}});
	}
	
}
