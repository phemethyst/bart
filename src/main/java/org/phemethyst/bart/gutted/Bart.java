package org.phemethyst.bart.gutted;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.phemethyst.bart.gutted.buff.SerializersAndAttachments;
import org.phemethyst.bart.gutted.buffold.ModAttachments;
import org.phemethyst.bart.gutted.entity.ModEntities;
import org.phemethyst.bart.gutted.entity.client.BartRenderer;
import org.phemethyst.bart.gutted.item.ModItems;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

public class Bart {
    public static final String MODID = "bart";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final int MAXINT = 2147483647;

    public Bart(IEventBus modEventBus, ModContainer modContainer) {
        NeoForge.EVENT_BUS.register(this);

        ModItems.register(modEventBus);
        ModEntities.register(modEventBus);

        SerializersAndAttachments.register(modEventBus);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HEY GUYS WELCOME TO MY MINECRAFT LETSPLAY WHERE TODAY I'LL BE KILLING PHEMETHYST!!!!!!");
    }

    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModEntities.BART.get(), BartRenderer::new);
        }
    }
}
