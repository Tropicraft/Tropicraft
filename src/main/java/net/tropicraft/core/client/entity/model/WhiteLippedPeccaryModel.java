package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
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
        head_base = body_base.getChild("head_base");

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

//        texWidth = 64;
//        texHeight = 64;
//
//        head_base = new ModelPart(this);
//        head_base.setPos(0.0F, 14.0F, -5.5F);
//        setRotationAngle(head_base, 0.0873F, 0.0F, 0.0F);
//        head_base.texOffs(0, 20).addBox(-2.5F, -3.0F, -3.0F, 5.0F, 7.0F, 4.0F, 0.0F, false);
//
//        head_connection = new ModelPart(this);
//        head_connection.setPos(0.0F, 2.0F, -3.0F);
//        head_base.addChild(head_connection);
//        head_connection.texOffs(0, 32).addBox(-1.5F, -1.0F, -5.0F, 3.0F, 3.0F, 5.0F, 0.005F, false);
//
//        ear_right = new ModelPart(this);
//        ear_right.setPos(-1.5F, -3.0F, -1.0F);
//        head_base.addChild(ear_right);
//        setRotationAngle(ear_right, -0.829F, -0.2618F, -0.3491F);
//        ear_right.texOffs(27, 41).addBox(-1.0F, -2.0F, 0.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
//
//        ear_left = new ModelPart(this);
//        ear_left.setPos(1.5F, -3.0F, -1.0F);
//        head_base.addChild(ear_left);
//        setRotationAngle(ear_left, -0.829F, 0.2618F, 0.3491F);
//        ear_left.texOffs(0, 50).addBox(0.0F, -2.0F, 0.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
//
//        head_snout_bridge = new ModelPart(this);
//        head_snout_bridge.setPos(0.0F, -3.0F, -3.0F);
//        head_base.addChild(head_snout_bridge);
//        setRotationAngle(head_snout_bridge, 0.48F, 0.0F, 0.0F);
//        head_snout_bridge.texOffs(19, 20).addBox(-1.5F, 0.0F, -6.0F, 3.0F, 4.0F, 6.0F, 0.0F, false);
//
//        head_snout = new ModelPart(this);
//        head_snout.setPos(0.0F, 0.0F, -6.0F);
//        head_snout_bridge.addChild(head_snout);
//        setRotationAngle(head_snout, -0.1309F, 0.0F, 0.0F);
//
//        head_snout_r1 = new ModelPart(this);
//        head_snout_r1.setPos(0.0F, 0.0F, 0.0F);
//        head_snout.addChild(head_snout_r1);
//        setRotationAngle(head_snout_r1, -0.2182F, 0.0F, 0.0F);
//        head_snout_r1.texOffs(18, 41).addBox(-1.5F, 0.0F, -0.5F, 3.0F, 3.0F, 1.0F, 0.006F, false);
//
//        body_base = new ModelPart(this);
//        body_base.setPos(0.0F, 12.0F, 4.0F);
//        body_base.texOffs(0, 0).addBox(-3.0F, -1.0F, -9.0F, 6.0F, 7.0F, 12.0F, 0.0F, false);
//
//        hair_base_right = new ModelPart(this);
//        hair_base_right.setPos(0.0F, -1.0F, -9.0F);
//        body_base.addChild(hair_base_right);
//        setRotationAngle(hair_base_right, -0.1309F, 0.0F, -0.2182F);
//        hair_base_right.texOffs(37, 14).addBox(-1.5F, -3.0F, 0.0F, 1.0F, 3.0F, 10.0F, 0.0F, false);
//
//        hair_base_left = new ModelPart(this);
//        hair_base_left.setPos(0.0F, -1.0F, -9.0F);
//        body_base.addChild(hair_base_left);
//        setRotationAngle(hair_base_left, -0.1309F, 0.0F, 0.2182F);
//        hair_base_left.texOffs(37, 0).addBox(0.5F, -3.0F, 0.0F, 1.0F, 3.0F, 10.0F, 0.0F, true);
//
//        leg_left_ba = new ModelPart(this);
//        leg_left_ba.setPos(2.0F, 5.9F, 2.0F);
//        body_base.addChild(leg_left_ba);
//        leg_left_ba.texOffs(26, 32).addBox(-1.005F, 0.1F, -1.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);
//
//        leg_right_ba = new ModelPart(this);
//        leg_right_ba.setPos(-2.0F, 5.9F, 2.0F);
//        body_base.addChild(leg_right_ba);
//        leg_right_ba.texOffs(17, 32).addBox(-0.995F, 0.1F, -1.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);
//
//        leg_left_fr = new ModelPart(this);
//        leg_left_fr.setPos(2.0F, 5.9F, -8.0F);
//        body_base.addChild(leg_left_fr);
//        leg_left_fr.texOffs(9, 41).addBox(-1.005F, 0.1F, -1.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);
//
//        leg_right_fr = new ModelPart(this);
//        leg_right_fr.setPos(-2.0F, 5.9F, -8.0F);
//        body_base.addChild(leg_right_fr);
//        leg_right_fr.texOffs(0, 41).addBox(-0.995F, 0.1F, -1.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);
    }

    public static LayerDefinition create() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();

        PartDefinition modelPartBody = modelPartData.addOrReplaceChild("body_base",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-3.0F, -1.0F, -9.0F, 6.0F, 7.0F, 12.0F, false),
                PartPose.offset(0.0F, 12.0F, 4.0F));

        PartDefinition modelPartHead = modelPartBody.addOrReplaceChild("head_base",
                CubeListBuilder.create()
                        .texOffs(0, 20)
                        .addBox(-2.5F, -3.0F, -3.0F, 5.0F, 7.0F, 4.0F, false),
                PartPose.offsetAndRotation(0.0F, 14.0F, -5.5F, 0.0873F, 0.0F, 0.0F));

        modelPartHead.addOrReplaceChild("head_connection",
                CubeListBuilder.create().mirror(false)
                        .texOffs(0, 32)
                        .addBox(-1.5F, -1.0F, -5.0F, 3.0F, 3.0F, 5.0F, new CubeDeformation(0.005F)),
                PartPose.offset(0.0F, 2.0F, -3.0F));

        modelPartHead.addOrReplaceChild("ear_right",
                CubeListBuilder.create()
                        .texOffs(27, 41)
                        .addBox(-1.0F, -2.0F, 0.0F, 1.0F, 2.0F, 2.0F, false),
                PartPose.offsetAndRotation(-1.5F, -3.0F, -1.0F, -0.829F, -0.2618F, -0.3491F));

        modelPartHead.addOrReplaceChild("ear_left",
                CubeListBuilder.create()
                        .texOffs(0, 50)
                        .addBox(0.0F, -2.0F, 0.0F, 1.0F, 2.0F, 2.0F, false),
                PartPose.offsetAndRotation(1.5F, -3.0F, -1.0F, -0.829F, 0.2618F, 0.3491F));

        PartDefinition modelPartHeadSnoutBridge = modelPartHead.addOrReplaceChild("head_snout_bridge",
                CubeListBuilder.create()
                        .texOffs(19, 20)
                        .addBox(-1.5F, 0.0F, -6.0F, 3.0F, 4.0F, 6.0F, false),
                PartPose.offsetAndRotation(0.0F, -3.0F, -3.0F, 0.48F, 0.0F, 0.0F));

        PartDefinition modelPartHeadSnout = modelPartHeadSnoutBridge.addOrReplaceChild("head_snout",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(0.0F, 0.0F, -6.0F, -0.1309F, 0.0F, 0.0F));

        modelPartHeadSnout.addOrReplaceChild("head_snout_r1",
                CubeListBuilder.create().mirror(false)
                        .texOffs(18, 41)
                        .addBox(-1.5F, 0.0F, -0.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.006F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

        modelPartBody.addOrReplaceChild("hair_base_right",
                CubeListBuilder.create()
                        .texOffs(37, 14)
                        .addBox(-1.5F, -3.0F, 0.0F, 1.0F, 3.0F, 10.0F, false),
                PartPose.offsetAndRotation(0.0F, -1.0F, -9.0F, -0.1309F, 0.0F, -0.2182F));

        modelPartBody.addOrReplaceChild("hair_base_left",
                CubeListBuilder.create()
                        .texOffs(37, 0)
                        .addBox(0.5F, -3.0F, 0.0F, 1.0F, 3.0F, 10.0F, false),
                PartPose.offsetAndRotation(0.0F, -1.0F, -9.0F, -0.1309F, 0.0F, 0.2182F));

        modelPartBody.addOrReplaceChild("leg_left_ba",
                CubeListBuilder.create()
                        .texOffs(26, 32)
                        .addBox(-1.005F, 0.1F, -1.0F, 2.0F, 6.0F, 2.0F, false),
                PartPose.offset(2.0F, 5.9F, 2.0F));

        modelPartBody.addOrReplaceChild("leg_right_ba",
                CubeListBuilder.create()
                        .texOffs(17, 32)
                        .addBox(-0.995F, 0.1F, -1.0F, 2.0F, 6.0F, 2.0F, false),
                PartPose.offset(-2.0F, 5.9F, 2.0F));

        modelPartBody.addOrReplaceChild("leg_left_fr",
                CubeListBuilder.create()
                        .texOffs(9, 41)
                        .addBox(-1.005F, 0.1F, -1.0F, 2.0F, 6.0F, 2.0F, false),
                PartPose.offset(2.0F, 5.9F, -8.0F));

        modelPartBody.addOrReplaceChild("leg_right_fr",
                CubeListBuilder.create()
                        .texOffs(0, 41).addBox(-0.995F, 0.1F, -1.0F, 2.0F, 6.0F, 2.0F, false),
                PartPose.offset(-2.0F, 5.9F, -8.0F));

        return LayerDefinition.create(modelData, 64, 64);
    }

	@Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float age, float headYaw, float headPitch) {
		ModelAnimator.look(head_base, headYaw, headPitch);

		try (ModelAnimator.Cycle walk = ModelAnimator.cycle(limbSwing * 0.2F, limbSwingAmount)) {
			leg_left_fr.xRot = walk.eval(1.0F, 1.0F);
			leg_right_fr.xRot = walk.eval(-1.0F, 1.0F);
			leg_left_ba.xRot = walk.eval(-1.0F, 1.0F);
			leg_right_ba.xRot = walk.eval(1.0F, 1.0F);
		}
	}

	@Override
	protected ModelPart getHead() {
		return this.head_base;
	}

	@Override
	protected ModelPart getBody() {
		return this.body_base;
	}

    private void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
