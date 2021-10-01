package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.Constants;
import net.tropicraft.core.client.entity.model.CuberaModel;
import net.tropicraft.core.common.entity.underdasea.CuberaEntity;

public class CuberaRenderer extends MobRenderer<CuberaEntity, CuberaModel<CuberaEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MODID, "textures/entity/cubera.png");

    public CuberaRenderer(EntityRendererManager renderManager) {
        super(renderManager, new CuberaModel<>(), 0.6F);
    }

    @Override
    public void render(CuberaEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
        getEntityModel().inWater = entity.isInWater();
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
    }

    @Override
    protected void preRenderCallback(CuberaEntity entity, MatrixStack matrixStack, float partialTicks) {
        matrixStack.scale(1.25F, 1.25F, 1.25F);
    }

    @Override
    public ResourceLocation getEntityTexture(CuberaEntity entity) {
        return TEXTURE;
    }
}
