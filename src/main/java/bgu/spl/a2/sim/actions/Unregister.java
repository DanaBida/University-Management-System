package bgu.spl.a2.sim.actions;

import java.util.ArrayList;
import java.util.List;

import bgu.spl.a2.Action;
import bgu.spl.a2.ActorThreadPool;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

/*
 * Behavior: If the student is enrolled in the course, this action should unregister him (update the
list of students of course, remove the course from the grades sheet of the student and increases the
number of available spaces).
 Actor: Must be initially submitted to the course's actor.
 */
public class Unregister extends Action<String> 
{
	String studentId;
	String courseName;
	ActorThreadPool pool;

	public Unregister(String studentId, String courseName, ActorThreadPool pool)
	{
		this.courseName = courseName;
		this.studentId = studentId;
		this.pool = pool;
	}
	
	protected void start()
	{
		CoursePrivateState c = (CoursePrivateState) pool.getActors().get(courseName); 
		//check if student is in course
		boolean isFound=c.getRegStudents().contains(studentId);
		if (isFound){ 
			//delete student from course and update numbers
			c.deleteStudent(studentId);
			int registered = c.getRegistered();
			c.setRegistered(registered-1);
			int spots = c.getAvailableSpots();
			if (spots!=-1) 
			//if action is called from closing a course, and course spots was already updated to -1, we won't change it
				c.setAvailableSpots(spots+1);
			//send student an action that removes the course from his grade list
        	List<Action<String>> actions = new ArrayList<>();
   	    	Action<String> a = new RemoveCourseFromStudent(studentId, courseName, pool);
   	    	actions.add(a);
   	    	sendMessage(a, studentId, new StudentPrivateState());
   	    	then(actions,()->{
   	    		complete(courseName);
   	    		c.addRecord("Unregister");
   	    	}); 
		}
	}
}
