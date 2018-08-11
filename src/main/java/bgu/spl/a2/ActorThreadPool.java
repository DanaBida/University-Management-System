package bgu.spl.a2;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

/**
 * represents an actor thread pool - to understand what this class does please
 * refer to your assignment.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */
public class ActorThreadPool 
{
	Thread [] threads ; //the threads of the thread pool
	ConcurrentHashMap<String , PrivateState> ps;//the queues of the actors indexed by the actors id
	VersionMonitor vm;
	ConcurrentHashMap<String, QueuePair> actors;//the queues of the actors indexed by the actors id
	/*be aware that ConcurrentLinkedDeque is safety among multiple threads,
	 */

	/**
	 * creates a {@link ActorThreadPool} which has nthreads. Note, threads
	 * should not get started until calling to the {@link #start()} method.
	 *
	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 *
	 * @param nthreads
	 *            the number of threads that should be started by this thread
	 *            pool
	 */
	public ActorThreadPool(int nthreads) 
	{
    	threads = new Thread[nthreads];
    	vm = new VersionMonitor();
    	actors = new ConcurrentHashMap<String, QueuePair>(nthreads); //set the initial capacity of the map to nthreads. 
    	ps = new ConcurrentHashMap<String, PrivateState>(nthreads);
    	for(int i = 0; i<nthreads; i++)
    	{
    		threads[i] = new Thread(()->
	        {	        	
	        	//while we haven't shut down the pool
	        	while (!Thread.currentThread().isInterrupted())
	        	{
	        		//we check if the actors map is empty, if it is - we need to wait until new actor will added to the hash map
	        		if(actors.isEmpty()){
						try {
							vm.await(vm.getVersion());//waits until the version monitor will be changed
						}
						catch (InterruptedException e1) {
						    Thread.currentThread().interrupt();
						}
			    	}
	        		int oldVersion = vm.getVersion(); //save in case the actors are changed in the middle of iteration
	        		boolean finishIterate= false; //saves whether we completed iterating through all queues
        			AtomicBoolean isAllEmpty = new AtomicBoolean(true);
	        		while(!finishIterate) //while we didn't iterate through queues
		        	{
	            		try{
		        			Iterator <ConcurrentHashMap.Entry<String, QueuePair>>it = actors.entrySet().iterator();		        			
					    	while (it.hasNext())//if there is an available actors queue, execute one of it actions 
					    	{
					    		if(oldVersion!=vm.getVersion()) //if version was changed in the middle of iterating
				        			throw new ConcurrentModificationException();
					    		ConcurrentHashMap.Entry<String, QueuePair> current = it.next(); 
					    		if (!current.getValue().queue.isEmpty()) {
				        			isAllEmpty.set(false);
						        	if (current.getValue().lock()) //if the lock is free, we lock it (compareAndSet)
						        	{
						        		try { //in the rare case the queue became empty after we checked but before we locked it
						        			current.getValue().queue.pop().handle(this, current.getKey(), ps.get(current.getKey()));
						        		}
						        		catch (NoSuchElementException e) {
						        		}
					        			current.getValue().unlock();
						        	}
					    		}
			        		}
					    	finishIterate = true; //we've finished iterating all the actors
		        		}
		    	    	catch(ConcurrentModificationException e){
		    	    		oldVersion = vm.getVersion();
		    	    	}       
		        	}
	        		if (isAllEmpty.get()) //if we finished iterating and the actors are all empty
		        		try {
		        			vm.await(vm.getVersion());//waits until the version monitor will be changed
						}
						catch (InterruptedException e1) {
						    Thread.currentThread().interrupt();
						}	
	        	}
	        });
		}
	}
	
	/**
	 * getter for actors
	 * @return actors
	 */
	public Map<String, PrivateState> getActors(){
		return ps;
	}
	
	/**
	 * getter for actor's private state
	 * @param actorId actor's id
	 * @return actor's private state
	 */
	public PrivateState getPrivateState(String actorId){
		return ps.get(actorId);
	}

	/**
	 * submits an action into an actor to be executed by a thread belongs to
	 * this thread pool
	 *
	 * @param action
	 *            the action to execute
	 * @param actorId
	 *            corresponding actor's id
	 * @param actorState
	 *            actor's private state (actor's information)
	 */
	public void submit(Action<?> action, String actorId, PrivateState actorState)
	{
		if (actors.containsKey(actorId)) { //if actor already exists
			actors.get(actorId).queue.add(action); //add new action to queue
			vm.inc(); //increments the version monitor because we added a new actor to the actors hash map, and we need to notify the threads
		}
		else
		{
			QueuePair qp = new QueuePair(); //new actor queue
			qp.queue.add(action); //add action to new queue
			actors.put(actorId, qp); //add new queue to actors pool
			actors.get(actorId).isLocked().set(false);
			ps.put(actorId, actorState);
			vm.inc();//increments the version monitor because we added a new actor to the actors hash map, and we need to notify the threads
		}
	}

	/**
	 * closes the thread pool - this method interrupts all the threads and waits
	 * for them to stop - it is returns *only* when there are no live threads in
	 * the queue.
	 *
	 * after calling this method - one should not use the queue anymore.
	 *
	 * @throws InterruptedException
	 *             if the thread that shut down the threads is interrupted
	 */
	public void shutdown() throws InterruptedException 
	{
		for(int i=0; i < threads.length; i++)
		{
			if(Thread.currentThread().getId()==threads[i].getId())
    			throw new UnsupportedOperationException("the thread can't shut down itself");
    		try
    		{
    			threads[i].interrupt();
        		threads[i].join();
    		}
    		catch(InterruptedException exception){
        		throw new InterruptedException("The thread that shuts down the pool was interuppted");
    		}
    	}
	}

	/**
	 * start the threads belongs to this thread pool
	 */
	public void start() 
	{
    	for(int i=0; i<threads.length; i++) {
    		threads[i].start();
    	}
	}
	
}
