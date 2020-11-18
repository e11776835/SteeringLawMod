package com.steeringlawstudy.mod.events;

import com.steeringlawstudy.mod.SteeringLawStudy;
import com.steeringlawstudy.mod.util.RayTrace;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// this has to be verified
@Mod.EventBusSubscriber(modid = SteeringLawStudy.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void getTargetBlockWithFood(LivingEvent.LivingUpdateEvent event) {
        LivingEntity player = event.getEntityLiving();

        if (!player.getHeldItem(Hand.MAIN_HAND).getItem().isFood()) return;

        BlockRayTraceResult lookingAt = RayTrace.getTargetBlock(player, 100);

        BlockPos pos;

        if (lookingAt != null) {
            pos = new BlockPos(lookingAt.getHitVec().getX(), lookingAt.getHitVec().getY(), lookingAt.getHitVec().getZ());
            String blockstate = Minecraft.getInstance().world.getBlockState(pos).toString();

            player.sendMessage(new StringTextComponent(blockstate), player.getUniqueID());
            // SteeringLawStudy.LOGGER.info(blockstate);

        } else {
            player.sendMessage(new StringTextComponent("nothing to see here.."), player.getUniqueID());
            SteeringLawStudy.LOGGER.info("nothing to see here..");
        }
    }
}
