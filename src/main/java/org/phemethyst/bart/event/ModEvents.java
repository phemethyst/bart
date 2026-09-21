package org.phemethyst.bart.event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import org.phemethyst.bart.Bart;
import org.phemethyst.bart.entity.ModEntities;
import org.phemethyst.bart.entity.client.BartModel;
import org.phemethyst.bart.entity.custom.BartEntity;
import org.phemethyst.bart.item.ModItems;

@EventBusSubscriber(modid = Bart.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModEvents {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BartModel.LAYER_LOCATION, BartModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.BART.get(), BartEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        LivingEntity entity = event.getEntity();

        if (entity.getItemBySlot(EquipmentSlot.FEET)
                .is(ModItems.MAID_DRESS_BOOTS.get())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void imagineHittingBartCouldntBeMe(AttackEntityEvent event) {
        if (!(event.getTarget() instanceof BartEntity)) {
            return;
        }

        BartEntity bart = (BartEntity) event.getTarget();

        if (bart.passive) {
            bart.passive = false;
            bart.level().broadcastEntityEvent(bart, (byte) 2);
        }
    }
}
