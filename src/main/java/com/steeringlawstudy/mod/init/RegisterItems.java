package com.steeringlawstudy.mod.init;

import com.steeringlawstudy.mod.SteeringLawStudy;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

// class defining all custom items of this mod
public class RegisterItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SteeringLawStudy.MOD_ID);

    // Items
    public static final RegistryObject<Item> RUBY = ITEMS.register("ruby", () -> new Item(new Item.Properties().group(SteeringLawStudy.TAB)));

    // Block Items (= blocks in inventory/hand when removed)
    public static final RegistryObject<Item> RUBY_BLOCK_ITEM = ITEMS.register("ruby_block",
            () -> new BlockItem(RegisterBlocks.RUBY_BLOCK.get(), new Item.Properties().group(SteeringLawStudy.TAB)));
}
