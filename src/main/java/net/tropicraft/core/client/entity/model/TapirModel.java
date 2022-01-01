package net.tropicraft.core.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.tropicraft.core.common.entity.passive.TapirEntity;

public class TapirModel<T extends TapirEntity> extends TropicraftAgeableModel<T> {
    private final ModelPart body_base;
    private final ModelPart head_base;
    private final ModelPart tail_base;
    private final ModelPart trunk_base;
    private final ModelPart trunk_tip;
    private final ModelPart ear_left;
    private final ModelPart ear_left_r1;
    private final ModelPart ear_right;
    private final ModelPart ear_right_r1;
    private final ModelPart leg_front_left;
    private final ModelPart leg_front_right;
    private final ModelPart leg_back_left;
    private final ModelPart leg_back_right;

    public TapirModel(ModelPart model) {
        this.body_base = model;
        this.head_base = model;
        this.tail_base = model.getChild("tail_base");
        this.trunk_base = model.getChild("trunk_base");
        this.trunk_tip = this.trunk_base.getChild("trunk_tip");
        this.ear_left = model.getChild("ear_left");
        this.ear_left_r1 = this.ear_left.getChild("ear_left_r1");
        this.ear_right = model.getChild("ear_right");
        this.ear_right_r1 = this.ear_right.getChild("ear_right_r1");
        this.leg_front_left = model.getChild("leg_front_left");
        this.leg_front_right = model.getChild("leg_front_right");
        this.leg_back_left = model.getChild("leg_back_left");
        this.leg_back_right = model.getChild("leg_back_right");
    }

    public static LayerDefinition create() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();

        partDefinition.addOrReplaceChild("body_base", CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-5.0f, -3.0f, -11.0f, 10.0f, 10.0f, 18.0f),
                PartPose.offsetAndRotation(0.0f, 7.0f, 3.0f, 0.0f, 0.0f, 0.0f));

        partDefinition.addOrReplaceChild("head_base", CubeListBuilder.create()
                        .texOffs(0, 29).addBox(-4.0f, -3.0f, -10.0f, 8.0f, 8.0f, 10.0f),
                PartPose.offsetAndRotation(0.0f, 6.0f, -8.0f, 0.0873f, 0.0f, 0.0f));

        partDefinition.addOrReplaceChild("tail_base", CubeListBuilder.create()
                        .texOffs(4, 0).addBox(-1.5f, 0.0f, -1.0f, 3.0f, 5.0f, 1.0f),
                PartPose.offsetAndRotation(0.0f, 5.0f, 7.0f, 0.3054f, 0.0f, 0.0f));

        PartDefinition partDefinition2 = partDefinition.addOrReplaceChild("trunk_base", CubeListBuilder.create()
                        .texOffs(41, 0).addBox(-2.0f, 0.0f, -4.0f, 4.0f, 4.0f, 4.0f),
                PartPose.offsetAndRotation(0.0f, 4.0f, -10.0f, 0.6109f, 0.0f, 0.0f));

        partDefinition2.addOrReplaceChild("trunk_tip", CubeListBuilder.create()
                        .texOffs(40, 10).addBox(-2.0f, 0.0f, -3.0f, 4.0f, 4.0f, 3.0f, new CubeDeformation(0.00f)),
                PartPose.offsetAndRotation(0.0f, 0.0f, -4.0f, 0.2618f, 0.0f, 0.0f));

        PartDefinition partDefinition4 = partDefinition.addOrReplaceChild("ear_left", CubeListBuilder.create(),
                PartPose.offsetAndRotation(4.0f, 3.0f, -2.0f, 0.0f, 0.2618f, 0.3491f));

        partDefinition4.addOrReplaceChild("ear_left_r1", CubeListBuilder.create()
                        .texOffs(17, 70).addBox(0.0f, -2.0f, -2.0f, 1.0f, 3.0f, 3.0f),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));

        PartDefinition partDefinition6 = partDefinition.addOrReplaceChild("ear_right", CubeListBuilder.create(),
                PartPose.offsetAndRotation(-4.0f, 3.0f, -2.0f, 0.0f, -0.2618f, -0.3491f));

        partDefinition6.addOrReplaceChild("ear_right_r1", CubeListBuilder.create()
                        .texOffs(17, 63).addBox(-1.0f, -2.0f, -2.0f, 1.0f, 3.0f, 3.0f),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));

        partDefinition.addOrReplaceChild("leg_front_left", CubeListBuilder.create()
                        .texOffs(0, 63).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 10.0f, 4.0f),
                PartPose.offsetAndRotation(3.0f, 14.0f, -9.0f, 0.0f, 0.0f, 0.0f));

        partDefinition.addOrReplaceChild("leg_front_right", CubeListBuilder.create()
                        .texOffs(34, 48).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 10.0f, 4.0f),
                PartPose.offsetAndRotation(-3.0f, 14.0f, -9.0f, 0.0f, 0.0f, 0.0f));

        partDefinition.addOrReplaceChild("leg_back_left", CubeListBuilder.create()
                        .texOffs(17, 48).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 10.0f, 4.0f),
                PartPose.offsetAndRotation(3.0f, 14.0f, 4.0f, 0.0f, 0.0f, 0.0f));

        partDefinition.addOrReplaceChild("leg_back_right", CubeListBuilder.create()
                        .texOffs(0, 48).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 10.0f, 4.0f),
                PartPose.offsetAndRotation(-3.0f, 14.0f, 4.0f, 0.0f, 0.0f, 0.0f));

        return LayerDefinition.create(meshDefinition, 128, 128);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float age, float headYaw, float headPitch) {
        ModelAnimator.look(head_base, headYaw, headPitch);

        try (ModelAnimator.Cycle walk = ModelAnimator.cycle(limbSwing * 0.1F, limbSwingAmount)) {
            leg_front_left.xRot = walk.eval(1.0F, 1.0F);
            leg_front_right.xRot = walk.eval(-1.0F, 1.0F);
            leg_back_left.xRot = walk.eval(-1.0F, 1.0F);
            leg_back_right.xRot = walk.eval(1.0F, 1.0F);
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        body_base.render(poseStack, buffer, packedLight, packedOverlay);
        head_base.render(poseStack, buffer, packedLight, packedOverlay);
    }

    @Override
    protected ModelPart getHead() {
        return head_base;
    }

    @Override
    protected ModelPart getBody() {
        return body_base;
    }
}
