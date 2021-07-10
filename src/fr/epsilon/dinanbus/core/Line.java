package fr.epsilon.dinanbus.core;


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
	 * Create a line objetct manually with a number and list of Stops
	 * @param number the number of the line
	 * @param stop_list list of stops in the line
	 * @see #number
	 * @see #stop_list
	 */
	public Line(int number, ArrayList<Stop> stop_list) {
		this.number = Math.abs(number);
		if(stop_list != null && stop_list.size() > 0) {
			this.stop_list = stop_list;
		}else {
			throw new IllegalArgumentException("Stop list can't be empty or null");
		}
	}
	/**
	 * Create a line object from an XML file.
	 * The default location for files is the data folder
	 * @param filepath to XML file
	 * @see #parseXMLFile(String)
	 */
	public Line(String filepath) {
		if(filepath != null && filepath.length() > 0) {
			parseXMLFile(filepath);
		}else {
			throw new IllegalArgumentException("file path list can't be empty or null");
		}
	}
	/**
	 * Extract data from an XML file to instantiate a Line object 
	 * @param filepath path to file
	 */
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
		for(Stop s : this.stop_list) {
			message = message + "\n\t" + s.toString();
		}
		return message;
	}
}
