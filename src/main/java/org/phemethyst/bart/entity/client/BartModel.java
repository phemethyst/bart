package org.phemethyst.bart.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.phemethyst.bart.Bart;
import org.phemethyst.bart.entity.custom.BartEntity;

public class BartModel<T extends BartEntity> extends HierarchicalModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Bart.MODID, "bart"), "main");
    private final ModelPart root;
    private final ModelPart waist;
    private final ModelPart head;

    public BartModel(ModelPart root) {
        this.root = root.getChild("root");
        this.waist = this.root.getChild("waist");
        this.head = this.waist.getChild("head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0, 24, 0));

        PartDefinition waist = root.addOrReplaceChild("waist", CubeListBuilder.create().texOffs(-1, -1).addBox(0, 0, 0, 1, 1, 1), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition head = waist.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -24.0F, 0.0F));

        PartDefinition body = waist.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -24.0F, 0.0F));

        PartDefinition rightarm = waist.addOrReplaceChild("rightarm", CubeListBuilder.create().texOffs(0, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, -22.0F, 0.0F));

        PartDefinition leftarm = waist.addOrReplaceChild("leftarm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(5.0F, -22.0F, 0.0F));

        PartDefinition rightleg = waist.addOrReplaceChild("rightleg", CubeListBuilder.create().texOffs(16, 48).mirror().addBox(-2.1F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.9F, -12.0F, 0.0F));

        PartDefinition leftleg = waist.addOrReplaceChild("leftleg", CubeListBuilder.create().texOffs(32, 48).addBox(-1.9F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(1.9F, -12.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(BartEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(netHeadYaw, headPitch);

        this.animateWalk(BartAnimations.walk, limbSwing, limbSwingAmount, 5f, 1);
        this.animate(entity.orangeteleportalAnimState, BartAnimations.orangeteleportal, ageInTicks, 1f);
        this.animate(entity.idle2AnimState, BartAnimations.idle2, ageInTicks, 1f);
        this.animate(entity.idle1AnimState, BartAnimations.idle1, ageInTicks, 1f);
        this.animate(entity.wakeupeepyheadAnimState, BartAnimations.wakeupeepyhead, ageInTicks, 1f);
        this.animate(entity.dashAnimState, BartAnimations.dash, ageInTicks, 1f);
    }

    private void applyHeadRotation(float headYaw, float headPitch) {
        headYaw = Mth.clamp(headYaw, -90, 90);
        headPitch = Mth.clamp(headPitch, -45, 80);

        this.head.yRot = headYaw * (Mth.PI / 180f);
        this.head.xRot = headPitch * (Mth.PI / 180f);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int colour) {
        waist.render(poseStack, vertexConsumer, packedLight, packedOverlay, colour);
    }

    @Override
    public ModelPart root() {
        return root;
    }
}
