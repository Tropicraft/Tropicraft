package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.tropicraft.core.common.entity.underdasea.TropicraftDolphinEntity;

public class TropicraftDolphinModel extends HierarchicalModel<TropicraftDolphinEntity> {
    private final ModelPart root;
    private final ModelPart lowerJaw3;
    private final ModelPart lowerJaw4;
    private final ModelPart lowerJaw5;
    private final ModelPart upperJaw2;
    private final ModelPart upperJaw4;
    private final ModelPart upperJaw5;
    private final ModelPart head2;
    private final ModelPart head3;
    private final ModelPart head4;
    private final ModelPart head5;
    private final ModelPart body3;
    private final ModelPart rightPectoralFin1;
    private final ModelPart rightPectoralFin2;
    private final ModelPart rightPectoralFin3;
    private final ModelPart leftPectoralFin1;
    private final ModelPart leftPectoralFin2;
    private final ModelPart leftPectoralFin3;
    private final ModelPart tail1;
    private final ModelPart tail2;
    private final ModelPart tail3;
    private final ModelPart tail4;
    private final ModelPart fluke1;
    private final ModelPart fluke2;
    private final ModelPart fluke3;
    private final ModelPart fluke4;
    private final ModelPart fluke5;
    private final ModelPart fluke6;
    private final ModelPart fluke7;
    private final ModelPart fluke8;
    private final ModelPart dorsalFin1;
    private final ModelPart dorsalFin2;
    private final ModelPart dorsalFin3;
    private final ModelPart dorsalFin4;
    private final ModelPart dorsalFin5;

    public TropicraftDolphinModel(ModelPart root) {
        this.root = root;
        lowerJaw3 = root.getChild("lowerJaw3");
        lowerJaw4 = root.getChild("lowerJaw4");
        lowerJaw5 = root.getChild("lowerJaw5");
        upperJaw2 = root.getChild("upperJaw2");
        upperJaw4 = root.getChild("upperJaw4");
        upperJaw5 = root.getChild("upperJaw5");
        head2 = root.getChild("head2");
        head3 = root.getChild("head3");
        head4 = root.getChild("head4");
        head5 = root.getChild("head5");
        body3 = root.getChild("body3");
        rightPectoralFin1 = root.getChild("rightPectoralFin1");
        rightPectoralFin2 = root.getChild("rightPectoralFin2");
        rightPectoralFin3 = root.getChild("rightPectoralFin3");
        leftPectoralFin1 = root.getChild("leftPectoralFin1");
        leftPectoralFin2 = root.getChild("leftPectoralFin2");
        leftPectoralFin3 = root.getChild("leftPectoralFin3");
        tail1 = root.getChild("tail1");
        tail2 = root.getChild("tail2");
        tail3 = root.getChild("tail3");
        tail4 = root.getChild("tail4");
        fluke1 = root.getChild("fluke1");
        fluke2 = root.getChild("fluke2");
        fluke3 = root.getChild("fluke3");
        fluke4 = root.getChild("fluke4");
        fluke5 = root.getChild("fluke5");
        fluke6 = root.getChild("fluke6");
        fluke7 = root.getChild("fluke7");
        fluke8 = root.getChild("fluke8");
        dorsalFin1 = root.getChild("dorsalFin1");
        dorsalFin2 = root.getChild("dorsalFin2");
        dorsalFin3 = root.getChild("dorsalFin3");
        dorsalFin4 = root.getChild("dorsalFin4");
        dorsalFin5 = root.getChild("dorsalFin5");
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("body1",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-3.0f, -3.0f, -3.0f, 6, 6, 8),
                PartPose.offset(0.0f, 20.0f, 0.0f));

        root.addOrReplaceChild("body2",
                CubeListBuilder.create().texOffs(0, 14)
                        .addBox(-3.0f, -2.0f, -5.0f, 6, 5, 4),
                PartPose.offset(0.0f, 19.8f, -2.0f));

        root.addOrReplaceChild("head1",
                CubeListBuilder.create().texOffs(0, 57)
                        .addBox(-2.5f, -3.0f, -3.0f, 5, 4, 3),
                PartPose.offset(0.0f, 21.4f, -6.3f));

        root.addOrReplaceChild("lowerJaw1",
                CubeListBuilder.create().texOffs(16, 61)
                        .addBox(-2.5f, 0.0f, -1.0f, 5, 2, 1),
                PartPose.offset(0.0f, 20.4f, -9.3f));

        root.addOrReplaceChild("lowerJaw2",
                CubeListBuilder.create().texOffs(29, 60)
                        .addBox(-2.0f, 0.0f, -3.0f, 4, 1, 3),
                PartPose.offset(0.0f, 21.4f, -10.3f));

