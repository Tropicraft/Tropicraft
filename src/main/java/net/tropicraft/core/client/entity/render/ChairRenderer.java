package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.tropicraft.core.client.entity.model.ChairModel;
import net.tropicraft.core.common.entity.placeable.ChairEntity;

public class ChairRenderer extends FurnitureRenderer<ChairEntity> {

    public ChairRenderer(EntityRendererManager rendererManager) {
        super(rendererManager, "chair", new ChairModel(), 0.0625f);
        this.shadowSize = 0.65f;
    }
    
    @Override
    protected void setupTransforms() {
        GlStateManager.translated(0, 0, -0.15);
    }
}
