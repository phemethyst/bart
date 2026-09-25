package org.phemethyst.bart.buff;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

class Buff {
    public Component name;
    public Component altText;
    public ResourceLocation icon;

    public Buff (Component name, Component altText, ResourceLocation icon) {
        this.name = name;
        this.altText = altText;
        this.icon = icon;
    }
}