        root.addOrReplaceChild("lowerJaw3",
                CubeListBuilder.create().texOffs(29, 54)
                        .addBox(-2.0f, 0.0f, -3.0f, 4, 1, 3),
                PartPose.offset(0.0f, 20.4f, -10.3f));

        root.addOrReplaceChild("lowerJaw4",
                CubeListBuilder.create().texOffs(44, 61)
                        .addBox(-1.5f, 0.0f, -2.0f, 3, 1, 2),
                PartPose.offset(0.0f, 21.4f, -13.3f));

        root.addOrReplaceChild("lowerJaw5",
                CubeListBuilder.create().texOffs(45, 56)
                        .addBox(-1.5f, -1.0f, -1.0f, 3, 1, 1),
                PartPose.offset(0.0f, 22.4f, -15.3f));

        root.addOrReplaceChild("upperJaw1",
                CubeListBuilder.create().texOffs(52, 0)
                        .addBox(-2.5f, 0.0f, -1.0f, 5, 1, 1),
                PartPose.offset(0.0f, 19.4f, -9.3f));

        root.addOrReplaceChild("upperJaw2",
                CubeListBuilder.create().texOffs(50, 3)
                        .addBox(-2.0f, 0.0f, -3.0f, 4, 1, 3),
                PartPose.offset(0.0f, 19.4f, -10.3f));

        root.addOrReplaceChild("upperJaw3",
                CubeListBuilder.create().texOffs(54, 8)
                        .addBox(-1.5f, -1.0f, -2.0f, 3, 1, 2),
                PartPose.offset(0.0f, 21.36575f, -12.77706f));

        root.addOrReplaceChild("upperJaw4",
                CubeListBuilder.create().texOffs(58, 12)
                        .addBox(-1.0f, -1.0f, -1.0f, 2, 1, 1),
                PartPose.offset(0.0f, 21.36575f, -14.77706f));

        root.addOrReplaceChild("upperJaw5",
                CubeListBuilder.create().texOffs(52, 15)
                        .addBox(-1.0f, 0.0f, -4.0f, 2, 1, 4),
                PartPose.offset(0.0f, 19.74202f, -11.23969f));

        root.addOrReplaceChild("head2",
                CubeListBuilder.create().texOffs(0, 49)
                        .addBox(-2.0f, -1.0f, -4.0f, 4, 2, 4),
                PartPose.offset(0.0f, 18.4f, -6.3f));

        root.addOrReplaceChild("head3",
                CubeListBuilder.create().texOffs(14, 49)
                        .addBox(-1.5f, 0.0f, -1.0f, 3, 2, 1),
                PartPose.offset(0.0f, 17.99005f, -10.40267f));

        root.addOrReplaceChild("head4",
                CubeListBuilder.create().texOffs(24, 49)
                        .addBox(-1.5f, 0.0f, -1.0f, 3, 2, 1),
                PartPose.offset(0.0f, 18.43765f, -11.29691f));

        root.addOrReplaceChild("head5",
                CubeListBuilder.create().texOffs(34, 49)
                        .addBox(-1.5f, 0.0f, -1.0f, 3, 1, 1),
                PartPose.offset(0.0f, 19.10989f, -12.03724f));

        root.addOrReplaceChild("body3",
                CubeListBuilder.create().texOffs(20, 14)
                        .addBox(-2.5f, 0.0f, -4.3f, 5, 1, 5),
                PartPose.offset(0.0f, 17.1f, -2.5f));

        root.addOrReplaceChild("rightPectoralFin1",
                CubeListBuilder.create().texOffs(0, 37)
                        .addBox(-3.0f, 0.0f, 0.0f, 3, 1, 3),
                PartPose.offset(-3.0f, 21.3f, -5.0f));

        root.addOrReplaceChild("rightPectoralFin2",
                CubeListBuilder.create().texOffs(0, 41)
                        .addBox(-1.0f, 0.0f, 0.0f, 1, 1, 2),
                PartPose.offset(-5.104775f, 22.85859f, -3.227792f));

        root.addOrReplaceChild("rightPectoralFin3",
                CubeListBuilder.create().texOffs(8, 42)
                        .addBox(-1.0f, 0.0f, 0.0f, 1, 1, 1),
                PartPose.offset(-5.521684f, 23.16731f, -1.912163f));

        root.addOrReplaceChild("leftPectoralFin1",
                CubeListBuilder.create().texOffs(0, 37)
                        .addBox(0.0f, 0.0f, 0.0f, 3, 1, 3),
                PartPose.offset(3.0f, 21.3f, -5.0f));

