package com.steeringlawstudy.mod;

import com.steeringlawstudy.mod.init.RegisterBlocks;
import com.steeringlawstudy.mod.init.RegisterItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * "main" class for the mod, this is where the mod registers itself to the game and defines global constants
 */
@Mod("steeringlawstudy")
public class SteeringLawStudy {
    public static final String MOD_ID = "steeringlawstudy";

    // Creative Item Tab (own category for all the mods blocks/items in creative mode)
    public static final ItemGroup TAB = new ItemGroup("modTab") {
        @Override
        // Tab icon
        public ItemStack createIcon() {
            return new ItemStack(RegisterItems.RUBY.get());
        }
    };

    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();

    // in dev mode, the player can move freely; if disabled, mod functions normally
    public static final boolean DEV_MODE = false;

    // output path for study logfiles
    public static final String OUT_PATH = "../study";

    // block definitions
    public static final String START_BLOCK = "lime_concrete";
    public static final String STOP_BLOCK = "red_concrete";
    public static final String PATH_BLOCK = "white_concrete";
    public static final String PATH_VISITED_BLOCK = "light_gray_concrete";

    public SteeringLawStudy() {
        RegisterItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        RegisterBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
