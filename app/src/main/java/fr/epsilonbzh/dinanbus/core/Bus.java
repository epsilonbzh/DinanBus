package fr.epsilonbzh.dinanbus.core;


import java.util.ArrayList;

/**
 * The Bus class groups together all the lines of the same bus provider.
 * This allows you to do a stop search between all lines
 * @author epsilonbzh
 */
public class Bus {
    /**
     * list of all lines of the bus service
     * @see Line
     */
    private final ArrayList<Line> line_list;

    /**
     * Define a bus item
     * @param line_list list of lines
     * @see #line_list
     */
    public Bus(ArrayList<Line> line_list) {
        this.line_list = line_list;
    }

    /**
     * Get a line from the Line list
     * @param index index of the line
     * @return the line
     * @see #line_list
     */
    public Line getLine(int index) {
        return this.line_list.get(index);
    }

    /**
     * Separate stops with same coordinates
     */
    public void fixLinesWithSameCoordinates() {
        for(Line line : this.line_list) {
            for(Stop stop : line.getStops()) {
                if(stop.hasLinks()) {
                    int id = stop.getID();
                    for(Stop links  : stop.getLinks(this)) {
                        if(id < links.getID()) {
                            links.setLat(links.getLat() + 0.0000100);
                        }
                    }
                }
            }
            if(line.hasReverse()) {
                for (Stop stop : line.getStopsReverse()) {
                    if (stop.hasLinks()) {
                        int id = stop.getID();
                        for (Stop links : stop.getLinks(this)) {
                            if (id < links.getID()) {
                                links.setLat(links.getLat() + 0.0000100);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Stop with its unique identifier
     * @param id stop identifier
     * @return the corresponding stop
     * @see Stop
     */
    public Stop getStopByID(int id) {
        Stop ret = null;
        for(Line line : this.line_list) {
            for(Stop stop : line.getStops()) {
                if(stop.getID() == id) {
                    ret = stop;
                }
            }
            if(line.hasReverse()) {
                for(Stop stop : line.getStopsReverse()) {
                    if(stop.getID() == id) {
                        ret = stop;
                    }
                }
            }
        }
        if(ret == null) {
            throw new RuntimeException("getStopByID : item not found");
        }else {
            return ret;
        }
    }
}