        root.addOrReplaceChild("leftPectoralFin2",
                CubeListBuilder.create().texOffs(0, 41)
                        .addBox(3, -0.1f, 0.5f, 1, 1, 2),
                PartPose.offset(3.0f, 21.3f, -5.0f));

        root.addOrReplaceChild("leftPectoralFin3",
                CubeListBuilder.create().texOffs(8, 42)
                        .addBox(4, -0.15f, 0.5f, 1, 1, 1),
                PartPose.offset(3.0f, 21.3f, -5.0f));

        root.addOrReplaceChild("tail1",
                CubeListBuilder.create().texOffs(0, 24)
                        .addBox(-2.5f, -2.5f, -1.0f, 5, 5, 7),
                PartPose.offset(0.0f, 19.8f, 5.1f));

        root.addOrReplaceChild("tail2",
                CubeListBuilder.create().texOffs(24, 27)
                        .addBox(-2.0f, -2.0f, -1.0f, 4, 4, 5),
                PartPose.offset(0.0f, 20.07322f, 11.09378f));

        root.addOrReplaceChild("tail3",
                CubeListBuilder.create().texOffs(40, 24)
                        .addBox(-1.5f, -1.5f, -1.0f, 3, 3, 4),
                PartPose.offset(0.0f, 20.8163f, 15.02924f));

        root.addOrReplaceChild("tail4",
                CubeListBuilder.create().texOffs(27, 30)
                        .addBox(-1.0f, -1.0f, 0.0f, 2, 2, 3),
                PartPose.offset(0.0f, 21.49112f, 17.43644f));

        root.addOrReplaceChild("fluke1",
                CubeListBuilder.create().texOffs(44, 34)
                        .addBox(-3.0f, 0.0f, 0.0f, 6, 1, 1),
                PartPose.offset(0.0f, 22.1683f, 19.21166f));

        root.addOrReplaceChild("fluke2",
                CubeListBuilder.create().texOffs(43, 38)
                        .addBox(-4.5f, 0.0f, 0.0f, 9, 1, 1),
                PartPose.offset(0.0f, 22.25945f, 20.2075f));

        root.addOrReplaceChild("fluke3",
                CubeListBuilder.create().texOffs(30, 38)
                        .addBox(-5.0f, 0.0f, -1.0f, 5, 1, 1),
                PartPose.offset(4.9f, 22.44176f, 22.19917f));

        root.addOrReplaceChild("fluke4",
                CubeListBuilder.create().texOffs(14, 38)
                        .addBox(-5.0f, 0.0f, 0.0f, 6, 1, 1),
                PartPose.offset(4.9f, 22.44176f, 22.19917f));

        root.addOrReplaceChild("fluke5",
                CubeListBuilder.create().texOffs(30, 38)
                        .addBox(0.0f, 0.0f, -1.0f, 5, 1, 1),
                PartPose.offset(-4.9f, 22.44176f, 22.19917f));

        root.addOrReplaceChild("fluke6",
                CubeListBuilder.create().texOffs(14, 38)
                        .addBox(-1.0f, 0.0f, 0.0f, 6, 1, 1),
                PartPose.offset(-4.9f, 22.44176f, 22.19917f));

        root.addOrReplaceChild("fluke7",
                CubeListBuilder.create().texOffs(55, 30)
                        .addBox(-3.0f, 0.0f, 0.0f, 3, 1, 1),
                PartPose.offset(0.0f, 22.43265f, 22.09959f));

        root.addOrReplaceChild("fluke8",
                CubeListBuilder.create().texOffs(55, 30)
                        .addBox(0.0f, 0.0f, 0.0f, 3, 1, 1),
                PartPose.offset(0.0f, 22.43265f, 22.09959f));

        root.addOrReplaceChild("dorsalFin1",
                CubeListBuilder.create().texOffs(21, 0)
                        .addBox(-0.5f, -1.0f, -0.7f, 1, 1, 5),
                PartPose.offset(0.0f, 17.1f, 0.0f));

        root.addOrReplaceChild("dorsalFin2",
                CubeListBuilder.create().texOffs(35, 0)
                        .addBox(-0.5f, -1.0f, 0.0f, 1, 1, 4),
                PartPose.offset(0.0f, 16.10415f, 0.09098025f));

        root.addOrReplaceChild("dorsalFin3",
                CubeListBuilder.create().texOffs(30, 7)
                        .addBox(-0.5f, -1.0f, 0.0f, 1, 1, 3),
                PartPose.offset(0.0f, 15.30191f, 1.255631f));

