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

    @Override
    protected void init() {
        super.init();

        Font font = Minecraft.getInstance().font;

        Button test = Button.builder(
                Component.empty(), button -> {
                            Minecraft.getInstance().player.connection.sendCommand("bart execute @e \"" + buff.command + "\"");
                            Minecraft.getInstance().setScreen(null);
                })
                .size(250, 120)
                .pos((this.getRectangle().width() / 2) - 125, (this.getRectangle().height() / 2) + 60)
                .build();

        this.addRenderableWidget(test);
    }

    private void text(Font font, Component message, Vec2 pos, int color, GuiGraphics graphics) {
        font.drawInBatch(message, pos.x, pos.y, color, true, graphics.pose().last().pose(),
                Minecraft.getInstance().renderBuffers().bufferSource(), Font.DisplayMode.NORMAL, Color.TRANSLUCENT,
                15728880);
    }

    protected void renderImages(GuiGraphics graphics) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, buff.getIconLoc());

        graphics.blit(buff.getIconLoc(), (this.getRectangle().width() / 2) - 125, (this.getRectangle().height() / 2) + 60, 0, 0, 100, 100, 100, 100);
    }

    protected void renderText(GuiGraphics graphics) {
        MultiLineLabel label = MultiLineLabel.create(font, 120, 7, Component.literal(buff.name),
                Component.literal(" "), Component.literal(buff.altText), Component.literal(" "),
                Component.literal(buff.effect));

        label.renderCentered(graphics, (this.getRectangle().width() / 2) + 45, (this.getRectangle().height() / 2) + 85);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderImages(graphics);
        this.renderText(graphics);
    }
}
