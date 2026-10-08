package org.phemethyst.bart.packets;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.phemethyst.bart.Bart;
import org.phemethyst.bart.buff.Buff;
import org.phemethyst.bart.entity.custom.BartEntity;

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

        if (data.buffs().get(0).getTarget() == Buff.Target.PLAYER) {
            Bart.LOGGER.info("BEFORE REMOVE 2: {}", buffModified.stream().map(Buff::getName).toList());

            buffModified.remove(data.buffs().get(0));

            Bart.LOGGER.info("AFTER REMOVE: {}", buffModified.stream().map(Buff::getName).toList());

            plr.setBuffs(buffModified);
        } else {
            ServerLevel level = (ServerLevel) iPayloadContext.player().level();

            List<BartEntity> barts = level.getEntitiesOfClass(
                    BartEntity.class, iPayloadContext.player().getBoundingBox().inflate(1000));

            for (BartEntity b : barts) {
                for (Buff bu : data.buffs()) {
                    b.removeBuff(bu);
                }

                for (ServerPlayer player : level.players()) {
                    player.connection.send(
                            new Buff.ListRecord(b.getBuffs())
                    );
                }
            }
        }
    }
}
