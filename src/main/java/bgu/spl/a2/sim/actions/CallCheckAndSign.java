package bgu.spl.a2.sim.actions;
import java.util.List;
import bgu.spl.a2.Action;
import bgu.spl.a2.ActorThreadPool;
import bgu.spl.a2.sim.Computer;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class CallCheckAndSign extends Action<Long>{
	String studentID;
	List <String> conditions;
	Computer c;
	ActorThreadPool pool;
	
	public CallCheckAndSign(String studentID, List <String> conditions, Computer c, ActorThreadPool pool)
	{
		this.studentID=studentID;
		this.conditions=conditions;
		this.c=c;
		this.pool=pool;
	}
	
	public void start(){
		StudentPrivateState s = (StudentPrivateState)pool.getActors().get(studentID);
		long ans = c.checkAndSign(conditions, s.getGrades());
		s.setSignature(ans);
		complete(ans);
	}

}
