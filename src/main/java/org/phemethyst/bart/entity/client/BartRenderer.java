package org.phemethyst.bart.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.phemethyst.bart.Bart;
import org.phemethyst.bart.entity.custom.BartEntity;

public class BartRenderer extends MobRenderer<BartEntity, BartModel<BartEntity>> {
    public BartRenderer(EntityRendererProvider.Context context) {
        super(context, new BartModel<>(context.bakeLayer(BartModel.LAYER_LOCATION)), 0.25f);
    }

    @Override
    public ResourceLocation getTextureLocation(BartEntity bartEntity) {
        return ResourceLocation.fromNamespaceAndPath(Bart.MODID, "textures/entity/bart/bart.png");
    }

    @Override
    public void render(BartEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
