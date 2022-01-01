package net.tropicraft.core.client.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.tropicraft.core.common.entity.neutral.JaguarEntity;

public class JaguarModel<T extends JaguarEntity> extends AgeableListModel<T> {
    private final ModelPart body_base;
    private final ModelPart tail_base;
    private final ModelPart tail_tip;
    private final ModelPart tail_tip_r1;
    private final ModelPart leg_back_left;
    private final ModelPart torso_main;
    private final ModelPart leg_front_left;
    private final ModelPart head_base;
    private final ModelPart ear_left;
    private final ModelPart ear_left_r1;
    private final ModelPart head_snout;
    private final ModelPart ear_right;
    private final ModelPart ear_right_r1;
    private final ModelPart leg_front_right;
    private final ModelPart leg_back_right;

    public JaguarModel(ModelPart model) {
        this.body_base = model.getChild("body");
        this.tail_base = model.getChild("tail_base");
        this.tail_tip = this.tail_base.getChild("tail_tip");
        this.tail_tip_r1 = this.tail_tip.getChild("tail_tip_r1");
        this.leg_back_left = model.getChild("leg_back_left");
        this.torso_main = model.getChild("torso_main");
        this.leg_front_left = this.torso_main.getChild("leg_front_left");
        this.head_base = model.getChild("head");
        this.ear_left = model.getChild("ear_left");
        this.ear_left_r1 = this.ear_left.getChild("ear_left_r1");
        this.head_snout = model.getChild("head_snout");
        this.ear_right = model.getChild("ear_right");
        this.ear_right_r1 = this.ear_right.getChild("ear_right_r1");
        this.leg_front_right = this.torso_main.getChild("leg_front_right");
        this.leg_back_right = model.getChild("leg_back_right");
    }

    public static LayerDefinition create() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();

        partDefinition.addOrReplaceChild("body", CubeListBuilder.create()
                        .texOffs(37, 0).addBox(-3.5f, -1.0f, -4.0f, 7.0f, 8.0f, 10.0f),
                PartPose.offsetAndRotation(0.0f, 9.0f, 4.0f, 0.0f, 0.0f, 0.0f));

        PartDefinition partDefinition1 = partDefinition.addOrReplaceChild("tail_base", CubeListBuilder.create()
                        .texOffs(54, 20).addBox(-1.5f, 0.0f, 0.0f, 3.0f, 3.0f, 9.0f),
                PartPose.offsetAndRotation(0.0f, 8.0f, 6.0f, -1.0472f, 0.0f, 0.0f));

        PartDefinition partDefinition2 = partDefinition1.addOrReplaceChild("tail_tip", CubeListBuilder.create(),
                PartPose.offsetAndRotation(0.0f, 3.0f, 9.0f, 0.4363f, 0.0f, 0.0f));

