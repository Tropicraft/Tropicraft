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
                        .addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8),
                PartPose.offsetAndRotation(0.0f, 6.0f, 0.0f, 0.0f, 0.0f, 0.0f));

        root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(16, 16)
                        .addBox(-4.0f, 0.0f, -2.0f, 8, 12, 4),
                PartPose.offset(0.0f, 6.0f, 0.0f));

        root.addOrReplaceChild("leg3",
                CubeListBuilder.create().texOffs(0, 16)
                        .addBox(-2.0f, 2.0f, -2.0f, 4, 6, 4),
                PartPose.offset(-2.0f, 16, -4.0f));

        root.addOrReplaceChild("leg4",
                CubeListBuilder.create().texOffs(0, 16)
                        .addBox(-2.0f, 2.0f, -2.0f, 4, 6, 4),
                PartPose.offset(2.0f, 16, -4.0f));

        root.addOrReplaceChild("leg1",
                CubeListBuilder.create().texOffs(0, 16)
                        .addBox(-2.0f, 2.0f, -2.0f, 4, 6, 4),
                PartPose.offset(-2.0f, 16, 4.0f));

        root.addOrReplaceChild("leg2",
                CubeListBuilder.create().texOffs(0, 16)
                        .addBox(-2.0f, 2.0f, -2.0f, 4, 6, 4),
                PartPose.offset(2.0f, 16, 4.0f));

        modelPartHead.addOrReplaceChild("hat1",
                CubeListBuilder.create().texOffs(24, 0).mirror()
                        .addBox(-5.0f, -6.0f, -5.0f, 12, 1, 6),
                PartPose.offset(-1.0f, -3.0f, -1.0f));

        modelPartHead.addOrReplaceChild("hat2",
                CubeListBuilder.create().texOffs(40, 24)
                        .addBox(0.0f, -6.0f, 0.0f, 6, 2, 6),
                PartPose.offset(-3.0f, -5.0f, -3.0f));

        modelPartHead.addOrReplaceChild("hat3",
                CubeListBuilder.create().texOffs(24, 0)
                        .addBox(-5.0f, -6.0f, 0.0f, 12, 1, 6),
                PartPose.offset(-1.0f, -3.0f, 0.0f));

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(TropiCreeperEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        ModelAnimator.look(head, netHeadYaw, headPitch);
        leg1.xRot = Mth.cos(limbSwing * 0.6662f) * 1.4f * limbSwingAmount;
        leg2.xRot = Mth.cos(limbSwing * 0.6662f + Mth.PI) * 1.4f * limbSwingAmount;
        leg3.xRot = Mth.cos(limbSwing * 0.6662f + Mth.PI) * 1.4f * limbSwingAmount;
        leg4.xRot = Mth.cos(limbSwing * 0.6662f) * 1.4f * limbSwingAmount;
    }

    @Override
    public ModelPart root() {
        return root;
    }
}
