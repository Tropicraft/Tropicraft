package net.tropicraft.core.client.entity.render;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.entity.passive.fish.AbstractFishEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.TropicraftSpecialRenderHelper;
import net.tropicraft.core.client.entity.model.AbstractFishModel;
import net.tropicraft.core.common.entity.underdasea.IAtlasFish;

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
        boolean isVisible = this.isVisible(entity);
        boolean shouldRender = !isVisible && !entity.isInvisibleToPlayer(Minecraft.getInstance().player);
        if (isVisible || shouldRender) {
            renderFishy(entity, partialTicks, matrixStackIn, bufferIn.getBuffer(func_230042_a_(entity, isVisible, shouldRender)), packedLightIn, getPackedOverlay(entity, getOverlayProgress(entity, partialTicks)));
        }
        super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    protected void renderFishy(T entity, float partialTicks, MatrixStack stack, IVertexBuilder buffer, int light, int overlay) {
        stack.push();

		stack.rotate(Vector3f.YP.rotationDegrees(-90));
		stack.rotate(Vector3f.YP.rotationDegrees(-(MathHelper.lerp(partialTicks, entity.prevRotationYawHead, entity.rotationYawHead))));
		stack.rotate(Vector3f.XP.rotationDegrees(180));
		stack.scale(0.3f, 0.3f, 0.5f);
        stack.translate(.85F, -0.3F, 0.0F);

		int fishTex = 0;
		if (entity instanceof IAtlasFish) {
			fishTex = ((IAtlasFish) entity).getAtlasSlot() * 2;
		}

		renderHelper.renderFish(stack, buffer, fishTex, light, overlay);

		stack.translate(-1.7f, 0, 0);
		stack.translate(.85f, 0, 0.025f);
		stack.rotate(Vector3f.YP.rotation(entityModel.tail.rotateAngleY));
		stack.translate(-.85f, 0, -0.025f);
		renderHelper.renderFish(stack, buffer, fishTex + 1, light, overlay);

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