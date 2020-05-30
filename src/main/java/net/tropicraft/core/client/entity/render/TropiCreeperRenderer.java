package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.Constants;
import net.tropicraft.core.client.entity.model.TropiCreeperModel;
import net.tropicraft.core.common.entity.passive.TropiCreeperEntity;

@OnlyIn(Dist.CLIENT)
public class TropiCreeperRenderer extends MobRenderer<TropiCreeperEntity, TropiCreeperModel> {

    private static final ResourceLocation CREEPER_TEXTURE = new ResourceLocation(Constants.MODID, "textures/entity/tropicreeper.png");

    public TropiCreeperRenderer(EntityRendererManager rendererManager) {
        super(rendererManager, new TropiCreeperModel(), 0.5F);
    }

	// From CreperRenderer
	@Override
	protected void preRenderCallback(TropiCreeperEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
		float f = entitylivingbaseIn.getCreeperFlashIntensity(partialTickTime);
		float f1 = 1.0F + MathHelper.sin(f * 100.0F) * f * 0.01F;
		f = MathHelper.clamp(f, 0.0F, 1.0F);
		f = f * f;
		f = f * f;
		float f2 = (1.0F + f * 0.4F) * f1;
		float f3 = (1.0F + f * 0.1F) / f1;
		matrixStackIn.scale(f2, f3, f2);
	}

	// From Creeper Renderer
	@Override
	protected float getOverlayProgress(TropiCreeperEntity livingEntityIn, float partialTicks) {
		float f = livingEntityIn.getCreeperFlashIntensity(partialTicks);
		return (int) (f * 10.0F) % 2 == 0 ? 0.0F : MathHelper.clamp(f, 0.5F, 1.0F);
	}

    public ResourceLocation getEntityTexture(TropiCreeperEntity e) {
        return CREEPER_TEXTURE;
    }
}
