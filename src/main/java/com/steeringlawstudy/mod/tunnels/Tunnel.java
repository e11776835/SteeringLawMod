package com.steeringlawstudy.mod.tunnels;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * tunnel for steering law experiments, contains all segments from start to finish
 */
public class Tunnel {
    // UNIQUE name = BlockPos of START-BLOCK
    public String name;
    private World world;
    public HashMap<String, Segment> list = new HashMap<>();
    public ArrayList<BlockPos> availableCameraAngles = new ArrayList<>();
    public ArrayList<Integer> completionCount = new ArrayList<>();
    public ArrayList<Boolean> angleCompleted = new ArrayList<>();
    public Segment start, stop;
    public BlockPos playerStart;
    public boolean complete, allDone;

    public Tunnel(String n, World w) {
        name = n;
        world = w;
        allDone = false;
    }

    /**
     * adds new segment to tunnel
     */
    public void add(BlockPos pos, SegmentType type) {
        Segment s = new Segment(pos, type, this);
        list.put(TunnelManager.getSegmentName(pos), s);

        if (type == SegmentType.START) {
            start = s;
        } else if (type == SegmentType.STOP) {
            stop = s;
        }

        s.prepareNextRun();
    }

    /**
     * @return if segment is inside Tunnel already
     */
    public boolean checkFor(String segmentName) {
        return list.containsKey(segmentName);
    }

    /**
     * sets targeted block to visited
     */
    public void setVisited(String segmentName) {
        Segment s = list.get(segmentName);

        if (s.getType() == SegmentType.START) {
            reset();
            s.setVisited();
            start.setVisited();
            //SteeringLawStudy.LOGGER.info("now starting tunnel");
        } else if (start.wasVisited()) {
            s.setVisited();
        }
    }

    /**
     * resets visited path blocks back to standard path blocks
     */
    public void reset() {
        list.forEach((name, segment) -> segment.reset());
    }

    public void setAllDone() {
        allDone = true;
        list.forEach((name, segment) -> segment.setAllDone());
    }

    public void prepareNextRun() {
        allDone = false;
        completionCount = new ArrayList<>();
        angleCompleted = new ArrayList<>();
        setupCounts();
        list.forEach((name, segment) -> segment.prepareNextRun());
    }

    public SegmentType getType(String segmentName) {
        return list.get(segmentName).getType();
    }

    public World getWorld() {
        return world;
    }

    public void setupCounts() {
        for (BlockPos bp : availableCameraAngles) {
            completionCount.add(0);
            angleCompleted.add(false);
        }
    }
}
