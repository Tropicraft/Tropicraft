package net.tropicraft.core.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Vector3f;

import java.util.Set;

public abstract class MachineSpecialRenderer implements NoDataSpecialModelRenderer {
    private final Model model;
    private final Material material;

    public MachineSpecialRenderer(Model model, Material material) {
        this.model = model;
        this.material = material;
    }

    @Override
    public void render(ItemDisplayContext context, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, boolean hasFoilType) {
        poseStack.pushPose();
        transform(poseStack);
        VertexConsumer buffer = material.buffer(bufferSource, RenderType::entitySolid);
        model.renderToBuffer(poseStack, buffer, packedLight, packedOverlay);
        poseStack.popPose();
    }

    @Override
    public void getExtents(Set<Vector3f> output) {
        PoseStack poseStack = new PoseStack();
        transform(poseStack);
        model.root().getExtentsForGui(poseStack, output);
    }

    private static void transform(PoseStack poseStack) {
        poseStack.translate(0.5f, 1.5f, 0.5f);
        poseStack.mulPose(Axis.XP.rotationDegrees(180));
        poseStack.mulPose(Axis.YP.rotationDegrees(-90));
    }
}
