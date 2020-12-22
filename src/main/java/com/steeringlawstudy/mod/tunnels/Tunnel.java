package com.steeringlawstudy.mod.tunnels;

import net.minecraft.entity.LivingEntity;
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
    private LivingEntity player;
    public HashMap<String, Segment> list = new HashMap<>();
    public Segment start, stop;

    public Tunnel(String n, World w, LivingEntity p) {
        name = n;
        world = w;
        player = p;
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
    }

    /**
     * @return if segment is inside Tunnel already
     */
    public boolean checkFor(String segmentName) {
        return list.containsKey(segmentName);
    }

    /**
     * sets targeted block to visited; also keeps track if tunnel is finished / has to be restarted
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
