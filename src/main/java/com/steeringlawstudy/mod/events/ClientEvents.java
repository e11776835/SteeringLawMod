package com.steeringlawstudy.mod.events;

import com.steeringlawstudy.mod.SteeringLawStudy;
import com.steeringlawstudy.mod.util.RayTrace;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Direction;
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

        BlockRayTraceResult lookingAtMC = (BlockRayTraceResult) Minecraft.getInstance().objectMouseOver;
        BlockPos posMC;
        Direction dirMC = lookingAtMC.getFace();

        if (dirMC == Direction.SOUTH) {
            posMC = new BlockPos(
                    lookingAtMC.getHitVec().getX(),
                    lookingAtMC.getHitVec().getY(),
                    lookingAtMC.getHitVec().getZ() - 1);

        } else if (dirMC == Direction.EAST) {
            posMC = new BlockPos(
                    lookingAtMC.getHitVec().getX() - 1,
                    lookingAtMC.getHitVec().getY(),
                    lookingAtMC.getHitVec().getZ());

        } else if (dirMC == Direction.UP) {
            posMC = new BlockPos(
                    lookingAtMC.getHitVec().getX(),
                    lookingAtMC.getHitVec().getY() - 1,
                    lookingAtMC.getHitVec().getZ());

        } else {
            posMC = new BlockPos(
                    lookingAtMC.getHitVec().getX(),
                    lookingAtMC.getHitVec().getY(),
                    lookingAtMC.getHitVec().getZ());
        }

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
            Direction dirMod = lookingAtMod.getFace();

            if (dirMod == Direction.SOUTH) {
                posMod = new BlockPos(
                        lookingAtMod.getHitVec().getX(),
                        lookingAtMod.getHitVec().getY(),
                        lookingAtMod.getHitVec().getZ() - 1);

            } else if (dirMod == Direction.EAST) {
                posMod = new BlockPos(
                        lookingAtMod.getHitVec().getX() - 1,
                        lookingAtMod.getHitVec().getY(),
                        lookingAtMod.getHitVec().getZ());

            } else if (dirMod == Direction.UP) {
                posMod = new BlockPos(
                        lookingAtMod.getHitVec().getX(),
                        lookingAtMod.getHitVec().getY() - 1,
                        lookingAtMod.getHitVec().getZ());

            } else {
                posMod = new BlockPos(
                        lookingAtMod.getHitVec().getX(),
                        lookingAtMod.getHitVec().getY(),
                        lookingAtMod.getHitVec().getZ());
            }

            String block = world.getBlockState(posMod).getBlock().toString();
            String blockNameMod = world.getBlockState(posMod).getBlock().getTranslationKey();

            if (!blockNameMod.equals("block.minecraft.air") && !blockNameMod.equals("block.minecraft.void_air")) {
                player.sendMessage(new StringTextComponent("MOD " + block), player.getUniqueID());
                player.sendMessage(new StringTextComponent(posMod.toString()), player.getUniqueID());
            }
        }
    }
}
