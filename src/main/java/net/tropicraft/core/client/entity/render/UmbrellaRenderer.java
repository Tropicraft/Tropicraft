package net.tropicraft.core.client.entity.render;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.tropicraft.core.client.entity.model.UmbrellaModel;
import net.tropicraft.core.common.entity.placeable.UmbrellaEntity;

public class UmbrellaRenderer extends FurnitureRenderer<UmbrellaEntity> {

    public UmbrellaRenderer(EntityRendererManager rendererManager) {
        super(rendererManager, "umbrella", new UmbrellaModel(), 0.25f);
        this.shadowSize = 2.5f;
    }
    
    @Override
    public void doRenderShadowAndFire(Entity entityIn, double x, double y, double z, float yaw, float partialTicks) {
        if (this.renderManager.options != null) {
            if (this.renderManager.options.entityShadows && this.shadowSize > 0.0F && !entityIn.isInvisible() && this.renderManager.isRenderShadow()) {
                // Don't do distance culling for umbrella shadows
                UmbrellaEntity entityUmbrella = (UmbrellaEntity) entityIn;
 
                // Calculate which direction the umbrella is "shaking"
                Vec3d offsetVec = Vec3d.ZERO;
                float offset = getRockingAngle(entityUmbrella, partialTicks) / 10f;
                if (offset != 0.0F) {
                    offsetVec = offsetVec.add(offset, 0, 0).rotateYaw((float) Math.toRadians(90 - yaw));
                }
                
                // Move around the shadow renderer based on the shake
                entityIn.lastTickPosX += offsetVec.x;
                entityIn.posX += offsetVec.x;
                entityIn.lastTickPosZ += offsetVec.z;
                entityIn.posZ += offsetVec.z;
                this.renderShadow(entityIn, x + offsetVec.x, y, z + offsetVec.z, shadowOpaque, partialTicks);
                entityIn.lastTickPosX -= offsetVec.x;
                entityIn.posX -= offsetVec.x;
                entityIn.lastTickPosZ -= offsetVec.z;
                entityIn.posZ -= offsetVec.z;
            }

            if (entityIn.canRenderOnFire() && !entityIn.isSpectator()) {
                this.renderEntityOnFire(entityIn, x, y, z, partialTicks);
            }
        }
    }

    @Nullable
    @Override
    public ResourceLocation getEntityTexture(final UmbrellaEntity umbrella) {
        return null;
    }
}
