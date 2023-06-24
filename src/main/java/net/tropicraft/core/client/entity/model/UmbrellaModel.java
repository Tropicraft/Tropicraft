package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.tropicraft.core.common.entity.placeable.UmbrellaEntity;

public class UmbrellaModel extends HierarchicalModel<UmbrellaEntity> {
    private final ModelPart root;

    public UmbrellaModel(ModelPart root) {
        this.root = root;
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("base",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-0.5F, 0F, -0.5F, 1, 14, 1),
                PartPose.offset(0F, -13F, 0F));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-7.5F, -2F, -7.5F, 15, 1, 15),
                PartPose.offset(0F, -12F, 0F));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create().texOffs(25, 25)
                        .addBox(-1F, -1F, -1F, 2, 1, 2),
                PartPose.offset(0F, -14F, 0F));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create().texOffs(0, 20)
                        .addBox(-4F, -1F, 0F, 9, 1, 3),
                PartPose.offsetAndRotation(-0.5F, -13F, 7.5F, -0.2443461F, 0F, 0F));

        root.addOrReplaceChild("shape31",
                CubeListBuilder.create().texOffs(0, 24)
                        .addBox(-4.5F, -1F, 0F, 9, 1, 3),
                PartPose.offsetAndRotation(7.5F, -13F, 0F, -0.2443461F, 1.570796F, 0F));

        root.addOrReplaceChild("shape32",
                CubeListBuilder.create().texOffs(0, 28)
                        .addBox(-4.5F, -1F, -1F, 9, 1, 3),
                PartPose.offsetAndRotation(0F, -12.75F, -8.45F, -0.2443461F, 3.141593F, 0F));

        root.addOrReplaceChild("shape33",
                CubeListBuilder.create().texOffs(24, 28)
                        .addBox(-4.5F, -1F, 1F, 9, 1, 3),
                PartPose.offsetAndRotation(-6.5F, -13.25F, 0F, -0.2443461F, -1.570796F, 0F));

        root.addOrReplaceChild("shape11",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-0.5F, 0F, -0.5F, 1, 9, 1),
                PartPose.offsetAndRotation(0F, -10F, 0F, 1.902409F, 0F, 0F));

        root.addOrReplaceChild("shape12",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-0.5F, 0F, -0.5F, 1, 9, 1),
                PartPose.offsetAndRotation(0F, -10F, 0F, -1.902409F, 0F, 0F));

        root.addOrReplaceChild("shape111",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-0.5F, 0F, -0.5F, 1, 9, 1),
                PartPose.offsetAndRotation(0F, -10F, 0F, 1.902409F, 1.570796F, 0F));

        root.addOrReplaceChild("shape112",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-0.5F, 0F, -0.5F, 1, 9, 1),
                PartPose.offsetAndRotation(0F, -10F, 0F, 1.902409F, -1.570796F, 0F));

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(UmbrellaEntity umbrella, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public ModelPart root() {
        return root;
    }
}
