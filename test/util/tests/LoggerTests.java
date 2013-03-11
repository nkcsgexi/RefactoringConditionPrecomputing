package util.tests;
import org.apache.log4j.Logger;
import org.junit.Test;

import util.XLoggerFactory;


public class LoggerTests {

	@Test
	public void method1()
	{
		Logger logger = XLoggerFactory.GetLogger(null);
		logger.debug("debug");
		logger.fatal("fatal");
	}
	

	@Test
	public void method2()
	{
		Logger logger = XLoggerFactory.GetLogger(this.getClass());
		logger.debug("debug");
		logger.fatal("fatal");
	}
}
