package org.phemethyst.bart.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.phemethyst.bart.Bart;
import org.phemethyst.bart.entity.custom.BartEntity;

import java.util.List;

@EventBusSubscriber(modid = Bart.MODID, bus = EventBusSubscriber.Bus.GAME)
public class PlayerRespawn {
    @SubscribeEvent
    public static void respawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();

        player.handleEntityEvent((byte) 43);
        player.level().broadcastEntityEvent(player, (byte) 43);

        if (!(player.level() instanceof ServerLevel)) {
            return;
        }

        List<BartEntity> e = ((ServerLevel)player.level()).getEntitiesOfClass(BartEntity.class, player.getBoundingBox().inflate(1000));
        Bart.LOGGER.info("what the fuck");

        for (BartEntity b : e) {
            Bart.LOGGER.info("what the fuck");
            player.level().broadcastEntityEvent(b, (byte) 39);
            b.reset();
        }
    }

    @SubscribeEvent
    public static void clone(PlayerEvent.Clone event) {
        Player oldPlayer = event.getOriginal();
        Player newPlayer = event.getEntity();

        oldPlayer.getAttributes().getSyncableAttributes().forEach(oldInstance -> {
            AttributeInstance newInstance = newPlayer.getAttribute(oldInstance.getAttribute());

            if (newInstance == null) {
                return;
            }

            newInstance.setBaseValue(oldInstance.getBaseValue());

            for (AttributeModifier modifier : oldInstance.getModifiers()) {
                newInstance.addTransientModifier(modifier);
            }
        });
    }
}
