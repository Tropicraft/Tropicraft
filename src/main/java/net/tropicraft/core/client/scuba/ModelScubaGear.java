package net.tropicraft.core.client.scuba;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.tropicraft.core.client.entity.model.ModelAnimator;

public class ModelScubaGear extends PlayerModel {
    public ModelScubaGear(ModelPart root) {
        super(root, false);
    }

    public static LayerDefinition createGoggles() {
        MeshDefinition mesh = PlayerModel.createMesh(CubeDeformation.NONE, false);
        PartDefinition root = mesh.getRoot();

        PartDefinition body = ModelAnimator.clearAllChildren(root, PartNames.BODY);
        ModelAnimator.clearAllChildren(root, PartNames.LEFT_ARM);
        ModelAnimator.clearAllChildren(root, PartNames.RIGHT_ARM);
        ModelAnimator.clearAllChildren(root, PartNames.LEFT_LEG);
        ModelAnimator.clearAllChildren(root, PartNames.RIGHT_LEG);
        addHeadParts(root, body);

        return LayerDefinition.create(mesh, 128, 64);
    }

    public static LayerDefinition createHarness() {
        MeshDefinition mesh = PlayerModel.createMesh(CubeDeformation.NONE, false);
        PartDefinition root = mesh.getRoot();

        ModelAnimator.clearAllChildren(root, PartNames.HEAD);
        ModelAnimator.clearAllChildren(root, PartNames.BODY);
        ModelAnimator.clearAllChildren(root, PartNames.LEFT_ARM);
        ModelAnimator.clearAllChildren(root, PartNames.RIGHT_ARM);
        ModelAnimator.clearAllChildren(root, PartNames.LEFT_LEG);
        ModelAnimator.clearAllChildren(root, PartNames.RIGHT_LEG);
        addChestParts(root);

        return LayerDefinition.create(mesh, 128, 64);
    }

    public static LayerDefinition createFlippers() {
        MeshDefinition mesh = PlayerModel.createMesh(CubeDeformation.NONE, false);
        PartDefinition root = mesh.getRoot();

        ModelAnimator.clearAllChildren(root, PartNames.HEAD);
        ModelAnimator.clearAllChildren(root, PartNames.BODY);
        ModelAnimator.clearAllChildren(root, PartNames.LEFT_ARM);
        ModelAnimator.clearAllChildren(root, PartNames.RIGHT_ARM);
        ModelAnimator.clearAllChildren(root, PartNames.LEFT_LEG);
        ModelAnimator.clearAllChildren(root, PartNames.RIGHT_LEG);
        addFootParts(root);

        return LayerDefinition.create(mesh, 128, 64);
    }

