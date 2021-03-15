package com.steeringlawstudy.mod.tunnels;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;

/**
 * segment (block) of a steering law tunnel
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
        if (tunnel.allDone) return;
        if (type == SegmentType.PATH) {
            tunnel.getWorld().setBlockState(pos, Blocks.LIGHT_GRAY_CONCRETE.getDefaultState());
        }
        visited = true;
    }

    public boolean wasVisited() {
        return visited;
    }

    public SegmentType getType() {
        return type;
    }

    public BlockPos getPos() {
        return pos;
    }

    public void reset() {
        if (tunnel.allDone) return;

        if (type.equals(SegmentType.PATH)) {
            tunnel.getWorld().setBlockState(pos, Blocks.WHITE_CONCRETE.getDefaultState());
        }
        visited = false;
    }

    public void setAllDone() {
        tunnel.getWorld().setBlockState(pos, Blocks.AIR.getDefaultState());
    }

    public void prepareNextRun() {
        if (type == SegmentType.PATH) {
            tunnel.getWorld().setBlockState(pos, Blocks.WHITE_CONCRETE.getDefaultState());
        } else if (type == SegmentType.START) {
            tunnel.getWorld().setBlockState(pos, Blocks.LIME_CONCRETE.getDefaultState());
        } else if (type == SegmentType.STOP) {
            tunnel.getWorld().setBlockState(pos, Blocks.RED_CONCRETE.getDefaultState());
        }
    }
}
