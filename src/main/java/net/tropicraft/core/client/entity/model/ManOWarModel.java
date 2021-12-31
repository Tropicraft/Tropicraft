package net.tropicraft.core.client.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.tropicraft.core.common.entity.underdasea.ManOWarEntity;

//TODO: Fixed by using fixed values instead of using the values based off of DERP
public class ManOWarModel extends ListModel<ManOWarEntity> {
    ModelPart Body;
    ModelPart CenterTent;
    ModelPart CenterTent2;
    ModelPart CenterTent3;
    ModelPart Tent1;
    ModelPart Tent2;
    ModelPart Tent3;
    ModelPart Tent4;
    public boolean isOnGround = false;

    //32, 20, true
    static int i = 32;
    static int j = 20;
    static boolean derp = true;

    static final CubeDeformation delta = new CubeDeformation(0.001f);

    public ManOWarModel(ModelPart root){
        Body = root.getChild("Body");
        CenterTent = Body.getChild("CenterTent");
        CenterTent2 = CenterTent.getChild("CenterTent2");
        CenterTent3 = CenterTent2.getChild("CenterTent3");
        Tent1 = Body.getChild("Tent1");
        Tent2 = Body.getChild("Tent2");
        Tent3 = Body.getChild("Tent3");
        Tent4 = Body.getChild("Tent4");

        /*
        isOnGround = false;
        textureWidth = 64;
        textureHeight = 32;

        final float delta = 0.001f;
        Body = new ModelPart(this);
        Body.setPivot(0F, 18F, 0F);
        setRotation(Body, 0F, 0F, 0F);
        Body.mirror = true;
        Body.addCuboid("float", -2F, -4F, -2F, 4, 4, 8, delta, i, j);
        Body.addCuboid("Shape1", 0F, -6F, -2F, 0, 6, 10, delta, derp ? 32 : 15, derp ? 20 : -10);
        int tbOX = derp ? 0 : 32;
        int tbOY = derp ? 14 : 20;
        Body.addCuboid("tentbase", -2F, 0F, -2F, 4, 2, 4, delta, tbOX, tbOY);

        CenterTent = new ModelPart(this);
        CenterTent.setPivot(0F, 2F, 0F);
        setRotation(CenterTent, 0F, 0F, 0F);
        CenterTent.mirror = true;
        CenterTent.addCuboid("tent51", -0.5F, 0F, -0.5F, 1, 10, 1, delta, derp ? 7 : 32, derp ? 0 : 20);
        Body.addChild(CenterTent);

        CenterTent2 = new ModelPart(this);
        CenterTent2.setPivot(0F, 10F, 0F);
        setRotation(CenterTent2, 0F, 0F, 0F);
        CenterTent2.mirror = true;
        CenterTent2.addCuboid("tent52", -0.5F, 0F, -0.5F, 1, 4, 1, delta, derp ? 11 : 32, derp ? 0 : 20);
        CenterTent.addChild(CenterTent2);

        CenterTent3 = new ModelPart(this);
        CenterTent3.setPivot(0F, 4F, 0F);
        setRotation(CenterTent3, 0F, 0F, 0F);
        CenterTent3.mirror = true;
        CenterTent3.addCuboid("tent53", -0.5F, 0F, -0.5F, 1, 5, 1, delta, derp ? 11 : 32, derp ? 5 : 20);
        CenterTent2.addChild(CenterTent3);

        Tent1 = new ModelPart(this);
        Tent1.setPivot(-1.5F, 2F, -1.5F);
        setRotation(Tent1, 0F, 0F, 0F);
        Tent1.mirror = true;
        Tent1.addCuboid("tent1", -0.5F, 0F, -0.5F, 1, 11, 1, delta, derp ? 0 : 32, derp ? 0 : 20);
        Body.addChild(Tent1);

        Tent2 = new ModelPart(this);
        Tent2.setPivot(1.5F, 2F, 1.5F);
        setRotation(Tent2, 0F, 0F, 0F);
        Tent2.mirror = true;
        Tent2.addCuboid("tent2", -0.5F, 0F, -0.5F, 1, 11, 1, delta, derp ? 0 : 32, derp ? 0 : 20);
        Body.addChild(Tent2);

        Tent3 = new ModelPart(this);
        Tent3.setPivot(-1.5F, 2F, 1.5F);
        setRotation(Tent3, 0F, 0F, 0F);
        Tent3.mirror = true;
        Tent3.addCuboid("tent3", -0.5F, 0F, -0.5F, 1, 11, 1, delta, derp ? 0 : 32, derp ? 0 : 20);
        Body.addChild(Tent3);

        Tent4 = new ModelPart(this);
        Tent4.setPivot(1.5F, 2F, -1.5F);
        setRotation(Tent4, 0F, 0F, 0F);
        Tent4.mirror = true;
        Tent4.addCuboid("tent4", -0.5F, 0F, -0.5F, 1, 11, 1, delta, derp ? 0 : 32, derp ? 0 : 20);
        Body.addChild(Tent4);
         */
    }

