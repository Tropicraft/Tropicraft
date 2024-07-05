package net.tropicraft.core.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.tropicraft.core.client.scuba.ModelScubaGear;
import net.tropicraft.core.common.item.scuba.ScubaArmorItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public class HumanoidArmorLayerMixin<T extends LivingEntity, A extends HumanoidModel<T>> {
    @Inject(method = "renderModel(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/model/Model;ILnet/minecraft/resources/ResourceLocation;)V", at = @At("HEAD"), remap = false, cancellable = true, require = 0)
    private void renderModel(final PoseStack poseStack, final MultiBufferSource bufferSource, final int packedLight, final Model model, final int color, final ResourceLocation texture, final CallbackInfo ci) {
        if (model instanceof ModelScubaGear) {
            VertexConsumer consumer = bufferSource.getBuffer(model.renderType(texture));
            model.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, color);
            ci.cancel();
        }
    }
}