    public static LayerDefinition createTank() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        addTankParts(root);
        return LayerDefinition.create(mesh, 128, 64);
    }

    private static void addHeadParts(PartDefinition root, PartDefinition body) {
        PartDefinition head = root.addOrReplaceChild(PartNames.HEAD,
                CubeListBuilder.create().mirror()
                        .texOffs(0, 0)
                        .addBox(-4.0f, -4.0f, -4.0f, 8, 8, 8),
                PartPose.offsetAndRotation(0.0f, -4.0f, 0.0f, 0.0f, 0.0f, 0.0f));

        head.addOrReplaceChild("Mask",
                CubeListBuilder.create()
                        .texOffs(109, 60)
                        .addBox(-4.0f, -0.5f, -0.5f, 8, 1, 1),
                PartPose.offset(0.0f, -6.0f, -4.5f));

        head.addOrReplaceChild("Mask2",
                CubeListBuilder.create()
                        .texOffs(120, 55)
                        .addBox(-0.5f, -2.0f, -0.5f, 1, 4, 1),
                PartPose.offset(-4.0f, -4.0f, -4.5f));

        head.addOrReplaceChild("Mask3",
                CubeListBuilder.create()
                        .texOffs(116, 55)
                        .addBox(-0.5f, -2.0f, -0.5f, 1, 4, 1),
                PartPose.offset(4.0f, -4.0f, -4.5f));

        head.addOrReplaceChild("Mask4",
                CubeListBuilder.create()
                        .texOffs(114, 51)
                        .addBox(-1.5f, -0.5f, -0.5f, 3, 1, 1),
                PartPose.offset(-2.5f, -2.0f, -4.5f));

        head.addOrReplaceChild("Mask5",
                CubeListBuilder.create()
                        .texOffs(114, 53)
                        .addBox(-1.5f, -0.5f, -0.5f, 3, 1, 1),
                PartPose.offset(2.5f, -2.0f, -4.5f));

        head.addOrReplaceChild("Mask6",
                CubeListBuilder.create()
                        .texOffs(114, 49)
                        .addBox(-1.5f, -0.5f, -0.5f, 3, 1, 1),
                PartPose.offset(0.0f, -3.0f, -4.5f));

        head.addOrReplaceChild("Mask7",
                CubeListBuilder.create()
                        .texOffs(110, 38)
                        .addBox(-0.5f, -1.0f, -4.0f, 1, 2, 8),
                PartPose.offset(4.0f, -4.5f, 0.0f));

        head.addOrReplaceChild("Mask8",
                CubeListBuilder.create()
                        .texOffs(110, 38)
                        .addBox(-0.5f, -1.0f, -4.0f, 1, 2, 8),
                PartPose.offset(-4.0f, -4.5f, 0.0f));

        body.addOrReplaceChild("Mask9",
                CubeListBuilder.create()
                        .texOffs(110, 35)
                        .addBox(-4.0f, -1.0f, -0.5f, 8, 2, 1),
                PartPose.offset(0.0f, -4.5f, 4.0f));

        head.addOrReplaceChild("mouthpiece",
                CubeListBuilder.create()
                        .texOffs(115, 28)
                        .addBox(-1.5f, -1.5f, -0.5f, 3, 3, 1),
                PartPose.offset(0.0f, 0.0f, -5.0f));

        head.addOrReplaceChild("mouthpiece2",
                CubeListBuilder.create()
                        .texOffs(116, 25)
                        .addBox(-1.0f, -1.0f, -0.5f, 2, 2, 1),
                PartPose.offset(0.0f, 0.0f, -5.5f));

        head.addOrReplaceChild("mouthpiece3",
                CubeListBuilder.create()
                        .texOffs(116, 23)
                        .addBox(-1.0f, -0.5f, -0.5f, 2, 1, 1),
                PartPose.offset(0.0f, -0.6000004f, -4.0f));

        head.addOrReplaceChild("hose1",
                CubeListBuilder.create()
                        .texOffs(117, 16)
                        .addBox(-0.5f, -0.5f, -0.5f, 1, 1, 1),
                PartPose.offset(3.0f, -3.0f, 6.5f));

        head.addOrReplaceChild("hose2",
                CubeListBuilder.create()
                        .texOffs(117, 16)
                        .addBox(-1.5f, -0.5f, -0.5f, 3, 1, 1),
                PartPose.offset(5.0f, -3.0f, 6.5f));

        head.addOrReplaceChild("hose3",
                CubeListBuilder.create()
                        .texOffs(116, 15)
                        .addBox(-0.5f, -0.5f, -1.0f, 1, 1, 2),
                PartPose.offset(6.0f, -3.0f, 5.0f));

        head.addOrReplaceChild("hose4",
                CubeListBuilder.create()
                        .texOffs(106, 7)
                        .addBox(-0.5f, -0.5f, -10.0f, 1, 1, 10),
                PartPose.offsetAndRotation(6.0f, -3.0f, 4.2f, 17.62f * Mth.DEG_TO_RAD, 0.0f, 0.0f));

        head.addOrReplaceChild("hose5",
                CubeListBuilder.create()
                        .texOffs(115, 16)
                        .addBox(-2.5f, -0.5f, -0.5f, 5, 1, 1),
                PartPose.offset(4.0f, 0.0f, -5.0f));
    }

    private static void addChestParts(PartDefinition root) {
        PartDefinition body = root.addOrReplaceChild(PartNames.BODY,
                CubeListBuilder.create()
                        .texOffs(32, 16)
                        .addBox(-4.0f, 0.0f, -2.0f, 8, 12, 4),
                PartPose.ZERO);

        root.addOrReplaceChild(PartNames.RIGHT_ARM,
                CubeListBuilder.create()
                        .texOffs(56, 16)
                        .addBox(-4.0f, 0.0f, -2.0f, 4, 12, 4),
                PartPose.offset(-4.0f, 0.0f, 0.0f));

        root.addOrReplaceChild(PartNames.LEFT_ARM,
                CubeListBuilder.create()
                        .texOffs(72, 16)
                        .addBox(0.0f, 0.0f, -2.0f, 4, 12, 4),
                PartPose.offset(4.0f, 0.0f, 0.0f));

        body.addOrReplaceChild("BCD",
                CubeListBuilder.create()
                        .texOffs(65, 50)
                        .addBox(-4.0f, -6.0f, -1.0f, 8, 12, 2),
                PartPose.offset(0.0f, 6.5f, 3.0f));

        body.addOrReplaceChild("BCD12",
                CubeListBuilder.create()
                        .texOffs(102, 46)
                        .addBox(-0.5f, -0.5f, -0.5f, 1, 1, 1),
                PartPose.offset(0.0f, 10.0f, -2.7f));

        body.addOrReplaceChild("BCD11",
                CubeListBuilder.create()
                        .texOffs(79, 42)
                        .addBox(-0.5f, -0.5f, -2.0f, 1, 1, 4),
                PartPose.offset(3.6f, 3.0f, 0.0f));

        body.addOrReplaceChild("BCD4",
                CubeListBuilder.create()
                        .texOffs(97, 50)
                        .addBox(-1.0f, -5.5f, -0.5f, 2, 11, 1),
                PartPose.offset(3.0f, 5.5f, -2.5f));

        addTankParts(body);

        body.addOrReplaceChild("BCD2",
                CubeListBuilder.create()
                        .texOffs(66, 51)
                        .addBox(-3.5f, -5.0f, -0.5f, 7, 10, 1),
                PartPose.offset(0.0f, 6.5f, 4.0f));

        body.addOrReplaceChild("BCD6",
                CubeListBuilder.create()
                        .texOffs(68, 41)
                        .addBox(-0.5f, -1.0f, -2.0f, 1, 2, 4),
                PartPose.offset(-3.6f, 10.0f, 0.0f));

        body.addOrReplaceChild("BCD5",
                CubeListBuilder.create()
                        .texOffs(68, 41)
                        .addBox(-0.5f, -1.0f, -2.0f, 1, 2, 4),
                PartPose.offset(3.6f, 10.0f, 0.0f));

        body.addOrReplaceChild("BCD3",
                CubeListBuilder.create()
                        .texOffs(91, 50)
                        .addBox(-1.0f, -5.5f, -0.5f, 2, 11, 1),
                PartPose.offset(-3.0f, 5.5f, -2.5f));

        body.addOrReplaceChild("BCD7",
                CubeListBuilder.create()
                        .texOffs(91, 45)
                        .addBox(-2.0f, -1.0f, -0.5f, 4, 2, 1),
                PartPose.offset(0.0f, 10.0f, -2.5f));

        body.addOrReplaceChild("BCD8",
                CubeListBuilder.create()
                        .texOffs(91, 48)
                        .addBox(-2.0f, -0.5f, -0.5f, 4, 1, 1),
                PartPose.offset(0.0f, 3.0f, -2.5f));

        body.addOrReplaceChild("BCD9",
                CubeListBuilder.create()
                        .texOffs(79, 42)
                        .addBox(-0.5f, -0.5f, -2.0f, 1, 1, 4),
                PartPose.offset(-3.6f, 3.0f, 0.0f));

        body.addOrReplaceChild("BCD13",
                CubeListBuilder.create()
                        .texOffs(91, 38)
                        .addBox(-4.0f, -0.5f, -0.5f, 8, 1, 1),
                PartPose.offset(0.0f, 0.5f, 2.5f));

        body.addOrReplaceChild("hose6",
                CubeListBuilder.create()
                        .texOffs(115, 16)
                        .addBox(-1.5f, -0.5f, -0.5f, 3, 1, 1),
                PartPose.offset(0.0f, -0.5f, 6.5f));
    }

    private static void addTankParts(PartDefinition body) {
        body.addOrReplaceChild("Tank2",
                CubeListBuilder.create()
                        .texOffs(41, 50)
                        .addBox(-2.0f, -5.0f, -2.0f, 4, 10, 4),
                PartPose.offset(-3.0f, 7.0f, 6.5f));

        body.addOrReplaceChild("Tank2m1",
                CubeListBuilder.create()
                        .texOffs(45, 54)
                        .addBox(-1.5f, -4.5f, -0.5f, 3, 9, 1),
                PartPose.offset(-3.0f, 7.0f, 8.5f));

        body.addOrReplaceChild("Tank2m2",
                CubeListBuilder.create()
                        .texOffs(45, 54)
                        .addBox(-1.5f, -4.5f, -0.5f, 3, 9, 1),
                PartPose.offsetAndRotation(-5.0f, 7.0f, 6.5f, 0.0f, -Mth.HALF_PI, 0.0f));

        body.addOrReplaceChild("Tank2m3",
                CubeListBuilder.create()
                        .texOffs(45, 54)
                        .addBox(-1.5f, -4.5f, -0.5f, 3, 9, 1),
                PartPose.offsetAndRotation(-1.0f, 7.0f, 6.5f, 0.0f, -Mth.HALF_PI, 0.0f));

        body.addOrReplaceChild("Tank2m4",
                CubeListBuilder.create()
                        .texOffs(43, 46)
                        .addBox(-1.5f, -0.5f, -1.5f, 3, 1, 3),
                PartPose.offset(-3.0f, 1.5f, 6.5f));

        body.addOrReplaceChild("Tank2m5",
                CubeListBuilder.create()
                        .texOffs(38, 49)
                        .addBox(-0.5f, -2.0f, -0.5f, 1, 4, 1),
                PartPose.offset(-3.0f, -0.5f, 6.5f));

        body.addOrReplaceChild("Tank2m6",
                CubeListBuilder.create()
                        .texOffs(44, 44)
                        .addBox(-2.0f, -0.5f, -0.5f, 4, 1, 1),
                PartPose.offset(-3.5f, -0.5f, 6.5f));

        body.addOrReplaceChild("Tank2m7",
                CubeListBuilder.create()
                        .texOffs(36, 44)
                        .addBox(-1.0f, -1.0f, -1.0f, 2, 2, 2),
                PartPose.offset(-5.5f, -0.5f, 6.5f));

        body.addOrReplaceChild("Tank1",
                CubeListBuilder.create()
                        .texOffs(41, 50)
                        .addBox(-2.0f, -5.0f, -2.0f, 4, 10, 4),
                PartPose.offset(3.0f, 7.0f, 6.5f));

        body.addOrReplaceChild("Tank1m1",
                CubeListBuilder.create()
                        .texOffs(45, 54)
                        .addBox(-1.5f, -4.5f, -0.5f, 3, 9, 1),
                PartPose.offset(3.0f, 7.0f, 8.5f));

        body.addOrReplaceChild("Tank1m2",
                CubeListBuilder.create()
                        .texOffs(45, 54)
                        .addBox(-1.5f, -4.5f, -0.5f, 3, 9, 1),
                PartPose.offsetAndRotation(1.0f, 7.0f, 6.5f, 0.0f, -Mth.HALF_PI, 0.0f));

        body.addOrReplaceChild("Tank1m3",
                CubeListBuilder.create()
                        .texOffs(45, 54)
                        .addBox(-1.5f, -4.5f, -0.5f, 3, 9, 1),
                PartPose.offsetAndRotation(5.0f, 7.0f, 6.5f, 0.0f, -Mth.HALF_PI, 0.0f));

        body.addOrReplaceChild("Tank1m4",
                CubeListBuilder.create()
                        .texOffs(43, 46)
                        .addBox(-1.5f, -0.5f, -1.5f, 3, 1, 3),
                PartPose.offset(3.0f, 1.5f, 6.5f));

        body.addOrReplaceChild("Tank1m5",
                CubeListBuilder.create()
                        .texOffs(38, 49)
                        .addBox(-0.5f, -2.0f, -0.5f, 1, 4, 1),
                PartPose.offset(3.0f, -0.5f, 6.5f));

        body.addOrReplaceChild("Tank1m6",
                CubeListBuilder.create()
                        .texOffs(44, 44)
                        .addBox(-2.0f, -0.5f, -0.5f, 4, 1, 1),
                PartPose.offset(3.5f, -0.5f, 6.5f));

        body.addOrReplaceChild("Tank1m7",
                CubeListBuilder.create()
                        .texOffs(36, 44)
                        .addBox(-1.0f, -1.0f, -1.0f, 2, 2, 2),
                PartPose.offset(5.5f, -0.5f, 6.5f));
    }

    private static void addFootParts(PartDefinition root) {
        PartDefinition rightLeg = root.addOrReplaceChild(PartNames.RIGHT_LEG,
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .addBox(-2.0f, -6.0f, -2.0f, 4, 12, 4),
                PartPose.offset(-2.0f, 18.0f, 0.0f));

        PartDefinition rightFin = rightLeg.addOrReplaceChild("Fin1",
                CubeListBuilder.create().mirror()
                        .texOffs(10, 38)
                        .addBox(-5.0f, 22.0f, -2.5f, 5, 2, 5),
                PartPose.offset(2.5f, -12.0f, 0.0f));

        rightFin.addOrReplaceChild("Fin1m1",
                CubeListBuilder.create().mirror()
                        .texOffs(13, 47)
                        .addBox(-2.5f, -1.5f, -1.0f, 5, 1, 2),
                PartPose.offset(-3.19707f, 24.5f, -3.288924f));

        rightFin.addOrReplaceChild("Fin1m2",
                CubeListBuilder.create().mirror()
                        .texOffs(15, 45)
                        .addBox(-2.0f, -1.5f, -0.5f, 4, 1, 1),
                PartPose.offset(-3.02606f, 23.5f, -2.819078f));

        rightFin.addOrReplaceChild("Fin1m3",
                CubeListBuilder.create().mirror()
                        .texOffs(1, 52)
                        .addBox(-5.0f, -1.0f, -6.0f, 10, 0, 12),
                PartPose.offset(-5.420201f, 24.5f, -9.396926f));

        rightFin.addOrReplaceChild("Fin1m4",
                CubeListBuilder.create().mirror()
                        .texOffs(15, 50)
                        .addBox(-2.0f, -1.5f, -0.5f, 4, 1, 1),
                PartPose.offset(-3.710101f, 24.5f, -4.698463f));

        PartDefinition leftLeg = root.addOrReplaceChild(PartNames.LEFT_LEG,
                CubeListBuilder.create()
                        .texOffs(16, 16)
                        .addBox(-2.0f, -6.0f, -2.0f, 4, 12, 4),
                PartPose.offset(2.0f, 18.0f, 0.0f));

        PartDefinition leftFin = leftLeg.addOrReplaceChild("Fin2",
                CubeListBuilder.create().mirror()
                        .texOffs(10, 38)
                        .addBox(0.0f, 22.0f, -2.5f, 5, 2, 5),
                PartPose.offset(-2.0f, -12.0f, 0.0f));

        leftFin.addOrReplaceChild("Fin2m1",
                CubeListBuilder.create().mirror()
                        .texOffs(13, 47)
                        .addBox(-2.5f, -1.5f, -1.0f, 5, 1, 2),
                PartPose.offset(3.19707f, 24.5f, -3.288924f));

        leftFin.addOrReplaceChild("Fin2m2",
                CubeListBuilder.create().mirror()
                        .texOffs(15, 45)
                        .addBox(-2.0f, -1.5f, -0.5f, 4, 1, 1),
                PartPose.offset(3.02606f, 23.5f, -2.819078f));

        leftFin.addOrReplaceChild("Fin2m3",
                CubeListBuilder.create().mirror()
                        .texOffs(1, 52)
                        .addBox(-5.0f, -1.0f, -6.0f, 10, 0, 12),
                PartPose.offset(5.420201f, 24.5f, -9.396926f));

        leftFin.addOrReplaceChild("Fin2m4",
                CubeListBuilder.create().mirror()
                        .texOffs(15, 50)
                        .addBox(-2.0f, -1.5f, -0.5f, 4, 1, 1),
                PartPose.offset(3.710101f, 24.5f, -4.698463f));
    }
}
