package org.phemethyst.bart.buff;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.Objects;

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

    public Action(String type, String attribID, float attribValue, String attribModType, ItemStack item, String command) {
        this.type = Buff.getActionTypeFromString(type);

        this.attributeID = attribID;
        this.attributeValue = attribValue;
        this.modifyType = Action.getAttributeModTypeByString(attribModType);
        this.item = item;
        this.command = command;
    }

    public static final Codec<Action> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.fieldOf("type").forGetter(Action::getTypeString),
                    Codec.STRING.optionalFieldOf("attribid", "").forGetter(Action::getAttributeID),
                    Codec.FLOAT.optionalFieldOf("attribvalue", 0f).forGetter(Action::getAttributeValue),
                    Codec.STRING.optionalFieldOf("attribmod", "add").forGetter(Action::getAttributeModTypeString),
                    ItemStack.CODEC.optionalFieldOf("item", new ItemStack(Items.WAXED_WEATHERED_CUT_COPPER_STAIRS)).forGetter(Action::getItem),
                    Codec.STRING.optionalFieldOf("command", "").forGetter(Action::getCommand)
            ).apply(instance, Action::new));

    public static final Codec<List<Action>> LIST_CODEC = CODEC.listOf();

    public static final StreamCodec<RegistryFriendlyByteBuf, Action> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            Action::getTypeString,
            ByteBufCodecs.STRING_UTF8,
            Action::getAttributeID,
            ByteBufCodecs.FLOAT,
            Action::getAttributeValue,
            ByteBufCodecs.STRING_UTF8,
            Action::getAttributeModTypeString,
            ItemStack.STREAM_CODEC,
            Action::getItem,
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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Action)) {
            return false;
        }

        Action b = (Action)obj;

        return Objects.equals(this.attributeID, b.attributeID) &&
                this.attributeValue == b.attributeValue &&
                this.modifyType == b.modifyType &&
                this.item == b.item &&
                Objects.equals(this.command, b.command);
    }
}
