package bgu.spl.a2.sim.actions;


import bgu.spl.a2.Action;
import bgu.spl.a2.ActorThreadPool;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

/*
 * Behavior: This action adds a new student to a specified department.
 * Actor: Must be initially submitted to the Department's actor.
 */
public class AddStudent extends Action<String>
{
		String department;
		String studentId;
		ActorThreadPool pool;
		
		public AddStudent(String department, String studentId, ActorThreadPool pool) 
		{
			this.department=department;
			this.studentId = studentId;
			this.pool = pool;
		}
		
		//adds a new student actor to the actors queue in the thread pool, and adds it to the specific department
		protected void start() 
		{
			//adds student to the specific department
			DepartmentPrivateState d=(DepartmentPrivateState)pool.getPrivateState(department);
			d.addStudent(studentId);			
			//student actor doesn't exist yet, so we submit an empty action in order to create it.
			Action<String> a = new DoNothing();
			StudentPrivateState s = new StudentPrivateState();
			a.setActionName("Do Nothing");
			pool.submit(a, studentId, s);
			d.addRecord("Add Student");
			complete(studentId);
		}
	
}
