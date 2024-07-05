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

        PartDefinition bone = root.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, 19.0F, 0.0F));
        PartDefinition body = bone.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -4.0F, -5.0F, 7.0F, 7.0F, 10.0F), PartPose.ZERO);
        body.addOrReplaceChild("stinger", CubeListBuilder.create().texOffs(26, 7).addBox(0.0F, -1.0F, 5.0F, 0.0F, 1.0F, 2.0F), PartPose.ZERO);
        body.addOrReplaceChild("left_antenna", CubeListBuilder.create().texOffs(2, 0).addBox(1.5F, -2.0F, -3.0F, 1.0F, 2.0F, 3.0F), PartPose.offset(0.0F, -2.0F, -5.0F));
        body.addOrReplaceChild("right_antenna", CubeListBuilder.create().texOffs(2, 3).addBox(-2.5F, -2.0F, -3.0F, 1.0F, 2.0F, 3.0F), PartPose.offset(0.0F, -2.0F, -5.0F));
        CubeDeformation dilation = new CubeDeformation(0.001F);
        bone.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(0, 18).addBox(-9.0F, 0.0F, 0.0F, 9.0F, 0.0F, 6.0F, dilation), PartPose.offsetAndRotation(-1.5F, -4.0F, -3.0F, 0.0F, -0.2618F, 0.0F));
        bone.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(0, 18).mirror().addBox(0.0F, 0.0F, 0.0F, 9.0F, 0.0F, 6.0F, dilation), PartPose.offsetAndRotation(1.5F, -4.0F, -3.0F, 0.0F, 0.2618F, 0.0F));
        bone.addOrReplaceChild("front_legs", CubeListBuilder.create().addBox("front_legs", -5.0F, 0.0F, 0.0F, 7, 2, 0, 26, 1), PartPose.offset(1.5F, 3.0F, -2.0F));
        bone.addOrReplaceChild("middle_legs", CubeListBuilder.create().addBox("middle_legs", -5.0F, 0.0F, 0.0F, 7, 2, 0, 26, 3), PartPose.offset(1.5F, 3.0F, 0.0F));
        bone.addOrReplaceChild("back_legs", CubeListBuilder.create().addBox("back_legs", -5.0F, 0.0F, 0.0F, 7, 2, 0, 26, 5), PartPose.offset(1.5F, 3.0F, 2.0F));

        body.addOrReplaceChild("hat1",
                CubeListBuilder.create().texOffs(0, 32).mirror()
                        .addBox(-5F, -6F, -5F, 12, 1, 6),
                PartPose.offset(-1F, 1F, -1F));

        body.addOrReplaceChild("hat2",
                CubeListBuilder.create().texOffs(0, 48)
                        .addBox(0F, -6F, 0F, 6, 2, 6),
                PartPose.offset(-3F, -1F, -3F));

        body.addOrReplaceChild("hat3",
                CubeListBuilder.create().texOffs(0, 32)
                        .addBox(-5F, -6F, 0F, 12, 1, 6),
                PartPose.offset(-1F, 1F, 0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    public ModelPart body() {
        return body;
    }
}
