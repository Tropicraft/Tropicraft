package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.Entity;

public class FiddlerCrabModel<T extends Entity> extends HierarchicalModel<T> {
    private final ModelPart body_base;
    private final ModelPart eyestalk_right;
    private final ModelPart eyestalk_left;
    private final ModelPart claw_right_a;
    private final ModelPart claw_right_a_r1;
    private final ModelPart claw_left_a;
    private final ModelPart claw_left_c;
    private final ModelPart claw_left_c_r1;
    private final ModelPart claw_left_b;
    private final ModelPart leg_left_fra;
    private final ModelPart leg_left_frb;
    private final ModelPart leg_left_mia;
    private final ModelPart leg_left_mib;
    private final ModelPart leg_left_baa;
    private final ModelPart leg_left_bab;
    private final ModelPart leg_right_fra;
    private final ModelPart leg_right_frb;
    private final ModelPart leg_right_mia;
    private final ModelPart leg_right_mib;
    private final ModelPart leg_right_baa;
    private final ModelPart leg_right_bab;

    public FiddlerCrabModel(ModelPart root) {
        body_base = root.getChild("body_base");
        eyestalk_right = body_base.getChild("eyestalk_right");
        eyestalk_left = body_base.getChild("eyestalk_left");

        claw_right_a = body_base.getChild("claw_right_a");
        claw_right_a_r1 = claw_right_a.getChild("claw_right_a_r1");

        claw_left_a = body_base.getChild("claw_left_a");
        claw_left_c = claw_left_a.getChild("claw_left_c");
        claw_left_c_r1 = claw_left_c.getChild("claw_left_c_r1");

        claw_left_b = claw_left_a.getChild("claw_left_b");

        leg_left_fra = body_base.getChild("leg_left_fra");
        leg_left_frb = leg_left_fra.getChild("leg_left_frb");

        leg_left_mia = body_base.getChild("leg_left_mia");
        leg_left_mib = leg_left_mia.getChild("leg_left_mib");

        leg_left_baa = body_base.getChild("leg_left_baa");
        leg_left_bab = leg_left_baa.getChild("leg_left_bab");

        leg_right_fra = body_base.getChild("leg_right_fra");
        leg_right_frb = leg_right_fra.getChild("leg_right_frb");

        leg_right_mia = body_base.getChild("leg_right_mia");
        leg_right_mib = leg_right_mia.getChild("leg_right_mib");

        leg_right_baa = body_base.getChild("leg_right_baa");
        leg_right_bab = leg_right_baa.getChild("leg_right_bab");
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition body = root.addOrReplaceChild("body_base",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-2.0F, -2.0F, -2.0F, 4.0F, 2.0F, 3.0F, false),
                PartPose.offsetAndRotation(0.0F, 23.0F, 0.0F, -7.5F, 0.0F, 0.0F));

        body.addOrReplaceChild("eyestalk_right",
                CubeListBuilder.create()
                        .texOffs(0, 21)
                        .addBox(0.0F, -2.0F, 0.0F, 1.0F, 2.0F, 0.0F, false),
                PartPose.offsetAndRotation(-1.5F, -1.0F, -2.0F, 7.5F, 0.0F, 0.0F));

        body.addOrReplaceChild("eyestalk_left",
                CubeListBuilder.create()
                        .texOffs(3, 21)
                        .addBox(-1.0F, -2.0F, 0.0F, 1.0F, 2.0F, 0.0F, false),
                PartPose.offsetAndRotation(1.5F, -1.0F, -2.0F, 7.5F, 0.0F, 0.0F));

        PartDefinition modelPartClawRightA = body.addOrReplaceChild("claw_right_a",
                CubeListBuilder.create(),
                PartPose.offset(-2.0F, -0.5F, -2.0F));

