package org.phemethyst.bart.buff;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.phemethyst.bart.Bart;
import org.phemethyst.bart.network.BuffPacket;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class SerializersAndAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Bart.MODID);

    public static final Supplier<AttachmentType<List<Buff>>> PLAYER_BUFFS = ATTACHMENTS.register("buffs",
            () -> AttachmentType.<List<Buff>>builder(() -> new LinkedList<Buff>()).serialize(Buff.LIST_CODEC).copyOnDeath().build());

    public static final DeferredRegister<EntityDataSerializer<?>> DATA_SERIALIZERS =
            DeferredRegister.create(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, Bart.MODID);

    public static final EntityDataSerializer<BuffPacket> PACKET_SERIALIZER = new EntityDataSerializer<BuffPacket>() {
        @Override
        public StreamCodec<? super RegistryFriendlyByteBuf, BuffPacket> codec() {
            return BuffPacket.STREAM_CODEC;
        }

        @Override
        public BuffPacket copy(BuffPacket buffPacket) {
            return new BuffPacket(buffPacket.type, buffPacket.buffs);
        }
    };

    public static final Supplier<EntityDataSerializer<?>> PACKET_SERIALIZER_REG =
            DATA_SERIALIZERS.register("packet_serializer", () -> PACKET_SERIALIZER);

    public static void register(IEventBus eventBus) {
        ATTACHMENTS.register(eventBus);
        DATA_SERIALIZERS.register(eventBus);
    }
}
