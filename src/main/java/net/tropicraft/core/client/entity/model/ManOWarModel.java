package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.tropicraft.core.common.entity.underdasea.ManOWarEntity;

public class ManOWarModel extends HierarchicalModel<ManOWarEntity> {
    private final ModelPart Body;
    private final ModelPart CenterTent;
    private final ModelPart CenterTent2;
    private final ModelPart CenterTent3;
    private final ModelPart Tent1;
    private final ModelPart Tent2;
    private final ModelPart Tent3;
    private final ModelPart Tent4;

    private static final CubeDeformation DEFORMATION = new CubeDeformation(0.001f);

    public ManOWarModel(ModelPart root) {
        Body = root.getChild("Body");
        CenterTent = Body.getChild("CenterTent");
        CenterTent2 = CenterTent.getChild("CenterTent2");
        CenterTent3 = CenterTent2.getChild("CenterTent3");
        Tent1 = Body.getChild("Tent1");
        Tent2 = Body.getChild("Tent2");
        Tent3 = Body.getChild("Tent3");
        Tent4 = Body.getChild("Tent4");
    }

    public static LayerDefinition createOuterModel() {
        return create(32, 20, true);
    }

    public static LayerDefinition createGelLayerModel() {
        return create(0, 20, false);
    }

    private static LayerDefinition create(int i, int j, boolean derp) {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        //final float delta = 0.001f;
        int tbOX = derp ? 0 : 32;
        int tbOY = derp ? 14 : 20;

        PartDefinition body = root.addOrReplaceChild("Body",
                CubeListBuilder.create()
                        .mirror()
                        .addBox("float", -2.0f, -4.0f, -2.0f, 4, 4, 8, DEFORMATION, i, j)
                        .addBox("Shape1", 0.0f, -6.0f, -2.0f, 0, 6, 10, DEFORMATION, 15, -10)//derp ? 32 : 15, derp ? 20 : -10)
                        .addBox("tentbase", -2.0f, 0.0f, -2.0f, 4, 2, 4, DEFORMATION, tbOX, tbOY),
                PartPose.offset(0.0f, 18.0f, 0.0f));

        PartDefinition centerTent = body.addOrReplaceChild("CenterTent",
                CubeListBuilder.create()
                        .mirror()
                        .addBox("tent51", -0.5f, 0.0f, -0.5f, 1, 10, 1, DEFORMATION, derp ? 7 : 32, derp ? 0 : 20),
                PartPose.offset(0.0f, 2.0f, 0.0f));

        PartDefinition centerTent2 = centerTent.addOrReplaceChild("CenterTent2",
                CubeListBuilder.create()
                        .mirror()
                        .addBox("tent52", -0.5f, 0.0f, -0.5f, 1, 4, 1, DEFORMATION, derp ? 11 : 32, derp ? 0 : 20),
                PartPose.offset(0.0f, 10.0f, 0.0f));

        centerTent2.addOrReplaceChild("CenterTent3",
                CubeListBuilder.create()
                        .mirror()
                        .addBox("tent53", -0.5f, 0.0f, -0.5f, 1, 5, 1, DEFORMATION, derp ? 11 : 32, derp ? 5 : 20),
                PartPose.offset(0.0f, 4.0f, 0.0f));

        body.addOrReplaceChild("Tent1",
                tenticlePartBuilder("tent1"),
                PartPose.offset(-1.5f, 2.0f, -1.5f));
        body.addOrReplaceChild("Tent2",
                tenticlePartBuilder("tent2"),
                PartPose.offset(1.5f, 2.0f, 1.5f));
        body.addOrReplaceChild("Tent3",
                tenticlePartBuilder("tent3"),
                PartPose.offset(-1.5f, 2.0f, 1.5f));
        body.addOrReplaceChild("Tent4",
                tenticlePartBuilder("tent4"),
                PartPose.offset(1.5f, 2.0f, -1.5f));

        return LayerDefinition.create(mesh, 64, 32);
    }

    private static CubeListBuilder tenticlePartBuilder(String name) {
        return CubeListBuilder.create()
                .mirror()
                .addBox(name, -0.5f, 0.0f, -0.5f, 1, 11, 1, DEFORMATION, 0, 0);
    }

    @Override
    public void setupAnim(ManOWarEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.onGround()) {
            Tent3.zRot = 0.0f;
            Tent3.xRot = 0.0f;
            Tent1.zRot = 0.0f;
            Tent1.xRot = 0.0f;
            Tent4.zRot = 0.0f;
            Tent4.xRot = 0.0f;
            Tent2.zRot = 0.0f;
            Tent2.xRot = 0.0f;
            CenterTent.xRot = 0.0f;
            CenterTent2.xRot = 0.0f;
            CenterTent3.xRot = 0.0f;
        } else {
            Tent3.zRot = Mth.sin(ageInTicks * 0.1f) * 0.07f + 0.4f;
            Tent3.xRot = Mth.sin(ageInTicks * 0.1f) * 0.05f + 0.4f;
            Tent1.zRot = -Mth.sin(ageInTicks * 0.1f) * 0.06f + 0.4f;
            Tent1.xRot = -Mth.sin(ageInTicks * 0.1f) * 0.05f + 0.4f;
            Tent4.zRot = -Mth.sin(ageInTicks * 0.1f) * 0.06f - 0.4f;
            Tent4.xRot = -Mth.sin(ageInTicks * 0.1f) * 0.04f + 0.4f;
            Tent2.zRot = Mth.sin(ageInTicks * 0.025f) * 0.05f - 0.4f;
            Tent2.xRot = Mth.sin(ageInTicks * 0.025f) * 0.05f + 0.4f;
            CenterTent.xRot = Mth.sin(ageInTicks * 0.0125f) * 0.05f + 0.2f;
            CenterTent2.xRot = Mth.sin(ageInTicks * 0.0125f) * 0.65f + 1.507f;
            CenterTent3.xRot = Math.abs(Mth.sin(ageInTicks * 0.0125f) * 0.35f) + -1.25f;
        }
    }

    @Override
    public ModelPart root() {
        return Body;
    }
}
