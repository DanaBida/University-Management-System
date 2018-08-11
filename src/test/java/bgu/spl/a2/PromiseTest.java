package bgu.spl.a2;

import junit.framework.TestCase;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PromiseTest extends TestCase 
{

	Promise<AtomicInteger> p1;

	@Before
	public void setUp() throws Exception 
	{
		p1 = new Promise<AtomicInteger>();
	}


	@Test
	public void testGet()
	{
		//test exception if promise isn't resolved
		try{
			p1.get();
		}catch(IllegalStateException e){
			assertTrue(true);
		}

		AtomicInteger i = new AtomicInteger(1);
		p1.resolve(i);	
		assertEquals(i , p1.get());
	}

	@Test
	public void testIsResolved() 
	{
		AtomicInteger counter = new AtomicInteger(0);
		assertFalse(p1.isResolved());
		p1.resolve(counter);
		assertTrue(p1.isResolved());
	}
	
	@Test
	public void testResolve() 
	{
		AtomicInteger counter = new AtomicInteger(0);
		p1.subscribe(() -> { counter.incrementAndGet(); });
		AtomicInteger i = new AtomicInteger(1);
		p1.resolve(counter);
		assertEquals(i.get(),counter.get());
		
		try{
			p1.resolve(counter);
		}catch(IllegalStateException e){
			assertTrue(true);
		}
	}

	@Test
	public void testSubscribe()
	{
		AtomicInteger counter = new AtomicInteger(0);
		p1.subscribe(() -> {assertEquals(0,0);});
		p1.resolve(counter);
	}
	
	@After
	public void tearDown() throws Exception 
	{
		p1 = null;
	}
}



