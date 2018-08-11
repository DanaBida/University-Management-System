package bgu.spl.a2.sim;

import java.util.concurrent.ConcurrentLinkedDeque;
import bgu.spl.a2.Promise;

/**
 * 
 * this class is related to {@link Computer}
 * it indicates if a Computer is free or not
 * 
 * Note: this class can be implemented without any synchronization. 
 * However, using synchronization will be accepted as long as the implementation is blocking free.
 *
 */
public class SuspendingMutex {
	
	Computer comp;
	Boolean isFree;//indicates if the Computer is available to use
	ConcurrentLinkedDeque<Promise<Computer>> promises = new ConcurrentLinkedDeque<>();
	
	/**
	 * Constructor
	 * @param Computer
	 */
	public SuspendingMutex(Computer Computer){
		comp=Computer;
		isFree=true;
	}
	
	/**
	 * Computer acquisition procedure
	 * Note that this procedure is non-blocking and should return immediately
	 * 
	 * @return a promise for the requested Computer
	 */
	public synchronized Promise<Computer> down()
	/*we use synchronize in up and down to insure that we won't check if the promises queue is empty while inserting
	 * elements to the queue 
	 */
	{
		Promise<Computer> promise = new Promise<Computer>();
		if (isFree) 
		{
			promise.resolve(comp);
			isFree=false;
			return promise;
		}
		else 
		{
			promises.add(promise);
			return promise;
		}
	}
	/**
	 * Computer return procedure
	 * releases a Computer which becomes available in the warehouse upon completion
	 */
	public synchronized void up()
	/*we use synchronize in up and down to insure that we won't check if the promises queue is empty while inserting
	 * elements to the queue 
	 */
	{
		isFree=true;
    	while (!promises.isEmpty())
    		promises.pop().resolve(comp);
	}
	
	public Computer getComp() 
	{
		return comp;
	}
}
