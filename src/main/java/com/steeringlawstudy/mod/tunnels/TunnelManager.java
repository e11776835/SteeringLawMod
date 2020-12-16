package com.steeringlawstudy.mod.tunnels;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import java.util.HashMap;

/**
 * singleton manager of all steering law tunnels, interfaces with ClientEvents
 */
public class TunnelManager {
    public static HashMap<String, Tunnel> list = new HashMap<>();

    /**
     * checks if targeted block is registered and does so if necessary
     */
    public void checkFor(Block block, BlockPos pos) {
        if (list.isEmpty()) {
            list.put("test", new Tunnel());
        } else {
            list.forEach((name, tunnel) -> {
                if (tunnel.checkFor()) {
                    return;
                }
            });

            // TODO add the segment
        }
    }
}
