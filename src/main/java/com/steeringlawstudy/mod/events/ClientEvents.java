package com.steeringlawstudy.mod.events;

import com.steeringlawstudy.mod.SteeringLawStudy;
import com.steeringlawstudy.mod.util.RayTrace;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// this has to be verified
@Mod.EventBusSubscriber(modid = SteeringLawStudy.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void getTargetBlockWithFood(LivingEvent.LivingUpdateEvent event) {
        World world = event.getEntity().getEntityWorld();
        if (!world.isRemote) return;

        LivingEntity player = event.getEntityLiving();

        // if food is held, target blocks are recorded
        if (!player.getHeldItem(Hand.MAIN_HAND).getItem().isFood()) return;

        RayTraceResult lookingAtMC = Minecraft.getInstance().objectMouseOver;
        BlockPos posMC = new BlockPos(lookingAtMC.getHitVec().x, lookingAtMC.getHitVec().y, lookingAtMC.getHitVec().z);

        if (lookingAtMC.getType() == RayTraceResult.Type.BLOCK) {
            String blockMC = world.getBlockState(posMC).getBlock().toString();
            String blockNameMC = world.getBlockState(posMC).getBlock().getTranslationKey();

            if (!blockNameMC.equals("block.minecraft.air") && !blockNameMC.equals("block.minecraft.void_air")) {
                player.sendMessage(new StringTextComponent("MC " + blockMC), player.getUniqueID());
                player.sendMessage(new StringTextComponent(posMC.toString()), player.getUniqueID());
            }
            // if built-in player-raytrace misses, use custom one
        } else if (lookingAtMC.getType() == RayTraceResult.Type.MISS) {

            BlockRayTraceResult lookingAtMod = RayTrace.getTargetBlock(player, 200);
            BlockPos posMod;

            posMod = new BlockPos(lookingAtMod.getHitVec().getX(), lookingAtMod.getHitVec().getY(), lookingAtMod.getHitVec().getZ());
            String block = world.getBlockState(posMod).getBlock().toString();
            String blockNameMod = world.getBlockState(posMod).getBlock().getTranslationKey();

            if (!blockNameMod.equals("block.minecraft.air") && !blockNameMod.equals("block.minecraft.void_air")) {
                player.sendMessage(new StringTextComponent("MOD " + block), player.getUniqueID());
                player.sendMessage(new StringTextComponent(posMod.toString()), player.getUniqueID());
            }
        }
    }
}
