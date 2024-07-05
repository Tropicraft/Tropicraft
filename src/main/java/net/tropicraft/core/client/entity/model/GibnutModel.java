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
import net.tropicraft.core.common.entity.passive.GibnutEntity;

public class GibnutModel extends TropicraftAgeableHierarchicalModel<GibnutEntity> {
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart legBackLeft;
    private final ModelPart legBackRight;
    private final ModelPart legFrontLeft;
    private final ModelPart legFrontRight;
    private final ModelPart earLeft;
    private final ModelPart earRight;
    private final ModelPart whiskerLeft1;
    private final ModelPart whiskerLeft2;
    private final ModelPart whiskerRight1;
    private final ModelPart whiskerRight2;

    public GibnutModel(ModelPart root) {
        this.root = root;
        body = root.getChild("body_base");
        head = body.getChild("head_base");
        legBackLeft = body.getChild("leg_back_left");
        legBackRight = body.getChild("leg_back_right");
        legFrontLeft = body.getChild("leg_front_left");
        legFrontRight = body.getChild("leg_front_right");
        earLeft = head.getChild("ear_left");
        earRight = head.getChild("ear_right");
        whiskerLeft1 = head.getChild("whisker_left1");
        whiskerLeft2 = head.getChild("whisker_left2");
        whiskerRight1 = head.getChild("whisker_right1");
        whiskerRight2 = head.getChild("whisker_right2");
    }

    public static LayerDefinition create() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body_base = partdefinition.addOrReplaceChild("body_base", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0f, -3.0f, -7.0f, 4.0f, 4.0f, 8.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(0.0f, 21.0f, 4.5f, 0.0436f, 0.0f, 0.0f));

