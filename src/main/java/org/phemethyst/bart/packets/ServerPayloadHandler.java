package org.phemethyst.bart.packets;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.phemethyst.bart.Bart;
import org.phemethyst.bart.buff.Buff;

import java.util.LinkedList;
import java.util.List;

public class ServerPayloadHandler {
    public static void handleDataOnMain(CustomPacketPayload customPacketPayload, IPayloadContext iPayloadContext) {
        if (!(customPacketPayload instanceof Buff.ListRecord)) {
            return;
        }

        MixinBullshit plr = (MixinBullshit)iPayloadContext.player();

        List<Buff> buffModified = new LinkedList<>(plr.getBuffs());

        Buff.ListRecord data = (Buff.ListRecord)customPacketPayload;

        Bart.LOGGER.info("BEFORE REMOVE: {}", buffModified.stream().map(Buff::getName).toList());

        buffModified.remove(data.buffs().get(0));

        Bart.LOGGER.info("AFTER REMOVE: {}", buffModified.stream().map(Buff::getName).toList());

        plr.setBuffs(buffModified);
    }
}
