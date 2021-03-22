package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.MarlinModel;
import net.tropicraft.core.common.entity.underdasea.MarlinEntity;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class MarlinRenderer extends MobRenderer<MarlinEntity, MarlinModel> {
    public MarlinRenderer(EntityRendererManager renderManager) {
        super(renderManager, new MarlinModel(), 0.5F);
        shadowOpaque = 0.5f;
    }

    @Override
    public void render(MarlinEntity marlin, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        getEntityModel().inWater = marlin.isInWater();
        super.render(marlin, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Nullable
    @Override
    public ResourceLocation getEntityTexture(MarlinEntity marlin) {
        return TropicraftRenderUtils.getTextureEntity(marlin.getTexture());
    }
}
