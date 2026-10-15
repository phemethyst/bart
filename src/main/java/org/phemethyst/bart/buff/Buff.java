package org.phemethyst.bart.buff;

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

    public class Action {
        public enum AttribValueModType {
            ADD,
            MULT_BASE,
            MULT_TOTAL
        }

        public ActionType type;

        // Attribute-specific
        public String attributeID;
        public float attributeValue;
        public AttribValueModType modifyType;

        // Item
        public ItemStack item;

        // Command
        public String command;

        public Action(String attribID, float attribValue, AttribValueModType attribModType) {
            this.type = ActionType.ATTRIBUTE;
            this.attributeID = attribID;
            this.attributeValue = attribValue;
            this.modifyType = attribModType;
        }

        public Action(ItemStack item) {
            this.type = ActionType.ITEM;
            this.item = item;
        }

        public Action(String command) {
            this.type = ActionType.COMMAND;
            this.command = command;
        }
    }

    public Target target;
    public Action[] action;

    public Buff(Target t, Action[] a) {
        this.target = t;
        this.action = a;
    }
}
