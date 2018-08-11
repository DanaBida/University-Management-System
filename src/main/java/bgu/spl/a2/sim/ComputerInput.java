package bgu.spl.a2.sim;

import com.google.gson.annotations.SerializedName;

public class ComputerInput {
//a class that is built like the Json 'Computer'. 

	String Type;
	@SerializedName("Sig Fail")
	long failSig;
	@SerializedName("Sig Success")
	long successSig;

}