        root.addOrReplaceChild("dorsalFin4",
                CubeListBuilder.create().texOffs(39, 7)
                        .addBox(-0.5f, -1.0f, 0.0f, 1, 1, 2),
                PartPose.offset(0.0f, 14.60895f, 2.48844f));

        root.addOrReplaceChild("dorsalFin5",
                CubeListBuilder.create().texOffs(45, 0)
                        .addBox(-0.5f, -1.0f, 0.0f, 1, 1, 1),
                PartPose.offset(0.0f, 14.15063f, 3.826327f));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public ModelPart root() {
        return root;
    }

    @Override
    public void setupAnim(TropicraftDolphinEntity dolphin, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        boolean mouthOpen = dolphin.getMouthOpen();

        float tailVertSpeed = 1.0f;
        float tailHorzSpeed;
        if (dolphin.isInWater()) {
            tailVertSpeed = 0.5f / 2;
            tailHorzSpeed = 0.25f / 2;
            if (dolphin.getAirSupply() <= 0) {
                tailVertSpeed = 0.5f;
                tailHorzSpeed = 0.25f;
            }
        } else {
            if (dolphin.onGround()) {
                tailVertSpeed = 0.0f;
                tailHorzSpeed = 0.05f;
            } else {
                tailHorzSpeed = 0.5f;
            }
        }

        lowerJaw3.xRot = 0.3490658f;
        if (mouthOpen) {
            lowerJaw5.setPos(0.0f, 23.4f, -15.3f + 0.52f);
            lowerJaw4.xRot = 0.5f;
        } else {
            lowerJaw5.setPos(0.0f, 22.4f, -15.3f);
            lowerJaw4.xRot = 0.0f;
        }

        lowerJaw5.xRot = -0.2275909f;
        upperJaw2.xRot = 0.3490658f;
        upperJaw4.xRot = -0.09110618f;
        upperJaw5.xRot = 0.15132f;
        head2.xRot = 0.1453859f;
        head3.xRot = 0.4640831f;
        head4.xRot = 0.737227f;
        head5.xRot = 1.055924f;
        body3.xRot = 0.04555309f;

        rightPectoralFin1.xRot = 0.1612329f;
        rightPectoralFin1.yRot = 0.2214468f;
        rightPectoralFin1.zRot = -0.6194302f + Mth.sin(ageInTicks * 0.025f) * 0.3f;

        rightPectoralFin2.xRot = 0.2393862f;
        rightPectoralFin2.yRot = 0.3358756f;
        rightPectoralFin2.zRot = -0.5966207f + Mth.sin(ageInTicks * 0.025f) * 0.45f;

        rightPectoralFin3.xRot = 0.3620028f;
        rightPectoralFin3.yRot = 0.5368112f;
        rightPectoralFin3.zRot = -0.5368112f + Mth.sin(ageInTicks * 0.025f) * 0.5f;

        leftPectoralFin1.xRot = 0.1612329f;
        leftPectoralFin1.yRot = -0.2214468f;
        leftPectoralFin1.zRot = 0.6194302f + Mth.sin(ageInTicks * 0.025f) * 0.3f;

        leftPectoralFin2.xRot = 0.2393862f;
        leftPectoralFin2.yRot = -0.3358756f;
        leftPectoralFin2.zRot = 0.5966207f + Mth.sin(ageInTicks * 0.025f) * 0.35f;

        leftPectoralFin3.xRot = 0.3620028f;
        leftPectoralFin3.yRot = -0.5368112f;
        leftPectoralFin3.zRot = 0.5368112f + Mth.sin(ageInTicks * 0.025f) * 0.4f;

        tail1.xRot = -0.04555309f + Mth.sin(ageInTicks * tailVertSpeed) * 0.1f;
        tail1.yRot = Mth.sin(ageInTicks * tailHorzSpeed) * 0.135f;
        tail1.zRot = 0.0f;

        tail2.x = Mth.sin(ageInTicks * tailHorzSpeed) * 1;

        tail2.y = 20 - Mth.sin(ageInTicks * tailVertSpeed) * 0.8f;

        tail2.xRot = -0.1366593f + Mth.sin(ageInTicks * tailVertSpeed) * 0.1f;
        tail2.yRot = Mth.sin(ageInTicks * tailHorzSpeed) * 0.135f;
        tail2.zRot = 0.0f;

        tail3.x = Mth.sin(ageInTicks * tailHorzSpeed) * 1.85f;

        tail3.y = 20.5f - Mth.sin(ageInTicks * tailVertSpeed) * 1.5f;

        tail3.xRot = -0.2733185f + Mth.sin(ageInTicks * tailVertSpeed) * 0.2f;
        tail3.yRot = Mth.sin(ageInTicks * tailHorzSpeed) * 0.135f;
        tail3.zRot = 0.0f;

        tail4.x = Mth.sin(ageInTicks * tailHorzSpeed) * 2.4f;
        tail4.y = 21.5f - Mth.sin(ageInTicks * tailVertSpeed) * 2.5f;

        tail4.xRot = -0.3644247f + Mth.sin(ageInTicks * tailVertSpeed) * 0.5f;
        tail4.yRot = Mth.sin(ageInTicks * tailHorzSpeed) * 0.35f;
        tail4.zRot = 0.0f;

        fluke1.x = Mth.sin(ageInTicks * tailHorzSpeed) * 2.8f;
        fluke1.y = 22.0f - Mth.sin(ageInTicks * tailVertSpeed) * 4.0f;

        fluke1.xRot = -0.09128072f;
        fluke1.yRot = Mth.sin(ageInTicks * tailHorzSpeed) * 0.35f;
        fluke1.zRot = 0.0f;

        fluke2.y = 22.0f - Mth.sin(ageInTicks * tailVertSpeed) * 4.0f;

        fluke2.x = Mth.sin(ageInTicks * tailHorzSpeed) * 2.8f;

        fluke2.xRot = -0.09128071f;
        fluke2.yRot = Mth.sin(ageInTicks * tailHorzSpeed) * 0.35f;
        fluke2.zRot = 0.0f;

        fluke3.x = Mth.sin(ageInTicks * tailHorzSpeed) * 2.8f;
        fluke3.y = 22.0f - Mth.sin(ageInTicks * tailVertSpeed) * 4.0f;

        fluke3.xRot = -0.09118575f;
        fluke3.yRot = -0.04574326f + Mth.sin(ageInTicks * tailHorzSpeed) * 0.35f;
        fluke3.zRot = 0.00416824f;

        fluke4.y = 22.0f - Mth.sin(ageInTicks * tailVertSpeed) * 4.0f;

        fluke4.x = Mth.sin(ageInTicks * tailHorzSpeed) * 2.8f;

        fluke4.xRot = -0.08892051f + Mth.sin(ageInTicks * tailVertSpeed) * 0.8f;

        fluke4.yRot = -0.2285096f + Mth.sin(ageInTicks * tailHorzSpeed) * 0.35f;
        fluke4.zRot = 0.02065023f;

        fluke5.x = Mth.sin(ageInTicks * tailHorzSpeed) * 2.8f;
        fluke5.y = 22.0f - Mth.sin(ageInTicks * tailVertSpeed) * 4.0f;

        fluke5.xRot = -0.09118575f;
        fluke5.yRot = 0.04574326f + Mth.sin(ageInTicks * tailHorzSpeed) * 0.35f;
        fluke5.zRot = -0.00416824f;

        fluke6.x = Mth.sin(ageInTicks * tailHorzSpeed) * 2.8f;
        fluke6.y = 22.0f - Mth.sin(ageInTicks * tailVertSpeed) * 4.0f;

        fluke6.xRot = -0.08892051f + Mth.sin(ageInTicks * tailVertSpeed) * 0.8f;
        fluke6.yRot = 0.2285096f + Mth.sin(ageInTicks * tailHorzSpeed) * 0.35f;
        fluke6.zRot = -0.02065023f;

        fluke7.x = Mth.sin(ageInTicks * tailHorzSpeed) * 2.8f;
        fluke7.y = 22.0f - Mth.sin(ageInTicks * tailVertSpeed) * 4.0f;

        fluke7.xRot = -0.09042732f + Mth.sin(ageInTicks * tailVertSpeed) * 0.8f;
        fluke7.yRot = -0.1372235f + Mth.sin(ageInTicks * tailHorzSpeed) * 0.35f;
        fluke7.zRot = 0.01246957f;

        fluke8.x = Mth.sin(ageInTicks * tailHorzSpeed) * 2.8f;
        fluke8.y = 22.0f - Mth.sin(ageInTicks * tailVertSpeed) * 4.0f;

        fluke8.xRot = -0.09042732f + Mth.sin(ageInTicks * tailVertSpeed) * 0.8f;

        fluke8.yRot = 0.1372235f + Mth.sin(ageInTicks * tailHorzSpeed) * 0.35f;
        fluke8.zRot = -0.01246957f;

        dorsalFin1.xRot = -0.09110619f;
        dorsalFin2.xRot = -0.1822124f;
        dorsalFin3.xRot = -0.2733186f;
        dorsalFin4.xRot = -0.4553564f;
        dorsalFin5.xRot = -0.7285004f;
    }
}
