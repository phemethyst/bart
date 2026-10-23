package org.phemethyst.bart.gutted.buff;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.phemethyst.bart.gutted.Bart;

public class BuffRegistry {
    public static final ResourceKey<Registry<Buff>> BUFF_REGKEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Bart.MODID, "buffs"));

    public static void register(IEventBus modEventBus) {
        DeferredRegister.createDataComponents(BUFF_REGKEY, Bart.MODID);
    }
}
