package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.common.entity.projectile.PoisonBlotEntity;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class PoisonBlotRenderer extends EntityRenderer<PoisonBlotEntity> {

    public PoisonBlotRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    public void doRender(final PoisonBlotEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        final Tessellator tessellator = Tessellator.getInstance();
        bindEntityTexture(entity);
        GlStateManager.translated(x, y, z);

        GlStateManager.rotatef(180f - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);

        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        GlStateManager.normal3f(0.0F, 1.0F, 0.0F);
        buffer.pos(-.5, -.5, 0).tex(0, 1).endVertex();
        buffer.pos( .5, -.5, 0).tex(1, 1).endVertex();
        buffer.pos( .5,  .5, 0).tex(1, 0).endVertex();
        buffer.pos(-.5,  .5, 0).tex(0, 0).endVertex();
        tessellator.draw();

        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(PoisonBlotEntity entity) {
        return TropicraftRenderUtils.getTextureEntity("treefrog/blot");
    }
}
