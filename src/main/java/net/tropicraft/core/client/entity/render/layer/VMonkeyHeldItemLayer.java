package net.tropicraft.core.client.entity.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.core.common.entity.neutral.VMonkeyEntity;

@OnlyIn(Dist.CLIENT)
public class VMonkeyHeldItemLayer<T extends VMonkeyEntity, M extends EntityModel<T> & ArmedModel> extends ItemInHandLayer<T, M> {
    public VMonkeyHeldItemLayer(RenderLayerParent<T, M> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack stack, MultiBufferSource buffer, int packedLightIn, T monkey, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (monkey.isOrderedToSit() && !monkey.getMainHandItem().isEmpty()) {
            stack.pushPose();
            stack.translate(0.0F, 1.30F, -0.425F);
            stack.mulPose(Vector3f.ZP.rotationDegrees(180));
            stack.scale(0.5F, 0.5F, 0.5F);
            Minecraft.getInstance().getItemRenderer().renderStatic(monkey.getMainHandItem(), ItemTransforms.TransformType.NONE, packedLightIn, LivingEntityRenderer.getOverlayCoords(monkey, 0.0F), stack, buffer, monkey.getId());
            stack.popPose();
        }
    }
}
