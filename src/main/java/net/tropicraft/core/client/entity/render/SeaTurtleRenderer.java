package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.SeaTurtleModel;
import net.tropicraft.core.common.entity.SeaTurtleEntity;

public class SeaTurtleRenderer extends MobRenderer<SeaTurtleEntity, SeaTurtleModel> {

    public SeaTurtleRenderer(EntityRendererManager renderManager) {
        super(renderManager, new SeaTurtleModel(), 0.7F);
        shadowSize = 0.5f;
        shadowOpaque = 0.5f;
    }

    public void render(SeaTurtleEntity turtle, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn, int packedLightIn) {
        float scale = 0.3f;
        final float existingTime = (float) turtle.ticksExisted / 4000;
        if (turtle.ticksExisted < 30) {
            shadowOpaque = 0.5f;
            shadowSize = 0.2f + existingTime;
            if (shadowSize > 0.5f) {
                shadowSize = 0.5f;
            }
        } else {
            scale = 0.3f + existingTime;
            if (scale > 1f) {
                scale = 1f;
            }
        }
        if (turtle.isMature()) {
            scale = 1f;
        }
        stack.push();
        stack.scale(scale, scale, scale);

        super.render(turtle, entityYaw, partialTicks, stack, bufferIn, packedLightIn);

        stack.pop();
    }

    @Override
    public ResourceLocation getEntityTexture(SeaTurtleEntity seaTurtleEntity) {
        return TropicraftRenderUtils.getTextureEntity(String.format("turtle/sea_turtle%s", seaTurtleEntity.getTurtleType()));
    }
}
