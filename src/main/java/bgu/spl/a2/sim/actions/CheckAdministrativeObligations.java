package bgu.spl.a2.sim.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import bgu.spl.a2.Action;
import bgu.spl.a2.ActorThreadPool;
import bgu.spl.a2.sim.Computer;
import bgu.spl.a2.sim.Warehouse;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

/*
 * Behavior: The department's secretary have to allocate one of the computers available in the warehouse,
and check for each student if he meets some administrative obligations. The computer generates
a signature and save it in the private state of the students.
 Actor: Must be initially submitted to the department's actor.
 */
public class CheckAdministrativeObligations extends Action<String> 
{
	String department;
	String computer;
	List <String> students;
	List <String> conditions;
	Warehouse warehouse;
	ActorThreadPool pool;

	public CheckAdministrativeObligations(String department, String computer, ArrayList<String> students, ArrayList<String> conditions, ActorThreadPool pool, Warehouse warehouse)
	{
		this.department = department;
		this.computer = computer;
		this.students = students;
		this.conditions = conditions;
		this.pool = pool;
		this.warehouse = warehouse;
	}
	
	protected void start()
	{
		//submits an action that checks if computer is available
		List<Action<Computer>> actions = new ArrayList<>();
		Action<Computer> a = new checkIfComputerAvailable(warehouse, computer);
		a.setActionName("Check Computer");
		actions.add(a);
		sendMessage(a, department, pool.getPrivateState(department));
		then(actions, () ->{
			Computer c = actions.get(0).getResult().get();
			List<Action<Long>> actions2 = new ArrayList<>();
			Iterator<String> it = students.iterator();
			while (it.hasNext()){
			//iterate through student and submit each one a check and sign action
				String current = it.next();
				Action<Long> a2 = new CallCheckAndSign(current, conditions, c, pool);
				actions2.add(a2);
				sendMessage(a2, current, new StudentPrivateState());
			}
			then(actions2, () ->{ //all check and signs were finished
				pool.getPrivateState(department).addRecord("Administrative Check");
				complete(department);
			});
		});

	}	
}