        partDefinition2.addOrReplaceChild("tail_tip_r1", CubeListBuilder.create()
                        .texOffs(29, 20).addBox(-1.5f, -3.0f, 0.0f, 3.0f, 3.0f, 9.0f, new CubeDeformation(0.00f)),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.1745f, 0.0f, 0.0f));

        partDefinition.addOrReplaceChild("leg_back_left", CubeListBuilder.create()
                        .texOffs(0, 55).addBox(-3.0f, -2.0f, -2.0f, 3.0f, 13.0f, 4.0f),
                PartPose.offsetAndRotation(4.5f, 13.0f, 3.0f, 0.0f, 0.0f, 0.0f));

        PartDefinition partDefinition5 = partDefinition.addOrReplaceChild("torso_main", CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-4.0f, -1.0f, -10.0f, 8.0f, 9.0f, 10.0f),
                PartPose.offsetAndRotation(0.0f, 9.0f, -4.0f, 0.0f, 0.0f, 0.0f));

        partDefinition5.addOrReplaceChild("leg_front_left", CubeListBuilder.create()
                        .texOffs(15, 35).addBox(-2.0f, -2.0f, -1.0f, 3.0f, 15.0f, 4.0f),
                PartPose.offsetAndRotation(4.0f, 2.0f, -8.0f, 0.0f, 0.0f, 0.0f));

        PartDefinition partDefinition6 = partDefinition.addOrReplaceChild("head", CubeListBuilder.create()
                        .texOffs(0, 20).addBox(-3.5f, -2.0f, -7.0f, 7.0f, 7.0f, 7.0f),
                PartPose.offsetAndRotation(0.0f, 9.0f, -10.0f, 0.0436f, 0.0f, 0.0f));

        PartDefinition partDefinition7 = partDefinition.addOrReplaceChild("ear_left", CubeListBuilder.create(),
                PartPose.offsetAndRotation(2.0f, 7.0f, -3.0f, 0.0f, -0.5672f, 0.3927f));

        partDefinition7.addOrReplaceChild("ear_left_r1", CubeListBuilder.create()
                        .texOffs(34, 55).addBox(0.0f, -2.0f, 0.0f, 3.0f, 3.0f, 1.0f),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));

        partDefinition.addOrReplaceChild("head_snout", CubeListBuilder.create()
                        .texOffs(15, 55).addBox(-2.5f, 0.0f, -4.0f, 5.0f, 6.0f, 4.0f),
                PartPose.offsetAndRotation(0.0f, 8.0f, -7.0f, 0.2618f, 0.0f, 0.0f));

        PartDefinition partDefinition10 = partDefinition.addOrReplaceChild("ear_right", CubeListBuilder.create(),
                PartPose.offsetAndRotation(-2.0f, 7.0f, -3.0f, 0.0f, 0.5672f, -0.3927f));

        partDefinition10.addOrReplaceChild("ear_right_r1", CubeListBuilder.create()
                        .texOffs(15, 66).addBox(-3.0f, -2.0f, 0.0f, 3.0f, 3.0f, 1.0f),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));

        partDefinition5.addOrReplaceChild("leg_front_right", CubeListBuilder.create()
                        .texOffs(0, 35).addBox(-1.0f, -2.0f, -1.0f, 3.0f, 15.0f, 4.0f),
                PartPose.offsetAndRotation(-4.0f, 2.0f, -8.0f, 0.0f, 0.0f, 0.0f));

        partDefinition.addOrReplaceChild("leg_back_right", CubeListBuilder.create()
                        .texOffs(30, 35).addBox(0.0f, -2.0f, -2.0f, 3.0f, 13.0f, 4.0f),
                PartPose.offsetAndRotation(-4.5f, 13.0f, 3.0f, 0.0f, 0.0f, 0.0f));

        return LayerDefinition.create(meshDefinition, 128, 128);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float age, float headYaw, float headPitch) {
        ModelAnimator.look(head_base, headYaw, headPitch);

        try (ModelAnimator.Cycle walk = ModelAnimator.cycle(limbSwing * 0.1F, limbSwingAmount)) {
            leg_front_left.xRot = walk.eval(1.0F, 0.8F);
            leg_front_right.xRot = walk.eval(-1.0F, 0.8F);
            leg_back_left.xRot = walk.eval(-1.0F, 0.8F);
            leg_back_right.xRot = walk.eval(1.0F, 0.8F);
        }
    }

//    @Override
//    protected ModelPart getHead() {
//        return this.head_base;
//    }
//
//    @Override
//    protected ModelPart getBody() {
//        return this.body_base;
//    }

//    @Override
//    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
//        body_base.render(poseStack, buffer, packedLight, packedOverlay);
//        head_base.render(poseStack, buffer, packedLight, packedOverlay);
//    }

    @Override
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of(head_base);
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(body_base, tail_base, tail_tip, tail_tip_r1, leg_back_left, torso_main, leg_front_left,
                ear_left, ear_left_r1, head_snout, ear_right, ear_right_r1, leg_front_right, leg_back_right);
    }
}
