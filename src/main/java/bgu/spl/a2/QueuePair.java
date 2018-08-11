package bgu.spl.a2;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicBoolean;

public class QueuePair
//a pair object of a queue, and an AtomicBoolean that indicates if the queue is locked or not. 
{
	ConcurrentLinkedDeque<Action<?>> queue;
	private volatile AtomicBoolean isLocked;
	
	public QueuePair(){
		queue = new ConcurrentLinkedDeque<Action<?>>();
		isLocked = new AtomicBoolean(false);
	}

	public AtomicBoolean isLocked() {
		return isLocked;
	}
	
	public boolean lock (){
		boolean ans = this.isLocked.compareAndSet(false, true);
		return ans;
	}
	
	public boolean unlock (){
		boolean ans = this.isLocked.compareAndSet(true, false);
		return ans;
	}
}

