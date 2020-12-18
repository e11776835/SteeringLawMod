package com.steeringlawstudy.mod.tunnels;

import com.steeringlawstudy.mod.util.SegmentType;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;

/**
 * element of a steering law tunnel
 * can be start, stop or path
 */
public class Segment {
    public String name;
    private BlockPos pos;
    private SegmentType type;
    private int x, y, z;
    private boolean visited = false;
    private Tunnel tunnel;

    public Segment(BlockPos p, SegmentType st, Tunnel t) {
        pos = p;
        type = st;
        tunnel = t;
        x = p.getX();
        y = p.getY();
        z = p.getZ();
        name = TunnelManager.getSegmentName(p);
    }

    public void setVisited() {
        if (type == SegmentType.PATH) {
            tunnel.getWorld().setBlockState(pos, Blocks.YELLOW_CONCRETE.getDefaultState());
        }
        visited = true;
    }

    public boolean wasVisited() {
        return visited;
    }

    public SegmentType getType() {
        return type;
    }

    public void reset() {
        if (type.equals(SegmentType.PATH)) {
            tunnel.getWorld().setBlockState(pos, Blocks.WHITE_CONCRETE.getDefaultState());
        }
        visited = false;
    }
}
