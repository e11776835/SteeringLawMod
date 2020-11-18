package com.steeringlawstudy.mod.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class RayTrace {

    private static World world = Minecraft.getInstance().world;

    public static BlockRayTraceResult getTargetBlock(LivingEntity player, int maxdistance) {
        Vector3d vec = player.getPositionVec();
        Vector3d vec3 = new Vector3d(vec.x, vec.y + player.getEyeHeight(), vec.z);
        Vector3d vec3a = player.getLook(1.0F);
        Vector3d vec3b = vec3.add(vec3a.getX() * maxdistance, vec3a.getY() * maxdistance, vec3a.getZ() * maxdistance);

        RayTraceContext context = new RayTraceContext(vec3, vec3b, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.ANY, player);

        return world.rayTraceBlocks(context);
    }
}
