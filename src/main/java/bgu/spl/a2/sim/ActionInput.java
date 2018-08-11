package bgu.spl.a2.sim;
import java.util.ArrayList;

public class ActionInput
//a class that is built like the Json 'Action'. 
//not all fields are relevant ot all actions, so some of those will remain null 
{
	String Action;
	String Department;
	String Course;
	String Space;
	String Student;
	ArrayList<String> Prerequisites;
	ArrayList<String> Preferences;
	ArrayList<String> Grade;
	String Computer;
	ArrayList<String> Conditions;
	ArrayList<String> Students;
}