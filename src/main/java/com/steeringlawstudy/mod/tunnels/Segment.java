package com.steeringlawstudy.mod.tunnels;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

/**
 * element of a steering law tunnel
 * can be start, stop or path
 */
public class Segment {
    public String name;
    private Block block;
    private BlockPos pos;
    private int x,y,z;
    private boolean visited = false;
    private Tunnel tunnel;

    public Segment(Block b, BlockPos p) {
        block = b;
        pos = p;
        x = p.getX();
        y = p.getY();
        z = p.getZ();
    }

    public void reset() {
        visited = false;
    }
}
