package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.tropicraft.Constants;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.TapirModel;
import net.tropicraft.core.common.entity.passive.TapirEntity;

@OnlyIn(Dist.CLIENT)
public class TapirRenderer extends MobRenderer<TapirEntity, TapirModel<TapirEntity>> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Constants.MODID, "textures/entity/tapir.png");
    private static final ResourceLocation BABY_TEXTURE = ResourceLocation.fromNamespaceAndPath(Constants.MODID, "textures/entity/tapir_baby.png");

    public TapirRenderer(final EntityRendererProvider.Context context) {
        super(context, new TapirModel<>(context.bakeLayer(TropicraftRenderLayers.TAPIR_LAYER)), 0.6F);
    }

    @Override
    protected void scale(TapirEntity entity, PoseStack matrixStack, float partialTicks) {
        matrixStack.scale(0.8F, 0.8F, 0.8F);
    }

    @Override
    public ResourceLocation getTextureLocation(TapirEntity entity) {
        return entity.isBaby() ? BABY_TEXTURE : TEXTURE;
    }
}
