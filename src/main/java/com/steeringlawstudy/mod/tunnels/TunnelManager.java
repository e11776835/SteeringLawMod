package com.steeringlawstudy.mod.tunnels;

import com.steeringlawstudy.mod.SteeringLawStudy;
import com.steeringlawstudy.mod.gui.TunnelGUI;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
    private static HashMap<String, ArrayList<Integer>> completionCount = new HashMap<>();
    private static int currentCameraIndex, currentTunnelIndex;
    private static String currentTunnel;
    private static BlockPos currentPlayerLocation;

    /**
     * Registers all tunnels and their cameraAngles to data structures
     */
    public static void init(PlayerEntity p, World w) {
        player = p;
        world = w;

        // Start and Stop pos for experiment
        BlockPos pos_start = new BlockPos(153, 67, 2);
        Tunnel startTunnel = new Tunnel(TunnelManager.getSegmentName(pos_start), world);
        startTunnel.playerStart = pos_start;
        availableCameraAngles.put(startTunnel.name, new ArrayList<>());
        completionCount.put(startTunnel.name, new ArrayList<>());
        availableCameraAngles.get(startTunnel.name).add(startTunnel.playerStart);
        list.put(startTunnel.name, startTunnel);

        BlockPos pos_end = new BlockPos(305, 64, -31);
        Tunnel endTunnel = new Tunnel(TunnelManager.getSegmentName(pos_end), world);
        endTunnel.playerStart = pos_end;
        availableCameraAngles.put(endTunnel.name, new ArrayList<>());
        completionCount.put(endTunnel.name, new ArrayList<>());
        availableCameraAngles.get(endTunnel.name).add(endTunnel.playerStart);
        list.put(endTunnel.name, endTunnel);

        // no matter where player enters world, currentTunnel/Camera/Location can't be null
        currentTunnel = startTunnel.name;
        currentCameraIndex = availableCameraAngles.get(currentTunnel).indexOf(startTunnel.playerStart);
        currentTunnelIndex = 0;
        currentPlayerLocation = startTunnel.playerStart;

        // TUNNELS
        BlockPos start = new BlockPos(190, 66, -6);
        Tunnel tunnel = new Tunnel(TunnelManager.getSegmentName(start), world);
        tunnel.playerStart = new BlockPos(186, 65, -16);

        // tunnel 1: 7 wide, horizontal, angles: 10 blocks away, 18 blocks away
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

        BlockPos start_2 = new BlockPos(222, 64, 80);
        Tunnel tunnel_2 = new Tunnel(TunnelManager.getSegmentName(start_2), world);
        tunnel_2.playerStart = new BlockPos(223, 66, 75);

        // tunnel 2: 5 high, vertical, angles: 5 blocks away, 8 blocks away
        tunnel_2.add(start_2, SegmentType.START);
        tunnel_2.add(new BlockPos(222, 65, 80), SegmentType.PATH);
        tunnel_2.add(new BlockPos(222, 66, 80), SegmentType.PATH);
        tunnel_2.add(new BlockPos(222, 67, 80), SegmentType.PATH);
        tunnel_2.add(new BlockPos(222, 68, 80), SegmentType.PATH);
        tunnel_2.add(new BlockPos(222, 69, 80), SegmentType.PATH);
        tunnel_2.add(new BlockPos(222, 70, 80), SegmentType.STOP);
        list.put(tunnel_2.name, tunnel_2);

        BlockPos start_3 = new BlockPos(272, 83, 61);
        Tunnel tunnel_3 = new Tunnel(TunnelManager.getSegmentName(start_3), world);
        tunnel_3.playerStart = new BlockPos(270, 80, 54);

        // tunnel 3: 4 wide hor., corner, 4 vertical, angles: 7 blocks away, 17 blocks away
        tunnel_3.add(start_3, SegmentType.START);
        tunnel_3.add(new BlockPos(271, 83, 61), SegmentType.PATH);
        tunnel_3.add(new BlockPos(270, 83, 61), SegmentType.PATH);
        tunnel_3.add(new BlockPos(269, 83, 61), SegmentType.PATH);
        tunnel_3.add(new BlockPos(268, 83, 61), SegmentType.PATH);
        tunnel_3.add(new BlockPos(268, 82, 61), SegmentType.PATH);
        tunnel_3.add(new BlockPos(268, 81, 61), SegmentType.PATH);
        tunnel_3.add(new BlockPos(268, 80, 61), SegmentType.PATH);
        tunnel_3.add(new BlockPos(268, 79, 61), SegmentType.STOP);
        list.put(tunnel_3.name, tunnel_3);

        BlockPos start_4 = new BlockPos(253, 82, 137);
        Tunnel tunnel_4 = new Tunnel(TunnelManager.getSegmentName(start_4), world);
        tunnel_4.playerStart = new BlockPos(248, 81, 130);

        // tunnel 4: 9 wide, horizontal, angles: 7 blocks away, 17 blocks away
        tunnel_4.add(start_4, SegmentType.START);
        tunnel_4.add(new BlockPos(252, 82, 137), SegmentType.PATH);
        tunnel_4.add(new BlockPos(251, 82, 137), SegmentType.PATH);
        tunnel_4.add(new BlockPos(250, 82, 137), SegmentType.PATH);
        tunnel_4.add(new BlockPos(249, 82, 137), SegmentType.PATH);
        tunnel_4.add(new BlockPos(248, 82, 137), SegmentType.PATH);
        tunnel_4.add(new BlockPos(247, 82, 137), SegmentType.PATH);
        tunnel_4.add(new BlockPos(246, 82, 137), SegmentType.PATH);
        tunnel_4.add(new BlockPos(245, 82, 137), SegmentType.PATH);
        tunnel_4.add(new BlockPos(244, 82, 137), SegmentType.PATH);
        tunnel_4.add(new BlockPos(243, 82, 137), SegmentType.STOP);
        list.put(tunnel_4.name, tunnel_4);

        BlockPos start_5 = new BlockPos(211, 81, 39);
        Tunnel tunnel_5 = new Tunnel(TunnelManager.getSegmentName(start_5), world);
        tunnel_5.playerStart = new BlockPos(209, 82, 35);

        // tunnel 5: 5 wide, corner, 5 high, angles: 4 blocks away, 6 blocks away
        tunnel_5.add(start_5, SegmentType.START);
        tunnel_5.add(new BlockPos(210, 81, 39), SegmentType.PATH);
        tunnel_5.add(new BlockPos(209, 81, 39), SegmentType.PATH);
        tunnel_5.add(new BlockPos(208, 81, 39), SegmentType.PATH);
        tunnel_5.add(new BlockPos(207, 81, 39), SegmentType.PATH);
        tunnel_5.add(new BlockPos(206, 81, 39), SegmentType.PATH);
        tunnel_5.add(new BlockPos(206, 82, 39), SegmentType.PATH);
        tunnel_5.add(new BlockPos(206, 83, 39), SegmentType.PATH);
        tunnel_5.add(new BlockPos(206, 84, 39), SegmentType.PATH);
        tunnel_5.add(new BlockPos(206, 85, 39), SegmentType.PATH);
        tunnel_5.add(new BlockPos(206, 86, 39), SegmentType.STOP);
        list.put(tunnel_5.name, tunnel_5);

        // CAMERA ANGLES
        availableCameraAngles.put(tunnel.name, new ArrayList<>());
        availableCameraAngles.put(tunnel_2.name, new ArrayList<>());
        availableCameraAngles.put(tunnel_3.name, new ArrayList<>());
        availableCameraAngles.put(tunnel_4.name, new ArrayList<>());
        availableCameraAngles.put(tunnel_5.name, new ArrayList<>());

        completionCount.put(tunnel.name, new ArrayList<>());
        completionCount.put(tunnel_2.name, new ArrayList<>());
        completionCount.put(tunnel_3.name, new ArrayList<>());
        completionCount.put(tunnel_4.name, new ArrayList<>());
        completionCount.put(tunnel_5.name, new ArrayList<>());

        availableCameraAngles.get(tunnel.name).add(tunnel.playerStart);
        availableCameraAngles.get(tunnel_2.name).add(tunnel_2.playerStart);
        availableCameraAngles.get(tunnel_3.name).add(tunnel_3.playerStart);
        availableCameraAngles.get(tunnel_4.name).add(tunnel_4.playerStart);
        availableCameraAngles.get(tunnel_5.name).add(tunnel_5.playerStart);

        completionCount.get(tunnel.name).add(0);
        completionCount.get(tunnel.name).add(0);
        completionCount.get(tunnel_2.name).add(0);
        completionCount.get(tunnel_2.name).add(0);
        completionCount.get(tunnel_3.name).add(0);
        completionCount.get(tunnel_3.name).add(0);
        completionCount.get(tunnel_4.name).add(0);
        completionCount.get(tunnel_4.name).add(0);
        completionCount.get(tunnel_5.name).add(0);
        completionCount.get(tunnel_5.name).add(0);

        availableCameraAngles.get(tunnel.name).add(new BlockPos(186, 65, -24));
        availableCameraAngles.get(tunnel_2.name).add(new BlockPos(223, 66, 72));
        availableCameraAngles.get(tunnel_3.name).add(new BlockPos(270, 80, 44));
        availableCameraAngles.get(tunnel_4.name).add(new BlockPos(248, 81, 120));
        availableCameraAngles.get(tunnel_5.name).add(new BlockPos(209, 82, 33));
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
                world.playSound((PlayerEntity) player, currentPlayerLocation,
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

        // determine next tunnel depending on input
        if (goingUp) {
            if (list.higherEntry(currentTunnel) != null) {
                currentTunnel = list.higherEntry(currentTunnel).getKey();
                currentTunnelIndex += 1;
                sound = true;
            }
        } else if (goingDown && currentTunnelIndex > 0) {
            if (list.lowerEntry(currentTunnel) != null) {
                currentTunnel = list.lowerEntry(currentTunnel).getKey();
                currentTunnelIndex -= 1;
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
        TunnelGUI.currentTunnel = currentTunnelIndex;
        TunnelGUI.currentAngle = currentCameraIndex + 1;
        TunnelGUI.currentNumAngles = availableCameraAngles.get(currentTunnel).size();
        TunnelGUI.progress = calculateCompletionPercentage();
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

        TunnelGUI.currentAngle = currentCameraIndex + 1;
        TunnelGUI.currentNumAngles = availableCameraAngles.get(currentTunnel).size();
        TunnelGUI.progress = calculateCompletionPercentage();

        currentPlayerLocation = availableCameraAngles.get(currentTunnel).get(currentCameraIndex);
        player.setRawPosition(
                currentPlayerLocation.getX(),
                currentPlayerLocation.getY(),
                currentPlayerLocation.getZ()
        );

        if (availableCameraAngles.get(currentTunnel).size() > 1) {
            world.playSound((PlayerEntity) player, currentPlayerLocation,
                    SoundEvents.UI_BUTTON_CLICK, SoundCategory.MASTER, 20, 1);
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

            // only trigger completion sound once
            if (!t.complete) {
                t.list.forEach((name, segment) -> {
                    segment.setVisited();
                });

                world.playSound((PlayerEntity) player, currentPlayerLocation, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP,
                        SoundCategory.MASTER, 60, 0);

                t.complete = true;

                ArrayList countList = completionCount.get(t.name);
                Integer counter = (Integer) countList.get(currentCameraIndex);
                if (counter < SteeringLawStudy.COMPLETIONS) countList.set(currentCameraIndex, ++counter);

                TunnelGUI.progress = calculateCompletionPercentage();

                // shooting fireworks after completing an angle/tunnel
                if (counter == SteeringLawStudy.COMPLETIONS && !world.isRemote()) {
                    launchFireworks();
                }
            }
        }
    }

    /**
     * after a tunnel section is complete, launch fireworks
     */
    private static void launchFireworks() {
/*
                ParticleType type = ParticleTypes.FIREWORK;
                IParticleData particleData = new ItemParticleData(type, new ItemStack(Items.ROSE_BUSH));
                world.addParticle(particleData, currentPlayerLocation.getX(), currentPlayerLocation.getY(),
                        currentPlayerLocation.getZ() + 4, .5,.5,.5);
*/
        ItemStack firework = new ItemStack(Items.FIREWORK_ROCKET);
/*
                    CompoundNBT explosionNBT = new CompoundNBT();
                    CompoundNBT tagNBT = new CompoundNBT();
                    CompoundNBT fireworksNBT = new CompoundNBT();
                    CompoundNBT fireworksItemNBT = new CompoundNBT();
                    CompoundNBT rocketNBT = new CompoundNBT();
                    int[] aint = new int[2];
                    aint[0] = 11743532;
                    aint[1] = 2437522;

                    explosionNBT.putInt("Type", 1);
                    explosionNBT.putIntArray("Colors", aint);
                    explosionNBT.putIntArray("FadeColors", aint);
                    explosionNBT.putInt("Trail", 1);
                    explosionNBT.putInt("Flicker", 1);

                    tagNBT.put("Explosion", explosionNBT);
                    fireworksNBT.put("Explosions", explosionNBT);
                    tagNBT.put("Fireworks", fireworksNBT);
                    firework.setTag(tagNBT);
*/
        FireworkRocketEntity rocket = new FireworkRocketEntity(world,
                currentPlayerLocation.getX() - 2, currentPlayerLocation.getY(),
                currentPlayerLocation.getZ() + 4, firework);
        FireworkRocketEntity rocket1 = new FireworkRocketEntity(world,
                currentPlayerLocation.getX() - 1, currentPlayerLocation.getY(),
                currentPlayerLocation.getZ() + 4, firework);
        FireworkRocketEntity rocket2 = new FireworkRocketEntity(world,
                currentPlayerLocation.getX(), currentPlayerLocation.getY(),
                currentPlayerLocation.getZ() + 4, firework);
        FireworkRocketEntity rocket3 = new FireworkRocketEntity(world,
                currentPlayerLocation.getX() + 1, currentPlayerLocation.getY(),
                currentPlayerLocation.getZ() + 4, firework);
        FireworkRocketEntity rocket4 = new FireworkRocketEntity(world,
                currentPlayerLocation.getX() + 2, currentPlayerLocation.getY(),
                currentPlayerLocation.getZ() + 4, firework);
/*
                    rocketNBT.putInt("LifeTime", 20);
                    rocketNBT.putInt("Count", 5);
                    rocketNBT.put("FireworksItem", fireworksItemNBT);
                    rocket.writeAdditional(rocketNBT);
*/
        world.addEntity(rocket);
        world.addEntity(rocket1);
        world.addEntity(rocket2);
        world.addEntity(rocket3);
        world.addEntity(rocket4);
    }

    /**
     * @return completion % of currentTunnel for GUI
     */
    private static Float calculateCompletionPercentage() {
        Float currentCompletions = 0f;

        for (Integer count : completionCount.get(currentTunnel)) {
            currentCompletions += count;
        }

        int completionsNeeded = availableCameraAngles.get(currentTunnel).size() * SteeringLawStudy.COMPLETIONS;

        return currentCompletions / completionsNeeded * 100;
    }
}
