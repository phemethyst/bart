package org.phemethyst.bart.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.components.Button;

public class BartScreen extends Screen {
    public BartScreen(Component title) {
        super(Component.literal("Bart"));
    }

    @Override
    protected void init() {
        super.init();

        Button test = Button.builder(
                Component.literal(String.valueOf(this.getRectangle().width())), button -> {
                            Minecraft.getInstance().player.connection.sendCommand("bart execute @e \"title @a actionbar \\\"bart!\\\"\"");
                            Minecraft.getInstance().setScreen(null);
                })
                .size(250, 120)
                .pos((this.getRectangle().width() / 2) - 125, (this.getRectangle().height() / 2) + 60)
                .build();

        this.addRenderableWidget(test);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
    }
}
