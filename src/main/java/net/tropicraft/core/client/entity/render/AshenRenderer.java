package net.tropicraft.core.client.entity.render;

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

    public void doRender(AshenEntity entityAshen, double x, double y, double z, float yaw, float pitch) {
        entityModel.actionState = entityAshen.getActionState();
        if (entityAshen.getAttackTarget() != null && entityAshen.getDistance(entityAshen.getAttackTarget()) < 5.0F && !entityAshen.isSwingInProgress) {
            entityModel.swinging = true;
        } else {
            if (entityAshen.isSwingInProgress && entityAshen.swingProgressInt > 6) {
                entityModel.swinging = false;
            }
        }
        super.doRender(entityAshen, x, y, z, yaw, pitch);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(AshenEntity ashenEntity) {
        return ASHEN_TEXTURE_LOCATION;
    }
}
