package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix3f;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.passive.fish.AbstractFishEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.TropicraftSpecialRenderHelper;
import net.tropicraft.core.client.entity.model.AbstractFishModel;
import net.tropicraft.core.client.entity.model.SeaTurtleModel;
import net.tropicraft.core.common.entity.underdasea.IAtlasFish;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TropicraftFishRenderer<T extends AbstractFishEntity, M extends AbstractFishModel<T>> extends MobRenderer<T, M> {
    private TropicraftSpecialRenderHelper renderHelper;
    private static final Logger LOGGER = LogManager.getLogger();

    public TropicraftFishRenderer(final EntityRendererManager manager, M modelbase, float f) {
        super(manager, modelbase, f);
        renderHelper = new TropicraftSpecialRenderHelper();
    }

    /**
     * This override is a hack
     */
    @Override
    public void render(T entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        boolean flag = this.isVisible(entity);
        boolean flag1 = !flag && !entity.isInvisibleToPlayer(Minecraft.getInstance().player);
        RenderType rendertype = func_230042_a_(entity, flag, flag1);
        if (rendertype != null) {
          //  IVertexBuilder ivertexbuilder = bufferIn.getBuffer(rendertype);
            int i = getPackedOverlay(entity, getOverlayProgress(entity, partialTicks));
            entityModel.render(matrixStackIn, bufferIn.getBuffer(rendertype), packedLightIn, i, 1.0F, 1.0F, 1.0F, flag1 ? 0.15F : 1.0F);
            renderFishy(matrixStackIn, bufferIn.getBuffer(rendertype), entity, entityYaw, packedLightIn, i, partialTicks);
        }
//
//        boolean isVisible = this.isVisible(entity);
//        boolean shouldRender = !isVisible && !entity.isInvisibleToPlayer(Minecraft.getInstance().player);
//        if (isVisible || shouldRender) {
//            if (!this.bindEntityTexture(entity)) {
//                return;
//            }
//
//            if (shouldRender) {
//                RenderSystem.setProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
//            }
//
//            renderFishy(entity);
//
//            //this.entityModel.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
//            if (shouldRender) {
//                GlStateManager.unsetProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
//            }
//        }
    }
    
	private static void vertex(IVertexBuilder bufferIn, Matrix4f matrixIn, Matrix3f matrixNormalIn, float x, float y, float z, float red, float green, float blue, float alpha, float texU, float texV, int packedLight, int packedOverlay) {
		bufferIn.pos(matrixIn, x, y, z).color(red, green, blue, alpha).tex(texU, texV).overlay(packedOverlay).lightmap(packedLight).normal(matrixNormalIn, 0.0F, -1.0F, 0.0F).endVertex();
	}

    protected void renderFishy(final MatrixStack stack, final IVertexBuilder buffer, T entity, float yaw, int packedLightIn, int overlay, float partialTicks) {
        stack.push();

//        entityModel.body.render(stack, buffer, packedLightIn, overlay);
        //bindEntityTexture(entity);
        stack.rotate(Vector3f.YP.rotationDegrees(90F));
        stack.rotate(Vector3f.YP.rotationDegrees(-(MathHelper.lerp(partialTicks, entity.prevRotationYawHead, entity.rotationYawHead))));
        stack.translate(-.4F, 0.0F, 0.0F);
//
        int fishTex = 0;
        if (entity instanceof IAtlasFish) {
            fishTex = ((IAtlasFish) entity).getAtlasSlot()*2;
        }
        
        stack.scale(0.5f, 0.5f, 0.5f);
        renderSegment(buffer, stack.getLast().getMatrix(), stack.getLast().getNormal(), fishTex, packedLightIn, overlay);

//        renderHelper.renderFish(stack, buffer, fishTex, packedLightIn, overlay);
//
//        stack.rotate(Vector3f.YP.rotationDegrees(90F));
        float tailSwing = (float) (Math.sin((entity.ticksExisted + partialTicks) * .25F)) * .25F;
        stack.translate(1, 0, 0);
        stack.rotate(Vector3f.YP.rotation(tailSwing));
        renderSegment(buffer, stack.getLast().getMatrix(), stack.getLast().getNormal(), fishTex + 1, packedLightIn, overlay);
        stack.pop();
    }
    
    private void renderSegment(IVertexBuilder buffer, Matrix4f matrix, Matrix3f normal, int atlas, int light, int overlay) {
        float uMin = ((float) ((atlas % 8) * 32) + 0.0F) / 256F;
        float uMax = ((float) ((atlas % 8) * 32) + 31.99F) / 256F;
        float vMin = ((float) ((atlas / 8) * 32) + 0.0F) / 256F;
        float vMax = ((float) ((atlas / 8) * 32) + 31.99F) / 256F;
        
        vertex(buffer, matrix, normal, 0, 0, 0, 1, 1, 1, 1, uMin, vMax, light, overlay);
        vertex(buffer, matrix, normal, 1, 0, 0, 1, 1, 1, 1, uMax, vMax, light, overlay);
        vertex(buffer, matrix, normal, 1, 1, 0, 1, 1, 1, 1, uMax, vMin, light, overlay);
        vertex(buffer, matrix, normal, 0, 1, 0, 1, 1, 1, 1, uMin, vMin, light, overlay);
    }

    @Override
    protected void preRenderCallback(T entitylivingbaseIn, MatrixStack stack, float partialTickTime) {
        stack.scale(.75F, .20F, .20F);
    }

    @Override
    public ResourceLocation getEntityTexture(T entity) {
        return TropicraftRenderUtils.getTextureEntity("tropical_fish");
    }
}