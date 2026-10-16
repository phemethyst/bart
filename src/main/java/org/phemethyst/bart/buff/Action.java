package org.phemethyst.bart.buff;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

public class Action {
    public enum AttribValueModType {
        ADD,
        MULT_BASE,
        MULT_TOTAL
    }

    public Buff.ActionType type;

    // Attribute-specific
    public String attributeID;
    public float attributeValue;
    public AttribValueModType modifyType;

    // Item
    public ItemStack item;

    // Command
    public String command;

    public Action(String attribID, float attribValue, AttribValueModType attribModType) {
        this.type = Buff.ActionType.ATTRIBUTE;
        this.attributeID = attribID;
        this.attributeValue = attribValue;
        this.modifyType = attribModType;
    }

    public Action(String attribID, float attribValue, String attribModType) {
        this(attribID, attribValue, Action.getAttributeModTypeByString(attribModType));
    }

    public Action(ItemStack item) {
        this.type = Buff.ActionType.ITEM;
        this.item = item;
    }

    public Action(String command) {
        this.type = Buff.ActionType.COMMAND;
        this.command = command;
    }

    public static final Codec<Action> ATTRIB_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                Codec.STRING.fieldOf("attribid").forGetter(Action::getAttributeID),
                Codec.FLOAT.fieldOf("attribvalue").forGetter(Action::getAttributeValue),
                Codec.STRING.fieldOf("attribmod").forGetter(Action::getAttributeModTypeString)
            ).apply(instance, Action::new));

    public static final Codec<Action> ITEM_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ItemStack.CODEC.fieldOf("item").forGetter(Action::getItem)
            ).apply(instance, Action::new));

    public static final Codec<Action> COMMAND_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.fieldOf("command").forGetter(Action::getCommand)
            ).apply(instance, Action::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, Action> ATTRIB_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            Action::getAttributeID,
            ByteBufCodecs.FLOAT,
            Action::getAttributeValue,
            ByteBufCodecs.STRING_UTF8,
            Action::getAttributeModTypeString,
            Action::new
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, Action> ITEM_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodec(ItemStack.CODEC),
            Action::getItem,
            Action::new
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, Action> COMMAND_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            Action::getCommand,
            Action::new
    );

    public String getTypeString() {
        if (this.type == Buff.ActionType.ATTRIBUTE) {
            return "attribute";
        }

        if (this.type == Buff.ActionType.ITEM) {
            return "item";
        }

        if (this.type == Buff.ActionType.COMMAND) {
            return "command";
        }

        return "";
    }

    public Buff.ActionType getType() {
        return this.type;
    }

    public String getAttributeID() {
        return this.attributeID;
    }

    public float getAttributeValue() {
        return this.attributeValue;
    }

    public String getAttributeModTypeString() {
        if (this.modifyType == AttribValueModType.ADD) {
            return "add";
        }

        if (this.modifyType == AttribValueModType.MULT_BASE) {
            return "multbase";
        }

        if (this.modifyType == AttribValueModType.MULT_TOTAL) {
            return "multtotal";
        }

        return "";
    }

    public static AttribValueModType getAttributeModTypeByString(String type) {
        if (type == "add") {
            return AttribValueModType.ADD;
        }

        if (type == "multbase") {
            return AttribValueModType.MULT_BASE;
        }

        if (type == "multtotal") {
            return AttribValueModType.MULT_TOTAL;
        }

        return AttribValueModType.ADD;
    }

    public AttribValueModType getAttributeModType() {
        return this.modifyType;
    }

    public ItemStack getItem() {
        return item;
    }

    public String getCommand() {
        return command;
    }
}
