package dlf.refactoring.precondition.util;

import javaEventing.EventObject;

abstract public class TimedEventObject<T> extends EventObject{

	private final long creationTime;
	private final T information; 
	
	public TimedEventObject(T information)
	{
		creationTime = System.currentTimeMillis();
		this.information = information;
	}
	
	public long getCreationTime()
	{
		return this.creationTime;
	}
	
	public T getInformation()
	{
		return this.information;
	}
}
