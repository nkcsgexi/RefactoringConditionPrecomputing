package util.tests;

import javaEventing.EventManager;
import javaEventing.EventObject;
import javaEventing.interfaces.Event;
import javaEventing.interfaces.GenericEventListener;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import dlf.refactoring.precondition.util.XLoggerFactory;

public class JavaEventingTests {

	
		private class MyEvent extends EventObject {};
		private Logger logger = XLoggerFactory.GetLogger(this.getClass());
		
		@Before
		public void methodBefore()
		{	
			EventManager.registerEventListener(new GenericEventListener(){
				@Override
				public void eventTriggered(Object arg0, Event arg1) {
					logger.info("event triggered.");
				}}, MyEvent.class);
		}
		
		@Test
		public void method1()
		{
			EventManager.triggerEvent(this, new MyEvent());
		}
}
