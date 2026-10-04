package org.phemethyst.bart.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.phemethyst.bart.buff.Buff;
import org.phemethyst.bart.ui.PlayerScreen;

public class ClientPayloadHandler {
    public static void handleDataOnMain(CustomPacketPayload customPacketPayload, IPayloadContext iPayloadContext) {
        if (!(customPacketPayload instanceof Buff.ListRecord)) {
            return;
        }

        Buff.ListRecord data = (Buff.ListRecord)customPacketPayload;

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
    }
}
