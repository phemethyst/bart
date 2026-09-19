package org.phemethyst.bart.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import org.phemethyst.bart.Bart;
import org.phemethyst.bart.entity.ModEntities;
import org.phemethyst.bart.entity.client.BartModel;
import org.phemethyst.bart.entity.custom.BartEntity;

@EventBusSubscriber(modid = Bart.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BartModel.LAYER_LOCATION, BartModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.BART.get(), BartEntity.createAttributes().build());
    }
}
