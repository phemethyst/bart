package org.phemethyst.bart.gutted.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.registries.DataPackRegistriesHooks;
import org.phemethyst.bart.gutted.Bart;
import org.phemethyst.bart.gutted.buff.Action;
import org.phemethyst.bart.gutted.buff.Buff;
import org.phemethyst.bart.gutted.buff.BuffRegistry;

import java.util.List;

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

            if (b.target == Buff.Target.BART) {

            } else if (b.target == Buff.Target.PLAYER) {
                Player plr = context.player();

                for (Action a : b.action) {
                    if (a.type == Buff.ActionType.ITEM) {
                        try {
                            plr.getInventory().add(a.item);
                        } catch (RuntimeException e) {
                            Bart.LOGGER.error("That upgrade you just chose, it has type item but no item to give!");
                        }
                    } else if (a.type == Buff.ActionType.COMMAND) {
                        try {
                            plr.level().getServer().getCommands().performPrefixedCommand
                                    (plr.level().getServer().createCommandSourceStack(), a.command);
                        } catch (RuntimeException e) {
                            Bart.LOGGER.error("That upgrade you just chose, it has type command but no command to run!");
                        }
                    } else if (a.type == Buff.ActionType.ATTRIBUTE) {
                        try {
                            List<RegistryDataLoader.RegistryData<?>> regList = DataPackRegistriesHooks.getDataPackRegistries();

                            for (RegistryDataLoader.RegistryData<?> reg : regList) {
                                if (reg.key() == BuffRegistry.BUFF_REGKEY) {
                                    // this is the registry
                                }
                            }

                            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(Bart.MODID, );
                        } catch (RuntimeException e) {
                            Bart.LOGGER.error("That upgrade you just chose, it has type attribute and it may be missing some values!");
                        }
                    }
                }
            }

            return;
        }
    }
}
