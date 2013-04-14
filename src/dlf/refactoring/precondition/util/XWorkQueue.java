package dlf.refactoring.precondition.util;

import java.util.LinkedList;

import org.apache.log4j.Logger;

import dlf.refactoring.precondition.util.interfaces.IOperation;

public class XWorkQueue
{
    private final int nThreads;
    private final PoolWorker[] threads;
    private final LinkedList<Runnable> queue;
    private final XArrayList<XWorkItemListener> listeners;

    private XWorkQueue(int nThreads)
    {
        this.nThreads = nThreads;
        queue = new LinkedList<Runnable>();
        threads = new PoolWorker[nThreads];

        for (int i=0; i<nThreads; i++) {
            threads[i] = new PoolWorker();
            threads[i].start();
        }
        
        this.listeners = new XArrayList<XWorkItemListener>();
    }

    public static XWorkQueue createSingleThreadWorkQueue()
    {
    	return new XWorkQueue(1);
    }
    
    public void execute(Runnable r) {
        synchronized(queue) {
            queue.addLast(r);
            queue.notify();
        }
    }
    
    
    public void addWorkItemListener(XWorkItemListener lis)
    {
    	synchronized(this.listeners){
    		this.listeners.add(lis);
    	}
    }
    
    private class PoolWorker extends Thread {
    	Logger logger = XLoggerFactory.GetLogger(this.getClass());
    	
        public void run() {
            Runnable r;

            while (true) {
                synchronized(queue) {
                    while (queue.isEmpty()) {
                        try
                        {
                            queue.wait();
                        }
                        catch (InterruptedException ignored)
                        {
                        	logger.fatal(ignored);
                        }
                    }

                    r = (Runnable) queue.removeFirst();
                }

                // If we don't catch RuntimeException, 
                // the pool could leak threads
                try {
                	synchronized(listeners){
                		for(XWorkItemListener l : listeners)
                		{
                			l.beforeRunning(r);
                		}
                		
                		r.run();
                		
                		for(XWorkItemListener l : listeners)
                		{
                			l.afterRunning(r);
                		}
                	}
                }
                catch (Exception e) {
                    logger.fatal(e);
                }
            }
        }
    }
}