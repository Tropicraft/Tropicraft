package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.Constants;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.CuberaModel;
import net.tropicraft.core.common.entity.underdasea.CuberaEntity;

public class CuberaRenderer extends MobRenderer<CuberaEntity, CuberaModel<CuberaEntity>> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Constants.MODID, "textures/entity/cubera.png");

    public CuberaRenderer(EntityRendererProvider.Context context) {
        super(context, new CuberaModel<>(context.bakeLayer(TropicraftRenderLayers.CUBERA_LAYER)), 0.6F);
    }

    @Override
    protected void scale(CuberaEntity entity, PoseStack matrixStack, float partialTicks) {
        matrixStack.scale(1.25F, 1.25F, 1.25F);
    }

    @Override
    public ResourceLocation getTextureLocation(CuberaEntity entity) {
        return TEXTURE;
    }
}
