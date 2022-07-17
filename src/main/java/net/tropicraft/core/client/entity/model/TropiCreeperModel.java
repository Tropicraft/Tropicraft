package net.tropicraft.core.client.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.tropicraft.core.common.entity.passive.TropiCreeperEntity;

public class TropiCreeperModel extends ListModel<TropiCreeperEntity> {
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart leg3;
    private final ModelPart leg4;
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart hat1;
    private final ModelPart hat2;
    private final ModelPart hat3;

    public TropiCreeperModel(ModelPart root) {

        head = root.getChild("head");
        body = root.getChild("body");
        leg3 = root.getChild("leg3");
        leg4 = root.getChild("leg4");
        leg1 = root.getChild("leg1");
        leg2 = root.getChild("leg2");
        hat1 = head.getChild("hat1");
        hat2 = head.getChild("hat2");
        hat3 = head.getChild("hat3");

        /*
        int i = 4;
        head = new ModelPart(this, 0, 0);
        head.addCuboid(-4F, -8F, -4F, 8, 8, 8);
        head.setPivot(0F, 6F, 0F);
        head.pitch = 0F;
        head.yaw = 0F;
        head.roll = 0F;
        head.mirror = false;

        body = new ModelPart(this, 16, 16);
        body.addCuboid(-4F, 0F, -2F, 8, 12, 4);
        body.setPivot(0F, 6F, 0F);

        leg3 = new ModelPart(this, 0, 16);
        leg3.addCuboid(-2F, 0.0F, -2F, 4, 6, 4, 0);
        leg3.setPivot(-2F, 12 + i, -4F);

        leg4 = new ModelPart(this, 0, 16);
        leg4.addCuboid(-2F, 0.0F, -2F, 4, 6, 4, 0);
        leg4.setPivot(2.0F, 12 + i, -4F);

        leg1 = new ModelPart(this, 0, 16);
        leg1.addCuboid(-2F, 0.0F, -2F, 4, 6, 4, 0);
        leg1.setPivot(-2F, 12 + i, 4F);

        leg2 = new ModelPart(this, 0, 16);
        leg2.addCuboid(-2F, 0.0F, -2F, 4, 6, 4, 0);
        leg2.setPivot(2.0F, 12 + i, 4F);

        hat1 = new ModelPart(this, 24, 0);
        hat1.addCuboid(-5F, -6F, -5F, 12, 1, 6);
        hat1.setPivot(-1F, -3F, -1F);
        hat1.mirror = true;
        head.addChild(hat1);

        hat2 = new ModelPart(this, 40, 24);
        hat2.addCuboid(0F, -6F, 0F, 6, 2, 6);
        hat2.setPivot(-3F, -5F, -3F);
        hat2.mirror = false;
        head.addChild(hat2);

        hat3 = new ModelPart(this, 24, 0);
        hat3.addCuboid(-5F, -6F, 0F, 12, 1, 6);
        hat3.setPivot(-1F, -3F, 0F);
        hat3.mirror = false;
        head.addChild(hat3);
         */
    }

    public static LayerDefinition create() {
        int i = 4;

        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();

        PartDefinition modelPartHead = modelPartData.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-4F, -8F, -4F, 8, 8, 8),
                PartPose.offsetAndRotation(0F, 6F, 0F, 0F, 0F, 0F));

        modelPartData.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(16, 16)
                        .addBox(-4F, 0F, -2F, 8, 12, 4),
                PartPose.offset(0F, 6F, 0F));

        modelPartData.addOrReplaceChild("leg3",
                CubeListBuilder.create().texOffs(0, 16)
                        .addBox(-2F, 2F, -2F, 4, 6, 4),
                PartPose.offset(-2F, 12 + i, -4F));

        modelPartData.addOrReplaceChild("leg4",
                CubeListBuilder.create().texOffs(0, 16)
                        .addBox(-2F, 2F, -2F, 4, 6, 4),
                PartPose.offset(2.0F, 12 + i, -4F));

        modelPartData.addOrReplaceChild("leg1",
                CubeListBuilder.create().texOffs(0, 16)
                        .addBox(-2F, 2F, -2F, 4, 6, 4),
                PartPose.offset(-2F, 12 + i, 4F));

        modelPartData.addOrReplaceChild("leg2",
                CubeListBuilder.create().texOffs(0, 16)
                        .addBox(-2F, 2F, -2F, 4, 6, 4),
                PartPose.offset(2.0F, 12 + i, 4F));

        modelPartHead.addOrReplaceChild("hat1",
                CubeListBuilder.create().texOffs(24, 0).mirror()
                        .addBox(-5F, -6F, -5F, 12, 1, 6),
                PartPose.offset(-1F, -3F, -1F));

        modelPartHead.addOrReplaceChild("hat2",
                CubeListBuilder.create().texOffs(40, 24)
                        .addBox(0F, -6F, 0F, 6, 2, 6),
                PartPose.offset(-3F, -5F, -3F));

        modelPartHead.addOrReplaceChild("hat3",
                CubeListBuilder.create().texOffs(24, 0)
                        .addBox(-5F, -6F, 0F, 12, 1, 6),
                PartPose.offset(-1F, -3F, 0F));

        return LayerDefinition.create(modelData, 64, 32);
    }

    @Override
    public void setupAnim(TropiCreeperEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        head.yRot = netHeadYaw * ((float) Math.PI / 180F);
        head.xRot = headPitch * ((float) Math.PI / 180F);
        leg1.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        leg2.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        leg3.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        leg4.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    }

    @Override
    public Iterable<ModelPart> parts() {
        return ImmutableList.of(
            head, body, leg1, leg2, leg3, leg4
        );
    }
}