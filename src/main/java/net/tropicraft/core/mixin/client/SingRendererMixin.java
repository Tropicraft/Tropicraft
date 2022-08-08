package net.tropicraft.core.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.block.TropicraftStandingSignBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(SignRenderer.class)
public class SingRendererMixin {

    @Unique
    private static BlockState savedState;

    @Inject(method = "render(Lnet/minecraft/world/level/block/entity/SignBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/blockentity/SignRenderer;getWoodType(Lnet/minecraft/world/level/block/Block;)Lnet/minecraft/world/level/block/state/properties/WoodType;"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void tropicraft$saveBlockState(SignBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay, CallbackInfo ci, BlockState state, float f){
        savedState = state;
    }

    @ModifyVariable(method = "render(Lnet/minecraft/world/level/block/entity/SignBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/blockentity/SignRenderer;getWoodType(Lnet/minecraft/world/level/block/Block;)Lnet/minecraft/world/level/block/state/properties/WoodType;", shift = At.Shift.BY, by = 2))
    private WoodType changeWoodType(WoodType value){
        if(savedState.is(TropicraftBlocks.MANGROVE_SIGN.get())){
            return savedState.getValue(TropicraftStandingSignBlock.BARK_TYPE).type;
        }

        return value;
    }
}
