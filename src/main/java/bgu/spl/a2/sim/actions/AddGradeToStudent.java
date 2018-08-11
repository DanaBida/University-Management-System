package bgu.spl.a2.sim.actions;
import java.util.ArrayList;
import bgu.spl.a2.Action;
import bgu.spl.a2.ActorThreadPool;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class AddGradeToStudent extends Action<String>{
	String studentID;
	String courseName;
	ArrayList<String> grade;
	ActorThreadPool pool;
	
	public AddGradeToStudent(String studentID, String courseName, ArrayList<String> grade, ActorThreadPool pool)
	{
		this.studentID=studentID;
		this.courseName=courseName;
		this.grade=grade;
		this.pool=pool;
	}
	
	public void start(){
		StudentPrivateState s = (StudentPrivateState)pool.getActors().get(studentID);
		s.addGrade(grade.get(0), courseName);
		complete(studentID);
	}

}
