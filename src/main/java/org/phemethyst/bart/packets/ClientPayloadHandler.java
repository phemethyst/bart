package org.phemethyst.bart.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.vehicle.Minecart;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.phemethyst.bart.Bart;
import org.phemethyst.bart.buff.Buff;
import org.phemethyst.bart.entity.custom.BartEntity;
import org.phemethyst.bart.ui.BartScreen;
import org.phemethyst.bart.ui.PlayerScreen;

public class ClientPayloadHandler {
    public static void handleDataOnMain(CustomPacketPayload customPacketPayload, IPayloadContext iPayloadContext) {
        if (!(customPacketPayload instanceof Buff.ListRecord)) {
            return;
        }

        Buff.ListRecord data = (Buff.ListRecord)customPacketPayload;

        Bart.LOGGER.info("meow2");

        if (data.buffs().isEmpty()) {
            return;
        }

        Bart.LOGGER.info("meow23");

        if (data.buffs().get(0).getTarget() == Buff.Target.PLAYER) {
            if (data.buffs().size() < 3) {
                return;
            }

            iPayloadContext.enqueueWork(() -> {
                Minecraft.getInstance().setScreen(
                        new PlayerScreen(
                                Component.literal("bart"),
                                data.buffs().get(0),
                                data.buffs().get(1),
                                data.buffs().get(2)
                        )
                );
            });
        } else {
            Bart.LOGGER.info("meow2");

            if (data.buffs().size() == 1) {
                iPayloadContext.enqueueWork(() -> {
                    Minecraft.getInstance().level
                            .getEntitiesOfClass(BartEntity.class, Minecraft.getInstance().player.getBoundingBox().inflate(1000))
                            .stream().findFirst().ifPresent(bart -> bart.removeBuff(data.buffs().get(0)));

                    Minecraft.getInstance().setScreen(new BartScreen(Component.literal("Bart"), data.buffs().get(0)));
                });
            } else {
                iPayloadContext.enqueueWork(() -> {
                    Minecraft.getInstance().level
                            .getEntitiesOfClass(BartEntity.class, Minecraft.getInstance().player.getBoundingBox().inflate(1000))
                            .stream().findFirst().ifPresent(bart -> bart.setBuffs(data.buffs()));
                });
            }
        }
    }
}
