package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;

public class SeaUrchinModel extends EntityModel<LivingEntityRenderState> {
    private static final int VERTICAL_SPINES = 12;
    private static final int HORIZONTAL_SPINES = 12;

    public SeaUrchinModel(ModelPart root) {
        super(root);
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

        for (int v = 0; v < VERTICAL_SPINES; v++) {
            for (int h = 0; h < HORIZONTAL_SPINES; h++) {
                root.addOrReplaceChild("spine" + h + "_" + v,
                        CubeListBuilder.create().mirror()
                                .texOffs(24, 0).addBox(-0.5f, -9.0f, -0.5f, 1, 6, 1),
                        ModelAnimator.toPose(new Matrix4f()
                                .translate(0.0f, 20.0f, 0.0f)
                                .rotateZ(Mth.TWO_PI * (float) v / VERTICAL_SPINES)
                                .rotateX(Mth.TWO_PI * (float) h / HORIZONTAL_SPINES)
                                .translate(0.0f, -6.4f, 0.0f)
                                .scale(0.33f, 1.0f, 0.33f)
                                .translate(0.0f, 19.0f, 0.0f))
                );
            }
        }

        return LayerDefinition.create(mesh, 64, 64);
    }
}
