package com.steeringlawstudy.mod.tunnels;

import com.steeringlawstudy.mod.util.SegmentType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;

/**
 * tunnel for steering law experiments, contains all segments from start to finish
 */
public class Tunnel {
    // TODO: EINDEUTIGE ID
    public String name;
    private World world;
    private HashMap<String, Segment> list = new HashMap<>();

    public Tunnel(String n, World w) {
        name = n;
        world = w;
    }

    /**
     * adds new segment to tunnel
     */
    public void add(BlockPos pos, SegmentType type) {
        Segment s = new Segment(pos, type, this);
        list.put(TunnelManager.getSegmentName(pos), s);
    }

    /**
     * @return if segment is inside Tunnel already
     */
    public boolean checkFor(String segmentName) {
        return list.containsKey(segmentName);
    }

    public void setVisited(String segmentName) {
        list.get(segmentName).setVisited();
    }

    public SegmentType getType(String segmentName) {
        return list.get(segmentName).getType();
    }

    public World getWorld() {
        return world;
    }

    /**
     * resets visited path blocks back to standard path blocks
     */
    public void reset() {
        list.forEach((name, segment) -> segment.reset());
    }
}
