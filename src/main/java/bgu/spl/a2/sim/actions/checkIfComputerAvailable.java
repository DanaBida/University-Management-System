package bgu.spl.a2.sim.actions;
import bgu.spl.a2.Action;
import bgu.spl.a2.Promise;
import bgu.spl.a2.sim.Computer;
import bgu.spl.a2.sim.SuspendingMutex;
import bgu.spl.a2.sim.Warehouse;

public class checkIfComputerAvailable extends Action<Computer> {
	Warehouse warehouse;
	String computer;

	public checkIfComputerAvailable(Warehouse warehouse, String computer)
	{
		this.warehouse=warehouse;
		this.computer=computer;
	}
	
	public void start(){
		SuspendingMutex s = warehouse.getMutex().get(computer);
		Promise<Computer> p = s.down();
		if (p.isResolved()) {
			Computer c = warehouse.getComputers().get(computer);
			complete(c);
		}
	}
}
