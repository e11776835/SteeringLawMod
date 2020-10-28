package com.steeringlawstudy.mod.events;

import com.steeringlawstudy.mod.SteeringLawStudy;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// this has to be verified
@Mod.EventBusSubscriber(modid = SteeringLawStudy.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void getTargetBlockOnJump(LivingEvent.LivingJumpEvent event) {
        LivingEntity player = event.getEntityLiving();
        RayTraceResult lookingAt = Minecraft.getInstance().objectMouseOver;

        if (lookingAt != null) {
            if (lookingAt.getType() == RayTraceResult.Type.BLOCK) {
                BlockPos pos = new BlockPos(lookingAt.getHitVec());
                player.sendMessage(new StringTextComponent(pos.toString()), player.getUniqueID());
            } else if (lookingAt.getType() == RayTraceResult.Type.MISS) {
                player.sendMessage(new StringTextComponent("nothing to see here.."), player.getUniqueID());
            }
        }
    }
}
