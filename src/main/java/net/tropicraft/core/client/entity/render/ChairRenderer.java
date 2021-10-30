package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.tropicraft.core.client.entity.model.ChairModel;
import net.tropicraft.core.common.entity.placeable.ChairEntity;

public class ChairRenderer extends FurnitureRenderer<ChairEntity> {

    public ChairRenderer(EntityRenderDispatcher rendererManager) {
        super(rendererManager, "chair", new ChairModel());
        shadowRadius = 0.65f;
    }
    
    @Override
    protected void setupTransforms(PoseStack stack) {
        stack.translate(0, 0, -0.15);
    }
}
