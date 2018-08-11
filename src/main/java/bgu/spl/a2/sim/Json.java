package bgu.spl.a2.sim;

import com.google.gson.annotations.SerializedName;

public class Json
//a class that is built like the Json. 
//The Json will be read into an object of this type. 
{
	int threads;
	ComputerInput[] Computers;
	@SerializedName("Phase 1")
	ActionInput[] phase1;
	@SerializedName("Phase 2")
	ActionInput[] phase2;
	@SerializedName("Phase 3")
	ActionInput[] phase3;
}