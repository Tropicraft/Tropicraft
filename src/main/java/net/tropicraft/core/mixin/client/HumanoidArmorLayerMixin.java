package net.tropicraft.core.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.tropicraft.core.common.item.scuba.ScubaArmorItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public class HumanoidArmorLayerMixin {
    @Inject(method = "renderModel(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/item/ArmorItem;Lnet/minecraft/client/model/Model;ZFFFLnet/minecraft/resources/ResourceLocation;)V", at = @At("HEAD"), remap = false, cancellable = true, require = 0)
    private void renderModel(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, ArmorItem armor, Model model, boolean glint, float red, float green, float blue, ResourceLocation texture, CallbackInfo ci) {
        if (armor instanceof ScubaArmorItem) {
            VertexConsumer consumer = bufferSource.getBuffer(model.renderType(texture));
            model.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0f);
            ci.cancel();
        }
    }
}
