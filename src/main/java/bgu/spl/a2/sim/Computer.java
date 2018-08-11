package bgu.spl.a2.sim;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Computer {

	String Type;
	long failSig;
	long successSig;
	
	public Computer(String computerType) {
		this.Type = computerType;
		failSig=0;
		successSig=0;
	}
	
	public String getType() {
		return Type;
	}

	public void setFailSig(long failSig) {
		this.failSig = failSig;
	}

	public void setSuccessSig(long successSig) {
		this.successSig = successSig;
	}

	/**
	 * this method checks if the courses' grades fulfill the conditions
	 * @param courses
	 * 							courses that should be pass
	 * @param coursesGrades
	 * 							courses' grade
	 * @return a signature if couersesGrades grades meet the conditions
	 */
	public long checkAndSign(List<String> courses, Map<String, String> coursesGrades){
		Iterator <String>it = courses.iterator();
		while (it.hasNext()) {
			String current = it.next();
			if (!coursesGrades.containsKey(current)) 
				return failSig;
			else {
				int grade = Integer.parseInt(coursesGrades.get(current)); 
				if (grade<56)
					return failSig;
			}
		}
		return successSig;
	}
}
