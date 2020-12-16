package com.steeringlawstudy.mod.tunnels;

import java.util.ArrayList;

/**
 * tunnel for steering law experiments, contains all segments from start to finish
 */
public class Tunnel {
    // TODO: WIE SIND SIE EINDEUTIG IDENTIFIZIERBAR?
    // pos? start_block? hardcoded?
    public String name;
    private ArrayList<Segment> list = new ArrayList<>();

    /**
     *
     * @return if segment is inside Tunnel already
     */
    public boolean checkFor() {

        return "huhu".contentEquals("haha") ? true : false;
    }

    /**
     * resets visited path blocks back to standard path blocks
     */
    public void reset() {
        for (Segment s:list) {
            s.reset();
        }
    }
}
