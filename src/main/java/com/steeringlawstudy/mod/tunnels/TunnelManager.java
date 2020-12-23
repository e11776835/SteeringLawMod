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
import java.util.TreeMap;

/**
 * manager of all steering law tunnels, interfaces with ClientEvents
 */
public class TunnelManager {
    public static TreeMap<String, Tunnel> list = new TreeMap<>();
    public static World world;
    public static LivingEntity player;
    public static boolean found, started;

    private static HashMap<String, ArrayList<BlockPos>> availableCameraAngles = new HashMap<>();
    private static int currentCameraIndex;
    private static String currentTunnel;
    private static BlockPos currentPlayerLocation;

    /**
     * Registers all tunnels and their cameraAngles to data structures
     */
    public static void init() {
        // TUNNELS
        BlockPos start = new BlockPos(-305, 89, 316);
        currentTunnel = TunnelManager.getSegmentName(start);
        Tunnel tunnel = new Tunnel(currentTunnel, world, player);
        tunnel.add(start, SegmentType.START);
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

        // PLAYER POSITIONS
        currentPlayerLocation = new BlockPos(player.getPositionVec());

        availableCameraAngles.put(currentTunnel, new ArrayList<BlockPos>());
        availableCameraAngles.get(currentTunnel).add(new BlockPos(-305, 90, 330));
        availableCameraAngles.get(currentTunnel).add(new BlockPos(-305, 90, 340));
        availableCameraAngles.get(currentTunnel).add(new BlockPos(-305, 95, 350));

        tunnel.playerStart = currentPlayerLocation;
    }

    /**
     * handles all considering the tunnels, called within ClientEvents.getTargetBlock
     * checks if tunnel has to be restarted (and plays sound)
     *
     * @param pos of targeted block
     * @param w   World
     * @param p   Player
     */
    public static void manage(BlockPos pos, World w, LivingEntity p) {
        String segmentName = getSegmentName(pos);
        found = false;

        if (list.isEmpty()) {
            player = p;
            world = w;
            init();
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
            world.playSound((PlayerEntity) player, list.get(currentTunnel).start.getPos(), SoundEvents.BLOCK_NOTE_BLOCK_BASS, SoundCategory.MASTER, 100, 0);
        }
    }

    /**
     * @return name string for tunnel segment (block) at position pos
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

    /**
     * Changes camera angle of each tunnel with A/D
     * TODO add change tunnel with W/S
     */
    public static void teleportPlayer(InputUpdateEvent event) {
        boolean goingLeft = event.getMovementInput().leftKeyDown;
        boolean goingRight = event.getMovementInput().rightKeyDown;
        PlayerEntity player = event.getPlayer();

        // CAUTION: right now, this REQUIRES startingPos of player in world to be a known camera angle
        currentCameraIndex = availableCameraAngles.get(currentTunnel).indexOf(currentPlayerLocation);
        if (currentCameraIndex == -1) return;

        // determine next cameraIndex depending on input
        if (goingLeft) {
            if (currentCameraIndex == 0) {
                currentCameraIndex = availableCameraAngles.get(currentTunnel).size() - 1;
            } else {
                currentCameraIndex = currentCameraIndex - 1;
            }

        } else if (goingRight) {
            if (currentCameraIndex == availableCameraAngles.get(currentTunnel).size() - 1) {
                currentCameraIndex = 0;
            } else {
                currentCameraIndex = currentCameraIndex + 1;
            }
        }

        currentPlayerLocation = availableCameraAngles.get(currentTunnel).get(currentCameraIndex);
        player.setRawPosition(
                currentPlayerLocation.getX(),
                currentPlayerLocation.getY(),
                currentPlayerLocation.getZ()
        );
    }
}
