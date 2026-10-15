package org.phemethyst.bart.buff;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.phemethyst.bart.Bart;
import org.phemethyst.bart.buff.Buff;

public class BuffRegistry {
    public static final ResourceKey<Registry<Buff>> BUFF_REGKEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Bart.MODID, "buffs"));

    public static void register(IEventBus modEventBus) {
        DeferredRegister.create(BUFF_REGKEY, "bart");
    }
}
