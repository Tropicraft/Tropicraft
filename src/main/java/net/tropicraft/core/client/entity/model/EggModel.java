package net.tropicraft.core.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.tropicraft.core.common.entity.egg.EggEntity;

import java.util.List;

public class EggModel extends ListModel<EggEntity> {
    private final ModelPart body;

    public EggModel(ModelPart root) {
        body = root.getChild("body");
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("body", CubeListBuilder.create().mirror(true)
                .texOffs(0,16)
                .addBox(-3F, -10F, -3F, 6, 10, 6)
                .texOffs(0,0)
                .addBox(-1.5F, -11F, -1.5F, 3, 1, 3)
                .texOffs(0,7)
                .addBox(3F, -7F, -1.5F, 1, 6, 3)
                .texOffs(24,9)
                .addBox(-1.5F, -7F, 3F, 3, 6, 1)
                .texOffs(16,7)
                .addBox(-4F, -7F, -1.5F, 1, 6, 3)
                .texOffs(8,9)
                .addBox(-1.5F, -7F, -4F, 3, 6, 1), PartPose.offset(0F,24F,0F));
        return LayerDefinition.create(mesh,64,32);
    }

    @Override
    public void setupAnim(EggEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public Iterable<ModelPart> parts() {
        return List.of(body);
    }

    @Override
    public void prepareMobModel(EggEntity egg, float limbSwing, float limbSwingAmount, float partialTick) {
        boolean hatching = egg.isNearHatching();
        float randRotator = (float) egg.rotationRand;
        body.yRot = 0F;
        if (hatching) {
            body.yRot = Mth.sin((egg.tickCount + partialTick) * .6f) * .6f;
            body.xRot = Mth.sin(randRotator * 4) * .6f;
            body.zRot = Mth.cos(randRotator * 4) * .6f;
        } else {
            body.xRot = 0F;
            body.zRot = 0F;
        }
    }
    
    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderToBuffer(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}