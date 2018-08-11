package bgu.spl.a2.sim.actions;
import java.util.ArrayList;
import java.util.List;

import bgu.spl.a2.Action;
import bgu.spl.a2.ActorThreadPool;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;

/*
 * Behavior: This action should close a course. Should unregister all the registered students in the
course and remove the course from the department courses' list and from the grade sheets of the
students. The number of available spaces of the closed course should be updated to -1. DO NOT
remove its actor. After closing the course, all the request for registration should be denied.
	Actor: Must be initially submitted to the department's actor.
 */
public class CloseACourse extends Action<String> 
{
	String courseName;
	String department;
	ActorThreadPool pool;

	public CloseACourse(String courseName, String department, ActorThreadPool pool)
	{
		this.courseName = courseName;
		this.department = department;
		this.pool = pool;
	}
	
	protected void start()
	{
		//remove a course from the department private state
		DepartmentPrivateState d = (DepartmentPrivateState) pool.getActors().get(department);		
		d.removeCourse(courseName);
		
		//sends the course an action to close itself. this action also will delete the course from students
    	List<Action<String>> actions = new ArrayList<>();
	    Action<String> a = new CloseYourself(courseName, pool);
	    actions.add(a);
	   	sendMessage(a, courseName, new CoursePrivateState());
	   	then(actions,()->{
	   		complete(courseName);
			d.addRecord("Close Course");
	   	});
	}
}
