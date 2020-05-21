package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.AshenModel;
import net.tropicraft.core.client.entity.render.layer.AshenHeldItemLayer;
import net.tropicraft.core.client.entity.render.layer.AshenMaskLayer;
import net.tropicraft.core.common.entity.hostile.AshenEntity;

import javax.annotation.Nullable;

public class AshenRenderer extends MobRenderer<AshenEntity, AshenModel> {

    private static final ResourceLocation ASHEN_TEXTURE_LOCATION = TropicraftRenderUtils.bindTextureEntity("ashen/ashen");

    public AshenRenderer(EntityRendererManager manager) {
        super(manager, new AshenModel(), 0.5f);

        addLayer(new AshenMaskLayer(this));
        AshenHeldItemLayer<AshenEntity, AshenModel> layer = new AshenHeldItemLayer<>(this);
        layer.setAshenModel(entityModel);
        addLayer(layer);
        shadowOpaque = 0.5f;
        shadowSize = 0.3f;
    }

    @Override
    public void render(AshenEntity entityAshen, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        entityModel.actionState = entityAshen.getActionState();
        if (entityAshen.getAttackTarget() != null && entityAshen.getDistance(entityAshen.getAttackTarget()) < 5.0F && !entityAshen.isSwingInProgress) {
            entityModel.swinging = true;
        } else {
            if (entityAshen.isSwingInProgress && entityAshen.swingProgressInt > 6) {
                entityModel.swinging = false;
            }
        }
        super.render(entityAshen, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Nullable
    @Override
    public ResourceLocation getEntityTexture(AshenEntity ashenEntity) {
        return ASHEN_TEXTURE_LOCATION;
    }
}
