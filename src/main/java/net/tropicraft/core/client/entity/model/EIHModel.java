package net.tropicraft.core.client.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.tropicraft.core.common.entity.neutral.EIHEntity;

public class EIHModel extends ListModel<EIHEntity> {
    private ModelPart body;
    private ModelPart base;
    private ModelPart nose;
    private ModelPart mouth;
    private ModelPart top;
    private ModelPart leye;
    private ModelPart reye;

    public EIHModel(ModelPart root) {
        this.body = root.getChild("body");
        this.base = root.getChild("base");
        this.nose = root.getChild("nose");
        this.mouth = root.getChild("mouth");
        this.top = root.getChild("top");
        this.leye = root.getChild("leye");
        this.reye = root.getChild("reye");

        /*
        body = new ModelPart(this, 34, 8);
        body.addCuboid(-4F, 1.0F, -1F, 8, 17, 7);
        body.setPivot(0.0F, -2F, 0.0F);
        base = new ModelPart(this, 0, 0);
        base.addCuboid(-4F, 11F, -3F, 8, 8, 11);
        base.setPivot(0.0F, 5F, -2F);
        nose = new ModelPart(this, 27, 2);
        nose.addCuboid(13.5F, -1F, -3F, 13, 2, 3);
        nose.setPivot(0.0F, -14.8F, -1F);
        nose.roll = 1.570796F;
        mouth = new ModelPart(this, 56, 11);
        mouth.addCuboid(-1.5F, 4F, -1F, 3, 3, 1);
        mouth.setPivot(0.0F, 7.5F, -0.5F);
        top = new ModelPart(this, 0, 17);
        top.addCuboid(-4F, -1F, -10F, 8, 5, 10);
        top.setPivot(0.0F, -5F, 6F);
        leye = new ModelPart(this, 56, 7);
        leye.addCuboid(0.0F, 0.0F, 0.0F, 3, 3, 1);
        leye.setPivot(1.0F, -1F, -2F);
        leye.mirror = true;
        reye = new ModelPart(this, 56, 7);
        reye.addCuboid(-1.5F, -1F, -1F, 3, 3, 1);
        reye.setPivot(-2.5F, 0.0F, -1F);
         */
    }

    public static LayerDefinition create() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();

        modelPartData.addOrReplaceChild("body", CubeListBuilder.create().texOffs(34,8).addBox(-4F, 1.0F, -1F, 8, 17, 7), PartPose.offset(0.0F,-2F,0.0F));
        modelPartData.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0,0).addBox(-4F, 11F, -3F, 8, 8, 11), PartPose.offset(0.0F, 5F, -2F));
        modelPartData.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(27,2).addBox(13.5F, -1F, -3F, 13, 2, 3), PartPose.offsetAndRotation(0.0F, -14.8F, -1F,0,0, 1.570796F));
        modelPartData.addOrReplaceChild("mouth", CubeListBuilder.create().texOffs(56,11).addBox(-1.5F, 4F, -1F, 3, 3, 1), PartPose.offset(0.0F,7.5F,-0.5F));
        modelPartData.addOrReplaceChild("top", CubeListBuilder.create().texOffs(0,17).addBox(-4F, -1F, -10F, 8, 5, 10), PartPose.offset(0.0F, -5F, 6F));
        modelPartData.addOrReplaceChild("leye", CubeListBuilder.create().texOffs(56,7).mirror().addBox(0.0F, 0.0F, 0.0F, 3, 3, 1), PartPose.offset(1.0F, -1F, -2F));
        modelPartData.addOrReplaceChild("reye", CubeListBuilder.create().texOffs(56,7).addBox(-1.5F, -1F, -1F, 3, 3, 1), PartPose.offset(-2.5F, 0.0F, -1F));

        return LayerDefinition.create(modelData,64,32);
    }

    @Override
    public void setupAnim(EIHEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // it's a statue, what do you want from me
    }

    @Override
    public Iterable<ModelPart> parts() {
        return ImmutableList.of(body, base, nose, mouth, top, leye, reye);
    }
}
