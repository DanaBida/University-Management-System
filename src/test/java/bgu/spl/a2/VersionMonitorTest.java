package bgu.spl.a2;

import junit.framework.TestCase;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class VersionMonitorTest extends TestCase 
{

	VersionMonitor vm;

	@Before
	public void setUp() throws Exception 
	{
		vm = new VersionMonitor();
	}

	@Test
	public void testGetVersion() {
		assertEquals(0,vm.getVersion());
	}

	@Test
	public void testInc() {
		int before, after;
		before = vm.getVersion();
		vm.inc();
		after = vm.getVersion();
		assertEquals(before+1, after);
	}

	@Test
	public void testAwait()
	{
		int first = vm.getVersion();
		Thread t1 = new Thread( () ->
		{
			try
			{
				vm.await(first);
			}
			catch (Exception e)
			{
				e.printStackTrace(); //prints the error
			}
		} );
		
		t1.start();
		vm.inc();
		try
		{
			t1.join();
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		assertNotEquals(first, vm.getVersion());
	}
	

	@After
	public void tearDown() throws Exception 
	{
		vm = null;
	}
}