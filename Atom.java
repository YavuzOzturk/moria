//This class is the smallest part of audio signature which holds 3 variables from filtered result
//time : holds time seconds
//location : holds location of value in a specific time
//value : is the exact frequency value at specific time and location

public class Atom {

	private int location;
	private int time;
	private double value;
	
	public Atom(int loc, int tim, double val){
		this.setLocation(loc);
		this.setTime(tim);
		this.setValue(val);
	}
	
	public String toString(){
		return ("Time: " + this.getTime() + " Location: " + this.getLocation() + " Value: " + this.getValue());
	}
	
	public void setLocation(int loc){
		this.location = loc;
	}
	
	public void setTime(int tim){
		this.time = tim;
	}
	
	public void setValue(double val){
		this.value = val;
	}
	
	public int getLocation(){
		return this.location;
	}
	
	public int getTime(){
		return this.time;
	}
	
	public double getValue(){
		return this.value;
	}
}