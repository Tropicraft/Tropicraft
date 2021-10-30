package net.tropicraft.core.client.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.tropicraft.core.common.entity.placeable.UmbrellaEntity;

public class UmbrellaModel extends ListModel<UmbrellaEntity> {

    public ModelPart base;
    public ModelPart shape2;
    public ModelPart shape3;
    public ModelPart shape31;
    public ModelPart shape32;
    public ModelPart shape33;
    public ModelPart shape4;
    public ModelPart shape11;
    public ModelPart shape12;
    public ModelPart shape111;
    public ModelPart shape112;

    public UmbrellaModel(ModelPart root) {

        base = root.getChild("base");
        shape2 = root.getChild("shape2");
        shape4 = root.getChild("shape4");
        shape3 = root.getChild("shape3");
        shape31 = root.getChild("shape31");
        shape32 = root.getChild("shape32");
        shape33 = root.getChild("shape33");
        shape11 = root.getChild("shape11");
        shape12 = root.getChild("shape12");
        shape111 = root.getChild("shape111");
        shape112 = root.getChild("shape112");



        /*
        base = new ModelPart(this, 0, 0);
        base.addCuboid(-0.5F, 0F, -0.5F, 1, 14, 1, 0F);
        base.setPivot(0F, -13F, 0F);

        shape2 = new ModelPart(this, 0, 0);
        shape2.addCuboid(-7.5F, -2F, -7.5F, 15, 1, 15, 0F);
        shape2.setPivot(0F, -12F, 0F);

        shape3 = new ModelPart(this, 0, 20);
        shape3.addCuboid(-4F, -1F, 0F, 9, 1, 3, 0F);
        shape3.setPivot(-0.5F, -13F, 7.5F);

        shape3.pitch = -0.2443461F;
        shape3.yaw = 0F;
        shape3.roll = 0F;
        shape3.mirror = false;

        shape31 = new ModelPart(this, 0, 24);
        shape31.addCuboid(-4.5F, -1F, 0F, 9, 1, 3, 0F);
        shape31.setPivot(7.5F, -13F, 0F);

        shape31.pitch = -0.2443461F;
        shape31.yaw = 1.570796F;
        shape31.roll = 0F;
        shape31.mirror = false;

        shape32 = new ModelPart(this, 0, 28);
        shape32.addCuboid(-4.5F, -1F, -1F, 9, 1, 3, 0F);
        shape32.setPivot(0F, -12.75F, -8.45F);

        shape32.pitch = -0.2443461F;
        shape32.yaw = 3.141593F;
        shape32.roll = 0F;
        shape32.mirror = false;

        shape33 = new ModelPart(this, 24, 28);
        shape33.addCuboid(-4.5F, -1F, 1F, 9, 1, 3, 0F);
        shape33.setPivot(-6.5F, -13.25F, 0F);

        shape33.pitch = -0.2443461F;
        shape33.yaw = -1.570796F;
        shape33.roll = 0F;
        shape33.mirror = false;

        shape4 = new ModelPart(this, 25, 25);
        shape4.addCuboid(-1F, -1F, -1F, 2, 1, 2, 0F);
        shape4.setPivot(0F, -14F, 0F);

        shape11 = new ModelPart(this, 0, 0);
        shape11.addCuboid(-0.5F, 0F, -0.5F, 1, 9, 1, 0F);
        shape11.setPivot(0F, -10F, 0F);

        shape11.pitch = 1.902409F;
        shape11.yaw = 0F;
        shape11.roll = 0F;
        shape11.mirror = false;

        shape12 = new ModelPart(this, 0, 0);
        shape12.addCuboid(-0.5F, 0F, -0.5F, 1, 9, 1, 0F);
        shape12.setPivot(0F, -10F, 0F);

        shape12.pitch = -1.902409F;
        shape12.yaw = 0F;
        shape12.roll = 0F;
        shape12.mirror = false;

        shape111 = new ModelPart(this, 0, 0);
        shape111.addCuboid(-0.5F, 0F, -0.5F, 1, 9, 1, 0F);
        shape111.setPivot(0F, -10F, 0F);

        shape111.pitch = 1.902409F;
        shape111.yaw = 1.570796F;
        shape111.roll = 0F;
        shape111.mirror = false;

        shape112 = new ModelPart(this, 0, 0);
        shape112.addCuboid(-0.5F, 0F, -0.5F, 1, 9, 1, 0F);
        shape112.setPivot(0F, -10F, 0F);

        shape112.pitch = 1.902409F;
        shape112.yaw = -1.570796F;
        shape112.roll = 0F;
        shape112.mirror = false;

        */
    }

