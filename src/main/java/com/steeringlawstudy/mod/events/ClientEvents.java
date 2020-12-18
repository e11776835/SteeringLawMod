package com.steeringlawstudy.mod.events;

import com.steeringlawstudy.mod.SteeringLawStudy;
import com.steeringlawstudy.mod.tunnels.TunnelManager;
import com.steeringlawstudy.mod.util.DataValidator;
import com.steeringlawstudy.mod.util.PosHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * here certain events triggered by the player are handled
 */
@Mod.EventBusSubscriber(modid = SteeringLawStudy.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEvents {
    private static BlockPos lastTargetPos;

    /**
     * while holding a food item, the targeted block is recorded
     * this will enable verification of user movement through paths
     */
    @SubscribeEvent
    public static void getTargetBlock(LivingEvent.LivingUpdateEvent event) {
        World world = event.getEntity().getEntityWorld();
        if (!world.isRemote) return;

        LivingEntity player = event.getEntityLiving();

        // if food is held, target blocks are recorded.. otherwise abort
        if (!player.getHeldItem(Hand.MAIN_HAND).getItem().isFood()) return;

        // only execute if target is a block, not an entity
        if (Minecraft.getInstance().objectMouseOver.getClass() == EntityRayTraceResult.class) return;

        if (lastTargetPos == null) lastTargetPos = new BlockPos(0, 0, 0);

        BlockRayTraceResult lookingAt = (BlockRayTraceResult) Minecraft.getInstance().objectMouseOver;

        // if built-in player-raytrace misses, use extended one
        if (lookingAt.getType() == RayTraceResult.Type.MISS) {
            lookingAt = PosHelper.getTargetBlock(player, 200);
        }

        BlockPos pos = PosHelper.fixCoords(lookingAt);
        String blockName = world.getBlockState(pos).getBlock().getTranslationKey();

        // only record if target block changes
        if (!PosHelper.isPosEqual(lastTargetPos, pos)) {
            lastTargetPos = pos;

            StringTextComponent mcText = new StringTextComponent("trgt " + blockName +
                    " [" + pos.getX() + "/" + pos.getY() + "/" + pos.getZ() + "]");
            SteeringLawStudy.LOGGER.info(mcText.getText());
            // player.sendMessage(mcText, player.getUniqueID());

            // give coordinates of targeted block to TunnelManager
            TunnelManager.manage(pos, world);
        }
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
