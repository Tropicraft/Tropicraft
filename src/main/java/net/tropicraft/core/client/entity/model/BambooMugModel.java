package net.tropicraft.core.client.entity.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.function.Function;

public class BambooMugModel extends Model {

    public ModelPart base;
    public ModelPart wall1;
    public ModelPart wall2;
    public ModelPart wall3;
    public ModelPart wall4;
    public ModelPart liquid;
    public ModelPart handletop;
    public ModelPart handlebottom;
    public ModelPart handle;

    public boolean renderLiquid;
    public int liquidColor;

    public BambooMugModel(ModelPart root, Function<ResourceLocation, RenderType> renderTypeIn) {
        super(renderTypeIn);
        this.base = root.getChild("base");
        this.wall1 = root.getChild("wall1");
        this.wall2 = root.getChild("wall2");
        this.wall3 = root.getChild("wall3");
        this.wall4 = root.getChild("wall4");
        this.liquid = root.getChild("liquid");
        this.handletop = root.getChild("handletop");
        this.handlebottom = root.getChild("handlebottom");
        this.handle = root.getChild("handle");

    }

    public static LayerDefinition create() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();

        modelPartData.addOrReplaceChild("base", CubeListBuilder.create().texOffs(10,0).mirror().addBox(-2F, 23F, -2F, 4, 1, 4), PartPose.offset(0F, 0F, 0F));
        modelPartData.addOrReplaceChild("wall1", CubeListBuilder.create().texOffs(0,10).mirror().addBox(-2F, 17F, -3F, 4, 6, 1),PartPose.offset(0F, 0F, 0F));
        modelPartData.addOrReplaceChild("wall2", CubeListBuilder.create().texOffs(0,10).mirror().addBox(-2F, 17F, 2F, 4, 6, 1),PartPose.offset(0F, 0F, 0F));
        modelPartData.addOrReplaceChild("wall3", CubeListBuilder.create().texOffs(0,0).mirror().addBox(2F, 17F, -2F, 1, 6, 4),PartPose.offset(0F, 0F, 0F));
        modelPartData.addOrReplaceChild("wall4", CubeListBuilder.create().texOffs(0,0).mirror().addBox(-3F, 17F, -2F, 1, 6, 4),PartPose.offset(0F, 0F, 0F));
        modelPartData.addOrReplaceChild("liquid", CubeListBuilder.create().texOffs(10,5).mirror().addBox(-2F, 18F, -2F, 4, 1, 4),PartPose.offset(0F, 0F, 0F));
        modelPartData.addOrReplaceChild("handletop", CubeListBuilder.create().texOffs(26,0).mirror().addBox(-1F, 18F, -4F, 2, 1, 1),PartPose.offset(0F, 0F, 0F));
        modelPartData.addOrReplaceChild("handlebottom", CubeListBuilder.create().texOffs(26,2).mirror().addBox(-1F, 21F, -4F, 2, 1, 1),PartPose.offset(0F, 0F, 0F));
        modelPartData.addOrReplaceChild("handle", CubeListBuilder.create().texOffs(32,0).mirror().addBox(-1F, 19F, -5F, 2, 2, 1),PartPose.offset(0F, 0F, 0F));

        return LayerDefinition.create(modelData,64,32);
    }

    public Iterable<ModelPart> getMugParts() {
        return ImmutableList.of(
            base, wall1, wall2, wall3, wall4, handletop, handlebottom, handle
        );
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        getMugParts().forEach((part) -> {
            part.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        });

        if (renderLiquid) {
            float r = (float)(liquidColor >> 16 & 255) / 255.0F;
            float g = (float)(liquidColor >> 8 & 255) / 255.0F;
            float b = (float)(liquidColor & 255) / 255.0F;

            liquid.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red * r, green * g, blue * b, alpha);
        }
    }
}
