/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl.a2.sim;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import bgu.spl.a2.ActorThreadPool;
import bgu.spl.a2.PrivateState;
import bgu.spl.a2.sim.actions.AddStudent;
import bgu.spl.a2.sim.actions.CheckAdministrativeObligations;
import bgu.spl.a2.sim.actions.CloseACourse;
import bgu.spl.a2.sim.actions.OpenANewCourse;
import bgu.spl.a2.sim.actions.OpeningNewPlacesInACourse;
import bgu.spl.a2.sim.actions.ParticipatingInCourse;
import bgu.spl.a2.sim.actions.RegisterWithPreferences;
import bgu.spl.a2.sim.actions.Unregister;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

/**
 * A class describing the simulator for part 2 of the assignment
 */
public class Simulator {
	
	static Json input;
	static Warehouse warehouse;
	static ActorThreadPool pool;
	/**
	* Begin the simulation Should not be called before attachActorThreadPool()
	*/
    public static void start(){
    	pool.start();
    	createComputers();
    	executePhase(input.phase1);
    	executePhase(input.phase2);
    	executePhase(input.phase3);
    }
	
    public static void createComputers()
    {
    	for (int i=0; i<input.Computers.length; i++){
    		ComputerInput temp = input.Computers[i];
    		warehouse.addComputer(temp.Type, temp.failSig, temp.successSig);
    	}
    }
    
    public static void executePhase(ActionInput[] actions){
    	//CountDownLatch is used to make sure all actions were finished before finishing phase
		CountDownLatch l = new CountDownLatch(actions.length); 
    	for (int i=0; i<actions.length; i++){
    		ActionInput temp = actions[i];
    	    switch (temp.Action) {
             case "Open Course":
            	 OpenANewCourse a1 = new OpenANewCourse(
            			 temp.Department, 
            			 temp.Course, 
            			 Integer.parseInt(temp.Space), 
            			 temp.Prerequisites, 
            			 pool);
            	 a1.setActionName(temp.Action);
            	 pool.submit(a1, temp.Department, new DepartmentPrivateState());
            	 a1.getResult().subscribe(()-> {
		            l.countDown();
            	 });
            	 break;
             case "Add Student":
            	 AddStudent a2 = new AddStudent(
            			 temp.Department, 
            			 temp.Student,
            			 pool);
            	 pool.submit(a2, temp.Department, new DepartmentPrivateState());
            	 a2.setActionName(temp.Action);
            	 a2.getResult().subscribe(()-> {
		            l.countDown();
            	 });
            	 break;
             case "Participate In Course":
            	 ParticipatingInCourse a3 = new ParticipatingInCourse(
            			 temp.Student,
            			 temp.Course,
            			 temp.Grade,
            			 pool);
            	 pool.submit(a3, temp.Course, new CoursePrivateState());
            	 a3.setActionName(temp.Action);
            	 a3.getResult().subscribe(()-> {
		            l.countDown();    
            	 });
            	 break;
             case "Unregister":
            	 Unregister a4 = new Unregister(
            			 temp.Student,
            			 temp.Course,
            			 pool);
            	 pool.submit(a4, temp.Course, new CoursePrivateState());
            	 a4.setActionName(temp.Action);
            	 a4.getResult().subscribe(()-> {
		            l.countDown();    
            	 });
            	 break;
             case "Close Course":
            	 CloseACourse a5 = new CloseACourse(
            			 temp.Course,
            			 temp.Department,
            			 pool);
            	 pool.submit(a5, temp.Department, new DepartmentPrivateState());
            	 a5.setActionName(temp.Action);
            	 a5.getResult().subscribe(()-> {
		            l.countDown(); 
            	 });
            	 break;
             case "Opening New Places In a Course":
            	 OpeningNewPlacesInACourse a6 = new OpeningNewPlacesInACourse(
            			 temp.Course,
            			 Integer.parseInt(temp.Space), 
            			 pool);
            	 pool.submit(a6, temp.Course, new CoursePrivateState());
            	 a6.setActionName(temp.Action);
            	 a6.getResult().subscribe(()-> {
		            l.countDown();  
            	 });
            	 break;
             case "Administrative Check":
            	 CheckAdministrativeObligations a7 = new CheckAdministrativeObligations(
            			 temp.Department,
            			 temp.Computer,
            			 temp.Students,
            			 temp.Conditions,
            			 pool,
            			 warehouse);
            	 pool.submit(a7, temp.Department, new CoursePrivateState());
            	 a7.setActionName(temp.Action);
            	 a7.getResult().subscribe(()-> {
		             l.countDown();  
            	 });
            	 break;
             case "Register With Preferences":
            	 RegisterWithPreferences a8 = new RegisterWithPreferences(
            			 temp.Student,
            			 temp.Preferences,
            			 temp.Grade,
            			 0,
            			 pool);
            	 pool.submit(a8, temp.Student, new StudentPrivateState());
            	 a8.setActionName(temp.Action);
            	 a8.getResult().subscribe(()-> {
            		 l.countDown();    
            	 });
            	 break;
    	    }
    	}
     	try
     	{
     		//simulator waits for all actions to finish
     		l.await();
     	}
     	catch(InterruptedException e) {
     		System.out.println("interrupted");
     		}	    	
    }
	/**
	* attach an ActorThreadPool to the Simulator, this ActorThreadPool will be used to run the simulation
	* 
	* @param myActorThreadPool - the ActorThreadPool which will be used by the simulator
	*/
	public static void attachActorThreadPool(ActorThreadPool myActorThreadPool){
		pool = myActorThreadPool;
	}
	
	/**
	* shut down the simulation
	* returns list of private states
	*/
	public static HashMap<String,PrivateState> end(){
     	try
		{
     		pool.shutdown();
     	}
     	catch(InterruptedException e) {
     		System.out.println("interrupted during trying to shut down the pool");
     	}
     	//copy all private states to hashmap
		ConcurrentHashMap<String , PrivateState> ps = (ConcurrentHashMap<String , PrivateState>)pool.getActors();
		Iterator <ConcurrentHashMap.Entry<String, PrivateState>> it = ps.entrySet().iterator();
		HashMap<String,PrivateState> ret = new HashMap<String,PrivateState>();
		while (it.hasNext()){
    		ConcurrentHashMap.Entry<String, PrivateState> current = it.next();      		 	
    		ret.put(current.getKey(), current.getValue());
		}
		return ret;
	}
	
	public static void main(String [] args)
	{
    	warehouse = new Warehouse();
		input =  null;
		BufferedReader reader = null;
		HashMap<String, PrivateState> SimulationResult = new HashMap<>();

		//read the json file
		try
		{
			reader = new BufferedReader(new FileReader(args[0]));
			Gson gson = new GsonBuilder().create();
	    	input = gson.fromJson(reader, Json.class);
		}catch(Exception e){
			System.out.println(e + " we get exception in trying to building json");
		}
		//creates new thread pool and starts&end the simulator
    	ActorThreadPool newPool = new ActorThreadPool(input.threads);
    	attachActorThreadPool(newPool);
    	start();
    	SimulationResult = end();
    	//shut down the simulator and returns the corresponding output
    	try
		{
			FileOutputStream fout = new FileOutputStream("result.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(SimulationResult);
			oos.close();
		}catch(IOException e){
			System.out.println(e + " we get exception in trying to finish");
		}
    }
}