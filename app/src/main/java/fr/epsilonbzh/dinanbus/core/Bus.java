package fr.epsilonbzh.dinanbus.core;

import java.util.ArrayList;

public class Bus {
    private final ArrayList<Line> line_list;

    public Bus(ArrayList<Line> line_list) {
        this.line_list = line_list;
    }

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
