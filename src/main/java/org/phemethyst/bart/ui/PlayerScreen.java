package org.phemethyst.bart.ui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.phemethyst.bart.buff.Buff;

import java.awt.*;

public class PlayerScreen extends Screen {
    public Buff buff1;
    public Buff buff2;
    public Buff buff3;

    public PlayerScreen(Component title, Buff b1, Buff b2, Buff b3) {
        super(Component.literal("bart"));
        this.buff1 = b1;
        this.buff2 = b2;
        this.buff3 = b3;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    protected void init() {
        super.init();
    }

    protected void renderText(GuiGraphics graphics) {
        MultiLineLabel flavour = MultiLineLabel.create(font, 200, 1,
                Component.literal("Bart deems you unworthy."));

        flavour.renderCentered(graphics, (this.getRectangle().width() / 2), (this.getRectangle().height() / 2) + 40);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderText(graphics);
    }
}
