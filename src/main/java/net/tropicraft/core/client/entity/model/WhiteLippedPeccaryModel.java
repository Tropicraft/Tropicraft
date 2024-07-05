package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.Entity;

public class WhiteLippedPeccaryModel<T extends Entity> extends TropicraftAgeableModel<T> {
    private final ModelPart head_base;
    private final ModelPart head_connection;
    private final ModelPart ear_right;
    private final ModelPart ear_left;
    private final ModelPart head_snout_bridge;
    private final ModelPart head_snout;
    private final ModelPart head_snout_r1;
    private final ModelPart body_base;
    private final ModelPart hair_base_right;
    private final ModelPart hair_base_left;
    private final ModelPart leg_left_ba;
    private final ModelPart leg_right_ba;
    private final ModelPart leg_left_fr;
    private final ModelPart leg_right_fr;

    public WhiteLippedPeccaryModel(ModelPart root) {

        body_base = root.getChild("body_base");
        head_base = root.getChild("head_base");

        head_connection = head_base.getChild("head_connection");
        ear_right = head_base.getChild("ear_right");
        ear_left = head_base.getChild("ear_left");

        head_snout_bridge = head_base.getChild("head_snout_bridge");
        head_snout = head_snout_bridge.getChild("head_snout");
        head_snout_r1 = head_snout.getChild("head_snout_r1");

        hair_base_right = body_base.getChild("hair_base_right");
        hair_base_left = body_base.getChild("hair_base_left");
        leg_left_ba = body_base.getChild("leg_left_ba");
        leg_right_ba = body_base.getChild("leg_right_ba");
        leg_left_fr = body_base.getChild("leg_left_fr");
        leg_right_fr = body_base.getChild("leg_right_fr");
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition modelPartBody = root.addOrReplaceChild("body_base",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-3.0f, -1.0f, -9.0f, 6.0f, 7.0f, 12.0f, false),
                PartPose.offset(0.0f, 12.0f, 4.0f));

        PartDefinition modelPartHead = root.addOrReplaceChild("head_base",
                CubeListBuilder.create()
                        .texOffs(0, 20)
                        .addBox(-2.5f, -3.0f, -3.0f, 5.0f, 7.0f, 4.0f, false),
                PartPose.offsetAndRotation(0.0f, 14.0f, -5.5f, 0.0873f, 0.0f, 0.0f));

        modelPartHead.addOrReplaceChild("head_connection",
                CubeListBuilder.create().mirror(false)
                        .texOffs(0, 32)
                        .addBox(-1.5f, -1.0f, -5.0f, 3.0f, 3.0f, 5.0f, new CubeDeformation(0.005f)),
                PartPose.offset(0.0f, 2.0f, -3.0f));

        modelPartHead.addOrReplaceChild("ear_right",
                CubeListBuilder.create()
                        .texOffs(27, 41)
                        .addBox(-1.0f, -2.0f, 0.0f, 1.0f, 2.0f, 2.0f, false),
                PartPose.offsetAndRotation(-1.5f, -3.0f, -1.0f, -0.829f, -0.2618f, -0.3491f));

        modelPartHead.addOrReplaceChild("ear_left",
                CubeListBuilder.create()
                        .texOffs(0, 50)
                        .addBox(0.0f, -2.0f, 0.0f, 1.0f, 2.0f, 2.0f, false),
                PartPose.offsetAndRotation(1.5f, -3.0f, -1.0f, -0.829f, 0.2618f, 0.3491f));

        PartDefinition modelPartHeadSnoutBridge = modelPartHead.addOrReplaceChild("head_snout_bridge",
                CubeListBuilder.create()
                        .texOffs(19, 20)
                        .addBox(-1.5f, 0.0f, -6.0f, 3.0f, 4.0f, 6.0f, false),
                PartPose.offsetAndRotation(0.0f, -3.0f, -3.0f, 0.48f, 0.0f, 0.0f));

        PartDefinition modelPartHeadSnout = modelPartHeadSnoutBridge.addOrReplaceChild("head_snout",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(0.0f, 0.0f, -6.0f, -0.1309f, 0.0f, 0.0f));

        modelPartHeadSnout.addOrReplaceChild("head_snout_r1",
                CubeListBuilder.create().mirror(false)
                        .texOffs(18, 41)
                        .addBox(-1.5f, 0.0f, -0.5f, 3.0f, 3.0f, 1.0f, new CubeDeformation(0.006f)),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, -0.2182f, 0.0f, 0.0f));

        modelPartBody.addOrReplaceChild("hair_base_right",
                CubeListBuilder.create()
                        .texOffs(37, 14)
                        .addBox(-1.5f, -3.0f, 0.0f, 1.0f, 3.0f, 10.0f, false),
                PartPose.offsetAndRotation(0.0f, -1.0f, -9.0f, -0.1309f, 0.0f, -0.2182f));

        modelPartBody.addOrReplaceChild("hair_base_left",
                CubeListBuilder.create()
                        .texOffs(37, 0)
                        .addBox(0.5f, -3.0f, 0.0f, 1.0f, 3.0f, 10.0f, false),
                PartPose.offsetAndRotation(0.0f, -1.0f, -9.0f, -0.1309f, 0.0f, 0.2182f));

        modelPartBody.addOrReplaceChild("leg_left_ba",
                CubeListBuilder.create()
                        .texOffs(26, 32)
                        .addBox(-1.005f, 0.1f, -1.0f, 2.0f, 6.0f, 2.0f, false),
                PartPose.offset(2.0f, 5.9f, 2.0f));

        modelPartBody.addOrReplaceChild("leg_right_ba",
                CubeListBuilder.create()
                        .texOffs(17, 32)
                        .addBox(-0.995f, 0.1f, -1.0f, 2.0f, 6.0f, 2.0f, false),
                PartPose.offset(-2.0f, 5.9f, 2.0f));

        modelPartBody.addOrReplaceChild("leg_left_fr",
                CubeListBuilder.create()
                        .texOffs(9, 41)
                        .addBox(-1.005f, 0.1f, -1.0f, 2.0f, 6.0f, 2.0f, false),
                PartPose.offset(2.0f, 5.9f, -8.0f));

        modelPartBody.addOrReplaceChild("leg_right_fr",
                CubeListBuilder.create()
                        .texOffs(0, 41).addBox(-0.995f, 0.1f, -1.0f, 2.0f, 6.0f, 2.0f, false),
                PartPose.offset(-2.0f, 5.9f, -8.0f));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float age, float headYaw, float headPitch) {
        ModelAnimator.look(getHead(), headYaw, headPitch);

        try (ModelAnimator.Cycle walk = ModelAnimator.cycle(limbSwing * 0.2f, limbSwingAmount)) {
            leg_left_fr.xRot = walk.eval(1.0f, 1.0f);
            leg_right_fr.xRot = walk.eval(-1.0f, 1.0f);
            leg_left_ba.xRot = walk.eval(-1.0f, 1.0f);
            leg_right_ba.xRot = walk.eval(1.0f, 1.0f);
        }
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
