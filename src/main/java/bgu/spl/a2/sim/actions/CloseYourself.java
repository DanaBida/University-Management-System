package bgu.spl.a2.sim.actions;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import bgu.spl.a2.Action;
import bgu.spl.a2.ActorThreadPool;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class CloseYourself extends Action<String>{
	String courseName;
	ActorThreadPool pool;
	
	public CloseYourself(String courseName, ActorThreadPool pool)
	{
		this.courseName=courseName;
		this.pool=pool;
	}
	
	public void start(){
		//set available spots to -1
		CoursePrivateState c = (CoursePrivateState) pool.getActors().get(courseName); 
		c.setAvailableSpots(-1);
		
		//remove all students
    	List<Action<String>> actions = new ArrayList<>();
		ArrayList<String> students = (ArrayList<String>)c.getRegStudents();
		Iterator <String> it = students.iterator();
		while (it.hasNext()){ //send 'unregister' action to each student 
			String current = it.next();
			Action <String> a = new Unregister(current, courseName, pool);
			actions.add(a);
		   	sendMessage(a, courseName, new StudentPrivateState());
		}
	   	then(actions,()->{
	   		complete(courseName);
	   	});
	}
}
