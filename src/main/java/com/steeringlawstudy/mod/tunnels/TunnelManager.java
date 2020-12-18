package com.steeringlawstudy.mod.tunnels;

import com.steeringlawstudy.mod.SteeringLawStudy;
import com.steeringlawstudy.mod.util.SegmentType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;

/**
 * singleton manager of all steering law tunnels, interfaces with ClientEvents
 */
public class TunnelManager {
    public static HashMap<String, Tunnel> list = new HashMap<>();
    public static boolean found = false;

    /**
     * handles all considering the tunnels, called within ClientEvents.getTargetBlock
     * @param pos of targeted block
     */
    public static void manage(BlockPos pos, World world) {
        String segmentName = TunnelManager.getSegmentName(pos);
        found = false;

        if (list.isEmpty()) {
            // HARDCODED FOR TESTING
            Tunnel tunnel = new Tunnel("test", world);
            tunnel.add(new BlockPos(-305, 89, 316), SegmentType.START);
            tunnel.add(new BlockPos(-306, 89, 316), SegmentType.PATH);
            tunnel.add(new BlockPos(-307, 89, 316), SegmentType.PATH);
            tunnel.add(new BlockPos(-307, 90, 316), SegmentType.PATH);
            tunnel.add(new BlockPos(-307, 91, 316), SegmentType.PATH);
            tunnel.add(new BlockPos(-307, 92, 316), SegmentType.PATH);
            tunnel.add(new BlockPos(-306, 92, 316), SegmentType.PATH);
            tunnel.add(new BlockPos(-305, 92, 316), SegmentType.PATH);
            tunnel.add(new BlockPos(-304, 92, 316), SegmentType.PATH);
            tunnel.add(new BlockPos(-303, 92, 316), SegmentType.PATH);
            tunnel.add(new BlockPos(-302, 92, 316), SegmentType.PATH);
            tunnel.add(new BlockPos(-301, 92, 316), SegmentType.PATH);
            tunnel.add(new BlockPos(-301, 91, 316), SegmentType.PATH);
            tunnel.add(new BlockPos(-301, 90, 316), SegmentType.PATH);
            tunnel.add(new BlockPos(-301, 89, 316), SegmentType.PATH);
            tunnel.add(new BlockPos(-302, 89, 316), SegmentType.PATH);
            tunnel.add(new BlockPos(-303, 89, 316), SegmentType.STOP);

            list.put(tunnel.name, tunnel);
        }

        // search current pos, set it visited
        list.forEach((name, tunnel) -> {
            if (tunnel.checkFor(segmentName)) {
                tunnel.setVisited(segmentName);
                found = true;
            }
        });

        // targeted block not part of tunnel --> reset tunnel
        if (!found) {
            list.forEach((name, tunnel) -> tunnel.reset());
            //SteeringLawStudy.LOGGER.info("out of bounds, tunnel restarted.");
        }
    }

    /**
     * @return name string for segment at position pos
     */
    public static String getSegmentName(BlockPos pos) {
        return "" + pos.getX() + "/" + pos.getY() + "/" + pos.getZ();
    }
}
