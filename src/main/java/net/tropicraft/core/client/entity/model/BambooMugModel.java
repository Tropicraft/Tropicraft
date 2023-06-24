package net.tropicraft.core.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

public class BambooMugModel extends Model {
    private final List<ModelPart> parts;
    @Nullable
    private final ModelPart liquid;

    public BambooMugModel(ModelPart root, Function<ResourceLocation, RenderType> renderType, boolean hasLiquid) {
        super(renderType);
        parts = List.of(
                root.getChild("base"),
                root.getChild("wall1"),
                root.getChild("wall2"),
                root.getChild("wall3"),
                root.getChild("wall4"),
                root.getChild("handletop"),
                root.getChild("handlebottom"),
                root.getChild("handle")
        );
        liquid = hasLiquid ? root.getChild("liquid") : null;
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("base", CubeListBuilder.create().texOffs(10, 0).mirror().addBox(-2F, 23F, -2F, 4, 1, 4), PartPose.offset(0F, 0F, 0F));
        root.addOrReplaceChild("wall1", CubeListBuilder.create().texOffs(0, 10).mirror().addBox(-2F, 17F, -3F, 4, 6, 1), PartPose.offset(0F, 0F, 0F));
        root.addOrReplaceChild("wall2", CubeListBuilder.create().texOffs(0, 10).mirror().addBox(-2F, 17F, 2F, 4, 6, 1), PartPose.offset(0F, 0F, 0F));
        root.addOrReplaceChild("wall3", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(2F, 17F, -2F, 1, 6, 4), PartPose.offset(0F, 0F, 0F));
        root.addOrReplaceChild("wall4", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-3F, 17F, -2F, 1, 6, 4), PartPose.offset(0F, 0F, 0F));
        root.addOrReplaceChild("liquid", CubeListBuilder.create().texOffs(10, 5).mirror().addBox(-2F, 18F, -2F, 4, 1, 4), PartPose.offset(0F, 0F, 0F));
        root.addOrReplaceChild("handletop", CubeListBuilder.create().texOffs(26, 0).mirror().addBox(-1F, 18F, -4F, 2, 1, 1), PartPose.offset(0F, 0F, 0F));
        root.addOrReplaceChild("handlebottom", CubeListBuilder.create().texOffs(26, 2).mirror().addBox(-1F, 21F, -4F, 2, 1, 1), PartPose.offset(0F, 0F, 0F));
        root.addOrReplaceChild("handle", CubeListBuilder.create().texOffs(32, 0).mirror().addBox(-1F, 19F, -5F, 2, 2, 1), PartPose.offset(0F, 0F, 0F));

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        for (ModelPart part : parts) {
            part.render(poseStack, consumer, packedLight, packedOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
        }
        if (liquid != null) {
            liquid.render(poseStack, consumer, packedLight, packedOverlay, red, green, blue, alpha);
        }
    }
}