    public static LayerDefinition createOuterModel() {
        return create(32, 20, true);
    }

    public static LayerDefinition createGelLayerModel() {
        return create(0, 20, false);
    }

    private static LayerDefinition create(final int i, final int j, final boolean derp) {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();

        //final float delta = 0.001f;
        int tbOX = derp ? 0 : 32;
        int tbOY = derp ? 14 : 20;

        PartDefinition ModelPartBody = modelPartData.addOrReplaceChild("Body",
                CubeListBuilder.create()
                        .mirror()
                        .addBox("float", -2F, -4F, -2F, 4, 4, 8, delta, i, j)
                        .addBox("Shape1", 0F, -6F, -2F, 0, 6, 10, delta, 15, -10)//derp ? 32 : 15, derp ? 20 : -10)
                        .addBox("tentbase", -2F, 0F, -2F, 4, 2, 4, delta, tbOX, tbOY),
                PartPose.offset(0F, 18F, 0F));

        PartDefinition ModelPartCtent1 = ModelPartBody.addOrReplaceChild("CenterTent",
                CubeListBuilder.create()
                        .mirror()
                        .addBox("tent51", -0.5F, 0F, -0.5F, 1, 10, 1, delta, derp ? 7 : 32, derp ? 0 : 20),
                PartPose.offset(0F, 2F, 0F));

        PartDefinition ModelPartCtent2 = ModelPartCtent1.addOrReplaceChild("CenterTent2",
                CubeListBuilder.create()
                        .mirror()
                        .addBox("tent52", -0.5F, 0F, -0.5F, 1, 4, 1, delta, derp ? 11 : 32, derp ? 0 : 20),
                PartPose.offset(0F, 10F, 0F));

        ModelPartCtent2.addOrReplaceChild("CenterTent3",
                CubeListBuilder.create()
                        .mirror()
                        .addBox("tent53", -0.5F, 0F, -0.5F, 1, 5, 1, delta, derp ? 11 : 32, derp ? 5 : 20),
                PartPose.offset(0F, 4F, 0F));

        ModelPartBody.addOrReplaceChild("Tent1",
                tenticlePartBuilder("tent1"),
                PartPose.offset(-1.5F, 2F, -1.5F));
        ModelPartBody.addOrReplaceChild("Tent2",
                tenticlePartBuilder("tent2"),
                PartPose.offset(1.5F, 2F, 1.5F));
        ModelPartBody.addOrReplaceChild("Tent3",
                tenticlePartBuilder("tent3"),
                PartPose.offset(-1.5F, 2F, 1.5F));
        ModelPartBody.addOrReplaceChild("Tent4",
                tenticlePartBuilder("tent4"),
                PartPose.offset(1.5F, 2F, -1.5F));

        return LayerDefinition.create(modelData, 64,32);
    }

    private static CubeListBuilder tenticlePartBuilder(String name){
        return CubeListBuilder.create()
                .mirror()
                .addBox(name, -0.5F, 0F, -0.5F, 1, 11, 1, delta, derp ? 0 : 32, derp ? 0 : 20);
    }

    @Override
    public Iterable<ModelPart> parts() {
        return ImmutableList.of(Body);
    }

    @Override
    public void setupAnim(ManOWarEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.isOnGround()) {
            Tent3.zRot = 0F;
            Tent3.xRot = 0F;
            Tent1.zRot = 0F;
            Tent1.xRot = 0F;
            Tent4.zRot = 0F;
            Tent4.xRot = 0F;
            Tent2.zRot = 0f;
            Tent2.xRot = 0F;
            CenterTent.xRot = 0F;
            CenterTent2.xRot = 0F;
            CenterTent3.xRot = 0F;
        } else {
            Tent3.zRot = (float) (Math.sin(ageInTicks * .1F)) * .07F + .4F;
            Tent3.xRot = (float) (Math.sin(ageInTicks * .1F)) * .05F + .4F;
            Tent1.zRot = -(float) (Math.sin(ageInTicks * .1F)) * .06F + .4F;
            Tent1.xRot = -(float) (Math.sin(ageInTicks * .1F)) * .05F + .4F;
            Tent4.zRot = -(float) (Math.sin(ageInTicks * .1F)) * .06F - .4F;
            Tent4.xRot = -(float) (Math.sin(ageInTicks * .1F)) * .04F + .4F;
            Tent2.zRot = (float) (Math.sin(ageInTicks * .025F)) * .05F - .4f;
            Tent2.xRot = (float) (Math.sin(ageInTicks * .025F)) * .05F + .4F;
            CenterTent.xRot = (float) (Math.sin(ageInTicks * .0125F)) * .05F + .2F;
            CenterTent2.xRot = (float) (Math.sin(ageInTicks * .0125F)) * .65F + 1.507F;
            CenterTent3.xRot = Math.abs((float) (Math.sin(ageInTicks * .0125F)) * .35F) + -1.25F;
        }
    }
}
