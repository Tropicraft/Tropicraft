package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.Constants;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.SpiderMonkeyModel;
import net.tropicraft.core.common.entity.passive.monkey.SpiderMonkeyEntity;

public class SpiderMonkeyRenderer extends MobRenderer<SpiderMonkeyEntity, SpiderMonkeyModel<SpiderMonkeyEntity>> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Constants.MODID, "textures/entity/spider_monkey.png");

    public SpiderMonkeyRenderer(EntityRendererProvider.Context context) {
        super(context, new SpiderMonkeyModel<>(context.bakeLayer(TropicraftRenderLayers.SPIDER_MONKEY_LAYER)), 0.4F);
    }

    @Override
    protected void scale(SpiderMonkeyEntity entity, PoseStack matrixStack, float partialTicks) {
        matrixStack.scale(0.7F, 0.7F, 0.7F);
    }

    @Override
    public ResourceLocation getTextureLocation(SpiderMonkeyEntity entity) {
        return TEXTURE;
    }
}
