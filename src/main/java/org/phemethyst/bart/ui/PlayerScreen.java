package org.phemethyst.bart.ui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import org.phemethyst.bart.buff.Buff;

import java.awt.*;
import java.util.LinkedList;

public class PlayerScreen extends Screen {
    public Buff buff1;
    public Buff buff2;
    public Buff buff3;

    private int width;
    private int height;

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

        width = this.getRectangle().width();
        height = this.getRectangle().height();

        float scale = Math.min(width / 960f, height / 540f);

        Button b1button = Button.builder(
                        Component.empty(), button -> {
                            Buff.ListRecord bTemp = new Buff.ListRecord(new LinkedList<>());
                            bTemp.buffs().add(buff1);

                            Minecraft.getInstance().player.connection.sendCommand("bart execute @e \"" + buff1.command + "\"");
                            PacketDistributor.sendToServer(bTemp);
                            Minecraft.getInstance().setScreen(null);
                        })
                .size((int)(240 * scale), (int)(67.5 * scale))
                .pos((int)(88 * scale), (int)(360 * scale))
                .build();

        Button b2button = Button.builder(
                        Component.empty(), button -> {
                            Buff.ListRecord bTemp = new Buff.ListRecord(new LinkedList<>());
                            bTemp.buffs().add(buff2);

                            Minecraft.getInstance().player.connection.sendCommand("bart execute @e \"" + buff2.command + "\"");
                            PacketDistributor.sendToServer(bTemp);
                            Minecraft.getInstance().setScreen(null);
                        })
                .size((int)(240 * scale), (int)(67.5 * scale))
                .pos((int)(388 * scale), (int)(360 * scale))
                .build();

        Button b3button = Button.builder(
                        Component.empty(), button -> {
                            Buff.ListRecord bTemp = new Buff.ListRecord(new LinkedList<>());
                            bTemp.buffs().add(buff3);

                            Minecraft.getInstance().player.connection.sendCommand("bart execute @e \"" + buff3.command + "\"");
                            PacketDistributor.sendToServer(bTemp);
                            Minecraft.getInstance().setScreen(null);
                        })
                .size((int)(240 * scale), (int)(67.5 * scale))
                .pos((int)(688 * scale), (int)(360 * scale))
                .build();

        this.addRenderableWidget(b1button);
        this.addRenderableWidget(b2button);
        this.addRenderableWidget(b3button);
    }

    protected void renderImages(GuiGraphics graphics) {
        float scale = Math.min(width / 960f, height / 540f);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        RenderSystem.setShaderTexture(0, buff1.getIconLoc());

        graphics.blit(buff1.getIconLoc(), (int)(95 * scale), (int)(362 * scale),
                0, 0, (int)(60 * scale), (int)(60 * scale),
                (int)(60 * scale), (int)(60 * scale));

        RenderSystem.setShaderTexture(0, buff2.getIconLoc());

        graphics.blit(buff2.getIconLoc(), (int)(395 * scale), (int)(362 * scale),
                0, 0, (int)(60 * scale), (int)(60 * scale),
                (int)(60 * scale), (int)(60 * scale));

        RenderSystem.setShaderTexture(0, buff3.getIconLoc());

        graphics.blit(buff3.getIconLoc(), (int)(695 * scale), (int)(362 * scale),
                0, 0, (int)(60 * scale), (int)(60 * scale),
                (int)(60 * scale), (int)(60 * scale));
    }

    protected void renderText(GuiGraphics graphics) {
        float scale = Math.min(width / 960f, height / 540f);

        MultiLineLabel flavour = MultiLineLabel.create(font, 403, 3,
                Component.literal("Bart deems you unworthy."));

        flavour.renderCentered(graphics, width / 2, this.getRectangle().bottom() - 200);

        MultiLineLabel label1 = MultiLineLabel.create(font, 160, 5, Component.literal(buff1.name),
                Component.literal(" "), Component.literal(buff1.altText), Component.literal(" "),
                Component.literal(buff1.effect));

        label1.renderCentered(graphics, (int)(225 * scale), (int)(370 * scale));

        MultiLineLabel label2 = MultiLineLabel.create(font, 160, 5, Component.literal(buff2.name),
                Component.literal(" "), Component.literal(buff2.altText), Component.literal(" "),
                Component.literal(buff2.effect));

        label2.renderCentered(graphics, (int)(535 * scale), (int)(370 * scale));

        MultiLineLabel label3 = MultiLineLabel.create(font, 160, 5, Component.literal(buff3.name),
                Component.literal(" "), Component.literal(buff3.altText), Component.literal(" "),
                Component.literal(buff3.effect));

        label3.renderCentered(graphics, (int)(835 * scale), (int)(370 * scale));
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
