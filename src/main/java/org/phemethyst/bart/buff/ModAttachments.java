package org.phemethyst.bart.buff;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.phemethyst.bart.Bart;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Bart.MODID);

    public static final EntityDataSerializer<Buff.ListRecord> ENTITY_SERIALIZER = new EntityDataSerializer<Buff.ListRecord>() {
        @Override
        public StreamCodec<? super RegistryFriendlyByteBuf, Buff.ListRecord> codec() {
            return Buff.ListRecord.STREAM_CODEC;
        }

        @Override
        public Buff.ListRecord copy(Buff.ListRecord buffs) {
            Buff.ListRecord output = new Buff.ListRecord(new LinkedList<>());
            output.buffs().addAll(buffs.buffs());
            return output;
        }
    };

    public static final DeferredRegister<EntityDataSerializer<?>> ENTITY_DATA_SERIALIZERS =
            DeferredRegister.create(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, Bart.MODID);

    public static final Supplier<EntityDataSerializer<?>> A = ENTITY_DATA_SERIALIZERS.register("serializer",
            () -> ENTITY_SERIALIZER);

    public static final Supplier<AttachmentType<List<Buff>>> PLAYER_BUFFS = ATTACHMENTS.register("buffs",
            () -> AttachmentType.<List<Buff>>builder(() -> new LinkedList<Buff>()).serialize(Buff.CODEC.listOf()).copyOnDeath().build());

    public static void register(IEventBus modBus) {
        ATTACHMENTS.register(modBus);
        ENTITY_DATA_SERIALIZERS.register(modBus);
    }
}
