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

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("floatCross4", CubeListBuilder.create().texOffs(0, 0).addBox(0.0f, -1.0f, -1.0f, 16, 2, 2), PartPose.offset(0.0f, 23.0f, -6.0f));
        root.addOrReplaceChild("floatCross3", CubeListBuilder.create().texOffs(0, 0).addBox(0.0f, -1.0f, -1.0f, 16, 2, 2), PartPose.offset(0.0f, 23.0f, -2.0f));
        root.addOrReplaceChild("floatCross2", CubeListBuilder.create().texOffs(0, 0).addBox(0.0f, -1.0f, -1.0f, 16, 2, 2), PartPose.offset(0.0f, 23.0f, 2.0f));
        root.addOrReplaceChild("floatCross1", CubeListBuilder.create().texOffs(0, 0).addBox(0.0f, -1.0f, -1.0f, 16, 2, 2), PartPose.offset(0.0f, 23.0f, 6.0f));
        root.addOrReplaceChild("topFloatCross4", CubeListBuilder.create().texOffs(0, 0).addBox(0.0f, -1.0f, -1.0f, 16, 2, 2), PartPose.offsetAndRotation(0.0f, 23.0f, -6.0f, 0.0f, 0.0f, 3.141593f));
        root.addOrReplaceChild("topFloatCross3", CubeListBuilder.create().texOffs(0, 0).addBox(0.0f, -1.0f, -1.0f, 16, 2, 2), PartPose.offsetAndRotation(0.0f, 23.0f, -2.0f, 0.0f, 0.0f, 3.141593f));
        root.addOrReplaceChild("topFloatCross2", CubeListBuilder.create().texOffs(0, 0).addBox(0.0f, 0.0f, 1.0f, 16, 2, 2), PartPose.offsetAndRotation(0.0f, 24.0f, 0.0f, 0.0f, 0.0f, 3.141593f));
        root.addOrReplaceChild("topFloatCross1", CubeListBuilder.create().texOffs(0, 0).addBox(0.0f, -1.0f, -1.0f, 16, 2, 2), PartPose.offsetAndRotation(0.0f, 23.0f, 6.0f, 0.0f, 0.0f, 3.141593f));
        root.addOrReplaceChild("floatFoot", CubeListBuilder.create().texOffs(0, 4).addBox(-7.0f, -1.0f, 0.0f, 14, 2, 2), PartPose.offsetAndRotation(16.0f, 23.0f, 0.0f, 0.0f, 1.570796f, 0.0f));
        root.addOrReplaceChild("floatTop", CubeListBuilder.create().texOffs(0, 4).addBox(-7.0f, -1.0f, 0.0f, 14, 2, 2), PartPose.offsetAndRotation(-17.0f, 24.0f, 0.0f, 1.570796f, -1.570796f, 0.0f));
        root.addOrReplaceChild("headPillow", CubeListBuilder.create().texOffs(0, 13).addBox(-6.0f, -1.5f, -4.0f, 12, 2, 4), PartPose.offsetAndRotation(-12.0f, 22.0f, 0.0f, 0.0f, 1.570796f, 0.0f));
        root.addOrReplaceChild("topBed", CubeListBuilder.create().texOffs(0, 19).addBox(-6.0f, -0.5f, -6.0f, 14, 1, 12), PartPose.offset(-6.0f, 22.0f, 0.0f));
        root.addOrReplaceChild("bottomBed", CubeListBuilder.create().texOffs(0, 19).addBox(-6.0f, -0.5f, -6.0f, 14, 1, 12), PartPose.offset(8.0f, 22.0f, 0.0f));

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(BeachFloatEntity beachFloat, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(-90));
        super.renderToBuffer(poseStack, buffer, packedLight, packedOverlay, color);
        poseStack.popPose();
    }

    @Override
    public ModelPart root() {
        return root;
    }
}
