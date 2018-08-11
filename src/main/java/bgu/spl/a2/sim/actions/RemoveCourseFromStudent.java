package bgu.spl.a2.sim.actions;
import bgu.spl.a2.Action;
import bgu.spl.a2.ActorThreadPool;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class RemoveCourseFromStudent extends Action<String>{
	String studentID;
	String courseName;
	ActorThreadPool pool;
	
	public RemoveCourseFromStudent(String studentID, String courseName, ActorThreadPool pool)
	{
		this.studentID=studentID;
		this.courseName=courseName;
		this.pool=pool;
	}
	
	public void start(){
		StudentPrivateState s = (StudentPrivateState)pool.getActors().get(studentID);
		s.removeCourse(courseName);
		complete(studentID);
	}

}
