package net.tropicraft.core.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.tropicraft.core.common.entity.passive.FiddlerCrabEntity;

public class FiddlerCrabModel<T extends FiddlerCrabEntity> extends EntityModel<T> {
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

    public FiddlerCrabModel(ModelPart model) {
        this.body_base = model;
        this.eyestalk_right = model.getChild("eyestalk_right");
        this.eyestalk_left = model.getChild("eyestalk_left");
        this.claw_right_a = model.getChild("claw_right_a");
        this.claw_right_a_r1 = this.claw_right_a.getChild("claw_right_a_r1");
        this.claw_left_a = model.getChild("claw_left_a");
        this.claw_left_c = this.claw_left_a.getChild("claw_left_c");
        this.claw_left_c_r1 = this.claw_left_c.getChild("claw_left_c_r1");
        this.claw_left_b = this.claw_left_a.getChild("claw_left_b");
        this.leg_left_fra = model.getChild("leg_left_fra");
        this.leg_left_frb = this.leg_left_fra.getChild("leg_left_frb");
        this.leg_left_mia = model.getChild("leg_left_mia");
        this.leg_left_mib = this.leg_left_mia.getChild("leg_left_mib");
        this.leg_left_baa = model.getChild("leg_left_baa");
        this.leg_left_bab = this.leg_left_baa.getChild("leg_left_bab");
        this.leg_right_fra = model.getChild("leg_right_fra");
        this.leg_right_frb = this.leg_right_fra.getChild("leg_right_frb");
        this.leg_right_mia = model.getChild("leg_right_mia");
        this.leg_right_mib = this.leg_right_mia.getChild("leg_right_mib");
        this.leg_right_baa = model.getChild("leg_right_baa");
        this.leg_right_bab = this.leg_right_baa.getChild("leg_right_bab");
    }

    public static LayerDefinition create() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();

