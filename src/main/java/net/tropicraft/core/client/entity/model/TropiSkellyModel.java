package net.tropicraft.core.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.AbstractZombieModel;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.HumanoidArm;
import net.tropicraft.core.common.entity.hostile.TropiSkellyEntity;

public class TropiSkellyModel extends AbstractZombieModel<TropiSkellyEntity> implements ArmedModel {

    public TropiSkellyModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = createMesh(CubeDeformation.NONE, 0.0F);
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("right_arm",
                CubeListBuilder.create().texOffs(40, 16)
                        .addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2),
                PartPose.offset(-5.0F, 2.0F, 0.0F));

        root.addOrReplaceChild("left_arm",
                CubeListBuilder.create().texOffs(40, 16).mirror()
                        .addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2),
                PartPose.offset(5.0F, 2.0F, 0.0F));

        root.addOrReplaceChild("right_leg",
                CubeListBuilder.create().texOffs(0, 16)
                        .addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2),
                PartPose.offset(-2.0F, 12.0F, 0.0F));

        root.addOrReplaceChild("left_leg",
                CubeListBuilder.create().texOffs(0, 16)
                        .addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2),
                PartPose.offset(2.0F, 12.0F, 0.0F));

        PartDefinition body = root.getChild("body");

        body.addOrReplaceChild("skirt",
                CubeListBuilder.create().texOffs(40, 0)
                        .addBox(-4.0F, 12.0F, -2.0F, 8, 3, 4),
                PartPose.ZERO);

        return LayerDefinition.create(mesh, 64, 64);
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
