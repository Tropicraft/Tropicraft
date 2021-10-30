package net.tropicraft.core.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.AbstractZombieModel;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.HumanoidArm;
import net.tropicraft.core.common.entity.hostile.TropiSkellyEntity;

public class TropiSkellyModel extends AbstractZombieModel<TropiSkellyEntity> implements ArmedModel {
    
    private final ModelPart skirt;

    public TropiSkellyModel(ModelPart root) {
        super(root);

        skirt = root.getChild("body").getChild("skirt");

        //super(0.0F, 0.0F, 64, 64);
        //float g = 0.0F;
        //textureWidth = 64;
        //textureHeight = 64;
        /*
        rightArm = new ModelPart(this, 40, 16);
        rightArm.addCuboid(-1.0F, -2.0F, -1.0F, 2, 12, 2, g);
        rightArm.setPivot(-5.0F, 2.0F, 0.0F);

        leftArm = new ModelPart(this, 40, 16);
        leftArm.mirror = true;
        leftArm.addCuboid(-1.0F, -2.0F, -1.0F, 2, 12, 2, g);
        leftArm.setPivot(5.0F, 2.0F, 0.0F);

        rightLeg = new ModelPart(this, 0, 16);
        rightLeg.addCuboid(-1.0F, 0.0F, -1.0F, 2, 12, 2, g);
        rightLeg.setPivot(-2.0F, 12.0F, 0.0F);

        leftLeg = new ModelPart(this, 0, 16);
        leftLeg.mirror = true;
        leftLeg.addCuboid(-1.0F, 0.0F, -1.0F, 2, 12, 2, g);
        leftLeg.setPivot(2.0F, 12.0F, 0.0F);

        // Hula Skirt
        skirt = new ModelPart(this, 40, 0);
        skirt.addCuboid(-4.0F, 12.0F, -2.0F, 8, 3, 4, 0.0F);
        body.addChild(skirt);
         */
    }

    public static LayerDefinition create() {
        MeshDefinition modelData = createMesh(CubeDeformation.NONE, 0.0F);
        PartDefinition modelPartData = modelData.getRoot();

        modelPartData.addOrReplaceChild("right_arm",
                CubeListBuilder.create().texOffs(40, 16)
                        .addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2),
                PartPose.offset(-5.0F, 2.0F, 0.0F));

        modelPartData.addOrReplaceChild("left_arm",
                CubeListBuilder.create().texOffs(40, 16).mirror()
                        .addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2),
                PartPose.offset(5.0F, 2.0F, 0.0F));

        modelPartData.addOrReplaceChild("right_leg",
                CubeListBuilder.create().texOffs(0, 16)
                        .addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2),
                PartPose.offset(-2.0F, 12.0F, 0.0F));

        modelPartData.addOrReplaceChild("left_leg",
                CubeListBuilder.create().texOffs(0, 16)
                        .addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2),
                PartPose.offset(2.0F, 12.0F, 0.0F));

        PartDefinition modelPartBody = modelPartData.getChild("body");

        modelPartBody.addOrReplaceChild("skirt",
                CubeListBuilder.create().texOffs(40, 0)
                        .addBox(-4.0F, 12.0F, -2.0F, 8, 3, 4),
                PartPose.ZERO);

        return LayerDefinition.create(modelData, 64, 64);
    }

    @Override
    public void translateToHand(HumanoidArm side, final PoseStack stack) {
        super.translateToHand(side, stack);
        stack.translate((side == HumanoidArm.LEFT ? -1 : 1) * 0.1f, 0, 0.0F);
    }

    @Override
    public boolean isAggressive(TropiSkellyEntity entityIn) {
        return entityIn.isAggressive();
    }
}