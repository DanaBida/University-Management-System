package bgu.spl.a2.sim.actions;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import bgu.spl.a2.Action;
import bgu.spl.a2.ActorThreadPool;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class checkPrequisites extends Action<Boolean>{
	String studentID;
	List<String> prequisites;
	ActorThreadPool pool;
	
	public checkPrequisites(String studentID, List<String> prequisites, ActorThreadPool pool)
	{
		this.studentID=studentID;
		this.prequisites=prequisites;
		this.pool=pool;
	}
	
	public void start(){
		StudentPrivateState s = (StudentPrivateState)pool.getActors().get(studentID);
		boolean isAllprequisites = true;
		Iterator<String> it = prequisites.iterator();
    	while (it.hasNext() && isAllprequisites)
    	{
    		String current = it.next();
    		if(!s.getGrades().containsKey(current))
    			isAllprequisites = false;
    	}
    	complete(isAllprequisites);
	}

}