package org.phemethyst.bart.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.phemethyst.bart.buff.Buff;

public class ServerPayloadHandler {
    public static void handleDataOnMain(CustomPacketPayload packet, IPayloadContext context) {
        if (!(packet instanceof BuffPacket)) {
            return;
        }

        BuffPacket buff = (BuffPacket)packet;

        if (buff.type == BuffPacket.BuffPacketType.OPTIONS) {
            // Packet is server -> client
            return;
        }

        if (buff.type == BuffPacket.BuffPacketType.PICKED) {
            // Packet is client -> server

            // You only ever pick one.
            // soycake starT speedrun stream 1 2:25:51
            Buff b = buff.buffs.getFirst();

            // TODO: apply b

            return;
        }
    }
}
