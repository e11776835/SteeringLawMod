package com.steeringlawstudy.mod.events;

import com.steeringlawstudy.mod.SteeringLawStudy;
import com.steeringlawstudy.mod.util.DataValidator;
import com.steeringlawstudy.mod.util.PosHelper;
import net.minecraft.block.Blocks;
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
    public static void getTargetBlockWithFood(LivingEvent.LivingUpdateEvent event) {
        World world = event.getEntity().getEntityWorld();
        if (!world.isRemote) return;

        LivingEntity player = event.getEntityLiving();

        // if food is held, target blocks are recorded.. otherwise abort
        if (!player.getHeldItem(Hand.MAIN_HAND).getItem().isFood()) return;

        // only execute if target is a block, not an entity
        if (Minecraft.getInstance().objectMouseOver.getClass() == EntityRayTraceResult.class) return;

        if (lastTargetPos == null) lastTargetPos = new BlockPos(0, 0, 0);

        BlockRayTraceResult lookingAtMC = (BlockRayTraceResult) Minecraft.getInstance().objectMouseOver;
        BlockPos posMC = PosHelper.fixCoords(lookingAtMC);

        if (lookingAtMC.getType() == RayTraceResult.Type.BLOCK) {
            // String blockMC = world.getBlockState(posMC).getBlock().toString();
            String blockNameMC = world.getBlockState(posMC).getBlock().getTranslationKey();

            if (posMC != lastTargetPos) {
                lastTargetPos = posMC;

                StringTextComponent mcText = new StringTextComponent("trgt " + blockNameMC +
                        " [" + posMC.getX() + "/" + posMC.getY() + "/" + posMC.getZ() + "]");
                SteeringLawStudy.LOGGER.info(mcText.getText());
                // player.sendMessage(mcText, player.getUniqueID());

                // only record changing of targeted blocks
                if (PosHelper.isPosEqual(lastTargetPos, posMC)) return;
                // TODO list.add(blockNameMC);
//                if (blockNameMC.equals("block.minecraft.white_concrete")) {
//                    world.setBlockState(posMC, Blocks.YELLOW_CONCRETE.getDefaultState());
//                }
            }

            // if built-in player-raytrace misses, use extended one
        } else if (lookingAtMC.getType() == RayTraceResult.Type.MISS) {

            BlockRayTraceResult lookingAtMod = PosHelper.getTargetBlock(player, 200);
            BlockPos posMod = PosHelper.fixCoords(lookingAtMod);

            // String blockMod = world.getBlockState(posMod).getBlock().toString();
            String blockNameMod = world.getBlockState(posMod).getBlock().getTranslationKey();

            if (posMod != lastTargetPos) {
                lastTargetPos = posMod;

                StringTextComponent modText = new StringTextComponent("trgt " + blockNameMod +
                        " [" + posMod.getX() + "/" + posMod.getY() + "/" + posMod.getZ() + "]");
                SteeringLawStudy.LOGGER.info(modText.getText());
                // player.sendMessage(modText, player.getUniqueID());

                if (PosHelper.isPosEqual(lastTargetPos, posMod)) return;
                // TODO list.add(blockNameMod);
//                if (blockNameMod.equals("block.minecraft.white_concrete")) {
//                    world.setBlockState(posMod, Blocks.YELLOW_CONCRETE.getDefaultState());
//                }
            }
        }
    }

    /**
     * executes dataValidator when participant leaves testing world
     */
    @SubscribeEvent
    public static void validateDataOnExit(PlayerEvent.PlayerLoggedOutEvent event) {
        SteeringLawStudy.LOGGER.info("starting data parsing");
        // TODO SteeringLawStudy.LOGGER.info(list.toString());
        DataValidator.parseData();
        SteeringLawStudy.LOGGER.info("parse completed");
    }
}
