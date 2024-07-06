package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.TropiCreeperModel;
import net.tropicraft.core.common.entity.passive.TropiCreeperEntity;

@OnlyIn(Dist.CLIENT)
public class TropiCreeperRenderer extends MobRenderer<TropiCreeperEntity, TropiCreeperModel> {

    private static final ResourceLocation CREEPER_TEXTURE = Tropicraft.location("textures/entity/tropicreeper.png");

    public TropiCreeperRenderer(EntityRendererProvider.Context context) {
        super(context, new TropiCreeperModel(context.bakeLayer(TropicraftRenderLayers.TROPI_CREEPER_LAYER)), 0.5f);
    }

    // From CreperRenderer
    @Override
    protected void scale(TropiCreeperEntity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        float f = entitylivingbaseIn.getCreeperFlashIntensity(partialTickTime);
        float f1 = 1.0f + Mth.sin(f * 100.0f) * f * 0.01f;
        f = Mth.clamp(f, 0.0f, 1.0f);
        f = f * f;
        f = f * f;
        float f2 = (1.0f + f * 0.4f) * f1;
        float f3 = (1.0f + f * 0.1f) / f1;
        matrixStackIn.scale(f2, f3, f2);
    }

    // From Creeper Renderer
    @Override
    protected float getWhiteOverlayProgress(TropiCreeperEntity livingEntityIn, float partialTicks) {
        float f = livingEntityIn.getCreeperFlashIntensity(partialTicks);
        return (int) (f * 10.0f) % 2 == 0 ? 0.0f : Mth.clamp(f, 0.5f, 1.0f);
    }

    @Override
    public ResourceLocation getTextureLocation(TropiCreeperEntity e) {
        return CREEPER_TEXTURE;
    }
}
