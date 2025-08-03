package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class BambooMugModel {
    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("base", CubeListBuilder.create().texOffs(10, 0).mirror().addBox(-2.0f, 23.0f, -2.0f, 4, 1, 4), PartPose.offset(0.0f, 0.0f, 0.0f));
        root.addOrReplaceChild("wall1", CubeListBuilder.create().texOffs(0, 10).mirror().addBox(-2.0f, 17.0f, -3.0f, 4, 6, 1), PartPose.offset(0.0f, 0.0f, 0.0f));
        root.addOrReplaceChild("wall2", CubeListBuilder.create().texOffs(0, 10).mirror().addBox(-2.0f, 17.0f, 2.0f, 4, 6, 1), PartPose.offset(0.0f, 0.0f, 0.0f));
        root.addOrReplaceChild("wall3", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(2.0f, 17.0f, -2.0f, 1, 6, 4), PartPose.offset(0.0f, 0.0f, 0.0f));
        root.addOrReplaceChild("wall4", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-3.0f, 17.0f, -2.0f, 1, 6, 4), PartPose.offset(0.0f, 0.0f, 0.0f));
        root.addOrReplaceChild("handletop", CubeListBuilder.create().texOffs(26, 0).mirror().addBox(-1.0f, 18.0f, -4.0f, 2, 1, 1), PartPose.offset(0.0f, 0.0f, 0.0f));
        root.addOrReplaceChild("handlebottom", CubeListBuilder.create().texOffs(26, 2).mirror().addBox(-1.0f, 21.0f, -4.0f, 2, 1, 1), PartPose.offset(0.0f, 0.0f, 0.0f));
        root.addOrReplaceChild("handle", CubeListBuilder.create().texOffs(32, 0).mirror().addBox(-1.0f, 19.0f, -5.0f, 2, 2, 1), PartPose.offset(0.0f, 0.0f, 0.0f));

        return LayerDefinition.create(mesh, 64, 32);
    }

    public static LayerDefinition createLiquid() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("liquid", CubeListBuilder.create().texOffs(10, 5).mirror().addBox(-2.0f, 18.0f, -2.0f, 4, 1, 4), PartPose.offset(0.0f, 0.0f, 0.0f));
        return LayerDefinition.create(mesh, 64, 32);
    }
}
