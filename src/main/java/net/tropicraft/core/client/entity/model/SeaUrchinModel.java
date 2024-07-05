package net.tropicraft.core.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.tropicraft.core.common.entity.underdasea.SeaUrchinEntity;

import java.util.List;

public class SeaUrchinModel extends ListModel<SeaUrchinEntity> {
    private static final int VERTICAL_SPINES = 12;
    private static final int HORIZONTAL_SPINES = 12;

    private final ModelPart base;
    private final ModelPart top1;
    private final ModelPart top2;
    private final ModelPart front1;
    private final ModelPart front2;
    private final ModelPart left1;
    private final ModelPart left2;
    private final ModelPart back1;
    private final ModelPart back2;
    private final ModelPart right1;
    private final ModelPart right2;
    private final ModelPart bottom1;
    private final ModelPart bottom2;
    private final ModelPart spine;

    public SeaUrchinModel(ModelPart root) {
        base = root.getChild("base");
        top1 = root.getChild("top1");
        top2 = root.getChild("top2");
        front1 = root.getChild("front1");
        front2 = root.getChild("front2");
        left1 = root.getChild("left1");
        left2 = root.getChild("left2");
        back1 = root.getChild("back1");
        back2 = root.getChild("back2");
        right1 = root.getChild("right1");
        right2 = root.getChild("right2");
        bottom1 = root.getChild("bottom1");
        bottom2 = root.getChild("bottom2");
        spine = root.getChild("spine");
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("base",
                CubeListBuilder.create().mirror()
                        .texOffs(0, 0).addBox(-3.0f, 16.0f, -3.0f, 6, 6, 6),
                PartPose.offset(0.0f, 0.0f, 0.0f));

        root.addOrReplaceChild("top1",
                CubeListBuilder.create().mirror()
                        .texOffs(0, 38).addBox(-2.0f, 15.0f, -2.0f, 4, 1, 4),
                PartPose.offset(0.0f, 0.0f, 0.0f));

        root.addOrReplaceChild("top2",
                CubeListBuilder.create().mirror()
                        .texOffs(16, 38).addBox(-1.0f, 14.0f, -1.0f, 2, 1, 2),
                PartPose.offset(0.0f, 0.0f, 0.0f));

        root.addOrReplaceChild("front1",
                CubeListBuilder.create().mirror()
                        .texOffs(0, 12).addBox(-2.0f, 17.0f, -4.0f, 4, 4, 1),
                PartPose.offset(0.0f, 0.0f, 0.0f));

        root.addOrReplaceChild("front2",
                CubeListBuilder.create().mirror()
                        .texOffs(10, 12).addBox(-1.0f, 18.0f, -5.0f, 2, 2, 1),
                PartPose.offset(0.0f, 0.0f, 0.0f));

        root.addOrReplaceChild("left1",
                CubeListBuilder.create().mirror()
                        .texOffs(0, 17).addBox(3.0f, 17.0f, -2.0f, 1, 4, 4),
                PartPose.offset(0.0f, 0.0f, 0.0f));

        root.addOrReplaceChild("left2",
                CubeListBuilder.create().mirror()
                        .texOffs(10, 17).addBox(4.0f, 18.0f, -1.0f, 1, 2, 2),
                PartPose.offset(0.0f, 0.0f, 0.0f));

        root.addOrReplaceChild("back1",
                CubeListBuilder.create().mirror()
                        .texOffs(0, 25).addBox(-2.0f, 17.0f, 3.0f, 4, 4, 1),
                PartPose.offset(0.0f, 0.0f, 0.0f));

        root.addOrReplaceChild("back2",
                CubeListBuilder.create().mirror()
                        .texOffs(10, 25).addBox(-1.0f, 18.0f, 4.0f, 2, 2, 1),
                PartPose.offset(0.0f, 0.0f, 0.0f));

        root.addOrReplaceChild("right1",
                CubeListBuilder.create().mirror()
                        .texOffs(0, 30).addBox(-4.0f, 17.0f, -2.0f, 1, 4, 4),
                PartPose.offset(0.0f, 0.0f, 0.0f));

        root.addOrReplaceChild("right2",
                CubeListBuilder.create().mirror()
                        .texOffs(10, 30).addBox(-5.0f, 18.0f, -1.0f, 1, 2, 2),
                PartPose.offset(0.0f, 0.0f, 0.0f));

        root.addOrReplaceChild("bottom1",
                CubeListBuilder.create().mirror()
                        .texOffs(0, 38).addBox(-2.0f, 22.0f, -2.0f, 4, 1, 4),
                PartPose.offset(0.0f, 0.0f, 0.0f));

        root.addOrReplaceChild("bottom2",
                CubeListBuilder.create().mirror()
                        .texOffs(16, 38).addBox(-1.0f, 23.0f, -1.0f, 2, 1, 2),
                PartPose.offset(0.0f, 0.0f, 0.0f));

        root.addOrReplaceChild("spine",
                CubeListBuilder.create().mirror()
                        .texOffs(24, 0).addBox(-0.5f, -9.0f, -0.5f, 1, 6, 1),
                PartPose.offset(0.0f, 19.0f, 0.0f));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(SeaUrchinEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // Yeah look it's a spiky ball okay
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        super.renderToBuffer(poseStack, buffer, packedLight, packedOverlay, color);

        for (int v = 0; v < VERTICAL_SPINES; v++) {
            for (int h = 0; h < HORIZONTAL_SPINES; h++) {
                poseStack.pushPose();
                poseStack.translate(0.0f, 1.25f, 0.0f);
                poseStack.mulPose(Axis.ZP.rotationDegrees(360 * ((float) v) / VERTICAL_SPINES));
                poseStack.mulPose(Axis.XP.rotationDegrees(360 * ((float) h) / HORIZONTAL_SPINES));
                poseStack.translate(0.0f, -0.4f, 0.0f);
                poseStack.scale(0.33f, 1.0f, 0.33f);
                spine.render(poseStack, buffer, packedLight, packedOverlay, color);
                poseStack.popPose();
            }
        }
    }

    @Override
    public Iterable<ModelPart> parts() {
        return List.of(
                base, top1, top2, front1, front2, left1, left2, back1, back2, right1, right2, bottom1, bottom2
        );
    }
}
