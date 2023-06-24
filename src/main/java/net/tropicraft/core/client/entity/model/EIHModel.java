package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.tropicraft.core.common.entity.neutral.EIHEntity;

public class EIHModel extends HierarchicalModel<EIHEntity> {
    private final ModelPart root;

    public EIHModel(ModelPart root) {
        this.root = root;
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(34, 8).addBox(-4F, 1.0F, -1F, 8, 17, 7), PartPose.offset(0.0F, -2F, 0.0F));
        root.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 0).addBox(-4F, 11F, -3F, 8, 8, 11), PartPose.offset(0.0F, 5F, -2F));
        root.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(27, 2).addBox(13.5F, -1F, -3F, 13, 2, 3), PartPose.offsetAndRotation(0.0F, -14.8F, -1F, 0, 0, 1.570796F));
        root.addOrReplaceChild("mouth", CubeListBuilder.create().texOffs(56, 11).addBox(-1.5F, 4F, -1F, 3, 3, 1), PartPose.offset(0.0F, 7.5F, -0.5F));
        root.addOrReplaceChild("top", CubeListBuilder.create().texOffs(0, 17).addBox(-4F, -1F, -10F, 8, 5, 10), PartPose.offset(0.0F, -5F, 6F));
        root.addOrReplaceChild("leye", CubeListBuilder.create().texOffs(56, 7).mirror().addBox(0.0F, 0.0F, 0.0F, 3, 3, 1), PartPose.offset(1.0F, -1F, -2F));
        root.addOrReplaceChild("reye", CubeListBuilder.create().texOffs(56, 7).addBox(-1.5F, -1F, -1F, 3, 3, 1), PartPose.offset(-2.5F, 0.0F, -1F));

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(EIHEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // it's a statue, what do you want from me
    }

    @Override
    public ModelPart root() {
        return root;
    }
}
