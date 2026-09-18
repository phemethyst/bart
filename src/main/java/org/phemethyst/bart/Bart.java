package org.phemethyst.bart;

import net.neoforged.neoforge.common.NeoForge;
import org.phemethyst.bart.entity.ModEntities;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
@Mod(Bart.MODID)
public class Bart {
    public static final String MODID = "bart";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Bart(IEventBus modEventBus, ModContainer modContainer) {
        NeoForge.EVENT_BUS.register(this);

        ModEntities.register(modEventBus);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Bart welcomes you.");
    }
}
