package org.phemethyst.bart.buff;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;

// TODO:
// Actually implement ts
// Fix client-server shenanigans

public class Buff {
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
    public Action[] action;

    public Buff(Target t, Action[] a) {
        this.target = t;
        this.action = a;
    }
}