    public static LayerDefinition create() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();

        modelPartData.addOrReplaceChild("base",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-0.5F, 0F, -0.5F, 1, 14, 1),
                PartPose.offset(0F, -13F, 0F));

        modelPartData.addOrReplaceChild("shape2",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-7.5F, -2F, -7.5F, 15, 1, 15),
                PartPose.offset(0F, -12F, 0F));

        modelPartData.addOrReplaceChild("shape4",
                CubeListBuilder.create().texOffs(25, 25)
                        .addBox(-1F, -1F, -1F, 2, 1, 2),
                PartPose.offset(0F, -14F, 0F));

        modelPartData.addOrReplaceChild("shape3",
                CubeListBuilder.create().texOffs(0, 20)
                        .addBox(-4F, -1F, 0F, 9, 1, 3),
                PartPose.offsetAndRotation(-0.5F, -13F, 7.5F, -0.2443461F, 0F, 0F));

        modelPartData.addOrReplaceChild("shape31",
                CubeListBuilder.create().texOffs(0, 24)
                        .addBox(-4.5F, -1F, 0F, 9, 1, 3),
                PartPose.offsetAndRotation(7.5F, -13F, 0F, -0.2443461F, 1.570796F, 0F));

        modelPartData.addOrReplaceChild("shape32",
                CubeListBuilder.create().texOffs(0, 28)
                        .addBox(-4.5F, -1F, -1F, 9, 1, 3),
                PartPose.offsetAndRotation(0F, -12.75F, -8.45F, -0.2443461F, 3.141593F, 0F));

        modelPartData.addOrReplaceChild("shape33",
                CubeListBuilder.create().texOffs(24, 28)
                        .addBox(-4.5F, -1F, 1F, 9, 1, 3),
                PartPose.offsetAndRotation(-6.5F, -13.25F, 0F, -0.2443461F, -1.570796F, 0F));

        modelPartData.addOrReplaceChild("shape11",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-0.5F, 0F, -0.5F, 1, 9, 1),
                PartPose.offsetAndRotation(0F, -10F, 0F, 1.902409F, 0F, 0F));

        modelPartData.addOrReplaceChild("shape12",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-0.5F, 0F, -0.5F, 1, 9, 1),
                PartPose.offsetAndRotation(0F, -10F, 0F, -1.902409F, 0F, 0F));

        modelPartData.addOrReplaceChild("shape111",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-0.5F, 0F, -0.5F, 1, 9, 1),
                PartPose.offsetAndRotation(0F, -10F, 0F, 1.902409F, 1.570796F, 0F));

        modelPartData.addOrReplaceChild("shape112",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-0.5F, 0F, -0.5F, 1, 9, 1),
                PartPose.offsetAndRotation(0F, -10F, 0F, 1.902409F, -1.570796F, 0F));



        return LayerDefinition.create(modelData, 64, 32);
    }

    @Override
    public void setupAnim(UmbrellaEntity umbrella, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public Iterable<ModelPart> parts() {
        return ImmutableList.of(
            base, shape2, shape3, shape31, shape32, shape33,
            shape4, shape11, shape12, shape111, shape112
        );
    }
}
