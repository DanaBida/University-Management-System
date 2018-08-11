package bgu.spl.a2;

/**
 * Describes a monitor that supports the concept of versioning - its idea is
 * simple, the monitor has a version number which you can receive via the method
 * {@link #getVersion()} once you have a version number, you can call
 * {@link #await(int)} with this version number in order to wait until this
 * version number changes.
 *
 * you can also increment the version number by one using the {@link #inc()}
 * method.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */
public class VersionMonitor 
{
	
    volatile int v;

    public int getVersion() 
    {
    	return v;
    }

    public void inc() 
    {
    	/*	we use synchronized because we want to prevent the situation that many threads changing the value of v at the same time.
    	 *  in that way, we always hold the correct value of v.
    	 *  in addition, we want to notify the sleeping threads in this monitor when the version is changed
    	*/
    	synchronized (this) 
    	{ 
    		v++;
    		notifyAll();
    	}
    }

    public void await(int version) throws InterruptedException 
    {
    	//we use synchronized because wait can be used only in a synchronized block
    	synchronized (this) 
    	{
        	while (v==version)
        		this.wait();		
		}
    }
}
