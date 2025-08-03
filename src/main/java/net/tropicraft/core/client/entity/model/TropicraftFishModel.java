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

public class TropicraftFishModel extends EntityModel<EntityRenderState> {
    public final ModelPart tail;

    public TropicraftFishModel(ModelPart root) {
        super(root);
        ModelPart body = root.getChild("body");
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
    public void setupAnim(EntityRenderState state) {
        super.setupAnim(state);
        tail.yRot = Mth.sin(state.ageInTicks * 0.25f) * 0.25f;
    }
}
