package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.tropicraft.core.client.entity.render.state.KoaRenderState;

public class KoaModel extends HumanoidModel<KoaRenderState> {
    private final ModelPart headband;
    private final ModelPart armbandR;
    private final ModelPart leaf1;
    private final ModelPart leaf3;
    private final ModelPart leaf2;
    private final ModelPart leaf4;
    private final ModelPart leaf5;
    private final ModelPart leaf6;
    private final ModelPart leaf7;
    private final ModelPart leaf8;
    private final ModelPart leaf9;
    private final ModelPart leaf10;
    private final ModelPart armbandL;

    public KoaModel(ModelPart root) {
        super(root);
        headband = head.getChild("headband");
        armbandR = rightArm.getChild("armbandR");
        armbandL = leftArm.getChild("armbandL");
        leaf1 = headband.getChild("leaf1");
        leaf3 = headband.getChild("leaf3");
        leaf2 = headband.getChild("leaf2");
        leaf4 = headband.getChild("leaf4");
        leaf5 = headband.getChild("leaf5");
        leaf6 = headband.getChild("leaf6");
        leaf7 = headband.getChild("leaf7");
        leaf8 = headband.getChild("leaf8");
        leaf9 = headband.getChild("leaf9");
        leaf10 = headband.getChild("leaf10");

        hat.visible = false;
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = createMesh(CubeDeformation.NONE, 0.0f);
        PartDefinition root = mesh.getRoot();

        PartDefinition modelPartHead = root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 2)
                        .mirror()
                        .addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8),
                PartPose.offset(0.0f, 0.0f, 0.0f));

        PartDefinition modelPartHeadBand = modelPartHead.addOrReplaceChild("headband",
                CubeListBuilder.create()
                        .texOffs(24, 1)
                        .mirror()
                        .addBox(-5.0f, 0.0f, -5.0f, 10, 2, 10),
                PartPose.offset(0.0f, -7.0f, 0.0f));

        PartDefinition modelPartRightArm = root.getChild("right_arm");
        modelPartRightArm.addOrReplaceChild("armbandR",
                CubeListBuilder.create()
                        .texOffs(35, 6)
                        .mirror()
                        .addBox(2.5f, -2.0f, -2.5f, 5, 1, 5),
                PartPose.offset(-6.0f, 3.0f, 0.0f));

        PartDefinition modelPartLeftArm = root.getChild("left_arm");
        modelPartLeftArm.addOrReplaceChild("armbandL",
                CubeListBuilder.create()
                        .texOffs(34, 1)
                        .mirror()
                        .addBox(-7.5f, -2.0f, -2.5f, 5, 1, 5),
                PartPose.offset(6.0f, 3.0f, 0.0f));

        modelPartHeadBand.addOrReplaceChild("leaf1",
                leafModelBuilder(),
                PartPose.offset(2.0f, -6.0f, -6.0f));
        modelPartHeadBand.addOrReplaceChild("leaf3",
                leafModelBuilder(),
                PartPose.offset(-1.0f, -6.0f, -6.0f));
        modelPartHeadBand.addOrReplaceChild("leaf2",
                leafModelBuilder(),
                PartPose.offset(-4.0f, -6.0f, -6.0f));
        modelPartHeadBand.addOrReplaceChild("leaf4",
                leafModelBuilder(),
                PartPose.offset(0.0f, -7.0f, -6.0f));
        modelPartHeadBand.addOrReplaceChild("leaf5",
                leafModelBuilder(),
                PartPose.offset(5.0f, -6.0f, -1.0f));
        modelPartHeadBand.addOrReplaceChild("leaf6",
                leafModelBuilder(),
                PartPose.offset(5.0f, -6.0f, 3.0f));
        modelPartHeadBand.addOrReplaceChild("leaf7",
                leafModelBuilder(),
                PartPose.offset(-6.0f, -6.0f, 0.0f));
        modelPartHeadBand.addOrReplaceChild("leaf8",
                leafModelBuilder(),
                PartPose.offset(-6.0f, -6.0f, -4.0f));
        modelPartHeadBand.addOrReplaceChild("leaf9",
                leafModelBuilder(),
                PartPose.offset(-2.0f, -6.0f, 5.0f));
        modelPartHeadBand.addOrReplaceChild("leaf10",
                leafModelBuilder(),
                PartPose.offset(2.0f, -6.0f, 5.0f));

        return LayerDefinition.create(mesh, 64, 32);
    }

    public static LayerDefinition createBaby() {
        return create().apply(ModelAnimator.hierarchicalBaby("head", 0.5f, 0.75f));
    }

    public static CubeListBuilder leafModelBuilder() {
        return CubeListBuilder.create()
                .texOffs(0, 0)
                .mirror()
                .addBox(0.0f, 7.0f, 0.0f, 1, 0, 1);
    }

    @Override
    public void setupAnim(KoaRenderState state) {
        boolean isPassenger = state.isPassenger;
        // Bit of a hack to force sitting pose
        state.isPassenger |= state.isSitting;
        super.setupAnim(state);
        state.isPassenger = isPassenger;

        hat.visible = false;

        if (state.isDancing) {
            float danceAnimation = state.ageInTicks * 35.0f * Mth.DEG_TO_RAD;
            float headRot = Mth.cos(danceAnimation);
            head.zRot = headRot * 0.05f;
            head.xRot += Mth.sin(danceAnimation) * 0.05f;

            float amp = 0.5f;
            float x = Mth.PI + Mth.PI / 4 + headRot * amp;
            float y = headRot * amp;
            float z = headRot * amp;

            rightArm.xRot += x;
            rightArm.yRot += y;
            rightArm.zRot += z;

            leftArm.xRot += x;
            leftArm.yRot += y;
            leftArm.zRot += z;
        }
    }
}
