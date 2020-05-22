package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.passive.fish.AbstractFishEntity;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.TropicraftSpecialRenderHelper;
import net.tropicraft.core.client.entity.model.AbstractFishModel;
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
            //entityModel.render(matrixStackIn, ivertexbuilder, packedLightIn, i, 1.0F, 1.0F, 1.0F, flag1 ? 0.15F : 1.0F);

            renderFishy(matrixStackIn, bufferIn, TropicraftRenderUtils.getEntityCutoutBuilder(bufferIn, getEntityTexture(entity)), entity, packedLightIn, i);
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

    protected void renderFishy(final MatrixStack stack, final IRenderTypeBuffer bufferIn, final IVertexBuilder buffer, T entity, int packedLightIn, int overlay) {
        stack.push();

        entityModel.body.render(stack, buffer, packedLightIn, overlay);
        //bindEntityTexture(entity);
        stack.rotate(Vector3f.YP.rotationDegrees(90F));
        stack.translate(.85F, 0.0F, 0.0F);

        int fishTex = 0;
        if (entity instanceof IAtlasFish) {
            fishTex = ((IAtlasFish) entity).getAtlasSlot()*2;
        }

        renderHelper.renderFish(stack, buffer, fishTex, packedLightIn, overlay);
        stack.pop();

        stack.push();
        entityModel.tail.render(stack, buffer, packedLightIn, overlay);
        stack.rotate(Vector3f.YP.rotationDegrees(90F));
        stack.translate(-.90F, 0.725F, 0.0F);
        renderHelper.renderFish(stack, buffer, fishTex + 1, packedLightIn, overlay);
        stack.pop();
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