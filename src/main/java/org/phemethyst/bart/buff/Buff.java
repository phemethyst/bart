package org.phemethyst.bart.buff;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

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

    public Buff (String name, String altText, String effect, String command, String icon, String target) {
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
