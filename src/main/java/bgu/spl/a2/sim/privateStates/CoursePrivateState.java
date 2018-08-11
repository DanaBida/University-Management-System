package bgu.spl.a2.sim.privateStates;

import java.util.ArrayList;
import java.util.List;
import bgu.spl.a2.PrivateState;

/**
 * this class describe course's private state
 */
public class CoursePrivateState extends PrivateState implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	private Integer availableSpots;//available places
	private Integer registered;//how many it contains
	private List<String> regStudents;
	private List<String> prequisites;
	
	/**
 	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 */
	public CoursePrivateState() {
		availableSpots=0;
		registered=0;
		regStudents=new ArrayList<String>();
		prequisites=new ArrayList<String>();
	}

	public Integer getAvailableSpots() {
		return availableSpots;
	}

	public Integer getRegistered() {
		return registered;
	}

	public List<String> getRegStudents() {
		return regStudents;
	}

	public List<String> getPrequisites() {
		return prequisites;
	}

	public void setAvailableSpots(Integer availableSpots) {
		this.availableSpots = availableSpots;
	}

	public void setRegistered(Integer registered) {
		this.registered = registered;
	}

	public void addStudent (String student_name) {
		this.regStudents.add(student_name);
	}

	public void setPrequisites(List<String> prequisites) {
		this.prequisites = prequisites;
	}
	
	public void deleteStudent (String student_name) {
		this.regStudents.remove(student_name);
	}
}
