package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.AbstractFish;

public class TropicraftFishModel<T extends AbstractFish> extends HierarchicalModel<T> {
    private final ModelPart body;
    public final ModelPart tail;

    public TropicraftFishModel(ModelPart root) {
        body = root.getChild("body");
        tail = body.getChild("tail");
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().addBox(0, 0, 0, 0, 1, 1), PartPose.offset(0.0f, 16.0f, 0.0f));
        body.addOrReplaceChild("tail", CubeListBuilder.create().addBox(0, 0, 0, 0, 1, 1), PartPose.offset(0.0f, 0.0f, -1.0f));
        return LayerDefinition.create(mesh, 0, 0);
    }

    @Override
    public ModelPart root() {
        return body;
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        tail.yRot = Mth.sin(ageInTicks * 0.25f) * 0.25f;
    }
}
