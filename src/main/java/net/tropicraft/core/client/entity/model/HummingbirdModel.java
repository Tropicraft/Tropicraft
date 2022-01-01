package net.tropicraft.core.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.tropicraft.core.common.entity.passive.HummingbirdEntity;

public class HummingbirdModel<T extends HummingbirdEntity> extends EntityModel<T> {
    private final ModelPart body_base;
    private final ModelPart tail_base;
    private final ModelPart wing_left;
    private final ModelPart head_base;
    private final ModelPart beak_base;
    private final ModelPart wing_right;

    public HummingbirdModel(ModelPart model) {
        this.body_base = model;
        this.tail_base = model.getChild("tail_base");
        this.wing_left = model.getChild("wing_left");
        this.head_base = model.getChild("head_base");
        this.beak_base = this.head_base.getChild("beak_base");
        this.wing_right = model.getChild("wing_right");
    }

    public static LayerDefinition create() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();

        partDefinition.addOrReplaceChild("body_base", CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-1.0f, -2.0f, -1.0f, 2.0f, 3.0f, 2.0f),
                PartPose.offsetAndRotation(0.0f, 20.0f, 0.0f, 0.4363f, 0.0f, 0.0f));

        partDefinition.addOrReplaceChild("tail_base", CubeListBuilder.create()
                        .texOffs(0, 6).addBox(-1.5f, 0.0f, 0.0f, 3.0f, 4.0f, 0.0f),
                PartPose.offsetAndRotation(0.0f, 21.0f, 1.0f, 0.2618f, 0.0f, 0.0f));

        partDefinition.addOrReplaceChild("wing_left", CubeListBuilder.create()
                        .texOffs(9, 11).addBox(0.0f, 0.0f, 0.0f, 4.0f, 2.0f, 0.0f),
                PartPose.offsetAndRotation(1.0f, 18.0f, 1.0f, 0.0f, 0.0f, 0.0f));

        PartDefinition partDefinition3 = partDefinition.addOrReplaceChild("head_base", CubeListBuilder.create()
                        .texOffs(9, 0).addBox(-1.0f, -2.0f, -1.0f, 2.0f, 2.0f, 2.0f, new CubeDeformation(0.00f)),
                PartPose.offsetAndRotation(0.0f, 18.0f, 0.0f, -0.2618f, 0.0f, 0.0f));

        partDefinition3.addOrReplaceChild("beak_base", CubeListBuilder.create()
                        .texOffs(7, 6).addBox(0.0f, 0.0f, -3.0f, 0.0f, 1.0f, 3.0f),
                PartPose.offsetAndRotation(0.0f, -2.0f, -1.0f, 0.3927f, 0.0f, 0.0f));

        partDefinition.addOrReplaceChild("wing_right", CubeListBuilder.create()
                        .texOffs(0, 11).addBox(-4.0f, 0.0f, 0.0f, 4.0f, 2.0f, 0.0f),
                PartPose.offsetAndRotation(-1.0f, 18.0f, 1.0f, 0.0f, 0.0f, 0.0f));

        return LayerDefinition.create(meshDefinition, 32, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float age, float headYaw, float headPitch) {
        ModelAnimator.look(head_base, headYaw, headPitch);

        try (ModelAnimator.Cycle fly = ModelAnimator.cycle(age * 0.25F, 1.0F)) {
            body_base.y = 20.0F + fly.eval(1.0F, 0.1F);

            wing_right.yRot = fly.eval(1.0F, 1.0F, 0.0F, 0.0F);
            wing_left.yRot = fly.eval(1.0F, -1.0F, 0.0F, 0.0F);
            wing_right.zRot = fly.eval(1.0F, 0.4F, 0.0F, 0.3F);
            wing_left.zRot = fly.eval(1.0F, -0.4F, 0.0F, -0.3F);
            wing_right.xRot = fly.eval(1.0F, 0.4F, 0.1F, 0.2F);
            wing_left.xRot = fly.eval(1.0F, 0.4F, 0.1F, 0.2F);
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        body_base.render(poseStack, buffer, packedLight, packedOverlay);
    }
}
