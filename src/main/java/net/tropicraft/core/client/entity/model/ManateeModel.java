package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.tropicraft.core.common.entity.underdasea.ManateeEntity;

public class ManateeModel extends HierarchicalModel<ManateeEntity> {
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart tailBase;
    private final ModelPart tailFanMain;
    private final ModelPart armLeft;
    private final ModelPart armRight;

    public ManateeModel(ModelPart root) {
        this.root = root;
        body = root.getChild("body_base");
        head = body.getChild("head_base");
        tailBase = body.getChild("tail_base");
        tailFanMain = tailBase.getChild("tail_fan_main");
        armLeft = body.getChild("arm_left");
        armRight = body.getChild("arm_right");
    }

    public static LayerDefinition create() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body_base = partdefinition.addOrReplaceChild("body_base", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0f, -3.0f, -30.0f, 18.0f, 14.0f, 31.0f, new CubeDeformation(0.0f)), PartPose.offset(0.0f, 11.0f, 11.0f));

        PartDefinition head_base = body_base.addOrReplaceChild("head_base", CubeListBuilder.create().texOffs(43, 66).addBox(-5.0f, 0.0f, -7.0f, 10.0f, 10.0f, 7.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(0.0f, -1.0f, -30.0f, 0.1745f, 0.0f, 0.0f));

        PartDefinition mouth = head_base.addOrReplaceChild("mouth", CubeListBuilder.create().texOffs(78, 66).addBox(-4.0f, 0.0f, -6.0f, 8.0f, 8.0f, 6.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(0.0f, 1.0f, -7.0f, 0.6109f, 0.0f, 0.0f));

        PartDefinition tail_base = body_base.addOrReplaceChild("tail_base", CubeListBuilder.create().texOffs(57, 46).addBox(-6.0f, 0.0f, 0.0f, 12.0f, 10.0f, 9.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(0.0f, -2.0f, 1.0f, -0.2182f, 0.0f, 0.0f));

        PartDefinition tail_fan_main = tail_base.addOrReplaceChild("tail_fan_main", CubeListBuilder.create().texOffs(0, 66).addBox(-7.0f, -2.0f, -4.0f, 14.0f, 3.0f, 7.0f, new CubeDeformation(0.0f)), PartPose.offset(0.0f, 5.0f, 7.0f));

        PartDefinition tail_fan_broad = tail_fan_main.addOrReplaceChild("tail_fan_broad", CubeListBuilder.create().texOffs(0, 46).addBox(-9.0f, -1.0f, 0.0f, 18.0f, 2.0f, 10.0f, new CubeDeformation(0.0f)), PartPose.offset(0.0f, -1.0f, 3.0f));

        PartDefinition tail_fan_end = tail_fan_broad.addOrReplaceChild("tail_fan_end", CubeListBuilder.create().texOffs(0, 84).addBox(-7.0f, -1.0f, 0.0f, 14.0f, 2.0f, 3.0f, new CubeDeformation(0.0f)), PartPose.offset(0.0f, 0.0f, 10.0f));

        PartDefinition arm_left = body_base.addOrReplaceChild("arm_left", CubeListBuilder.create().texOffs(50, 84).addBox(-1.0f, -1.0f, -2.0f, 2.0f, 10.0f, 5.0f, new CubeDeformation(0.0f)), PartPose.offset(9.0f, 10.0f, -25.0f));

        PartDefinition arm_right = body_base.addOrReplaceChild("arm_right", CubeListBuilder.create().texOffs(35, 84).addBox(-1.0f, -1.0f, -2.0f, 2.0f, 10.0f, 5.0f, new CubeDeformation(0.0f)), PartPose.offset(-9.0f, 10.0f, -25.0f));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(ManateeEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch) {
        body.getAllParts().forEach(ModelPart::resetPose);

        float partialTicks = ageInTicks - entity.tickCount;
        body.xRot = entity.getXBodyRot(partialTicks) * Mth.DEG_TO_RAD;
        ModelAnimator.look(head, Mth.wrapDegrees(headYaw) * 0.25f, Mth.clamp(Mth.wrapDegrees(headPitch), 0.0f, 30.0f));
        head.xRot -= body.xRot;

        if (entity.isInWater()) {
            try (ModelAnimator.Cycle idle = ModelAnimator.cycle(ageInTicks * 0.01f, 1.0f)) {
                body.y += idle.eval(1.0f, 0.6f);
            }
        }

        try (ModelAnimator.Cycle swim = ModelAnimator.cycle(limbSwing * 0.125f, Math.min(limbSwingAmount, 0.8f))) {
            body.y += swim.eval(1.0f, 2.0f, 0.65f, 0.0f);
            head.xRot += swim.eval(1.0f, 0.125f, -0.2f, 0.25f);
            body.xRot += swim.eval(1.0f, 0.125f, 0.0f, 0.0f);
            tailBase.xRot += swim.eval(1.0f, 0.75f, 0.25f, 0.0f);
            tailFanMain.xRot += swim.eval(1.0f, 1.5f, 0.5f, 0.0f);

            armLeft.xRot += swim.eval(0.5f, 1.0f, 0.0f, 1.0f);
            armLeft.yRot += swim.eval(0.5f, 2.0f, 0.6f, 2.0f);
            armLeft.zRot += swim.eval(0.5f, -1.5f, 0.4f, -1.5f);

            armRight.xRot += swim.eval(0.5f, 1.0f, 0.0f, 1.0f);
            armRight.yRot -= swim.eval(0.5f, 2.0f, 0.6f, 2.0f);
            armRight.zRot -= swim.eval(0.5f, -1.5f, 0.4f, -1.5f);
        }
    }

    @Override
    public ModelPart root() {
        return root;
    }
}
