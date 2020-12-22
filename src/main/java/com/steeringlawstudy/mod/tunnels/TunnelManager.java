package com.steeringlawstudy.mod.tunnels;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.InputUpdateEvent;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * singleton manager of all steering law tunnels, interfaces with ClientEvents
 */
public class TunnelManager {
    public static HashMap<String, Tunnel> list = new HashMap<>();
    public static World world;
    public static LivingEntity player;
    public static boolean found, started;

    private static ArrayList<BlockPos> availablePlayerLocations = new ArrayList<>();
    private static BlockPos currentPlayerLocation;
    private static int currentIndex;

    /**
     * handles all considering the tunnels, called within ClientEvents.getTargetBlock
     *
     * @param pos of targeted block
     */
    public static void manage(BlockPos pos, World w, LivingEntity p) {
        String segmentName = TunnelManager.getSegmentName(pos);
        found = false;

        if (list.isEmpty()) {
            player = p;
            world = w;

            // HARDCODED FOR TESTING
            Tunnel tunnel = new Tunnel("test", w, p);
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
                if (tunnel.start.wasVisited()) started = true;

                checkCompletion(tunnel);
            }
        });

        // targeted block not part of tunnel --> reset tunnel
        if (!found && started) {
            list.forEach((name, tunnel) -> tunnel.reset());
            //SteeringLawStudy.LOGGER.info("out of bounds, tunnel restarted.");
            started = false;
            world.playSound((PlayerEntity) player, list.get("test").start.getPos(), SoundEvents.BLOCK_NOTE_BLOCK_BASS, SoundCategory.MASTER, 100, 0);
        }
    }

    /**
     * @return name string for segment at position pos
     */
    public static String getSegmentName(BlockPos pos) {
        return "" + pos.getX() + "/" + pos.getY() + "/" + pos.getZ();
    }

    /**
     * Checks completion of Tunnel t, plays sound if fully completed
     */
    public static void checkCompletion(Tunnel t) {
        if (t.stop.wasVisited()) {
            t.list.forEach((name, segment) -> {
                if (!segment.wasVisited()) return;
            });

            t.start.reset();
            //SteeringLawStudy.LOGGER.info("now tunnel is finished");
            world.playSound((PlayerEntity) player, t.start.getPos(), SoundEvents.BLOCK_NOTE_BLOCK_BELL, SoundCategory.MASTER, 100, 0);
        }
    }

    public static void teleportPlayer(InputUpdateEvent event) {
        boolean goingLeft = event.getMovementInput().leftKeyDown;
        boolean goingRight = event.getMovementInput().rightKeyDown;
        PlayerEntity player = event.getPlayer();

        if (availablePlayerLocations.isEmpty()) {
            // HARDCODED FOR TESTING
            currentPlayerLocation = new BlockPos(player.getPositionVec());

            availablePlayerLocations.add(currentPlayerLocation);
            availablePlayerLocations.add(new BlockPos(-305, 90, 330));
            availablePlayerLocations.add(new BlockPos(-305, 90, 340));
            availablePlayerLocations.add(new BlockPos(-305, 90, 350));
        }

        currentIndex = availablePlayerLocations.indexOf(currentPlayerLocation);

        // determine next index depending on input
        if (goingLeft) {
            if (currentIndex == 0) {
                currentIndex = availablePlayerLocations.size() - 1;
            } else {
                currentIndex = currentIndex - 1;
            }

        } else if (goingRight) {
            if (currentIndex == availablePlayerLocations.size() - 1) {
                currentIndex = 0;
            } else {
                currentIndex = currentIndex + 1;
            }
        }

        currentPlayerLocation = availablePlayerLocations.get(currentIndex);
        player.setRawPosition(
                currentPlayerLocation.getX(),
                currentPlayerLocation.getY(),
                currentPlayerLocation.getZ()
        );
    }
}
