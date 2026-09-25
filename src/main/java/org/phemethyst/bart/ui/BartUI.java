package org.phemethyst.bart.ui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import org.phemethyst.bart.Bart;

public class BartUI {
    private static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Bart.MODID, "textures/upgrades/fistfulofdollar.png");

    public static void popup(GuiGraphics graphics, DeltaTracker deltaTracker) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);

        graphics.blit(GUI_TEXTURE, 0, 0, 0, 0, 64, 64, 64, 64);
    }
}
