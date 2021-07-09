package fr.epsilon.dinanbus.core;

import java.util.ArrayList;

public class Stop {
	private String name;
	private ArrayList<Integer> schedule_list;
	
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
	
	public String getName() {
		return this.name;
	}
	
	public ArrayList<Integer> getStop_list() {
		return this.schedule_list;
	}
	
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
