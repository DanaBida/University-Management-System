package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.ActorThreadPool;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;

/*
 * Behavior: This action should increase the number of available spaces for the course.
 Actor: Must be initially submitted to the course's actor
 */
public class OpeningNewPlacesInACourse extends Action<Integer>
{
	String courseName;
	Integer number;
	ActorThreadPool pool;
	
	public OpeningNewPlacesInACourse(String courseName, Integer number, ActorThreadPool pool)
	{
		this.courseName = courseName;
		this.number = number;
		this.pool = pool;
	}
		
	protected void start()
	{
		CoursePrivateState c = (CoursePrivateState) pool.getActors().get(courseName);
		c.setAvailableSpots(number);
		c.addRecord("Opening New Places In a Course");
		complete(number);
	}
}
