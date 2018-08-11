package bgu.spl.a2.sim;

import java.util.HashMap;

/**
 * represents a warehouse that holds a finite amount of computers
 * and their suspended mutexes.
 * releasing and acquiring should be blocking free.
 */
public class Warehouse {

	private HashMap<String, Computer> computers;
	private HashMap<String, SuspendingMutex> mutex;
	
	public Warehouse () {
		computers = new HashMap<String, Computer>();
		mutex = new HashMap<String, SuspendingMutex>();
	}
	
	public void addComputer(String type, long failSig, long successSig) {
		Computer a = new Computer(type);
		a.setFailSig(failSig);
		a.setSuccessSig(successSig);
		computers.put(type, a);
		SuspendingMutex m = new SuspendingMutex(a);
		mutex.put(type, m);
	}

	public HashMap<String, Computer> getComputers() {
		return computers;
	}

	public HashMap<String, SuspendingMutex> getMutex() {
		return mutex;
	}

}
