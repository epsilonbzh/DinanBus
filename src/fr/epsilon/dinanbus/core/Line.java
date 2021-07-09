package fr.epsilon.dinanbus.core;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Line {
	private int number;
	private ArrayList<Stop> stop_list;
	
	public Line(int number, ArrayList<Stop> stop_list) {
		this.number = Math.abs(number);
		if(stop_list != null && stop_list.size() > 0) {
			this.stop_list = stop_list;
		}else {
			throw new IllegalArgumentException("Stop list can't be empty or null");
		}
	}
	
	public Line(String filepath) {
		if(filepath != null && filepath.length() > 0) {
			parseXMLFile(filepath);
		}else {
			throw new IllegalArgumentException("file path list can't be empty or null");
		}
	}
	
	private void parseXMLFile(String filepath) {
		int linenumber;
		ArrayList<Stop> stoplist = new ArrayList<Stop>();
		try {
			File file = new File(filepath);
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line;
			String content = null;
			//read file
			while((line = reader.readLine()) != null) {
				content = content + line;
			}
			reader.close();
			//trim
			String[] items = content.split("\t");
			for(int i = 0; i<items.length; i++) {
				items[i] = items[i].replaceAll("\t", "");
			}
			ArrayList<String> list = new ArrayList<String>();
			for(String s : items) {
				if(!s.equals("")) {
					list.add(s);
				}
			}
			//parse
			String s_numberline = "-1";
			for(String elem : list) {
				if(elem.startsWith("<Line")){
					s_numberline = elem.replaceAll("Line", "").replaceAll("linenumber", "").replaceAll("<", "").replaceAll(">", "").replaceAll("=", "").replaceAll("\"", "").replace("/", "").replaceAll(" ", "");
				}
				if(elem.startsWith("<Stop ")) {
					int count = 1;
					String name = elem.replaceAll("Stop", "").replaceAll("name", "").replaceAll("<", "").replaceAll(">", "").replaceAll("=", "").replaceAll("\"", "");
					ArrayList<Integer> stopl = new ArrayList<Integer>();
					while(!list.get(list.indexOf(elem) + count).equalsIgnoreCase("</Stop>")) {
						String s_stop = list.get(list.indexOf(elem) + count).replaceAll("Schedule", "").replaceAll("hour", "").replaceAll("<", "").replaceAll(">", "").replaceAll("=", "").replaceAll("\"", "").replace("/", "").replaceAll(" ", "");
						stopl.add(Integer.valueOf(s_stop));
						count++;
					}
					stoplist.add(new Stop(name, stopl));
				}
			}
			linenumber = Integer.valueOf(s_numberline);
		}catch (FileNotFoundException e) {
			throw new IllegalArgumentException("data file not found");
		}catch (IOException e) {
			throw new IllegalArgumentException("error during reading process");
		}
		
		this.number = linenumber;
		this.stop_list = stoplist;
	}
	
	public Stop getStop(int index) {
		return stop_list.get(index);
	}
	
	@Override
	public String toString() {
		String message = "Line number : " + this.number;
		for(Stop s : this.stop_list) {
			message = message + "\n\t" + s.toString();
		}
		return message;
	}
}
