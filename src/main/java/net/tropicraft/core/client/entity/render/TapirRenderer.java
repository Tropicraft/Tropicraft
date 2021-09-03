package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.Constants;
import net.tropicraft.core.client.entity.model.TapirModel;
import net.tropicraft.core.common.entity.passive.TapirEntity;

@OnlyIn(Dist.CLIENT)
public class TapirRenderer extends MobRenderer<TapirEntity, TapirModel<TapirEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MODID, "textures/entity/tapir.png");

    public TapirRenderer(EntityRendererManager manager) {
        super(manager, new TapirModel<>(), 0.6F);
    }

    @Override
    protected void preRenderCallback(TapirEntity entity, MatrixStack matrixStack, float partialTicks) {
        matrixStack.scale(0.8F, 0.8F, 0.8F);
    }

    @Override
    public ResourceLocation getEntityTexture(TapirEntity entity) {
        return TEXTURE;
    }
}
