package net.tropicraft.core.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.tropicraft.core.common.entity.placeable.BeachFloatEntity;

public class BeachFloatModel extends HierarchicalModel<BeachFloatEntity> {
    private final ModelPart root;

    public BeachFloatModel(ModelPart root) {
        this.root = root;
    }

    public static LayerDefinition create(){
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("floatCross4", CubeListBuilder.create().texOffs(0, 0).addBox(0F, -1F, -1F, 16, 2, 2), PartPose.offset(0F, 23F, -6F));
        root.addOrReplaceChild("floatCross3", CubeListBuilder.create().texOffs(0, 0).addBox(0F, -1F, -1F, 16, 2, 2), PartPose.offset(0F, 23F, -2F));
        root.addOrReplaceChild("floatCross2", CubeListBuilder.create().texOffs(0, 0).addBox(0F, -1F, -1F, 16, 2, 2), PartPose.offset(0F, 23F, 2F));
        root.addOrReplaceChild("floatCross1", CubeListBuilder.create().texOffs(0, 0).addBox(0F, -1F, -1F, 16, 2, 2), PartPose.offset(0F, 23F, 6F));
        root.addOrReplaceChild("topFloatCross4", CubeListBuilder.create().texOffs(0, 0).addBox(0F, -1F, -1F, 16, 2, 2), PartPose.offsetAndRotation(0F, 23F, -6F, 0F,0F, 3.141593F));
        root.addOrReplaceChild("topFloatCross3", CubeListBuilder.create().texOffs(0, 0).addBox(0F, -1F, -1F, 16, 2, 2), PartPose.offsetAndRotation(0F, 23F, -2F, 0F,0F, 3.141593F));
        root.addOrReplaceChild("topFloatCross2", CubeListBuilder.create().texOffs(0, 0).addBox(0F, 0F, 1F, 16, 2, 2), PartPose.offsetAndRotation(0F, 24F, 0F, 0F,0F, 3.141593F));
        root.addOrReplaceChild("topFloatCross1", CubeListBuilder.create().texOffs(0, 0).addBox(0F, -1F, -1F, 16, 2, 2), PartPose.offsetAndRotation(0F, 23F, 6F, 0F,0F, 3.141593F));
        root.addOrReplaceChild("floatFoot", CubeListBuilder.create().texOffs(0, 4).addBox(-7F, -1F, 0F, 14, 2, 2), PartPose.offsetAndRotation(16F, 23F, 0F, 0F,1.570796F, 0F));
        root.addOrReplaceChild("floatTop", CubeListBuilder.create().texOffs(0, 4).addBox(-7F, -1F, 0F, 14, 2, 2), PartPose.offsetAndRotation(-17F, 24F, 0F, 1.570796F,-1.570796F, 0F));
        root.addOrReplaceChild("headPillow", CubeListBuilder.create().texOffs(0, 13).addBox(-6F, -1.5F, -4F, 12, 2, 4), PartPose.offsetAndRotation(-12F, 22F, 0F, 0F,1.570796F, 0F));
        root.addOrReplaceChild("topBed", CubeListBuilder.create().texOffs(0, 19).addBox(-6F, -0.5F, -6F, 14, 1, 12), PartPose.offset(-6F, 22F, 0F));
        root.addOrReplaceChild("bottomBed", CubeListBuilder.create().texOffs(0, 19).addBox(-6F, -0.5F, -6F, 14, 1, 12), PartPose.offset(8F, 22F, 0F));

        return LayerDefinition.create(mesh,64,32);
    }

    @Override
    public void setupAnim(BeachFloatEntity beachFloat, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(-90));
        super.renderToBuffer(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        poseStack.popPose();
    }

    @Override
    public ModelPart root() {
        return root;
    }
}