        PartDefinition head_base = body_base.addOrReplaceChild("head_base", CubeListBuilder.create().texOffs(0, 13).addBox(-1.5f, -0.5f, -4.0f, 3.0f, 3.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(0.0f, -3.0f, -7.0f, 0.3054f, 0.0f, 0.0f));

        PartDefinition ear_right = head_base.addOrReplaceChild("ear_right", CubeListBuilder.create().texOffs(3, 31).addBox(-1.0f, -1.0f, 0.0f, 1.0f, 1.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-0.5f, -0.5f, -0.75f, -0.0289f, 0.2163f, -0.3958f));

        PartDefinition ear_left = head_base.addOrReplaceChild("ear_left", CubeListBuilder.create().texOffs(0, 31).addBox(0.0f, -1.0f, 0.0f, 1.0f, 1.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(0.5f, -0.5f, -0.75f, -0.0289f, -0.2163f, 0.3958f));

        PartDefinition whisker_right1 = head_base.addOrReplaceChild("whisker_right1", CubeListBuilder.create(), PartPose.offset(-1.5f, 0.5f, -4.0f));

        PartDefinition whisker_right1_r1 = whisker_right1.addOrReplaceChild("whisker_right1_r1", CubeListBuilder.create().texOffs(27, 29).addBox(-3.0f, 0.0f, 0.0f, 3.0f, 0.0f, 1.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, -0.421f, 0.5574f, -0.3532f));

        PartDefinition whisker_right2 = head_base.addOrReplaceChild("whisker_right2", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.5f, 0.75f, -4.0f, 0.0f, 0.0f, -0.3054f));

        PartDefinition whisker_right2_r1 = whisker_right2.addOrReplaceChild("whisker_right2_r1", CubeListBuilder.create().texOffs(18, 29).addBox(-3.0f, 0.0f, 0.0f, 3.0f, 0.0f, 1.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, -0.421f, 0.5574f, -0.3532f));

        PartDefinition whisker_left1 = head_base.addOrReplaceChild("whisker_left1", CubeListBuilder.create(), PartPose.offset(-1.5f, 0.5f, -4.0f));

        PartDefinition whisker_left1_r1 = whisker_left1.addOrReplaceChild("whisker_left1_r1", CubeListBuilder.create().texOffs(9, 29).addBox(0.0f, 0.0f, 0.0f, 3.0f, 0.0f, 1.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(3.0f, 0.0f, 0.0f, -0.421f, -0.5574f, 0.3532f));

        PartDefinition whisker_left2 = head_base.addOrReplaceChild("whisker_left2", CubeListBuilder.create(), PartPose.offsetAndRotation(1.5f, 0.75f, -4.0f, 0.0f, 0.0f, 0.3054f));

        PartDefinition whisker_left2_r1 = whisker_left2.addOrReplaceChild("whisker_left2_r1", CubeListBuilder.create().texOffs(0, 29).addBox(0.0f, 0.0f, 0.0f, 3.0f, 0.0f, 1.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, -0.421f, -0.5574f, 0.3532f));

        PartDefinition leg_back_left = body_base.addOrReplaceChild("leg_back_left", CubeListBuilder.create().texOffs(15, 13).addBox(-0.5f, -0.5f, -1.0f, 1.0f, 5.0f, 2.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(2.0f, -1.5f, -0.5f, 0.0436f, 0.0f, 0.0f));

        PartDefinition leg_back_right = body_base.addOrReplaceChild("leg_back_right", CubeListBuilder.create().texOffs(0, 21).addBox(-0.5f, -0.5f, -1.0f, 1.0f, 5.0f, 2.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-2.0f, -1.5f, -0.5f, 0.0436f, 0.0f, 0.0f));

        PartDefinition leg_front_right = body_base.addOrReplaceChild("leg_front_right", CubeListBuilder.create().texOffs(14, 21).addBox(-0.5f, -1.0f, -1.0f, 1.0f, 3.0f, 2.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-2.0f, 0.75f, -6.0f, -0.1745f, 0.0f, 0.0f));

        PartDefinition leg_front_left = body_base.addOrReplaceChild("leg_front_left", CubeListBuilder.create().texOffs(7, 21).addBox(-0.5f, -1.0f, -1.0f, 1.0f, 3.0f, 2.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(2.0f, 0.75f, -6.0f, -0.1745f, 0.0f, 0.0f));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(GibnutEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch) {
        body.getAllParts().forEach(ModelPart::resetPose);

        ModelAnimator.look(head, headYaw, headPitch);

        try (ModelAnimator.Cycle walk = ModelAnimator.cycle(limbSwing * 0.125f, limbSwingAmount)) {
            legFrontLeft.xRot = walk.eval(1.0f, 1.0f);
            legFrontRight.xRot = walk.eval(-1.0f, 1.0f);
            legBackLeft.xRot = walk.eval(-1.0f, 1.0f);
            legBackRight.xRot = walk.eval(1.0f, 1.0f);
            body.y += walk.eval(2.0f, 0.5f, 0.5f, 0.0f);
            body.zRot += walk.eval(1.0f, 0.15f);
        }

        if (entity.isVibing()) {
            try (ModelAnimator.Cycle vibe = ModelAnimator.cycle(ageInTicks * 0.1f, 1.0f)) {
                head.xRot += vibe.eval(1.0f, 0.1f);
            }
        } else {
            try (ModelAnimator.Cycle sniff = ModelAnimator.cycle(ageInTicks, 1.0f)) {
                head.xRot += sniff.twitch(40.0f, 0.15f, -0.08f);
            }
        }

        try (ModelAnimator.Cycle whiskers = ModelAnimator.cycle(ageInTicks * 0.3f, 0.025f)) {
            whiskerLeft1.xRot += whiskers.eval(1.0f, 1.0f, 0.25f, 0.0f);
            whiskerLeft2.xRot += whiskers.eval(1.0f, 1.0f, 0.0f, 0.0f);
            whiskerRight1.xRot += whiskers.eval(1.0f, 1.0f, 0.0f, 0.0f);
            whiskerRight2.xRot += whiskers.eval(1.0f, 1.0f, 0.25f, 0.0f);
        }

        try (ModelAnimator.Cycle idle = ModelAnimator.cycle(ageInTicks, 1.0f)) {
            earLeft.xRot += idle.twitch(7.0f, 0.22f, 1.0f);
            earRight.xRot += idle.twitch(7.0f, 0.18f, 1.0f);
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        poseStack.pushPose();
        poseStack.translate(0.0f, 0.0f, -1.5f / 16.0f);
        super.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        poseStack.popPose();
    }

    @Override
    protected ModelPart head() {
        return head;
    }

    @Override
    protected ModelPart root() {
        return root;
    }
}
