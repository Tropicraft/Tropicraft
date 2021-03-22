package net.tropicraft.core.client.entity.render.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.core.common.entity.neutral.VMonkeyEntity;

@OnlyIn(Dist.CLIENT)
public class VMonkeyHeldItemLayer<T extends VMonkeyEntity, M extends EntityModel<T> & IHasArm> extends HeldItemLayer<T, M> {
    public VMonkeyHeldItemLayer(IEntityRenderer<T, M> renderer) {
        super(renderer);
    }

    @Override
    public void render(MatrixStack stack, IRenderTypeBuffer buffer, int packedLightIn, T monkey, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (monkey.isQueuedToSit() && !monkey.getHeldItemMainhand().isEmpty()) {
            stack.push();
            stack.translate(0.0F, 1.30F, -0.425F);
            stack.rotate(Vector3f.ZP.rotationDegrees(180));
            stack.scale(0.5F, 0.5F, 0.5F);
            Minecraft.getInstance().getItemRenderer().renderItem(monkey.getHeldItemMainhand(), ItemCameraTransforms.TransformType.NONE, packedLightIn, LivingRenderer.getPackedOverlay(monkey, 0.0F), stack, buffer);
            stack.pop();
        }
    }
}
