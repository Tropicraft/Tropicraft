package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.TropicraftDolphinModel;
import net.tropicraft.core.common.entity.underdasea.TropicraftDolphinEntity;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class TropicraftDolphinRenderer extends MobRenderer<TropicraftDolphinEntity, TropicraftDolphinModel> {

    public TropicraftDolphinRenderer(EntityRendererManager renderManager) {
        super(renderManager, new TropicraftDolphinModel(), 0.5F);
		shadowOpaque = 0.5f;
    }

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(TropicraftDolphinEntity dolphin) {
		return TropicraftRenderUtils.getTextureEntity(dolphin.getTexture());
	}
}
