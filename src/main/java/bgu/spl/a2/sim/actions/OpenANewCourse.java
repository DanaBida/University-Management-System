package bgu.spl.a2.sim.actions;
import java.util.ArrayList;
import bgu.spl.a2.Action;
import bgu.spl.a2.ActorThreadPool;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;

/*
 * Behavior: This action opens a new course in a specified department. The course has an initially
			available spaces and a list of prerequisites.
 	Actor: Must be initially submitted to the Department's actor.
 */
public class OpenANewCourse extends Action<String>{
	
	String department;
	String course_name;
	int space;
	ArrayList<String> prequisites;
	ActorThreadPool pool;
	
	public OpenANewCourse(String department, String course_name, int space, ArrayList<String> prequisites, ActorThreadPool pool) {
		this.department=department;
		this.course_name=course_name;
		this.space=space;
		this.prequisites=prequisites;
		this.pool=pool;
	}

	protected void start() 
	{	
		//add the new course to a specific department
		DepartmentPrivateState d=(DepartmentPrivateState)pool.getActors().get(department);
		d.addCourse(course_name);
		
		//adds new course actor
		Action<String> a = new DoNothing();
		CoursePrivateState c = new CoursePrivateState();
		c.setAvailableSpots(space);
		c.setPrequisites(prequisites); //registered is already initialized to 
		a.setActionName("Do Nothing");
		pool.submit(a, course_name, c);
		d.addRecord("Open Course");		
		complete(course_name);
	}
}
