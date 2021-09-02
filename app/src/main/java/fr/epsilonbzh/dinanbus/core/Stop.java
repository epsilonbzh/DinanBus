package fr.epsilonbzh.dinanbus.core;

import java.time.LocalDateTime;
import java.util.ArrayList;
/**
 * Stops are places that the bus serves.
 * It contains a name and timetables
 * @author epsilonbzh
 */
public class Stop {
	/**
	 * Line identifier,
	 * the value of this number can be anything, as long as it is unique among other ids.
	 */
	private int id;
	/**
	 * Name of the bus station.
	 */
	private String name;
	/**
	 * Latitude coordinates
	 */
	private double lat;
	/**
	 * Longitude coordinates
	 */
	private double lon;
	/**
	 * list containing the arrival times of the bus stop
	 * The time must be in minutes
	 * Example: 7h20 = 440 min
	 */
	private ArrayList<Integer> schedule_list;
	/**
	 * Create a Stop with the id,name, latitude, longitude, and the list of bus station
	 * @param id identifier of the stop
	 * @param name name of the stop
	 * @param lat latitude of the stop
	 * @param lon longitude of the stop
	 * @param schedule_list intergers list containing eatch times the bus stop at the station
	 * @see #id
	 * @see #name
	 * @see #lat
	 * @see #lon
	 * @see #schedule_list
	 */
	public Stop(int id,String name, double lat, double lon,ArrayList<Integer> schedule_list) {
		this.id = Math.abs(id);
		if(name != null && name.length() > 0) {
			this.name = name;
		}else {
			throw new IllegalArgumentException("Stop name can't be empty or null");
		}

		this.lat = lat;
		this.lon = lon;

		if(schedule_list != null && schedule_list.size() > 0) {
			this.schedule_list = schedule_list;
		}else {
			throw new IllegalArgumentException("Schedule list can't be empty or null");
		}
	}
	/**
	 * Get the station's Identifier
	 * @return id
	 * @see #id
	 */
	public int getID() {
		return this.id;
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
	 * Get the station's Latitude
	 * @return latitude
	 * @see #lat
	 */
	public double getLat() {
		return this.lat;
	}
	/**
	 * Get the station's Longitude
	 * @return longitude
	 * @see #lon
	 */
	public double getLon() {
		return this.lon;
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
	 * Get the time for the next bus stop
	 * based on device's current time
	 * @return remaining time in minutes
	 */
	public int getNext() {
		ArrayList<Integer> remainingTime = new ArrayList<Integer>(this.schedule_list);
		int currentTime = (LocalDateTime.now().getHour() *60) + LocalDateTime.now().getMinute();
		for(int v : remainingTime) {
			remainingTime.set(remainingTime.indexOf(v),v - currentTime);
		}
		int i = 0;
		boolean found = false;
		while(found == false && i < remainingTime.size()) {
			if(remainingTime.get(i) > 0) {
				found = true;
			}else {
				i++;
			}
		}
		if(found) {
			return remainingTime.get(i);
		}else {
			return 1440 - currentTime + this.schedule_list.get(0);
		}

	}
	/**
	 * Printable version of getNext(), showing time in hours and minutes
	 * @return remaining time in hours and minutes
	 * @see #getNext()
	 */
	public String printNext() {
		if(this.getNext() < 60) {
			return this.getNext() + "min";
		}else {
			if(this.getNext()%60 < 10) {
				return getNext() / 60 + "h0" + getNext() % 60 + "min";
			}else {
				return getNext() / 60 + "h" + getNext() % 60 + "min";
			}
		}

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
