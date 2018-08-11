package bgu.spl.a2.sim.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import bgu.spl.a2.Action;
import bgu.spl.a2.ActorThreadPool;
import bgu.spl.a2.Promise;
import bgu.spl.a2.sim.Computer;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class RegisterWithPreferences extends Action<Boolean> 
{
	private String studentId;
	private ArrayList<String> courses;
	private ArrayList<String> grades;
	int i; //the index of the course that we trying to register the student to 
	ActorThreadPool pool;

	public RegisterWithPreferences(String studentId, ArrayList<String> courses, ArrayList<String> grades, int i, ActorThreadPool pool)
	{
		this.courses = courses;
		this.grades = grades;
		this.studentId = studentId;
		this.pool = pool;
		this.i=i;
	}

    	protected void start(){
    		//tries to register the student to the course in the i position (when first submitted, i=0)
    		List<Action<Boolean>> actions = new ArrayList<>();
    		ArrayList<String> gradeArray = new ArrayList<>();
    		gradeArray.add(grades.get(i));
   			Action<Boolean> a = new ParticipatingInCourse(studentId, courses.get(i), gradeArray, pool);
   			actions.add(a);
   			sendMessage(a, courses.get(i), new CoursePrivateState());
    		then(actions, () ->{
    			Boolean result = a.getResult().get();
    			if (result==true){ //if succeed, we finished the action
    				complete(true);
    				pool.getPrivateState(studentId).addRecord("Register With Preferences");
    			}
    			else
    				if (i<courses.size()-1){ 
    					//we didn't register the student, and there's still more courses in the array
    					//we submit the action again, but this time with i+1, so we look at the next spot in the array
	    	    		Action<Boolean> a2 = new RegisterWithPreferences(studentId, courses, grades, i+1, pool);
	    	    		actions.add(a2);
	    	    		sendMessage(a2, courses.get(i+1), new CoursePrivateState());
	    	    		a2.getResult().subscribe(()->{complete(true);});
    				}
    				else{ //the student couldn't be registered to any course in the array
    					complete(false);		
    					pool.getPrivateState(studentId).addRecord("Register With Preferences");
    				}			
   			});
	}
}