package net.tropicraft.core.client.entity.render;

import net.tropicraft.core.client.entity.model.AbstractFishModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.TropicraftSpecialRenderHelper;
import net.tropicraft.core.common.entity.underdasea.IAtlasFish;

public class TropicraftFishRenderer<T extends AbstractFish, M extends AbstractFishModel<T>> extends MobRenderer<T, M> {
    private TropicraftSpecialRenderHelper renderHelper;
    private static final Logger LOGGER = LogManager.getLogger();

    public TropicraftFishRenderer(final EntityRenderDispatcher manager, M modelbase, float f) {
        super(manager, modelbase, f);
        renderHelper = new TropicraftSpecialRenderHelper();
    }

    /**
     * This override is a hack
     */
    @Override
    public void render(T entity, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        boolean isVisible = this.isBodyVisible(entity);
        boolean shouldRender = !isVisible && !entity.isInvisibleTo(Minecraft.getInstance().player);
        if (isVisible || shouldRender) {
            boolean glowing = Minecraft.getInstance().shouldEntityAppearGlowing(entity);
            renderFishy(entity, partialTicks, matrixStackIn, bufferIn.getBuffer(getRenderType(entity, isVisible, shouldRender, glowing)), packedLightIn, getOverlayCoords(entity, getWhiteOverlayProgress(entity, partialTicks)));
        }
        super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    protected void renderFishy(T entity, float partialTicks, PoseStack stack, VertexConsumer buffer, int light, int overlay) {
        stack.pushPose();

        stack.mulPose(Vector3f.YP.rotationDegrees(-90));
        stack.mulPose(Vector3f.YP.rotationDegrees(-(Mth.lerp(partialTicks, entity.yHeadRotO, entity.yHeadRot))));
        stack.mulPose(Vector3f.XP.rotationDegrees(180));
        stack.scale(0.3f, 0.3f, 0.5f);
        stack.translate(.85F, -0.3F, 0.0F);

        int fishTex = 0;
        if (entity instanceof IAtlasFish) {
            fishTex = ((IAtlasFish) entity).getAtlasSlot() * 2;
        }

        renderHelper.renderFish(stack, buffer, fishTex, light, overlay);

        stack.translate(-1.7f, 0, 0);
        stack.translate(.85f, 0, 0.025f);
        stack.mulPose(Vector3f.YP.rotation(model.tail.yRot));
        stack.translate(-.85f, 0, -0.025f);
        renderHelper.renderFish(stack, buffer, fishTex + 1, light, overlay);

        stack.popPose();
    }

    @Override
    protected void scale(T entity, PoseStack stack, float partialTickTime) {
        stack.scale(.75F, .20F, .20F);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return TropicraftRenderUtils.getTextureEntity("tropical_fish");
    }
}
