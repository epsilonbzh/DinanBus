package fr.epsilon.dinanbus.core;

import java.util.ArrayList;
/**
 * Stops are places that the bus serves.
 * It contains a name and timetables
 * @author epsilonbzh
 */
public class Stop {
	/**
	 * Name of the bus station.
	 */
	private String name;
	/**
	 * list containing the arrival times of the bus stop
	 * The time must be in minutes
	 * Example: 7h20 = 440 min
	 */
	private ArrayList<Integer> schedule_list;
	/**
	 * Create a Stop with the name, and the list of bus station
	 * @param name name of the stop
	 * @param schedule_list intergers list containing eatch times the bus stop at the station
	 * @see #name
	 * @see #schedule_list
	 */
	public Stop(String name, ArrayList<Integer> schedule_list) {
		if(name != null && name.length() > 0) {
			this.name = name;
		}else {
			throw new IllegalArgumentException("Stop name can't be empty or null");
		}
		if(schedule_list != null && schedule_list.size() > 0) {
			this.schedule_list = schedule_list;
		}else {
			throw new IllegalArgumentException("Schedule list can't be empty or null");
		}
	}
	/**
	 * Get the station's Name
	 * @return name
	 * @see #name
	 */
	public String getName() {
		return this.name;
	}
	/**
	 * Get schedules
	 * @return schedules
	 * @see #schedule_list
	 */
	public ArrayList<Integer> getStop_list() {
		return this.schedule_list;
	}
	/**
	 * Printable text containing station information
	 */
	@Override
	public String toString() {
		String message = this.name;
		if(this.name.length() < 8) {
			message = message + "\t\t\t";
		}else if(this.name.length() >= 8 && this.name.length() < 16) {
			message = message + "\t\t";
		}else if(this.name.length() >= 16 && this.name.length() < 24){
			message = message + "\t";
		}
		
		for(int s : schedule_list) {
			if(s%60 < 10) {
				message = message + "\t"  + s/60 + ":0" + s%60;
			}else {
				message = message + "\t"  + s/60 + ":" + s%60;
			}
		}
		return message;
		
	}
}
