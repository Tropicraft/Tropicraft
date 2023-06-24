package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.tropicraft.core.common.entity.passive.TropiCreeperEntity;

public class TropiCreeperModel extends HierarchicalModel<TropiCreeperEntity> {
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart leg3;
    private final ModelPart leg4;
    private final ModelPart leg1;
    private final ModelPart leg2;

    public TropiCreeperModel(ModelPart root) {
        this.root = root;
        head = root.getChild("head");
        leg3 = root.getChild("leg3");
        leg4 = root.getChild("leg4");
        leg1 = root.getChild("leg1");
        leg2 = root.getChild("leg2");
    }

    public static LayerDefinition create() {

        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition modelPartHead = root.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-4F, -8F, -4F, 8, 8, 8),
                PartPose.offsetAndRotation(0F, 6F, 0F, 0F, 0F, 0F));

        root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(16, 16)
                        .addBox(-4F, 0F, -2F, 8, 12, 4),
                PartPose.offset(0F, 6F, 0F));

        root.addOrReplaceChild("leg3",
                CubeListBuilder.create().texOffs(0, 16)
                        .addBox(-2F, 2F, -2F, 4, 6, 4),
                PartPose.offset(-2F, 16, -4F));

        root.addOrReplaceChild("leg4",
                CubeListBuilder.create().texOffs(0, 16)
                        .addBox(-2F, 2F, -2F, 4, 6, 4),
                PartPose.offset(2.0F, 16, -4F));

        root.addOrReplaceChild("leg1",
                CubeListBuilder.create().texOffs(0, 16)
                        .addBox(-2F, 2F, -2F, 4, 6, 4),
                PartPose.offset(-2F, 16, 4F));

        root.addOrReplaceChild("leg2",
                CubeListBuilder.create().texOffs(0, 16)
                        .addBox(-2F, 2F, -2F, 4, 6, 4),
                PartPose.offset(2.0F, 16, 4F));

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

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(TropiCreeperEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        head.yRot = netHeadYaw * Mth.DEG_TO_RAD;
        head.xRot = headPitch * Mth.DEG_TO_RAD;
        leg1.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        leg2.xRot = Mth.cos(limbSwing * 0.6662F + Mth.PI) * 1.4F * limbSwingAmount;
        leg3.xRot = Mth.cos(limbSwing * 0.6662F + Mth.PI) * 1.4F * limbSwingAmount;
        leg4.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    }

    @Override
    public ModelPart root() {
        return root;
    }
}
