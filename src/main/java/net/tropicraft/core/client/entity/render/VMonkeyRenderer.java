package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.Constants;
import net.tropicraft.core.client.entity.model.VMonkeyModel;
import net.tropicraft.core.client.entity.render.layer.VMonkeyHeldItemLayer;
import net.tropicraft.core.common.entity.neutral.VMonkeyEntity;

public class VMonkeyRenderer extends MobRenderer<VMonkeyEntity, VMonkeyModel> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MODID + ":textures/entity/monkeytext.png");
	private static final ResourceLocation ANGRY_TEXTURE = new ResourceLocation(Constants.MODID + ":textures/entity/monkey_angrytext.png");

    public VMonkeyRenderer(EntityRendererManager rendererManager) {
        super(rendererManager, new VMonkeyModel(), 0.5F);
        shadowSize = 0.3f;
        shadowOpaque = 0.5f;
        addLayer(new VMonkeyHeldItemLayer<>(this));
    }

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
    @Override
    public ResourceLocation getEntityTexture(VMonkeyEntity entity) {
        return entity.isAggressive() ? ANGRY_TEXTURE : TEXTURE;
	}
}
