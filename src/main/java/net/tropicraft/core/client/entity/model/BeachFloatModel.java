package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.util.Mth;

public class BeachFloatModel extends EntityModel<EntityRenderState> {
    public BeachFloatModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("floatCross4", CubeListBuilder.create().texOffs(0, 0).addBox(0.0f, -1.0f, -1.0f, 16, 2, 2), PartPose.offset(0.0f, 23.0f, -6.0f));
        root.addOrReplaceChild("floatCross3", CubeListBuilder.create().texOffs(0, 0).addBox(0.0f, -1.0f, -1.0f, 16, 2, 2), PartPose.offset(0.0f, 23.0f, -2.0f));
        root.addOrReplaceChild("floatCross2", CubeListBuilder.create().texOffs(0, 0).addBox(0.0f, -1.0f, -1.0f, 16, 2, 2), PartPose.offset(0.0f, 23.0f, 2.0f));
        root.addOrReplaceChild("floatCross1", CubeListBuilder.create().texOffs(0, 0).addBox(0.0f, -1.0f, -1.0f, 16, 2, 2), PartPose.offset(0.0f, 23.0f, 6.0f));
        root.addOrReplaceChild("topFloatCross4", CubeListBuilder.create().texOffs(0, 0).addBox(0.0f, -1.0f, -1.0f, 16, 2, 2), PartPose.offsetAndRotation(0.0f, 23.0f, -6.0f, 0.0f, 0.0f, Mth.PI));
        root.addOrReplaceChild("topFloatCross3", CubeListBuilder.create().texOffs(0, 0).addBox(0.0f, -1.0f, -1.0f, 16, 2, 2), PartPose.offsetAndRotation(0.0f, 23.0f, -2.0f, 0.0f, 0.0f, Mth.PI));
        root.addOrReplaceChild("topFloatCross2", CubeListBuilder.create().texOffs(0, 0).addBox(0.0f, 0.0f, 1.0f, 16, 2, 2), PartPose.offsetAndRotation(0.0f, 24.0f, 0.0f, 0.0f, 0.0f, Mth.PI));
        root.addOrReplaceChild("topFloatCross1", CubeListBuilder.create().texOffs(0, 0).addBox(0.0f, -1.0f, -1.0f, 16, 2, 2), PartPose.offsetAndRotation(0.0f, 23.0f, 6.0f, 0.0f, 0.0f, Mth.PI));
        root.addOrReplaceChild("floatFoot", CubeListBuilder.create().texOffs(0, 4).addBox(-7.0f, -1.0f, 0.0f, 14, 2, 2), PartPose.offsetAndRotation(16.0f, 23.0f, 0.0f, 0.0f, Mth.HALF_PI, 0.0f));
        root.addOrReplaceChild("floatTop", CubeListBuilder.create().texOffs(0, 4).addBox(-7.0f, -1.0f, 0.0f, 14, 2, 2), PartPose.offsetAndRotation(-17.0f, 24.0f, 0.0f, Mth.HALF_PI, -Mth.HALF_PI, 0.0f));
        root.addOrReplaceChild("headPillow", CubeListBuilder.create().texOffs(0, 13).addBox(-6.0f, -1.5f, -4.0f, 12, 2, 4), PartPose.offsetAndRotation(-12.0f, 22.0f, 0.0f, 0.0f, Mth.HALF_PI, 0.0f));
        root.addOrReplaceChild("topBed", CubeListBuilder.create().texOffs(0, 19).addBox(-6.0f, -0.5f, -6.0f, 14, 1, 12), PartPose.offset(-6.0f, 22.0f, 0.0f));
        root.addOrReplaceChild("bottomBed", CubeListBuilder.create().texOffs(0, 19).addBox(-6.0f, -0.5f, -6.0f, 14, 1, 12), PartPose.offset(8.0f, 22.0f, 0.0f));

        return LayerDefinition.create(mesh.transformed(pose -> PartPose.rotation(0.0f, -Mth.HALF_PI, 0.0f)), 64, 32);
    }
}
