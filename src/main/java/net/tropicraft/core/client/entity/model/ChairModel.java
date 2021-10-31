package net.tropicraft.core.client.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.tropicraft.core.common.entity.placeable.ChairEntity;

public class ChairModel extends ListModel<ChairEntity> {
    public ModelPart seat;
    public ModelPart back;
    public ModelPart backRightLeg;
    public ModelPart backLeftLeg;
    public ModelPart frontLeftLeg;
    public ModelPart frontRightLeg;
    public ModelPart rightArm;
    public ModelPart leftArm;

    public ChairModel(ModelPart root) {
        this.seat = root.getChild("seat");
        this.back = root.getChild("back");
        this.backRightLeg = root.getChild("backRightLeg");
        this.backLeftLeg = root.getChild("backLeftLeg");
        this.frontLeftLeg = root.getChild("frontLeftLeg");
        this.frontRightLeg = root.getChild("frontRightLeg");
        this.rightArm = root.getChild("rightArm");
        this.leftArm = root.getChild("leftArm");

        //seat = new ModelPart(this, 0, 0);
        //seat.addCuboid(-7F, 0F, -8F, 16, 1, 16, 0F);
        //seat.setPivot(-1F, 0F, 0F);

        //back = new ModelPart(this, 0, 0);
        //back.addCuboid(-7F, 0F, 0F, 16, 1, 16, 0F);
        //back.setPivot(-1F, 0F, 8F);
        //back.pitch = 1.169371F;

        //backRightLeg = new ModelPart(this, 0, 0);
        //backRightLeg.addCuboid(-1F, -1F, 0F, 1, 10, 1, 0F);
        //backRightLeg.setPivot(-8F, -3F, 6F);
        //backRightLeg.pitch = 0.4537856F;

        //backLeftLeg = new ModelPart(this, 0, 0);
        //backLeftLeg.addCuboid(0F, 0F, 0F, 1, 10, 1, 0F);
        //backLeftLeg.setPivot(8F, -4F, 5F);
        //backLeftLeg.pitch = 0.4537856F;

        //frontLeftLeg = new ModelPart(this, 0, 0);
        //frontLeftLeg.addCuboid(0F, 0F, -1F, 1, 10, 1, 0F);
        //frontLeftLeg.setPivot(8F, -4F, 0F);
        //frontLeftLeg.pitch = -0.4537856F;

        //frontRightLeg = new ModelPart(this, 0, 0);
        //frontRightLeg.addCuboid(-1F, 0F, -1F, 1, 10, 1, 0F);
        //frontRightLeg.setPivot(-8F, -4F, 0F);
        //frontRightLeg.pitch = -0.4537856F;

        //rightArm = new ModelPart(this, 0, 29);
        //rightArm.addCuboid(0F, -1F, 0F, 14, 1, 2, 0F);
        //rightArm.setPivot(-10F, -4F, 11F);
        //rightArm.yaw = 1.570796F;

        //leftArm = new ModelPart(this, 0, 29);
        //leftArm.addCuboid(0F, 0F, 0F, 14, 1, 2, 0F);
        //leftArm.setPivot(8F, -5F, 11F);
        //leftArm.yaw = 1.570796F;
    }

    public static LayerDefinition create(){
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();

        modelPartData.addOrReplaceChild("seat", CubeListBuilder.create().texOffs(0, 0).addBox(-7F, 0F, -8F, 16, 1, 16), PartPose.offset(-1F, 0F, 0F));
        modelPartData.addOrReplaceChild("back", CubeListBuilder.create().texOffs(0, 0).addBox(-7F, 0F, 0F, 16, 1, 16), PartPose.offsetAndRotation(-1F, 0F, 8F, 1.169371F, 0F, 0F));
        modelPartData.addOrReplaceChild("backRightLeg", CubeListBuilder.create().texOffs(0, 0).addBox(-1F, -1F, 0F, 1, 10, 1), PartPose.offsetAndRotation(-8F, -3F, 6F, 0.4537856F, 0F, 0F));
        modelPartData.addOrReplaceChild("backLeftLeg", CubeListBuilder.create().texOffs(0, 0).addBox(0F, 0F, 0F, 1, 10, 1), PartPose.offsetAndRotation(8F, -4F, 5F, 0.4537856F, 0F, 0F));
        modelPartData.addOrReplaceChild("frontLeftLeg", CubeListBuilder.create().texOffs(0, 0).addBox(0F, 0F, -1F, 1, 10, 1), PartPose.offsetAndRotation(8F, -4F, 0F, -0.4537856F, 0F, 0F));
        modelPartData.addOrReplaceChild("frontRightLeg", CubeListBuilder.create().texOffs(0, 0).addBox(-1F, 0F, -1F, 1, 10, 1), PartPose.offsetAndRotation(-8F, -4F, 0F, -0.4537856F, 0F, 0F));
        modelPartData.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(0, 29).addBox(0F, -1F, 0F, 14, 1, 2), PartPose.offsetAndRotation(-10F, -4F, 11F, 0F, 1.570796F, 0F));
        modelPartData.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(0, 29).addBox(0F, 0F, 0F, 14, 1, 2), PartPose.offsetAndRotation(8F, -5F, 11F, 0F, 1.570796F, 0F));

        return LayerDefinition.create(modelData, 64, 32);
    }

    @Override
    public void setupAnim(ChairEntity chair, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public Iterable<ModelPart> parts() {
        return ImmutableList.of(
            seat, back, backRightLeg, backLeftLeg, frontLeftLeg, frontRightLeg, rightArm, leftArm
        );
    }
}
