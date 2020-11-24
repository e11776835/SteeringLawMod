package com.steeringlawstudy.mod.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class RayTrace {

    private static World world = Minecraft.getInstance().world;

    // extends standard minecraft ray trace by more distance
    public static BlockRayTraceResult getTargetBlock(LivingEntity player, int maxdistance) {
        Vector3d vec = player.getPositionVec();
        Vector3d vec3 = new Vector3d(vec.x, vec.y + player.getEyeHeight(), vec.z);
        Vector3d vec3a = player.getLook(1.0F);
        Vector3d vec3b = vec3.add(vec3a.getX() * maxdistance, vec3a.getY() * maxdistance, vec3a.getZ() * maxdistance);

        RayTraceContext context = new RayTraceContext(vec3, vec3b, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.ANY, player);

        BlockRayTraceResult lookingAtMod = world.rayTraceBlocks(context);

        double xm = lookingAtMod.getHitVec().getX();
        double ym = lookingAtMod.getHitVec().getY();
        double zm = lookingAtMod.getHitVec().getZ();

        return new BlockRayTraceResult(lookingAtMod.getHitVec(), lookingAtMod.getFace(), new BlockPos(xm, ym, zm), false);
    }

    // fixes bug in minecrafts ray trace methods
    public static BlockPos fixCoords(BlockRayTraceResult lookingAt) {
        BlockPos pos;
        Direction dir = lookingAt.getFace();

        if (dir == Direction.SOUTH) {
            pos = new BlockPos(
                    lookingAt.getHitVec().getX(),
                    lookingAt.getHitVec().getY(),
                    lookingAt.getHitVec().getZ() - 1);

        } else if (dir == Direction.EAST) {
            pos = new BlockPos(
                    lookingAt.getHitVec().getX() - 1,
                    lookingAt.getHitVec().getY(),
                    lookingAt.getHitVec().getZ());

        } else if (dir == Direction.UP) {
            pos = new BlockPos(
                    lookingAt.getHitVec().getX(),
                    lookingAt.getHitVec().getY() - 1,
                    lookingAt.getHitVec().getZ());

        } else {
            pos = new BlockPos(
                    lookingAt.getHitVec().getX(),
                    lookingAt.getHitVec().getY(),
                    lookingAt.getHitVec().getZ());
        }

        return pos;
    }
}
