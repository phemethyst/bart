package org.phemethyst.bart.buff;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.jpountz.util.ByteBufferUtils;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import org.checkerframework.checker.signature.qual.Identifier;
import org.phemethyst.bart.Bart;

import java.util.List;

public class Buff {
    public enum Target {
        PLAYER,
        BART
    }
    public String name;
    public String altText;
    public String effect;
    public String command;
    public String icon;
    public Buff.Target target;

    public static final Codec<Buff> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.fieldOf("target").forGetter(Buff::getTargetString),
                    Codec.STRING.fieldOf("name").forGetter(Buff::getName),
                    Codec.STRING.fieldOf("altText").forGetter(Buff::getAltText),
                    Codec.STRING.fieldOf("effect").forGetter(Buff::getEffect),
                    Codec.STRING.fieldOf("command").forGetter(Buff::getCommand),
                    Codec.STRING.fieldOf("icon").forGetter(Buff::getIcon)
            ).apply(instance, Buff::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, Buff> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            Buff::getTargetString,
            ByteBufCodecs.STRING_UTF8,
            Buff::getName,
            ByteBufCodecs.STRING_UTF8,
            Buff::getAltText,
            ByteBufCodecs.STRING_UTF8,
            Buff::getEffect,
            ByteBufCodecs.STRING_UTF8,
            Buff::getCommand,
            ByteBufCodecs.STRING_UTF8,
            Buff::getIcon,
            Buff::new
    );

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Buff)) {
            return false;
        }

        return ((Buff)obj).getName().equals(this.getName());
    }


    public BuffRecord record() {
        return new BuffRecord(this.getTargetString(), this.name, this.altText, this.effect, this.command, this.icon);
    }

    public record ListRecord(List<Buff> buffs) implements CustomPacketPayload {
        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }

        public static final CustomPacketPayload.Type<ListRecord> TYPE =
                new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Bart.MODID, "buff_record"));

        public static final StreamCodec<? super RegistryFriendlyByteBuf, Buff.ListRecord> STREAM_CODEC = Buff.STREAM_CODEC.apply(ByteBufCodecs.list())
                .map(ListRecord::new, ListRecord::buffs);
    }

    public record BuffRecord(String target, String name, String altText, String effect, String command, String icon) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<BuffRecord> TYPE =
                new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Bart.MODID, "buffRecord"));

        public static final StreamCodec<RegistryFriendlyByteBuf, BuffRecord> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8,
                BuffRecord::target,
                ByteBufCodecs.STRING_UTF8,
                BuffRecord::name,
                ByteBufCodecs.STRING_UTF8,
                BuffRecord::altText,
                ByteBufCodecs.STRING_UTF8,
                BuffRecord::effect,
                ByteBufCodecs.STRING_UTF8,
                BuffRecord::command,
                ByteBufCodecs.STRING_UTF8,
                BuffRecord::icon,
                BuffRecord::new
        );

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }

        public Buff toBuff() {
            return new Buff(target, name, altText, effect, command, icon);
        }
    }

    public Buff (String target, String name, String altText, String effect, String command, String icon) {
        this.name = name;
        this.altText = altText;
        this.effect = effect;
        this.command = command;
        this.icon = icon;

        if (target.equals("bart")) {
            this.target = Target.BART;
        } else {
            this.target = Target.PLAYER;
        }
    }

    public Buff.Target getTarget() {
        return target;
    }

    public String getTargetString() {
        if (this.target == Target.BART) {
            return "bart";
        } else {
            return "player";
        }
    }

    public String getName() {
        return name;
    }

    public String getAltText() {
        return altText;
    }

    public String getEffect() {
        return effect;
    }

    public String getCommand() {
        return command;
    }

    public String getIcon() {
        return icon;
    }

    public ResourceLocation getIconLoc() {
        return ResourceLocation.fromNamespaceAndPath("bart", icon);
    }
}
