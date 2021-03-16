package com.steeringlawstudy.mod.events;

import com.steeringlawstudy.mod.SteeringLawStudy;
import com.steeringlawstudy.mod.tunnels.TunnelManager;
import com.steeringlawstudy.mod.util.DataValidator;
import com.steeringlawstudy.mod.util.PosHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * here certain events triggered by the player are handled
 */
@Mod.EventBusSubscriber(modid = SteeringLawStudy.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEvents {
    private static BlockPos lastTargetPos;
    private static int teleportCooldown;

    /**
     * while holding a food item, the targeted block is recorded
     * this will enable verification of user movement through paths
     */
    @SubscribeEvent
    public static void getTargetBlock(PlayerEvent.LivingUpdateEvent event) {
        World world = event.getEntity().getEntityWorld();
        if (!world.isRemote) return;
        if (lastTargetPos == null) lastTargetPos = new BlockPos(0, 0, 0);

        LivingEntity player = event.getEntityLiving();

        // if bamboo is held, target blocks are recorded.. otherwise abort
        if (!player.getHeldItem(Hand.OFF_HAND).getItem().getName().getString().equals("Bamboo")) return;

        // only execute if target is a block, not an entity
        if (Minecraft.getInstance().objectMouseOver.getClass() == EntityRayTraceResult.class) return;

        BlockRayTraceResult lookingAt = (BlockRayTraceResult) Minecraft.getInstance().objectMouseOver;

        // if built-in player-raytrace misses, use extended one
        if (lookingAt.getType() == RayTraceResult.Type.MISS) {
            lookingAt = PosHelper.getTargetBlock(player, 200);
        }

        BlockPos pos = PosHelper.fixCoords(lookingAt);
        // as of now, this cuts out the block.minecraft.; does not yet account for other modpacks
        String blockName = world.getBlockState(pos).getBlock().getTranslationKey().substring(16);

        // only record if target block changes
        if (!PosHelper.isPosEqual(lastTargetPos, pos)) {
            lastTargetPos = pos;

            StringTextComponent mcText = new StringTextComponent("trgt " + blockName +
                    " [" + pos.getX() + "/" + pos.getY() + "/" + pos.getZ() +
                    "] plyr" + player.getPositionVec());
            SteeringLawStudy.LOGGER.info(mcText.getText());

            // give coordinates of targeted block to TunnelManager
            TunnelManager.manage(pos, world, player);
        }
    }

    /**
     * overrides standard player movement
     * moves player between tunnels (down = previous pos, up = next pos)
     * moves player between camera angles (left = previous pos, right = next pos)
     */
    @SubscribeEvent
    public static void teleportPlayer(InputUpdateEvent event) {
        if (!SteeringLawStudy.BUILD_MODE) {
            LivingEntity player = event.getPlayer();
            MovementInput input = event.getMovementInput();
            float currMoveStrafe = input.moveStrafe;
            float currMoveForward = input.moveForward;

            if (teleportCooldown > 0) teleportCooldown--;

            // override player movement...
            player.setVelocity(0, 0, 0);
            input.moveStrafe = 0;
            input.moveForward = 0;
            player.setJumping(false);
            player.setSneaking(false);
            player.setSprinting(false);

            // ...and enable teleport functionality
            if (teleportCooldown == 0) {
                if (currMoveForward != 0) {
                    TunnelManager.changeTunnel(event);
                    // 10 ticks cooldown = 0.5 sec.
                    teleportCooldown = 10;
                }
            }
        }
    }

    /**
     * ignore clicking of player, preventing changes in the world
     * Q / E / F / MIDDLE MOUSE are handled by remapping them to other keys in options..
     * ..to prevent accidental usage
     */
    @SubscribeEvent
    public static void ignoreStandardInput(PlayerInteractEvent event) {
        if (!SteeringLawStudy.BUILD_MODE) {
            MouseHelper mh = Minecraft.getInstance().mouseHelper;

            if (mh.isLeftDown() || mh.isMiddleDown() || mh.isRightDown()) {
                if (event.isCancelable()) event.setCanceled(true);
            }
        }
    }

    /**
     * initializes TunnelManager
     */
    @SubscribeEvent
    public static void initTunnelManager(PlayerEvent.PlayerLoggedInEvent event) {
        SteeringLawStudy.LOGGER.info("starting tunnel initialization");
        TunnelManager.init(event.getPlayer(), event.getPlayer().world);
        SteeringLawStudy.LOGGER.info("initialization completed");
    }

    /**
     * executes dataValidator when participant leaves testing world
     */
    @SubscribeEvent
    public static void validateDataOnExit(PlayerEvent.PlayerLoggedOutEvent event) {
        SteeringLawStudy.LOGGER.info("starting data parsing");
        DataValidator.parseData();
        SteeringLawStudy.LOGGER.info("parse completed");
    }
}
