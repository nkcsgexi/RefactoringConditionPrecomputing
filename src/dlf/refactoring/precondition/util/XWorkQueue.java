package dlf.refactoring.precondition.util;

import java.util.LinkedList;

import javaEventing.EventManager;
import javaEventing.EventObject;
import javaEventing.interfaces.Event;
import javaEventing.interfaces.GenericEventListener;

import org.apache.log4j.Logger;

import dlf.refactoring.precondition.util.interfaces.IRunnable;



public final class XWorkQueue
{
    private final int nThreads;
    private final PoolWorker[] threads;
    private final LinkedList<IRunnable> queue;
    
    private final class EmptyWorkQueueEvent extends EventObject{}
    
    private final class WorkItemEnd extends TimedEventObject<IRunnable>{
		public WorkItemEnd(IRunnable information) {
			super(information);
		}}
 
    private XWorkQueue(int nThreads, int priority)
    {
        this.nThreads = nThreads;
        queue = new LinkedList<IRunnable>();
        threads = new PoolWorker[nThreads];
       
        for (int i=0; i<nThreads; i++) {
            threads[i] = new PoolWorker();
            threads[i].setPriority(priority);
            threads[i].start();
        }
    }

    public static XWorkQueue createSingleThreadWorkQueue(int priority)
    {
    	return new XWorkQueue(1, priority);
    }
    
    public void execute(IRunnable r) {
        synchronized(queue) {
            queue.addLast(r);
            queue.notify();
        }
    }
    
    
    public void addEmptyQueueEventListener(final GenericEventListener listener)
    {
    	final XWorkQueue workQueue = this;
    	EventManager.registerEventListener(new GenericEventListener(){
			@Override
			public void eventTriggered(Object arg0, Event arg1) {
				if(containsWorker((PoolWorker) arg0)) {
					listener.eventTriggered(workQueue, arg1);
				}
			}}, EmptyWorkQueueEvent.class);
    }
    
    public void addWorkItemListener(final XWorkItemListener listener)
    {  	
    	EventManager.registerEventListener(new GenericEventListener(){
			@Override
			public void eventTriggered(Object arg0, Event arg1) {
				if(containsWorker((PoolWorker) arg0)) {
					listener.runnableFinished(((WorkItemEnd)arg1).getInformation());
				}
			}}, WorkItemEnd.class);
    }
    
    
    private boolean containsWorker(PoolWorker worker)
    {
    	for(PoolWorker t : threads)
    	{
    		if(t == worker)
    		{
    			return true;
    		}
    	}
    	return false;
    }
    
    
    private class PoolWorker extends Thread {
    	private final Logger logger = XLoggerFactory.GetLogger(this.getClass());
	
        public void run() {
        	IRunnable r;

            while (true) {
                synchronized(queue) {
                    while (queue.isEmpty()) {
                        try
                        {
                        	EventManager.triggerEvent(this, new EmptyWorkQueueEvent());
                        	queue.wait();
                        }
                        catch (InterruptedException ignored)
                        {
                        	logger.fatal(ignored);
                        }
                    }

                    r = (IRunnable) queue.removeFirst();
                }

                // If we don't catch RuntimeException, 
                // the pool could leak threads
                try {
                		r.run();
                		EventManager.triggerEvent(this, new WorkItemEnd(r));
                }
                catch (Exception e) {
                    logger.fatal(e);
                }
            }
        }
    }
}