package org.phemethyst.bart.item;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.phemethyst.bart.Bart;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Bart.MODID);

    public static final DeferredItem<ArmorItem> MAID_DRESS_HELMET = ITEMS.register("maid_dress_helmet",
            () -> new ArmorItem(ModArmorMaterials.MAID_DRESS_MATERIAL, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(Bart.MAXINT)));
    public static final DeferredItem<ArmorItem> MAID_DRESS_CHESTPLATE = ITEMS.register("maid_dress_chestplate",
            () -> new ArmorItem(ModArmorMaterials.MAID_DRESS_MATERIAL, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().durability(Bart.MAXINT)));
    public static final DeferredItem<ArmorItem> MAID_DRESS_LEGGINGS = ITEMS.register("maid_dress_leggings",
            () -> new ArmorItem(ModArmorMaterials.MAID_DRESS_MATERIAL, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().durability(Bart.MAXINT)));
    public static final DeferredItem<ArmorItem> MAID_DRESS_BOOTS = ITEMS.register("maid_dress_boots",
            () -> new ArmorItem(ModArmorMaterials.MAID_DRESS_MATERIAL, ArmorItem.Type.BOOTS,
                    new Item.Properties().durability(Bart.MAXINT)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
