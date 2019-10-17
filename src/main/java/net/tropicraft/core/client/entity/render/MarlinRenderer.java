package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.MarlinModel;
import net.tropicraft.core.common.entity.underdasea.MarlinEntity;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class MarlinRenderer extends MobRenderer<MarlinEntity, MarlinModel> {
    public MarlinRenderer(EntityRendererManager renderManager) {
        super(renderManager, new MarlinModel(), 0.7F);
        shadowSize = 0.5f;
		shadowOpaque = 0.5f;
    }

    @Override
	public void doRender(MarlinEntity marlin, double x, double y, double z, float entityYaw, float partialTicks) {
		getEntityModel().inWater = marlin.isInWater();
		super.doRender(marlin, x, y, z, entityYaw, partialTicks);
	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(MarlinEntity marlin) {
		return TropicraftRenderUtils.getTextureEntity(marlin.getTexture());
	}
}