        modelPartClawRightA.addOrReplaceChild("claw_right_a_r1",
                CubeListBuilder.create()
                        .texOffs(7, 6)
                        .addBox(-0.75F, -0.5F, -1.0F, 2.0F, 1.0F, 1.0F, false),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 5.0F, 12.5F));

        PartDefinition modelPartClawLeftA = body.addOrReplaceChild("claw_left_a",
                CubeListBuilder.create()
                        .texOffs(0, 6)
                        .addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 1.0F, false),
                PartPose.offsetAndRotation(2.0F, -0.5F, -2.0F, 0.0F, -17.5F, -7.5F));

        PartDefinition modelPartClawLeftC = modelPartClawLeftA.addOrReplaceChild("claw_left_c",
                CubeListBuilder.create(),
                PartPose.offset(-1.0F, 0.0F, 0.0F));

        modelPartClawLeftC.addOrReplaceChild("claw_left_c_r1",
                CubeListBuilder.create()
                        .texOffs(14, 6)
                        .addBox(-2.0F, 0.0F, -0.99F, 2.0F, 1.0F, 1.0F, false),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -10.0F));

        modelPartClawLeftA.addOrReplaceChild("claw_left_b",
                CubeListBuilder.create()
                        .texOffs(15, 0)
                        .addBox(-3.0F, 0.0F, -1.0F, 3.0F, 1.0F, 1.0F, false),
                PartPose.offset(-1.0F, -1.0F, 0.0F));

        PartDefinition modelPartLegLeftFra = body.addOrReplaceChild("leg_left_fra",
                CubeListBuilder.create()
                        .texOffs(15, 17)
                        .addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, false),
                PartPose.offsetAndRotation(1.5F, 0.0F, -1.5F, -25.0F, -7.5F, -122.5F));

        modelPartLegLeftFra.addOrReplaceChild("leg_left_frb",
                CubeListBuilder.create()
                        .texOffs(7, 13)
                        .addBox(-2.0F, -1.0F, -0.5F, 2.0F, 1.0F, 1.0F, false),
                PartPose.offset(-0.5F, 2.0F, 0.0F));

        PartDefinition modelPartLegLeftMia = body.addOrReplaceChild("leg_left_mia",
                CubeListBuilder.create()
                        .texOffs(10, 17)
                        .addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, false),
                PartPose.offsetAndRotation(1.5F, 0.0F, -0.5F, -2.5F, 5.0F, -120.0F));

        modelPartLegLeftMia.addOrReplaceChild("leg_left_mib",
                CubeListBuilder.create()
                        .texOffs(0, 13)
                        .addBox(-2.0F, -1.0F, -0.5F, 2.0F, 1.0F, 1.0F, false),
                PartPose.offset(-0.5F, 2.0F, 0.0F));

        PartDefinition modelPartLegLeftBaa = body.addOrReplaceChild("leg_left_baa",
                CubeListBuilder.create()
                        .texOffs(5, 17)
                        .addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, false),
                PartPose.offsetAndRotation(1.5F, 0.0F, 0.5F, 37.5F, 45.0F, -92.5F));

        modelPartLegLeftBaa.addOrReplaceChild("leg_left_bab",
                CubeListBuilder.create()
                        .texOffs(21, 10)
                        .addBox(-2.0F, -1.0F, -0.5F, 2.0F, 1.0F, 1.0F, false),
                PartPose.offset(-0.5F, 2.0F, 0.0F));

        PartDefinition modelPartLegRightFra = body.addOrReplaceChild("leg_right_fra",
                CubeListBuilder.create()
                        .texOffs(0, 17)
                        .addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, false),
                PartPose.offsetAndRotation(-1.5F, 0.0F, -1.5F, -25.0F, 7.5F, 122.5F));

        modelPartLegRightFra.addOrReplaceChild("leg_right_frb",
                CubeListBuilder.create()
                        .texOffs(14, 10)
                        .addBox(0.0F, -1.0F, -0.5F, 2.0F, 1.0F, 1.0F, false),
                PartPose.offset(0.5F, 2.0F, 0.0F));

        PartDefinition modelPartLegRightMia = body.addOrReplaceChild("leg_right_mia",
                CubeListBuilder.create()
                        .texOffs(19, 13)
                        .addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, false),
                PartPose.offsetAndRotation(-1.5F, 0.0F, -0.5F, -2.5F, -5.0F, 120.0F));

        modelPartLegRightMia.addOrReplaceChild("leg_right_mib",
                CubeListBuilder.create()
                        .texOffs(7, 10)
                        .addBox(0.0F, -1.0F, -0.5F, 2.0F, 1.0F, 1.0F, false),
                PartPose.offset(0.5F, 2.0F, 0.0F));

        PartDefinition modelPartLegRightBaa = body.addOrReplaceChild("leg_right_baa",
                CubeListBuilder.create()
                        .texOffs(14, 13)
                        .addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, false),
                PartPose.offsetAndRotation(-1.5F, 0.0F, 0.5F, 37.5F, -45.0F, 92.5F));

        modelPartLegRightBaa.addOrReplaceChild("leg_right_bab",
                CubeListBuilder.create()
                        .texOffs(0, 10)
                        .addBox(0.0F, -1.0F, -0.5F, 2.0F, 1.0F, 1.0F, false),
                PartPose.offset(0.5F, 2.0F, 0.0F));

        return LayerDefinition.create(mesh, 32, 32);
    }

    private void setDefaultRotationAngles() {
        setRotationAngle(body_base, -7.5F, 0.0F, 0.0F);
        setRotationAngle(eyestalk_right, 7.5F, 0.0F, 0.0F);
        setRotationAngle(eyestalk_left, 7.5F, 0.0F, 0.0F);
        setRotationAngle(claw_right_a_r1, 0.0F, 5.0F, 12.5F);
        setRotationAngle(claw_left_a, 0.0F, -17.5F, -7.5F);
        setRotationAngle(claw_left_c_r1, 0.0F, 0.0F, -10.0F);
        setRotationAngle(leg_left_fra, -25.0F, -7.5F, -122.5F);
        setRotationAngle(leg_left_mia, -2.5F, 5.0F, -120.0F);
        setRotationAngle(leg_left_baa, 37.5F, 45.0F, -92.5F);
        setRotationAngle(leg_right_fra, -25.0F, 7.5F, 122.5F);
        setRotationAngle(leg_right_mia, -2.5F, -5.0F, 120.0F);
        setRotationAngle(leg_right_baa, 37.5F, -45.0F, 92.5F);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float age, float headYaw, float headPitch) {
        this.setDefaultRotationAngles();

        try (ModelAnimator.Cycle walk = ModelAnimator.cycle(limbSwing * 0.6F, limbSwingAmount)) {
            leg_right_fra.zRot += walk.eval(1.0F, 1.5F, 0.0F, 1.5F);
            leg_right_mia.zRot += walk.eval(1.0F, 1.5F, 0.4F, 1.5F);
            leg_right_baa.zRot += walk.eval(1.0F, 1.5F, 0.0F, 1.5F);

            leg_left_fra.zRot -= walk.eval(1.0F, 1.5F, 0.4F, 1.5F);
            leg_left_mia.zRot -= walk.eval(1.0F, 1.5F, 0.0F, 1.5F);
            leg_left_baa.zRot -= walk.eval(1.0F, 1.5F, 0.4F, 1.5F);
        }

        try (ModelAnimator.Cycle idle = ModelAnimator.cycle(age * 0.025F, 0.05F)) {
            claw_left_c_r1.zRot += idle.eval(1.0F, 1.0F, 0.0F, -0.5F);
        }
    }

    @Override
    public ModelPart root() {
        return body_base;
    }

    private void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x * ModelAnimator.DEG_TO_RAD;
        modelRenderer.yRot = y * ModelAnimator.DEG_TO_RAD;
        modelRenderer.zRot = z * ModelAnimator.DEG_TO_RAD;
    }
}
