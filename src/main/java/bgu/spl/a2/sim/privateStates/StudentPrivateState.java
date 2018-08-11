package bgu.spl.a2.sim.privateStates;

import java.util.HashMap;
import bgu.spl.a2.PrivateState;

/**
 * this class describe student private state
 */
public class StudentPrivateState extends PrivateState implements java.io.Serializable{
	
	private static final long serialVersionUID = 3L;
	private HashMap<String, String> grades;//the courses and the grades
	private long signature;
	
	/**
 	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 */
	public StudentPrivateState() {
		signature=0;
		grades = new HashMap<String,String>();
	}

	public HashMap<String, String> getGrades() {
		return grades;
	}

	public long getSignature() {
		return signature;
	}
	
	public void setSignature(long signature) {
		this.signature = signature;
	}

	public void addGrade(String grade, String courseName) {
		grades.put(courseName, grade);
	}
	
	public void removeCourse (String courseName){
		grades.remove(courseName);
	}

	public String getGrade(String course) {
		return grades.get(course);
	}
}
