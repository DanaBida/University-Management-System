package bgu.spl.a2.sim.actions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import bgu.spl.a2.Action;
import bgu.spl.a2.ActorThreadPool;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

/*
 * Behavior: This action should try to register the student in the course, if it succeeds, should add the
 * course to the grades sheet of the student, and give him a grade if supplied. See the input example.
 * Actor: Must be initially submitted to the course's actor.
 */
public class ParticipatingInCourse extends Action<Boolean> 
{
	String studentId;
	String courseName;
	ArrayList<String> grade;
	ActorThreadPool pool;

	public ParticipatingInCourse(String studentId, String courseName, ArrayList<String> grade, ActorThreadPool pool)
	{
		this.studentId = studentId;
		this.courseName = courseName;
		this.grade = grade;
		this.pool = pool;
	}
	
	protected void start()
	{
		CoursePrivateState c = (CoursePrivateState) pool.getActors().get(courseName);
		//submits an action that checks if the student has the prequisites
    	List<Action<Boolean>> actions = new ArrayList<>();
	    Action<Boolean> a = new checkPrequisites(studentId, c.getPrequisites(), pool);
	    actions.add(a);
	    sendMessage(a, studentId, new StudentPrivateState());
	   	then(actions,()->{		
		    if(c.getAvailableSpots()>0 && a.getResult().get() && !c.getRegStudents().contains(studentId)) 
		    //this also assures we can't add students to closed courses, as they will have -1 available spots. 
		    //also check student isn't already registered
		    {
		    	c.addStudent(studentId);
		   		//update course numbers
				int registered = c.getRegistered();
				c.setRegistered(registered+1);
				int spots = c.getAvailableSpots();
				c.setAvailableSpots(spots-1);
				//submits an action to add course to student's grade list
	        	List<Action<String>> actions2 = new ArrayList<>();
	   	    	Action<String> a2 = new AddGradeToStudent(studentId, courseName, grade, pool);
	   	    	actions2.add(a2);
	   	    	sendMessage(a2, studentId, new StudentPrivateState());
	   	    	then(actions2,()->{
	   	    		complete(true);
	   	    	});   
	   	}
	    else
	    	complete(false);
		
		c.addRecord("Participate In Course");
	   	});
	}
}