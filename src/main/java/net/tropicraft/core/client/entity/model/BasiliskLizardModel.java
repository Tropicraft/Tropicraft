package net.tropicraft.core.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.tropicraft.core.common.entity.passive.basilisk.BasiliskLizardEntity;

public class BasiliskLizardModel<T extends BasiliskLizardEntity> extends EntityModel<T> {
    private static final Minecraft CLIENT = Minecraft.getInstance();

    private static final float BACK_LEG_ANGLE = 65.0F * ModelAnimator.DEG_TO_RAD;
    private static final float FRONT_LEG_ANGLE = -40.0F * ModelAnimator.DEG_TO_RAD;

    private final ModelPart body_base;
    private final ModelPart sail_back;
    private final ModelPart leg_back_left;
    private final ModelPart leg_front_left;
    private final ModelPart head_base;
    private final ModelPart sail_head;
    private final ModelPart tail_base;
    private final ModelPart sail_tail;
    private final ModelPart tail_tip;
    private final ModelPart leg_back_right;
    private final ModelPart leg_front_right;

    public BasiliskLizardModel(ModelPart root) {
        body_base = root.getChild("body_base");
        sail_back = body_base.getChild("sail_back");
        leg_back_left = body_base.getChild("leg_back_left");
        leg_front_left = body_base.getChild("leg_front_left");
        head_base = body_base.getChild("head_base");
        sail_head = head_base.getChild("sail_head");
        tail_base = body_base.getChild("tail_base");
        sail_tail = tail_base.getChild("sail_tail");
        tail_tip = tail_base.getChild("tail_tip");
        leg_back_right = body_base.getChild("leg_back_right");
        leg_front_right = body_base.getChild("leg_front_right");
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition body = root.addOrReplaceChild("body_base",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1.0F, -1.0F, -5.0F, 2.0F, 2.0F, 6.0F, false),
                PartPose.offsetAndRotation(0.0F, 22.5F, 0.0F, -15.0F * ModelAnimator.DEG_TO_RAD, 0.0F, 0.0F));

        body.addOrReplaceChild("sail_back",
                CubeListBuilder.create()
                        .texOffs(0, 9)
                        .addBox(0.0F, -2.0F, 0.0F, 0.0F, 2.0F, 6.0F, false),
                PartPose.offsetAndRotation(0.0F, -1.0F, -5.0F, -2.5F * ModelAnimator.DEG_TO_RAD, 0.0F, 0.0F));

