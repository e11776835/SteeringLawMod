package com.steeringlawstudy.mod.events;

import com.steeringlawstudy.mod.SteeringLawStudy;
import com.steeringlawstudy.mod.util.DataValidator;
import com.steeringlawstudy.mod.util.RayTrace;
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

    /**
     * while holding a food item, the targeted block is recorded
     * this will enable verification of user movement through paths
     */
    @SubscribeEvent
    public static void getTargetBlockWithFood(LivingEvent.LivingUpdateEvent event) {
        World world = event.getEntity().getEntityWorld();
        if (!world.isRemote) return;

        LivingEntity player = event.getEntityLiving();

        // if food is held, target blocks are recorded.. otherwise abort
        if (!player.getHeldItem(Hand.MAIN_HAND).getItem().isFood()) return;

        if (Minecraft.getInstance().objectMouseOver.getClass() == EntityRayTraceResult.class) return;

        BlockRayTraceResult lookingAtMC = (BlockRayTraceResult) Minecraft.getInstance().objectMouseOver;
        BlockPos posMC = RayTrace.fixCoords(lookingAtMC);

        if (lookingAtMC.getType() == RayTraceResult.Type.BLOCK) {
            String blockMC = world.getBlockState(posMC).getBlock().toString();
            String blockNameMC = world.getBlockState(posMC).getBlock().getTranslationKey();

            if (!blockNameMC.equals("block.minecraft.air") && !blockNameMC.equals("block.minecraft.void_air")) {
                StringTextComponent mcText = new StringTextComponent("trgt " + blockNameMC /* + " - " + posMod.toString() */);
                SteeringLawStudy.LOGGER.info(mcText.getText());
                // player.sendMessage(mcText, player.getUniqueID());
            }

            // if built-in player-raytrace misses, use extended one
        } else if (lookingAtMC.getType() == RayTraceResult.Type.MISS) {

            BlockRayTraceResult lookingAtMod = RayTrace.getTargetBlock(player, 200);
            BlockPos posMod = RayTrace.fixCoords(lookingAtMod);

            String blockMod = world.getBlockState(posMod).getBlock().toString();
            String blockNameMod = world.getBlockState(posMod).getBlock().getTranslationKey();

            if (!blockNameMod.equals("block.minecraft.air") && !blockNameMod.equals("block.minecraft.void_air")) {
                StringTextComponent modText = new StringTextComponent("trgt " + blockNameMod /* + " - " + posMod.toString() */);
                SteeringLawStudy.LOGGER.info(modText.getText());
                // player.sendMessage(modText, player.getUniqueID());
            }
        }
    }

    /**
     * executes dataValidator when player leaves world
     */
    @SubscribeEvent
    public static void validateDataOnExit(PlayerEvent.PlayerLoggedOutEvent event) {
        SteeringLawStudy.LOGGER.info("starting data parsing");
        DataValidator.parseData();
        SteeringLawStudy.LOGGER.info("parse completed");
    }
}
