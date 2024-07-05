package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.tropicraft.core.common.entity.passive.SlenderHarvestMouseEntity;

public class SlenderHarvestMouseModel<T extends SlenderHarvestMouseEntity> extends TropicraftAgeableHierarchicalModel<T> {
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart legBackLeft;
    private final ModelPart legBackRight;
    private final ModelPart legFrontLeft;
    private final ModelPart legFrontRight;
    private final ModelPart earLeft;
    private final ModelPart earRight;
    private final ModelPart tail1;
    private final ModelPart tail2;
    private final ModelPart tail3;

    public SlenderHarvestMouseModel(ModelPart root) {
        this.root = root;
        body = root.getChild("body_base");
        head = body.getChild("head");
        legBackLeft = body.getChild("leg_back_left");
        legBackRight = body.getChild("leg_back_right");
        legFrontLeft = body.getChild("leg_front_left");
        legFrontRight = body.getChild("leg_front_right");
        earLeft = head.getChild("cute_lil_ear_left");
        earRight = head.getChild("cute_lil_ear_right");
        tail1 = body.getChild("tail1");
        tail2 = tail1.getChild("tail2");
        tail3 = tail2.getChild("tail3");
    }

    public static LayerDefinition create() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body_base = partdefinition.addOrReplaceChild("body_base", CubeListBuilder.create().texOffs(9, 0).addBox(-1.0f, -1.0f, -1.5f, 2.0f, 2.0f, 2.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(0.0f, 22.25f, 1.0f, -0.0873f, 0.0f, 0.0f));

        PartDefinition leg_front_right = body_base.addOrReplaceChild("leg_front_right", CubeListBuilder.create().texOffs(3, 11).addBox(-0.25f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offset(-1.0f, 1.0f, -1.5f));

        PartDefinition leg_front_left = body_base.addOrReplaceChild("leg_front_left", CubeListBuilder.create().texOffs(0, 11).addBox(-0.75f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offset(1.0f, 1.0f, -1.5f));

        PartDefinition leg_back_right = body_base.addOrReplaceChild("leg_back_right", CubeListBuilder.create().texOffs(9, 11).addBox(-0.25f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-1.0f, 1.0f, 0.5f, 0.2618f, 0.0f, 0.0f));

        PartDefinition leg_back_left = body_base.addOrReplaceChild("leg_back_left", CubeListBuilder.create().texOffs(6, 11).addBox(-0.75f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(1.0f, 1.0f, 0.5f, 0.2618f, 0.0f, 0.0f));

        PartDefinition head = body_base.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0f, -0.5f, -2.0f, 2.0f, 2.0f, 2.0f, new CubeDeformation(0.001f)), PartPose.offsetAndRotation(0.0f, -1.0f, -1.5f, 0.3491f, 0.0f, 0.0f));

        PartDefinition wee_nose = head.addOrReplaceChild("wee_nose", CubeListBuilder.create().texOffs(12, 11).addBox(-0.5f, 0.25f, -0.02f, 1.0f, 1.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offset(0.0f, -0.5f, -2.0f));

        PartDefinition cute_lil_ear_right = head.addOrReplaceChild("cute_lil_ear_right", CubeListBuilder.create().texOffs(10, 8).addBox(0.0f, -1.0f, 0.0f, 0.0f, 1.0f, 1.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-1.0f, 0.0f, 0.0f, 0.6545f, -0.9599f, 0.0f));

        PartDefinition cute_lil_ear_left = head.addOrReplaceChild("cute_lil_ear_left", CubeListBuilder.create().texOffs(7, 8).addBox(0.0f, -1.0f, 0.0f, 0.0f, 1.0f, 1.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(1.0f, 0.0f, 0.0f, 0.6545f, 0.9599f, 0.0f));

        PartDefinition tail1 = body_base.addOrReplaceChild("tail1", CubeListBuilder.create().texOffs(0, 8).addBox(-0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 2.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(0.0f, -1.0f, 0.5f, -0.6981f, 0.0f, 0.0f));

        PartDefinition tail2 = tail1.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(7, 5).addBox(-0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 2.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(0.0f, 0.0f, 2.0f, 0.3491f, 0.0f, 0.0f));

        PartDefinition tail3 = tail2.addOrReplaceChild("tail3", CubeListBuilder.create().texOffs(0, 5).addBox(-0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 2.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(0.0f, 0.0f, 2.0f, 0.3491f, 0.0f, 0.0f));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch) {
        body.getAllParts().forEach(ModelPart::resetPose);

        ModelAnimator.look(head, headYaw, headPitch + 20.0f);

        try (ModelAnimator.Cycle walk = ModelAnimator.cycle(limbSwing * 0.8f, limbSwingAmount * 1.5f)) {
            legFrontLeft.xRot = walk.eval(1.0f, 1.0f);
            legFrontRight.xRot = walk.eval(-1.0f, 1.0f);
            legBackLeft.xRot = walk.eval(-1.0f, 1.0f);
            legBackRight.xRot = walk.eval(1.0f, 1.0f);
            body.y += walk.eval(2.0f, 0.125f, 0.5f, 0.0f);
            body.x += walk.eval(1.0f, 0.125f, 0.5f, 0.0f);

            tail1.yRot += walk.eval(1.0f, 0.2f, 0.2f, 0.0f);
            tail2.yRot += walk.eval(1.0f, 0.2f, 0.3f, 0.0f);
            tail3.yRot += walk.eval(1.0f, 0.4f, 0.5f, 0.0f);
        }

        try (ModelAnimator.Cycle idle = ModelAnimator.cycle(ageInTicks, 1.0f)) {
            head.xRot += idle.eval(0.3f, 0.0125f);
        }

        try (ModelAnimator.Cycle idle = ModelAnimator.cycle(ageInTicks, 1.0f)) {
            earLeft.xRot += idle.twitch(7.0f, 0.22f, 1.0f);
            earRight.xRot += idle.twitch(7.0f, 0.18f, 1.0f);
            tail3.xRot += idle.twitch(15.0f, 0.15f, 0.5f);
        }
    }

    @Override
    protected ModelPart root() {
        return root;
    }

    @Override
    protected ModelPart head() {
        return head;
    }
}
