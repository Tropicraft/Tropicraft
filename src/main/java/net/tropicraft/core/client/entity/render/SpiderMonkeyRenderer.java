package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.Constants;
import net.tropicraft.core.client.entity.model.SpiderMonkeyModel;
import net.tropicraft.core.common.entity.passive.monkey.SpiderMonkeyEntity;

public class SpiderMonkeyRenderer extends MobRenderer<SpiderMonkeyEntity, SpiderMonkeyModel<SpiderMonkeyEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MODID, "textures/entity/spider_monkey.png");

    public SpiderMonkeyRenderer(EntityRendererManager manager) {
        super(manager, new SpiderMonkeyModel<>(), 0.4F);
    }

    @Override
    protected void preRenderCallback(SpiderMonkeyEntity entity, MatrixStack matrixStack, float partialTicks) {
        matrixStack.scale(0.7F, 0.7F, 0.7F);
    }

    @Override
    public ResourceLocation getEntityTexture(SpiderMonkeyEntity entity) {
        return TEXTURE;
    }
}
