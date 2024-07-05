package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.BeeModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.tropicraft.core.common.entity.TropiBeeEntity;

public class TropiBeeModel extends BeeModel<TropiBeeEntity> {
    private final ModelPart body;

    public TropiBeeModel(ModelPart root) {
        super(root);
        body = root.getChild("bone").getChild("body");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition bone = root.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0f, 19.0f, 0.0f));
        PartDefinition body = bone.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5f, -4.0f, -5.0f, 7.0f, 7.0f, 10.0f), PartPose.ZERO);
        body.addOrReplaceChild("stinger", CubeListBuilder.create().texOffs(26, 7).addBox(0.0f, -1.0f, 5.0f, 0.0f, 1.0f, 2.0f), PartPose.ZERO);
        body.addOrReplaceChild("left_antenna", CubeListBuilder.create().texOffs(2, 0).addBox(1.5f, -2.0f, -3.0f, 1.0f, 2.0f, 3.0f), PartPose.offset(0.0f, -2.0f, -5.0f));
        body.addOrReplaceChild("right_antenna", CubeListBuilder.create().texOffs(2, 3).addBox(-2.5f, -2.0f, -3.0f, 1.0f, 2.0f, 3.0f), PartPose.offset(0.0f, -2.0f, -5.0f));
        CubeDeformation dilation = new CubeDeformation(0.001f);
        bone.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(0, 18).addBox(-9.0f, 0.0f, 0.0f, 9.0f, 0.0f, 6.0f, dilation), PartPose.offsetAndRotation(-1.5f, -4.0f, -3.0f, 0.0f, -0.2618f, 0.0f));
        bone.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(0, 18).mirror().addBox(0.0f, 0.0f, 0.0f, 9.0f, 0.0f, 6.0f, dilation), PartPose.offsetAndRotation(1.5f, -4.0f, -3.0f, 0.0f, 0.2618f, 0.0f));
        bone.addOrReplaceChild("front_legs", CubeListBuilder.create().addBox("front_legs", -5.0f, 0.0f, 0.0f, 7, 2, 0, 26, 1), PartPose.offset(1.5f, 3.0f, -2.0f));
        bone.addOrReplaceChild("middle_legs", CubeListBuilder.create().addBox("middle_legs", -5.0f, 0.0f, 0.0f, 7, 2, 0, 26, 3), PartPose.offset(1.5f, 3.0f, 0.0f));
        bone.addOrReplaceChild("back_legs", CubeListBuilder.create().addBox("back_legs", -5.0f, 0.0f, 0.0f, 7, 2, 0, 26, 5), PartPose.offset(1.5f, 3.0f, 2.0f));

        body.addOrReplaceChild("hat1",
                CubeListBuilder.create().texOffs(0, 32).mirror()
                        .addBox(-5.0f, -6.0f, -5.0f, 12, 1, 6),
                PartPose.offset(-1.0f, 1.0f, -1.0f));

        body.addOrReplaceChild("hat2",
                CubeListBuilder.create().texOffs(0, 48)
                        .addBox(0.0f, -6.0f, 0.0f, 6, 2, 6),
                PartPose.offset(-3.0f, -1.0f, -3.0f));

        body.addOrReplaceChild("hat3",
                CubeListBuilder.create().texOffs(0, 32)
                        .addBox(-5.0f, -6.0f, 0.0f, 12, 1, 6),
                PartPose.offset(-1.0f, 1.0f, 0.0f));

        return LayerDefinition.create(mesh, 64, 64);
    }

    public ModelPart body() {
        return body;
    }
}
