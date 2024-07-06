package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class EIHMachineModel extends MachineModel {
    public EIHMachineModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("base",
                CubeListBuilder.create()
                        .texOffs(0, 44)
                        .mirror()
                        .addBox(-8.0f, -1.0f, -8.0f, 16, 3, 16),
                PartPose.offset(0.0f, 22.0f, 0.0f));

        root.addOrReplaceChild("back",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .mirror()
                        .addBox(-3.0f, -15.0f, -8.0f, 6, 25, 16),
                PartPose.offset(5.0f, 11.0f, 0.0f));

        root.addOrReplaceChild("nose",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .mirror()
                        .addBox(-1.0f, -7.0f, -2.0f, 2, 14, 4),
                PartPose.offset(1.0f, 8.0f, 0.0f));

        root.addOrReplaceChild("forehead",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .mirror()
                        .addBox(-1.0f, -1.0f, -8.0f, 3, 5, 16),
                PartPose.offset(0.0f, -3.0f, 0.0f));

        root.addOrReplaceChild("leftEye",
                CubeListBuilder.create()
                        .texOffs(1, 35)
                        .mirror()
                        .addBox(0.0f, -1.0f, -3.0f, 1, 4, 6),
                PartPose.offset(1.0f, 2.0f, 5.0f));

        root.addOrReplaceChild("rightEye",
                CubeListBuilder.create()
                        .texOffs(1, 35)
                        .mirror()
                        .addBox(0.0f, -1.0f, -3.0f, 1, 4, 6),
                PartPose.offset(1.0f, 2.0f, -5.0f));

        root.addOrReplaceChild("basinNearBack",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .mirror()
                        .addBox(-1.0f, 0.0f, -4.0f, 1, 1, 8),
                PartPose.offset(2.0f, 20.0f, 0.0f));

        root.addOrReplaceChild("basinSide1",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .mirror()
                        .addBox(-5.0f, 0.0f, -2.0f, 10, 1, 4),
                PartPose.offset(-3.0f, 20.0f, 6.0f));

        root.addOrReplaceChild("basinSide2",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .mirror()
                        .addBox(-5.0f, 0.0f, -2.0f, 10, 1, 4),
                PartPose.offset(-3.0f, 20.0f, -6.0f));

        root.addOrReplaceChild("basinNearFront",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .mirror()
                        .addBox(-1.0f, 0.0f, -4.0f, 2, 1, 8),
                PartPose.offset(-7.0f, 20.0f, 0.0f));

        root.addOrReplaceChild("basinCorner1",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .mirror()
                        .addBox(0.0f, 0.0f, 0.0f, 1, 1, 1),
                PartPose.offset(0.0f, 20.0f, 3.0f));

        root.addOrReplaceChild("basinCorner2",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .mirror()
                        .addBox(0.0f, 0.0f, 0.0f, 1, 1, 1),
                PartPose.offset(0.0f, 20.0f, -4.0f));

        root.addOrReplaceChild("basinCorner3",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .mirror()
                        .addBox(0.0f, 0.0f, 0.0f, 1, 1, 1),
                PartPose.offset(-6.0f, 20.0f, 3.0f));

        root.addOrReplaceChild("basinCorner4",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .mirror()
                        .addBox(0.0f, 0.0f, 0.0f, 1, 1, 1),
                PartPose.offset(-6.0f, 20.0f, -4.0f));

        root.addOrReplaceChild("lidBase",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .mirror()
                        .addBox(-4.0f, 0.0f, -8.0f, 9, 1, 16),
                PartPose.offset(3.0f, -5.0f, 0.0f));

        root.addOrReplaceChild("lidTop",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .mirror()
                        .addBox(-2.0f, 0.0f, -2.0f, 4, 1, 4),
                PartPose.offset(3.0f, -6.0f, 0.0f));

        root.addOrReplaceChild("mouth",
                CubeListBuilder.create()
                        .texOffs(54, 0)
                        .mirror()
                        .addBox(0.0f, -1.0f, -2.0f, 1, 3, 4),
                PartPose.offset(1.0f, 16.0f, 0.0f));

        return LayerDefinition.create(mesh, 64, 64);
    }
}
