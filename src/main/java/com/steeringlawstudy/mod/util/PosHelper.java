package com.steeringlawstudy.mod.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

/**
 * util class for methods concerning BlockPos and RayTracing
 */
public class PosHelper {
    private static World world = Minecraft.getInstance().world;

    /**
     * extends standard ingame-raytrace distance to custom value maxDist
     * @return targeted block and position
     */
    public static BlockRayTraceResult getTargetBlock(LivingEntity player, int maxDist) {
        Vector3d vec = player.getPositionVec();
        Vector3d vec3 = new Vector3d(vec.x, vec.y + player.getEyeHeight(), vec.z);
        Vector3d vec3a = player.getLook(1.0F);
        Vector3d vec3b = vec3.add(vec3a.getX() * maxDist, vec3a.getY() * maxDist, vec3a.getZ() * maxDist);

        RayTraceContext context = new RayTraceContext(vec3, vec3b,
                RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.ANY, player);
        BlockRayTraceResult lookingAtMod = world.rayTraceBlocks(context);

        double xPos = lookingAtMod.getHitVec().getX();
        double yPos = lookingAtMod.getHitVec().getY();
        double zPos = lookingAtMod.getHitVec().getZ();

        return new BlockRayTraceResult(lookingAtMod.getHitVec(), lookingAtMod.getFace(),
                new BlockPos(xPos, yPos, zPos), false);
    }

    /**
     * fixes bug in minecraft 1.16.1 raytrace methods
     * (depending on looking direction, raycast has issues)
     * @return corrected block and position
     */
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

    /**
     * compare two BlockPos to another
     * @return if they are equal
     */
    public static boolean isPosEqual(BlockPos a, BlockPos b) {
        if (a.getX() == b.getX() && a.getY() == b.getY() && a.getZ() == b.getZ()) {
            return true;
        }
        return false;
    }
}