        partDefinition.addOrReplaceChild("body_base", CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-2.0f, -2.0f, -2.0f, 4.0f, 2.0f, 3.0f),
                PartPose.offsetAndRotation(0.0f, 23.0f, 0.0f, 0.0f, 0.0f, 0.0f));

        partDefinition.addOrReplaceChild("eyestalk_right", CubeListBuilder.create()
                        .texOffs(0, 21).addBox(0.0f, -2.0f, 0.0f, 1.0f, 2.0f, 0.0f),
                PartPose.offsetAndRotation(-1.5f, 22.0f, -2.0f, 0.0f, 0.0f, 0.0f));

        partDefinition.addOrReplaceChild("eyestalk_left", CubeListBuilder.create()
                        .texOffs(3, 21).addBox(-1.0f, -2.0f, 0.0f, 1.0f, 2.0f, 0.0f),
                PartPose.offsetAndRotation(1.5f, 22.0f, -2.0f, 0.0f, 0.0f, 0.0f));

        PartDefinition partDefinition3 = partDefinition.addOrReplaceChild("claw_right_a", CubeListBuilder.create(),
                PartPose.offsetAndRotation(-2.0f, 22.5f, -2.0f, 0.0f, 0.0f, 0.0f));

        partDefinition3.addOrReplaceChild("claw_right_a_r1", CubeListBuilder.create()
                        .texOffs(7, 6).addBox(-0.75f, -0.5f, -1.0f, 2.0f, 1.0f, 1.0f),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));

        PartDefinition partDefinition5 = partDefinition.addOrReplaceChild("claw_left_a", CubeListBuilder.create()
                        .texOffs(0, 6).addBox(-1.0f, -1.0f, -1.0f, 2.0f, 2.0f, 1.0f),
                PartPose.offsetAndRotation(2.0f, 22.5f, -2.0f, 0.0f, 0.0f, 0.0f));

        PartDefinition partDefinition6 = partDefinition5.addOrReplaceChild("claw_left_c", CubeListBuilder.create(),
                PartPose.offsetAndRotation(-1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));

        partDefinition6.addOrReplaceChild("claw_left_c_r1", CubeListBuilder.create()
                        .texOffs(14, 6).addBox(-2.0f, 0.0f, -0.99f, 2.0f, 1.0f, 1.0f),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));

        partDefinition5.addOrReplaceChild("claw_left_b", CubeListBuilder.create()
                        .texOffs(15, 0).addBox(-3.0f, 0.0f, -1.0f, 3.0f, 1.0f, 1.0f),
                PartPose.offsetAndRotation(-1.0f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f));

        PartDefinition partDefinition9 = partDefinition.addOrReplaceChild("leg_left_fra", CubeListBuilder.create()
                        .texOffs(15, 17).addBox(-0.5f, 0.0f, -0.5f, 1.0f, 2.0f, 1.0f),
                PartPose.offsetAndRotation(1.5f, 23.0f, -1.5f, 0.0f, 0.0f, 0.0f));

        partDefinition9.addOrReplaceChild("leg_left_frb", CubeListBuilder.create()
                        .texOffs(7, 13).addBox(-2.0f, -1.0f, -0.5f, 2.0f, 1.0f, 1.0f),
                PartPose.offsetAndRotation(-0.5f, 2.0f, 0.0f, 0.0f, 0.0f, 0.0f));

        PartDefinition partDefinition11 = partDefinition.addOrReplaceChild("leg_left_mia", CubeListBuilder.create()
                        .texOffs(10, 17).addBox(-0.5f, 0.0f, -0.5f, 1.0f, 2.0f, 1.0f),
                PartPose.offsetAndRotation(1.5f, 23.0f, -0.5f, 0.0f, 0.0f, 0.0f));

        partDefinition11.addOrReplaceChild("leg_left_mib", CubeListBuilder.create()
                        .texOffs(0, 13).addBox(-2.0f, -1.0f, -0.5f, 2.0f, 1.0f, 1.0f),
                PartPose.offsetAndRotation(-0.5f, 2.0f, 0.0f, 0.0f, 0.0f, 0.0f));

        PartDefinition partDefinition13 = partDefinition.addOrReplaceChild("leg_left_baa", CubeListBuilder.create()
                        .texOffs(5, 17).addBox(-0.5f, 0.0f, -0.5f, 1.0f, 2.0f, 1.0f),
                PartPose.offsetAndRotation(1.5f, 23.0f, 0.5f, 0.0f, 0.0f, 0.0f));

        partDefinition13.addOrReplaceChild("leg_left_bab", CubeListBuilder.create()
                        .texOffs(21, 10).addBox(-2.0f, -1.0f, -0.5f, 2.0f, 1.0f, 1.0f),
                PartPose.offsetAndRotation(-0.5f, 2.0f, 0.0f, 0.0f, 0.0f, 0.0f));

        PartDefinition partDefinition15 = partDefinition.addOrReplaceChild("leg_right_fra", CubeListBuilder.create()
                        .texOffs(0, 17).addBox(-0.5f, 0.0f, -0.5f, 1.0f, 2.0f, 1.0f),
                PartPose.offsetAndRotation(-1.5f, 23.0f, -1.5f, 0.0f, 0.0f, 0.0f));

        partDefinition15.addOrReplaceChild("leg_right_frb", CubeListBuilder.create()
                        .texOffs(14, 10).addBox(0.0f, -1.0f, -0.5f, 2.0f, 1.0f, 1.0f),
                PartPose.offsetAndRotation(0.5f, 2.0f, 0.0f, 0.0f, 0.0f, 0.0f));

        PartDefinition partDefinition17 = partDefinition.addOrReplaceChild("leg_right_mia", CubeListBuilder.create()
                        .texOffs(19, 13).addBox(-0.5f, 0.0f, -0.5f, 1.0f, 2.0f, 1.0f),
                PartPose.offsetAndRotation(-1.5f, 23.0f, -0.5f, 0.0f, 0.0f, 0.0f));

        partDefinition17.addOrReplaceChild("leg_right_mib", CubeListBuilder.create()
                        .texOffs(7, 10).addBox(0.0f, -1.0f, -0.5f, 2.0f, 1.0f, 1.0f),
                PartPose.offsetAndRotation(0.5f, 2.0f, 0.0f, 0.0f, 0.0f, 0.0f));

        PartDefinition partDefinition19 = partDefinition.addOrReplaceChild("leg_right_baa", CubeListBuilder.create()
                        .texOffs(14, 13).addBox(-0.5f, 0.0f, -0.5f, 1.0f, 2.0f, 1.0f),
                PartPose.offsetAndRotation(-1.5f, 23.0f, 0.5f, 0.0f, 0.0f, 0.0f));

        partDefinition19.addOrReplaceChild("leg_right_bab", CubeListBuilder.create()
                        .texOffs(0, 10).addBox(0.0f, -1.0f, -0.5f, 2.0f, 1.0f, 1.0f),
                PartPose.offsetAndRotation(0.5f, 2.0f, 0.0f, 0.0f, 0.0f, 0.0f));

        return LayerDefinition.create(meshDefinition, 32, 32);
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
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        body_base.render(poseStack, buffer, packedLight, packedOverlay);
    }

    private void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
