package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.tropicraft.core.common.entity.egg.EggEntity;

public class EggModel extends HierarchicalModel<EggEntity> {
    private final ModelPart root;
    private final ModelPart body;

    public EggModel(ModelPart root) {
        this.root = root;
        body = root.getChild("body");
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("body", CubeListBuilder.create().mirror(true)
                .texOffs(0, 16)
                .addBox(-3.0f, -10.0f, -3.0f, 6, 10, 6)
                .texOffs(0, 0)
                .addBox(-1.5f, -11.0f, -1.5f, 3, 1, 3)
                .texOffs(0, 7)
                .addBox(3.0f, -7.0f, -1.5f, 1, 6, 3)
                .texOffs(24, 9)
                .addBox(-1.5f, -7.0f, 3.0f, 3, 6, 1)
                .texOffs(16, 7)
                .addBox(-4.0f, -7.0f, -1.5f, 1, 6, 3)
                .texOffs(8, 9)
                .addBox(-1.5f, -7.0f, -4.0f, 3, 6, 1), PartPose.offset(0.0f, 24.0f, 0.0f));
        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(EggEntity egg, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        boolean hatching = egg.isNearHatching();
        float randRotator = (float) egg.rotationRand;
        body.yRot = 0.0f;
        if (hatching) {
            body.yRot = Mth.sin(ageInTicks * 0.6f) * 0.6f;
            body.xRot = Mth.sin(randRotator * 4.0f) * 0.6f;
            body.zRot = Mth.cos(randRotator * 4.0f) * 0.6f;
        } else {
            body.xRot = 0.0f;
            body.zRot = 0.0f;
        }
    }

    @Override
    public ModelPart root() {
        return root;
    }
}
