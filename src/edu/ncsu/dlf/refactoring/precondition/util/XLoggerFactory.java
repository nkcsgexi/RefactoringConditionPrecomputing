package edu.ncsu.dlf.refactoring.precondition.util;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class XLoggerFactory {
	
	private static final String deskTopDirectory = "C:/Users/xige/Desktop";
	
	static
	{
		ConsoleAppender console = new ConsoleAppender();
		String PATTERN = "%d [%p|%c|%C{1}] %m%n";
		console.setLayout(new PatternLayout(PATTERN)); 
		console.setThreshold(Level.FATAL);
		console.activateOptions();
		Logger.getRootLogger().addAppender(console);

		AddFileAppender(deskTopDirectory + "/all.log", Level.DEBUG, "AllFileLogger");
		AddFileAppender(deskTopDirectory + "/fatal.log", Level.FATAL, "FatalFileLogger");
	}
	
	private static void AddFileAppender(String path, Level threshold, String loggerName)
	{
		FileAppender fa = new FileAppender();
		fa.setName(loggerName);
		fa.setFile(path);
		fa.setLayout(new PatternLayout("%d %-5p [%c{1}] %m%n"));
		fa.setThreshold(threshold);
		fa.setAppend(true);
		fa.activateOptions();
		Logger.getRootLogger().addAppender(fa);
	}
	
	public static Logger GetLogger(Class c)
	{
		if (c == null)
		{
			return Logger.getRootLogger();
		}
		return Logger.getLogger(c);
	}
}