        body.addOrReplaceChild("leg_back_left",
                CubeListBuilder.create()
                        .texOffs(5, 25)
                        .addBox(-0.5F, 0.0F, -0.5F, 1.0F, 4.0F, 1.0F, false),
                PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, BACK_LEG_ANGLE, 0.0F, -77.5F * ModelAnimator.DEG_TO_RAD));

        body.addOrReplaceChild("leg_front_left",
                CubeListBuilder.create()
                        .texOffs(15, 25)
                        .addBox(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, false),
                PartPose.offsetAndRotation(1.0F, 0.5F, -4.0F, FRONT_LEG_ANGLE, 40.0F * ModelAnimator.DEG_TO_RAD, -57.5F * ModelAnimator.DEG_TO_RAD));

        PartDefinition modelPartHead = body.addOrReplaceChild("head_base",
                CubeListBuilder.create().mirror(false)
                        .texOffs(0, 18)
                        .addBox(-1.0F, -1.0F, -3.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.001F)),
                PartPose.offsetAndRotation(0.0F, -1.0F, -5.0F, 7.5F * ModelAnimator.DEG_TO_RAD, 0.0F, 0.0F));

        modelPartHead.addOrReplaceChild("sail_head",
                CubeListBuilder.create()
                        .texOffs(20, 18)
                        .addBox(0.0F, -3.0F, 0.0F, 0.0F, 3.0F, 3.0F, false),
                PartPose.offsetAndRotation(0.0F, -1.0F, -2.0F, -20.0F * ModelAnimator.DEG_TO_RAD, 0.0F, 0.0F));

        PartDefinition modelPartTail = body.addOrReplaceChild("tail_base",
                CubeListBuilder.create()
                        .texOffs(13, 9)
                        .addBox(-0.5F, -2.0F, 0.0F, 1.0F, 2F, 4.0F, false),
                PartPose.offsetAndRotation(0.0F, 1.0F, 1.0F, 5.0F * ModelAnimator.DEG_TO_RAD, 0.0F, 0.0F));

        modelPartTail.addOrReplaceChild("sail_tail",
                CubeListBuilder.create()
                        .texOffs(11, 18)
                        .addBox(0.0F, -2.0F, 0.0F, 0.0F, 2.0F, 4.0F, false),
                PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, -2.5F * ModelAnimator.DEG_TO_RAD, 0.0F, 0.0F));

        modelPartTail.addOrReplaceChild("tail_tip",
                CubeListBuilder.create()
                        .texOffs(17, 0)
                        .addBox(-0.5F, -1.0F, 0.0F, 1.0F, 1.0F, 6.0F, false),
                PartPose.offsetAndRotation(0.0F, -1.0F, 4.0F, 5.0F * ModelAnimator.DEG_TO_RAD, 0.0F, 0.0F));

        body.addOrReplaceChild("leg_back_right",
                CubeListBuilder.create()
                        .texOffs(0, 25)
                        .addBox(-0.5F, 0.0F, -0.5F, 1.0F, 4.0F, 1.0F, false),
                PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, BACK_LEG_ANGLE, 0.0F, 77.5F * ModelAnimator.DEG_TO_RAD));

        body.addOrReplaceChild("leg_front_right",
                CubeListBuilder.create()
                        .texOffs(10, 25)
                        .addBox(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, false),
                PartPose.offsetAndRotation(-1.0F, 0.5F, -4.0F, FRONT_LEG_ANGLE, -40.0F * ModelAnimator.DEG_TO_RAD, 57.5F * ModelAnimator.DEG_TO_RAD));

        return LayerDefinition.create(mesh, 32, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float age, float headYaw, float headPitch) {
        ModelAnimator.look(head_base, headYaw, headPitch);

        float running = entity.getRunningAnimation(CLIENT.getFrameTime());
        body_base.xRot = Mth.lerp(running, -15.0F, -50.0F) * ModelAnimator.DEG_TO_RAD;
        tail_base.xRot = Mth.lerp(running, 5.0F, 30.0F) * ModelAnimator.DEG_TO_RAD;
        tail_tip.xRot = Mth.lerp(running, 5.0F, 20.0F) * ModelAnimator.DEG_TO_RAD;
        head_base.xRot = Mth.lerp(running, 7.5F, 35.0F) * ModelAnimator.DEG_TO_RAD;

        try (ModelAnimator.Cycle walk = ModelAnimator.cycle(limbSwing * 0.25F, limbSwingAmount)) {
            leg_front_left.xRot = FRONT_LEG_ANGLE + walk.eval(-1.0F, 1.0F, 0.0F, 1.0F);
            leg_front_right.xRot = FRONT_LEG_ANGLE + walk.eval(1.0F, 1.0F, 0.0F, 1.0F);
            leg_back_left.xRot = BACK_LEG_ANGLE + walk.eval(-1.0F, -0.9F, 0.0F, -0.9F);
            leg_back_right.xRot = BACK_LEG_ANGLE + walk.eval(1.0F, -0.9F, 0.0F, -0.9F);

            body_base.xRot += walk.eval(0.5F, running * 0.1F);
        }
    }

    @Override
    public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        matrixStack.pushPose();
        matrixStack.translate(0.0, 0.0, 0.1);
        body_base.render(matrixStack, buffer, packedLight, packedOverlay);
        matrixStack.popPose();
    }
}
