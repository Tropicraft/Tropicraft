package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.Constants;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.FailgullModel;
import net.tropicraft.core.common.entity.passive.FailgullEntity;
import net.tropicraft.core.common.entity.passive.TropiCreeperEntity;

@OnlyIn(Dist.CLIENT)
public class FailgullRenderer extends MobRenderer<FailgullEntity, FailgullModel> {
    private static final ResourceLocation FAILGULL_TEXTURE = new ResourceLocation(Constants.MODID, "textures/entity/failgull.png");

    public FailgullRenderer(final EntityRendererProvider.Context context) {
        super(context, new FailgullModel(context.bakeLayer(TropicraftRenderLayers.FAILGULL_LAYER)), 0.25F);
    }

    @Override
    public ResourceLocation getTextureLocation(FailgullEntity e) {
        return FAILGULL_TEXTURE;
    }
}
