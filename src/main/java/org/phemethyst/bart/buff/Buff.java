package org.phemethyst.bart.buff;

// TODO:
// Actually implement ts
// Fix client-server shenanigans

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.phemethyst.bart.Bart;

import java.util.List;

public class Buff implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<Buff> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Bart.MODID, "buff"));

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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Buff)) {
            return false;
        }

        return this.action == ((Buff) obj).action;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
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
