package org.phemethyst.bart.ui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.components.Button;
import net.minecraft.world.phys.Vec2;
import org.phemethyst.bart.buff.Buff;

import java.awt.*;

public class BartScreen extends Screen {
    public BartScreen(Component title, Buff buff) {
        super(Component.literal("Bart"));
        this.buff = buff;
    }

    public Buff buff;

    private int width;
    private int height;

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    protected void init() {
        super.init();

        width = this.getRectangle().width();
        height = this.getRectangle().height();
        float scale = Math.min(width / 960f, height / 540f);

        Font font = Minecraft.getInstance().font;

        Button bbutton = Button.builder(
                Component.empty(), button -> {
                            Minecraft.getInstance().player.connection.sendCommand("bart execute @e \"" + buff.command + "\"");
                            Minecraft.getInstance().setScreen(null);
                })
                .size((int)(240 * scale), (int)(67.5 * scale))
                .pos((int)(388 * scale), (int)(360 * scale))
                .build();

        this.addRenderableWidget(bbutton);
    }

    protected void renderImages(GuiGraphics graphics) {
        float scale = Math.min(width / 960f, height / 540f);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, buff.getIconLoc());

        graphics.blit(buff.getIconLoc(), (int)(395 * scale), (int)(362 * scale),
                0, 0, (int)(60 * scale), (int)(60 * scale),
                (int)(60 * scale), (int)(60 * scale));
    }

    protected void renderText(GuiGraphics graphics) {
        float scale = Math.min(width / 960f, height / 540f);

        MultiLineLabel label = MultiLineLabel.create(font, 160, 5, Component.literal(buff.name),
                Component.literal(" "), Component.literal(buff.altText), Component.literal(" "),
                Component.literal(buff.effect));

        label.renderCentered(graphics, (int)(535 * scale), (int)(370 * scale));

        MultiLineLabel flavour = MultiLineLabel.create(font, 403, 4,
                Component.literal("As if you had a choice.").withColor(Color.red.getRGB()));

        flavour.renderCentered(graphics, width / 2, this.getRectangle().bottom() - 200);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        width = this.getRectangle().width();
        height = this.getRectangle().height();

        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderImages(graphics);
        this.renderText(graphics);
    }
}
