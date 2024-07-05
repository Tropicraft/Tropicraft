package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
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
                        .addBox(-0.5f, 0.0f, -0.5f, 1, 14, 1),
                PartPose.offset(0.0f, -13.0f, 0.0f));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-7.5f, -2.0f, -7.5f, 15, 1, 15),
                PartPose.offset(0.0f, -12.0f, 0.0f));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create().texOffs(25, 25)
                        .addBox(-1.0f, -1.0f, -1.0f, 2, 1, 2),
                PartPose.offset(0.0f, -14.0f, 0.0f));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create().texOffs(0, 20)
                        .addBox(-4.0f, -1.0f, 0.0f, 9, 1, 3),
                PartPose.offsetAndRotation(-0.5f, -13.0f, 7.5f, -0.2443461f, 0.0f, 0.0f));

        root.addOrReplaceChild("shape31",
                CubeListBuilder.create().texOffs(0, 24)
                        .addBox(-4.5f, -1.0f, 0.0f, 9, 1, 3),
                PartPose.offsetAndRotation(7.5f, -13.0f, 0.0f, -0.2443461f, Mth.HALF_PI, 0.0f));

        root.addOrReplaceChild("shape32",
                CubeListBuilder.create().texOffs(0, 28)
                        .addBox(-4.5f, -1.0f, -1.0f, 9, 1, 3),
                PartPose.offsetAndRotation(0.0f, -12.75f, -8.45f, -0.2443461f, Mth.PI, 0.0f));

        root.addOrReplaceChild("shape33",
                CubeListBuilder.create().texOffs(24, 28)
                        .addBox(-4.5f, -1.0f, 1.0f, 9, 1, 3),
                PartPose.offsetAndRotation(-6.5f, -13.25f, 0.0f, -0.2443461f, -Mth.HALF_PI, 0.0f));

        root.addOrReplaceChild("shape11",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-0.5f, 0.0f, -0.5f, 1, 9, 1),
                PartPose.offsetAndRotation(0.0f, -10.0f, 0.0f, 1.902409f, 0.0f, 0.0f));

        root.addOrReplaceChild("shape12",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-0.5f, 0.0f, -0.5f, 1, 9, 1),
                PartPose.offsetAndRotation(0.0f, -10.0f, 0.0f, -1.902409f, 0.0f, 0.0f));

        root.addOrReplaceChild("shape111",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-0.5f, 0.0f, -0.5f, 1, 9, 1),
                PartPose.offsetAndRotation(0.0f, -10.0f, 0.0f, 1.902409f, Mth.HALF_PI, 0.0f));

        root.addOrReplaceChild("shape112",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-0.5f, 0.0f, -0.5f, 1, 9, 1),
                PartPose.offsetAndRotation(0.0f, -10.0f, 0.0f, 1.902409f, -Mth.HALF_PI, 0.0f));

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
