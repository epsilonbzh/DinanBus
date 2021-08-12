package fr.epsilonbzh.dinanbus.core;


import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Lines are objects containing stops.
 * A line is identified by a number.
 * You can create a line manually in code with an ArrayList of Stop and an identifier,
 * but it is recommended to import it from an XML file.
 * @author epsilonbzh
 */
public class Line {
	/**
	 * This number is the identifier of the line, it must be greater than 0.
     * A value of -1 means the XML parser could not find the data
	 */
	private int number;
	/**
	 * List of all the stops served on the line.
	 * @see Stop
	 */
	private ArrayList<Stop> stop_list;
	/**
	 * List of all the stops served
	 * when the bus goes the other way.
	 * @see Stop
	 */
	private ArrayList<Stop> stop_list_reverse;
	/**
	 * Create a line object manually with a number and list of Stops
	 * @param number the number of the line
	 * @param stop_list list of stops in the line
	 * @see #number
	 * @see #stop_list
	 */
	public Line(int number, ArrayList<Stop> stop_list) {
		this.number = Math.abs(number);
		if(stop_list != null && stop_list.size() > 0) {
			this.stop_list = stop_list;
			this.stop_list_reverse = new ArrayList<Stop>();
		}else {
			throw new IllegalArgumentException("Stop list can't be empty or null");
		}
	}
	
	/**
	 * Create a two-way line object manually with a number and two lists in both directions
	 * @param number the number of the line
	 * @param stop_list list of stops in a way
	 * @param stop_list_reverse list of stops in the other way
	 * @see #number
	 * @see #stop_list
	 * @see #stop_list_reverse
	 */
	public Line(int number, ArrayList<Stop> stop_list,ArrayList<Stop> stop_list_reverse) {
		this.number = Math.abs(number);
		if(stop_list != null && stop_list.size() > 0 && stop_list_reverse != null && stop_list_reverse.size() > 0) {
			this.stop_list = stop_list;
			this.stop_list_reverse = stop_list_reverse;
		}else {
			throw new IllegalArgumentException("Stop lists can't be empty or null");
		}
	}
	/**
	 * Create a line object from an XML file.
	 * The default location for files is the data folder
	 * @param filepath to XML file
	 * @see #parseXMLFile(String)
	 */
	public Line(Context context,String filepath) {
		if(filepath != null && filepath.length() > 0) {
			parseXMLFile(context,filepath);
		}else {
			throw new IllegalArgumentException("file path list can't be empty or null");
		}
	}
	/**
	 * Extract data from an XML file to instantiate a Line object 
	 * @param filepath path to file
	 */
	private void parseXMLFile(Context context,String filepath) {
		int linenumber;
		ArrayList<Stop> currentlist;
		ArrayList<Stop> stoplist = new ArrayList<Stop>();
		ArrayList<Stop> stoplist_reverse = new ArrayList<Stop>();
		currentlist = stoplist;
		try {
			File file = new File(filepath);
			BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(filepath),"UTF-8"));
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
			for(int i = 0; i<list.size(); i++) {
				String elem = list.get(i);
				if(elem.startsWith("<Line")){
					s_numberline = elem.replaceAll("Line", "").replaceAll("linenumber", "").replaceAll("<", "").replaceAll(">", "").replaceAll("=", "").replaceAll("\"", "").replace("/", "").replaceAll(" ", "");
				}
				if(elem.startsWith("<Stop ")) {
					String name = "-1";
					double lon = 0;
					double lat = 0;
					ArrayList<Integer> stopl = new ArrayList<Integer>();
					int count = 1;
					String[] param = elem.split("\"");
					for(int p = 0; p<param.length; p++) {
						param[p].replaceAll("Stop", "").replaceAll("name", "").replaceAll("<", "").replaceAll(">", "").replaceAll("=", "").replaceAll("\"", "");
						if(param[p].equalsIgnoreCase("<Stop name=")) {
							name = param[p+1];
						}else if(param[p].trim().equalsIgnoreCase("lat=")) {
							lat = Double.parseDouble(param[p+1]);
						}else if(param[p].trim().equalsIgnoreCase("lon=")) {
							lon = Double.parseDouble(param[p+1]);
						}
					}
					
					while(!list.get(i + count).equalsIgnoreCase("</Stop>")) {
						String s_stop = list.get(i + count).replaceAll("Schedule", "").replaceAll("hour", "").replaceAll("<", "").replaceAll(">", "").replaceAll("=", "").replaceAll("\"", "").replace("/", "").replaceAll(" ", "");
						stopl.add(Integer.valueOf(s_stop));
						count++;
					}
					currentlist.add(new Stop(name,lon,lat,stopl));
				}
				if(elem.startsWith("</StopList>")) {
					currentlist = stoplist_reverse;
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
		this.stop_list_reverse = stoplist_reverse;
	}
	/**
	 * Get a stop by index
	 * @param index index of the ArrayList
	 * @return the corresponding stop.
	 * @see Stop
	 */
	public Stop getStop(int index) {
		return stop_list.get(index);
	}
	/**
	 * Make a printable tab of the line containing all the stops and they schedule
	 * @return printable tab
	 * @see Stop#toString()
	 */
	@Override
	public String toString() {
		String message = "Line number : " + this.number;
		//tell the direction if there is a reverse line
		if(this.stop_list_reverse.size() != 0) {
			message = message + "\nTOWARD" + stop_list.get(stop_list.size() - 1).getName().toUpperCase() + " :";
		}
		//print main line
		for(Stop s : this.stop_list) {
			message = message + "\n\t" + s.toString();
		}
		//print reverse line if exist
		if(this.stop_list_reverse.size() != 0) {
			message = message + "\nTOWARD" + stop_list_reverse.get(stop_list_reverse.size() - 1).getName().toUpperCase() + " :";
			for(Stop s : this.stop_list_reverse) {
				message = message + "\n\t" + s.toString();
			}
		}
		return message;
	}
}
