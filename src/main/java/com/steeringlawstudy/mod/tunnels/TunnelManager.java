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
     * TODO: automate this?!
     */
    public static void init(PlayerEntity p, World w) {
        player = p;
        world = w;

        // Start and Stop pos for experiment
        BlockPos pos_start = new BlockPos(153, 67, 1);
        Tunnel startTunnel = new Tunnel(TunnelManager.getSegmentName(pos_start), world, player);
        startTunnel.playerStart = pos_start;
        availableCameraAngles.put(startTunnel.name, new ArrayList<>());
        availableCameraAngles.get(startTunnel.name).add(startTunnel.playerStart);
        list.put(startTunnel.name, startTunnel);

        BlockPos pos_end = new BlockPos(304, 63, -33);
        Tunnel endTunnel = new Tunnel(TunnelManager.getSegmentName(pos_end), world, player);
        endTunnel.playerStart = pos_end;
        availableCameraAngles.put(endTunnel.name, new ArrayList<>());
        availableCameraAngles.get(endTunnel.name).add(endTunnel.playerStart);
        list.put(endTunnel.name, endTunnel);

        // no matter where player enters world, currentTunnel/Camera/Location can't be null
        currentTunnel = startTunnel.name;
        currentCameraIndex = availableCameraAngles.get(currentTunnel).indexOf(startTunnel.playerStart);
        currentPlayerLocation = startTunnel.playerStart;

        // TUNNELS
        BlockPos start = new BlockPos(190, 66, -6);
        Tunnel tunnel = new Tunnel(TunnelManager.getSegmentName(start), world, player);
        tunnel.playerStart = new BlockPos(186, 65, -16);

        tunnel.add(start, SegmentType.START);
        tunnel.add(new BlockPos(189, 66, -6), SegmentType.PATH);
        tunnel.add(new BlockPos(188, 66, -6), SegmentType.PATH);
        tunnel.add(new BlockPos(187, 66, -6), SegmentType.PATH);
        tunnel.add(new BlockPos(186, 66, -6), SegmentType.PATH);
        tunnel.add(new BlockPos(185, 66, -6), SegmentType.PATH);
        tunnel.add(new BlockPos(184, 66, -6), SegmentType.PATH);
        tunnel.add(new BlockPos(183, 66, -6), SegmentType.PATH);
        tunnel.add(new BlockPos(182, 66, -6), SegmentType.PATH);
        tunnel.add(new BlockPos(181, 66, -6), SegmentType.PATH);
        tunnel.add(new BlockPos(180, 66, -6), SegmentType.STOP);
        list.put(tunnel.name, tunnel);

        BlockPos start_2 = new BlockPos(272, 83, 61);
        Tunnel tunnel_2 = new Tunnel(TunnelManager.getSegmentName(start_2), world, player);
        tunnel_2.playerStart = new BlockPos(270, 80, 54);

        tunnel_2.add(start_2, SegmentType.START);
        tunnel_2.add(new BlockPos(271, 83, 61), SegmentType.PATH);
        tunnel_2.add(new BlockPos(270, 83, 61), SegmentType.PATH);
        tunnel_2.add(new BlockPos(269, 83, 61), SegmentType.PATH);
        tunnel_2.add(new BlockPos(268, 83, 61), SegmentType.PATH);
        tunnel_2.add(new BlockPos(268, 82, 61), SegmentType.PATH);
        tunnel_2.add(new BlockPos(268, 81, 61), SegmentType.PATH);
        tunnel_2.add(new BlockPos(268, 80, 61), SegmentType.PATH);
        tunnel_2.add(new BlockPos(268, 79, 61), SegmentType.STOP);
        list.put(tunnel_2.name, tunnel_2);

        BlockPos start_3 = new BlockPos(222, 64, 80);
        Tunnel tunnel_3 = new Tunnel(TunnelManager.getSegmentName(start_3), world, player);
        tunnel_3.playerStart = new BlockPos(223, 66, 75);

        tunnel_3.add(start_3, SegmentType.START);
        tunnel_3.add(new BlockPos(222, 65, 80), SegmentType.PATH);
        tunnel_3.add(new BlockPos(222, 66, 80), SegmentType.PATH);
        tunnel_3.add(new BlockPos(222, 67, 80), SegmentType.PATH);
        tunnel_3.add(new BlockPos(222, 68, 80), SegmentType.PATH);
        tunnel_3.add(new BlockPos(222, 69, 80), SegmentType.PATH);
        tunnel_3.add(new BlockPos(222, 70, 80), SegmentType.STOP);
        list.put(tunnel_3.name, tunnel_3);

        BlockPos start_4 = new BlockPos(252, 82, 137);
        Tunnel tunnel_4 = new Tunnel(TunnelManager.getSegmentName(start_4), world, player);
        tunnel_4.playerStart = new BlockPos(248, 81, 130);

        tunnel_4.add(start_4, SegmentType.START);
        tunnel_4.add(new BlockPos(251, 82, 137), SegmentType.PATH);
        tunnel_4.add(new BlockPos(250, 82, 137), SegmentType.PATH);
        tunnel_4.add(new BlockPos(249, 82, 137), SegmentType.PATH);
        tunnel_4.add(new BlockPos(248, 82, 137), SegmentType.PATH);
        tunnel_4.add(new BlockPos(247, 82, 137), SegmentType.PATH);
        tunnel_4.add(new BlockPos(246, 82, 137), SegmentType.PATH);
        tunnel_4.add(new BlockPos(245, 82, 137), SegmentType.PATH);
        tunnel_4.add(new BlockPos(244, 82, 137), SegmentType.STOP);
        list.put(tunnel_4.name, tunnel_4);

        // CAMERA ANGLES
        availableCameraAngles.put(tunnel.name, new ArrayList<>());
        availableCameraAngles.put(tunnel_2.name, new ArrayList<>());
        availableCameraAngles.put(tunnel_3.name, new ArrayList<>());
        availableCameraAngles.put(tunnel_4.name, new ArrayList<>());

        availableCameraAngles.get(tunnel.name).add(tunnel.playerStart);
        availableCameraAngles.get(tunnel_2.name).add(tunnel_2.playerStart);
        availableCameraAngles.get(tunnel_3.name).add(tunnel_3.playerStart);
        availableCameraAngles.get(tunnel_4.name).add(tunnel_4.playerStart);

        availableCameraAngles.get(tunnel.name).add(new BlockPos(186, 68, -24));
        availableCameraAngles.get(tunnel_2.name).add(new BlockPos(270, 80, 44));
        availableCameraAngles.get(tunnel_3.name).add(new BlockPos(223, 66, 72));
        availableCameraAngles.get(tunnel_4.name).add(new BlockPos(247, 81, 120));
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
        player = p;
        Tunnel t = list.get(currentTunnel);

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

            if (!t.complete) {
                world.playSound((PlayerEntity) player, t.start.getPos(),
                        SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.MASTER, 90, 0);
            } else {
                t.start.reset();
                t.complete = false;
            }
        }
    }

    /**
     * Changes tunnel with W/S
     */
    public static void changeTunnel(InputUpdateEvent event) {
        boolean goingUp = event.getMovementInput().forwardKeyDown;
        boolean goingDown = event.getMovementInput().backKeyDown;
        boolean sound = false;
        PlayerEntity player = event.getPlayer();

        // determine next cameraIndex depending on input
        if (goingUp) {
            if (list.higherEntry(currentTunnel) != null) {
                currentTunnel = list.higherEntry(currentTunnel).getKey();
                sound = true;
            }
        } else if (goingDown) {
            if (list.lowerEntry(currentTunnel) != null) {
                currentTunnel = list.lowerEntry(currentTunnel).getKey();
                sound = true;
            }
        }

        // change player location to new one
        currentPlayerLocation = list.get(currentTunnel).playerStart;
        player.setRawPosition(
                currentPlayerLocation.getX(),
                currentPlayerLocation.getY(),
                currentPlayerLocation.getZ()
        );

        if (sound) {
            world.playSound((PlayerEntity) player, currentPlayerLocation,
                    SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.MASTER, 60, 1);
        }
        // reset currentCameraIndex
        currentCameraIndex = availableCameraAngles.get(currentTunnel).indexOf(list.get(currentTunnel).playerStart);
    }

    /**
     * Changes camera angle of each tunnel with A/D
     */
    public static void changeCameraAngle(InputUpdateEvent event) {
        boolean goingLeft = event.getMovementInput().leftKeyDown;
        boolean goingRight = event.getMovementInput().rightKeyDown;
        PlayerEntity player = event.getPlayer();

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

        world.playSound((PlayerEntity) player, currentPlayerLocation,
                SoundEvents.UI_BUTTON_CLICK, SoundCategory.MASTER, 20, 1);
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

            //SteeringLawStudy.LOGGER.info("now tunnel is finished");
            world.playSound((PlayerEntity) player, t.start.getPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP,
                    SoundCategory.MASTER, 90, 0);

            t.complete = true;
        }
    }

}
