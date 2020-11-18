package com.steeringlawstudy.mod;

import com.steeringlawstudy.mod.init.RegisterBlocks;
import com.steeringlawstudy.mod.init.RegisterItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
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

    public SteeringLawStudy() {
        RegisterItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        RegisterBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());


        // Register ourselves for server and other game events we are interested in
        // MinecraftForge.EVENT_BUS.register(this);
    }
}
