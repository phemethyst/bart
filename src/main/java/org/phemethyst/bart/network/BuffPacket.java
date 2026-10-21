package org.phemethyst.bart.network;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.phemethyst.bart.Bart;
import org.phemethyst.bart.buff.Buff;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class BuffPacket implements CustomPacketPayload {
    public static final StreamCodec<RegistryFriendlyByteBuf, BuffPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            BuffPacket::getTypeString,
            Buff.STREAM_CODEC.apply(ByteBufCodecs.list()),
            BuffPacket::getBuffs,
            BuffPacket::new
    );

    public enum BuffPacketType {
        OPTIONS,
        PICKED
    };

    public BuffPacket(BuffPacketType type, List<Buff> buffs) {
        this.type = type;
        this.buffs = buffs;
    }

    public BuffPacket(String type, List<Buff> buffs) {
        this(BuffPacket.getTypeFromString(type), buffs);
    }

    public BuffPacketType type;
    public List<Buff> buffs = new LinkedList<>();

    public static final CustomPacketPayload.Type<BuffPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Bart.MODID, "buff"));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public String getTypeString() {
        if (type == BuffPacketType.OPTIONS) {
            return "options";
        } else if (type == BuffPacketType.PICKED) {
            return "picked";
        }

        return "options";
    }

    public BuffPacketType getType() {
        return type;
    }

    public List<Buff> getBuffs() {
        return buffs;
    }

    public static BuffPacketType getTypeFromString(String t) {
        if (Objects.equals(t, "options")) {
            return BuffPacketType.OPTIONS;
        } else if (Objects.equals(t, "picked")) {
            return BuffPacketType.PICKED;
        }

        return BuffPacketType.OPTIONS;
    }
}
