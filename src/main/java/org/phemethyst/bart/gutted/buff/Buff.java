package org.phemethyst.bart.gutted.buff;

// TODO:
// Actually implement ts
// Fix client-server shenanigans

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Buff implements DataComponentType {
    @Override
    public @Nullable Codec codec() {
        return CODEC;
    }

    @Override
    public StreamCodec streamCodec() {
        return STREAM_CODEC;
    }

    public enum Target {
        BART,
        PLAYER
    }

    // What to do when this buff is chosen?
    public enum ActionType {
        ATTRIBUTE,
        ITEM,
        COMMAND
    }

    public Target target;
    public List<Action> action;

    public Buff(Target t, List<Action> a) {
        this.target = t;
        this.action = a;
    }

    public Buff(String s, List<Action> a) {
        this(Buff.getTargetFromString(s), a);
    }

    public static final Codec<Buff> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.fieldOf("target").forGetter(Buff::getTargetString),
                    Action.LIST_CODEC.fieldOf("actions").forGetter(Buff::getActions)
            ).apply(instance, Buff::new));

    public static final Codec<List<Buff>> LIST_CODEC = CODEC.listOf();

    public static final StreamCodec<RegistryFriendlyByteBuf, Buff> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            Buff::getTargetString,
            Action.STREAM_CODEC.apply(ByteBufCodecs.list()),
            Buff::getActions,
            Buff::new
    );

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Buff)) {
            return false;
        }

        return this.action == ((Buff) obj).action;
    }

    public String getTargetString() {
        if (this.target == Target.BART) {
            return "bart";
        }

        if (this.target == Target.PLAYER) {
            return "player";
        }

        return "";
    }

    public Target getTarget() {
        return this.target;
    }

    public List<Action> getActions() {
        return this.action;
    }

    public static ActionType getActionTypeFromString(String s) {
        if (s == "attribute") {
            return ActionType.ATTRIBUTE;
        }

        if (s == "item") {
            return ActionType.ITEM;
        }

        if (s == "command") {
            return ActionType.COMMAND;
        }

        return ActionType.ATTRIBUTE;
    }

    public static Target getTargetFromString(String s) {
        if (s == "bart") {
            return Target.BART;
        }

        if (s == "player") {
            return Target.PLAYER;
        }

        return Target.PLAYER;
    }
}
