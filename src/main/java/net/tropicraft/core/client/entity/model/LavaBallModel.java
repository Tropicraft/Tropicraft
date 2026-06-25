package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.tropicraft.core.common.entity.projectile.LavaBallEntity;

import java.util.List;

public class LavaBallModel extends ListModel<LavaBallEntity> {

    private final ModelPart base;
    private final ModelPart top;
    private final ModelPart front;
    private final ModelPart left;
    private final ModelPart back;
    private final ModelPart right;
    private final ModelPart bottom;


    public LavaBallModel(ModelPart root) {
        base = root.getChild("base");
        top = root.getChild("top");
        front = root.getChild("front");
        left = root.getChild("left");
        back = root.getChild("back");
        right = root.getChild("right");
        bottom = root.getChild("bottom");
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("base", CubeListBuilder.create().mirror().texOffs(0, 0).addBox(-3.0f, 16.0f, -3.0f, 6,
                6, 6), PartPose.offset(0.0f, 0.0f, 0.0f));

        root.addOrReplaceChild("top", CubeListBuilder.create().mirror().texOffs(0, 38).addBox(-2.0f, 15.0f, -2.0f, 4,
                1, 4), PartPose.offset(0.0f, 0.0f, 0.0f));

        root.addOrReplaceChild("front", CubeListBuilder.create().mirror().texOffs(0, 12).addBox(-2.0f, 17.0f, -4.0f,
                4, 4, 1), PartPose.offset(0.0f, 0.0f, 0.0f));

        root.addOrReplaceChild("left", CubeListBuilder.create().mirror().texOffs(0, 17).addBox(3.0f, 17.0f, -2.0f, 1,
                4, 4), PartPose.offset(0.0f, 0.0f, 0.0f));

        root.addOrReplaceChild("back", CubeListBuilder.create().mirror().texOffs(0, 25).addBox(-2.0f, 17.0f, 3.0f, 4,
                4, 1), PartPose.offset(0.0f, 0.0f, 0.0f));

        root.addOrReplaceChild("right", CubeListBuilder.create().mirror().texOffs(0, 30).addBox(-4.0f, 17.0f, -2.0f,
                1, 4, 4), PartPose.offset(0.0f, 0.0f, 0.0f));

        root.addOrReplaceChild("bottom", CubeListBuilder.create().mirror().texOffs(0, 38).addBox(-2.0f, 22.0f, -2.0f,
                4, 1, 4), PartPose.offset(0.0f, 0.0f, 0.0f));

        return LayerDefinition.create(mesh, 32, 64);
    }

    @Override
    public Iterable<ModelPart> parts() {
        return List.of(base, top, front, left, back, right, bottom);
    }

    @Override
    public void setupAnim(LavaBallEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks,
                          float netHeadYaw, float headPitch) {
    }
}
