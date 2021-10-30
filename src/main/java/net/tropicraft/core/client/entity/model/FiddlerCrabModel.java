package net.tropicraft.core.client.entity.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class FiddlerCrabModel<T extends Entity> extends EntityModel<T> {
    private final ModelRenderer body_base;
    private final ModelRenderer eyestalk_right;
    private final ModelRenderer eyestalk_left;
    private final ModelRenderer claw_right_a;
    private final ModelRenderer claw_right_a_r1;
    private final ModelRenderer claw_left_a;
    private final ModelRenderer claw_left_c;
    private final ModelRenderer claw_left_c_r1;
    private final ModelRenderer claw_left_b;
    private final ModelRenderer leg_left_fra;
    private final ModelRenderer leg_left_frb;
    private final ModelRenderer leg_left_mia;
    private final ModelRenderer leg_left_mib;
    private final ModelRenderer leg_left_baa;
    private final ModelRenderer leg_left_bab;
    private final ModelRenderer leg_right_fra;
    private final ModelRenderer leg_right_frb;
    private final ModelRenderer leg_right_mia;
    private final ModelRenderer leg_right_mib;
    private final ModelRenderer leg_right_baa;
    private final ModelRenderer leg_right_bab;

    public FiddlerCrabModel() {
        texWidth = 32;
        texHeight = 32;

        body_base = new ModelRenderer(this);
        body_base.setPos(0.0F, 23.0F, 0.0F);
        body_base.texOffs(0, 0).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 2.0F, 3.0F, 0.0F, false);

        eyestalk_right = new ModelRenderer(this);
        eyestalk_right.setPos(-1.5F, -1.0F, -2.0F);
        body_base.addChild(eyestalk_right);
        eyestalk_right.texOffs(0, 21).addBox(0.0F, -2.0F, 0.0F, 1.0F, 2.0F, 0.0F, 0.0F, false);

        eyestalk_left = new ModelRenderer(this);
        eyestalk_left.setPos(1.5F, -1.0F, -2.0F);
        body_base.addChild(eyestalk_left);
        eyestalk_left.texOffs(3, 21).addBox(-1.0F, -2.0F, 0.0F, 1.0F, 2.0F, 0.0F, 0.0F, false);

        claw_right_a = new ModelRenderer(this);
        claw_right_a.setPos(-2.0F, -0.5F, -2.0F);
        body_base.addChild(claw_right_a);

        claw_right_a_r1 = new ModelRenderer(this);
        claw_right_a_r1.setPos(0.0F, 0.0F, 0.0F);
        claw_right_a.addChild(claw_right_a_r1);
        claw_right_a_r1.texOffs(7, 6).addBox(-0.75F, -0.5F, -1.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);

        claw_left_a = new ModelRenderer(this);
        claw_left_a.setPos(2.0F, -0.5F, -2.0F);
        body_base.addChild(claw_left_a);
        claw_left_a.texOffs(0, 6).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 1.0F, 0.0F, false);

        claw_left_c = new ModelRenderer(this);
        claw_left_c.setPos(-1.0F, 0.0F, 0.0F);
        claw_left_a.addChild(claw_left_c);

        claw_left_c_r1 = new ModelRenderer(this);
        claw_left_c_r1.setPos(0.0F, 0.0F, 0.0F);
        claw_left_c.addChild(claw_left_c_r1);
        claw_left_c_r1.texOffs(14, 6).addBox(-2.0F, 0.0F, -0.99F, 2.0F, 1.0F, 1.0F, 0.0F, false);

        claw_left_b = new ModelRenderer(this);
        claw_left_b.setPos(-1.0F, -1.0F, 0.0F);
        claw_left_a.addChild(claw_left_b);
        claw_left_b.texOffs(15, 0).addBox(-3.0F, 0.0F, -1.0F, 3.0F, 1.0F, 1.0F, 0.0F, false);

        leg_left_fra = new ModelRenderer(this);
        leg_left_fra.setPos(1.5F, 0.0F, -1.5F);
        body_base.addChild(leg_left_fra);
        leg_left_fra.texOffs(15, 17).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        leg_left_frb = new ModelRenderer(this);
        leg_left_frb.setPos(-0.5F, 2.0F, 0.0F);
        leg_left_fra.addChild(leg_left_frb);
        leg_left_frb.texOffs(7, 13).addBox(-2.0F, -1.0F, -0.5F, 2.0F, 1.0F, 1.0F, 0.0F, false);

        leg_left_mia = new ModelRenderer(this);
        leg_left_mia.setPos(1.5F, 0.0F, -0.5F);
        body_base.addChild(leg_left_mia);
        leg_left_mia.texOffs(10, 17).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        leg_left_mib = new ModelRenderer(this);
        leg_left_mib.setPos(-0.5F, 2.0F, 0.0F);
        leg_left_mia.addChild(leg_left_mib);
        leg_left_mib.texOffs(0, 13).addBox(-2.0F, -1.0F, -0.5F, 2.0F, 1.0F, 1.0F, 0.0F, false);

        leg_left_baa = new ModelRenderer(this);
        leg_left_baa.setPos(1.5F, 0.0F, 0.5F);
        body_base.addChild(leg_left_baa);
        leg_left_baa.texOffs(5, 17).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        leg_left_bab = new ModelRenderer(this);
        leg_left_bab.setPos(-0.5F, 2.0F, 0.0F);
        leg_left_baa.addChild(leg_left_bab);
        leg_left_bab.texOffs(21, 10).addBox(-2.0F, -1.0F, -0.5F, 2.0F, 1.0F, 1.0F, 0.0F, false);

        leg_right_fra = new ModelRenderer(this);
        leg_right_fra.setPos(-1.5F, 0.0F, -1.5F);
        body_base.addChild(leg_right_fra);
        leg_right_fra.texOffs(0, 17).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        leg_right_frb = new ModelRenderer(this);
        leg_right_frb.setPos(0.5F, 2.0F, 0.0F);
        leg_right_fra.addChild(leg_right_frb);
        leg_right_frb.texOffs(14, 10).addBox(0.0F, -1.0F, -0.5F, 2.0F, 1.0F, 1.0F, 0.0F, false);

        leg_right_mia = new ModelRenderer(this);
        leg_right_mia.setPos(-1.5F, 0.0F, -0.5F);
        body_base.addChild(leg_right_mia);
        leg_right_mia.texOffs(19, 13).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        leg_right_mib = new ModelRenderer(this);
        leg_right_mib.setPos(0.5F, 2.0F, 0.0F);
        leg_right_mia.addChild(leg_right_mib);
        leg_right_mib.texOffs(7, 10).addBox(0.0F, -1.0F, -0.5F, 2.0F, 1.0F, 1.0F, 0.0F, false);

        leg_right_baa = new ModelRenderer(this);
        leg_right_baa.setPos(-1.5F, 0.0F, 0.5F);
        body_base.addChild(leg_right_baa);
        leg_right_baa.texOffs(14, 13).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        leg_right_bab = new ModelRenderer(this);
        leg_right_bab.setPos(0.5F, 2.0F, 0.0F);
        leg_right_baa.addChild(leg_right_bab);
        leg_right_bab.texOffs(0, 10).addBox(0.0F, -1.0F, -0.5F, 2.0F, 1.0F, 1.0F, 0.0F, false);

        this.setDefaultRotationAngles();
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
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        body_base.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    private void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x * ModelAnimator.DEG_TO_RAD;
        modelRenderer.yRot = y * ModelAnimator.DEG_TO_RAD;
        modelRenderer.zRot = z * ModelAnimator.DEG_TO_RAD;
    }
}
