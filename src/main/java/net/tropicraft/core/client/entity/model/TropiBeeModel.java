package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.BeeModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.tropicraft.core.common.entity.TropiBeeEntity;

public class TropiBeeModel extends BeeModel<TropiBeeEntity> {
    private final ModelPart hat1;
    private final ModelPart hat2;
    private final ModelPart hat3;

    public TropiBeeModel(ModelPart root) {
        super(root);

        ModelPart body = getBody();

        hat1 = body.getChild("hat1");
        hat2 = body.getChild("hat2");
        hat3 = body.getChild("hat3");
        /*
        hat1 = new ModelPart(this, 0, 32);
        hat1.addCuboid(-5F, -6F, -5F, 12, 1, 6);
        hat1.setPivot(-1F, 1F, -1F);
        hat1.mirror = true;
        ModelPart body = getBody();
        body.addChild(hat1);
        hat2 = new ModelPart(this, 0, 48);
        hat2.addCuboid(0F, -6F, 0F, 6, 2, 6);
        hat2.setPivot(-3F, -1F, -3F);
        hat2.mirror = false;
        body.addChild(hat2);
        hat3 = new ModelPart(this, 0, 32);
        hat3.addCuboid(-5F, -6F, 0F, 12, 1, 6);
        hat3.setPivot(-1F, 1F, 0F);
        hat3.mirror = false;
        body.addChild(hat3);
         */
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition modelData = new MeshDefinition();

        PartDefinition modelPartData = modelData.getRoot();

        PartDefinition modelPartData2 = modelPartData.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, 19.0F, 0.0F));
        PartDefinition modelPartData3 = modelPartData2.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -4.0F, -5.0F, 7.0F, 7.0F, 10.0F), PartPose.ZERO);
        modelPartData3.addOrReplaceChild("stinger", CubeListBuilder.create().texOffs(26, 7).addBox(0.0F, -1.0F, 5.0F, 0.0F, 1.0F, 2.0F), PartPose.ZERO);
        modelPartData3.addOrReplaceChild("left_antenna", CubeListBuilder.create().texOffs(2, 0).addBox(1.5F, -2.0F, -3.0F, 1.0F, 2.0F, 3.0F), PartPose.offset(0.0F, -2.0F, -5.0F));
        modelPartData3.addOrReplaceChild("right_antenna", CubeListBuilder.create().texOffs(2, 3).addBox(-2.5F, -2.0F, -3.0F, 1.0F, 2.0F, 3.0F), PartPose.offset(0.0F, -2.0F, -5.0F));
        CubeDeformation dilation = new CubeDeformation(0.001F);
        modelPartData2.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(0, 18).addBox(-9.0F, 0.0F, 0.0F, 9.0F, 0.0F, 6.0F, dilation), PartPose.offsetAndRotation(-1.5F, -4.0F, -3.0F, 0.0F, -0.2618F, 0.0F));
        modelPartData2.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(0, 18).mirror().addBox(0.0F, 0.0F, 0.0F, 9.0F, 0.0F, 6.0F, dilation), PartPose.offsetAndRotation(1.5F, -4.0F, -3.0F, 0.0F, 0.2618F, 0.0F));
        modelPartData2.addOrReplaceChild("front_legs", CubeListBuilder.create().addBox("front_legs", -5.0F, 0.0F, 0.0F, 7, 2, 0, 26, 1), PartPose.offset(1.5F, 3.0F, -2.0F));
        modelPartData2.addOrReplaceChild("middle_legs", CubeListBuilder.create().addBox("middle_legs", -5.0F, 0.0F, 0.0F, 7, 2, 0, 26, 3), PartPose.offset(1.5F, 3.0F, 0.0F));
        modelPartData2.addOrReplaceChild("back_legs", CubeListBuilder.create().addBox("back_legs", -5.0F, 0.0F, 0.0F, 7, 2, 0, 26, 5), PartPose.offset(1.5F, 3.0F, 2.0F));

        modelPartData3.addOrReplaceChild("hat1",
                CubeListBuilder.create().texOffs(0, 32).mirror()
                        .addBox(-5F, -6F, -5F, 12, 1, 6),
                PartPose.offset(-1F, 1F, -1F));

        modelPartData3.addOrReplaceChild("hat2",
                CubeListBuilder.create().texOffs(0, 48)
                        .addBox(0F, -6F, 0F, 6, 2, 6),
                PartPose.offset(-3F, -1F, -3F));

        modelPartData3.addOrReplaceChild("hat3",
                CubeListBuilder.create().texOffs(0, 32)
                        .addBox(-5F, -6F, 0F, 12, 1, 6),
                PartPose.offset(-1F, 1F, 0F));

        return LayerDefinition.create(modelData, 64, 64);
    }

    //TODO: CHECK IF THIS IS WORKING PROPERLY AS BEFORE IT MAY HAVE BEEN CORRECT
    public ModelPart getBody() {
        ModelPart bone = null;
        for (ModelPart b : bodyParts()) {
            bone = b;
            break;
        }
        return bone.getChild("body");
    }
}
