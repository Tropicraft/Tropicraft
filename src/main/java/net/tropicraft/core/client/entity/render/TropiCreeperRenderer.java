package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.Constants;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.TropiCreeperModel;
import net.tropicraft.core.common.entity.passive.TropiCreeperEntity;

@OnlyIn(Dist.CLIENT)
public class TropiCreeperRenderer extends MobRenderer<TropiCreeperEntity, TropiCreeperModel> {

    private static final ResourceLocation CREEPER_TEXTURE = new ResourceLocation(Constants.MODID, "textures/entity/tropicreeper.png");

    public TropiCreeperRenderer(final EntityRendererProvider.Context context) {
        super(context, new TropiCreeperModel(context.bakeLayer(TropicraftRenderLayers.TROPI_CREEPER_LAYER)), 0.5F);
    }

    // From CreperRenderer
    @Override
    protected void scale(TropiCreeperEntity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        float f = entitylivingbaseIn.getCreeperFlashIntensity(partialTickTime);
        float f1 = 1.0F + Mth.sin(f * 100.0F) * f * 0.01F;
        f = Mth.clamp(f, 0.0F, 1.0F);
        f = f * f;
        f = f * f;
        float f2 = (1.0F + f * 0.4F) * f1;
        float f3 = (1.0F + f * 0.1F) / f1;
        matrixStackIn.scale(f2, f3, f2);
    }

    // From Creeper Renderer
    @Override
    protected float getWhiteOverlayProgress(TropiCreeperEntity livingEntityIn, float partialTicks) {
        float f = livingEntityIn.getCreeperFlashIntensity(partialTicks);
        return (int) (f * 10.0F) % 2 == 0 ? 0.0F : Mth.clamp(f, 0.5F, 1.0F);
    }

    public ResourceLocation getTextureLocation(TropiCreeperEntity e) {
        return CREEPER_TEXTURE;
    }
}
