package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.UmbrellaModel;
import net.tropicraft.core.common.entity.placeable.UmbrellaEntity;

import javax.annotation.Nullable;

public class UmbrellaRenderer extends FurnitureRenderer<UmbrellaEntity> {

    public UmbrellaRenderer(final EntityRendererProvider.Context context) {
        super(context, "umbrella", new UmbrellaModel(context.bakeLayer(TropicraftRenderLayers.UMBRELLA_LAYER)), 4);
        this.shadowRadius = 2.5f;
    }

    //TODO
//    @Override
//    public void doRenderShadowAndFire(Entity entityIn, double x, double y, double z, float yaw, float partialTicks) {
//        if (this.renderManager.options != null) {
//            if (this.renderManager.options.entityShadows && this.shadowSize > 0.0F && !entityIn.isInvisible() && this.renderManager.isRenderShadow()) {
//                // Don't do distance culling for umbrella shadows
//                UmbrellaEntity entityUmbrella = (UmbrellaEntity) entityIn;
//
//                // Calculate which direction the umbrella is "shaking"
//                Vector3d offsetVec = Vector3d.ZERO;
//                float offset = getRockingAngle(entityUmbrella, partialTicks) / 10f;
//                if (offset != 0.0F) {
//                    offsetVec = offsetVec.add(offset, 0, 0).rotateYaw((float) Math.toRadians(90 - yaw));
//                }
//
//                // Move around the shadow renderer based on the shake
//                entityIn.lastTickPosX += offsetVec.x;
//                entityIn.posX += offsetVec.x;
//                entityIn.lastTickPosZ += offsetVec.z;
//                entityIn.posZ += offsetVec.z;
//                this.renderShadow(entityIn, x + offsetVec.x, y, z + offsetVec.z, shadowOpaque, partialTicks);
//                entityIn.lastTickPosX -= offsetVec.x;
//                entityIn.posX -= offsetVec.x;
//                entityIn.lastTickPosZ -= offsetVec.z;
//                entityIn.posZ -= offsetVec.z;
//            }
//
//            if (entityIn.canRenderOnFire() && !entityIn.isSpectator()) {
//                this.renderEntityOnFire(entityIn, x, y, z, partialTicks);
//            }
//        }
//    }

    @Nullable
    @Override
    public ResourceLocation getTextureLocation(final UmbrellaEntity umbrella) {
        return null;
    }
}
