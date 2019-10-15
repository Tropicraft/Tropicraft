package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.SeaTurtleModel;
import net.tropicraft.core.common.entity.SeaTurtleEntity;

import javax.annotation.Nullable;

public class RenderSeaTurtle extends MobRenderer<SeaTurtleEntity, SeaTurtleModel> {

    public RenderSeaTurtle(EntityRendererManager renderManager) {
        super(renderManager, new SeaTurtleModel(), 0.7F);
        shadowSize = 0.5f;
		shadowOpaque = 0.5f;
    }

	public void doRender(SeaTurtleEntity turtle, double x, double y, double z, float entityYaw, float partielTicks) {
		float scale = 0.3f;
		if (turtle.ticksExisted < 30) {
			this.shadowOpaque = 0.5f;
			this.shadowSize = 0.2f + (((float) turtle.ticksExisted/4000));
			if(this.shadowSize > 0.5f) {
				this.shadowSize = 0.5f;
			}
		} else {
			scale = 0.3f+(((float) turtle.ticksExisted/4000));
			if (scale > 1f) {
				scale = 1f;
			}
		}
		if (turtle.isMature()) {
			scale = 1f;
		}
		GlStateManager.pushMatrix();
		GlStateManager.translated(x, y, z);
		GlStateManager.scalef(scale, scale, scale);

		super.doRender(turtle, 0, 0, 0, 0, partielTicks);

		GlStateManager.popMatrix();
	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(SeaTurtleEntity seaTurtleEntity) {
		return TropicraftRenderUtils.getTextureEntity(String.format("turtle/sea_turtle%s", seaTurtleEntity.getTurtleType()));
	}
}
