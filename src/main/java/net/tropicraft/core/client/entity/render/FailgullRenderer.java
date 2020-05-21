package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.Constants;
import net.tropicraft.core.client.entity.model.FailgullModel;
import net.tropicraft.core.common.entity.passive.FailgullEntity;
import net.tropicraft.core.common.entity.passive.TropiCreeperEntity;

@OnlyIn(Dist.CLIENT)
public class FailgullRenderer extends MobRenderer<FailgullEntity, FailgullModel> {
    private static final ResourceLocation FAILGULL_TEXTURE = new ResourceLocation(Constants.MODID, "textures/entity/failgull.png");

    public FailgullRenderer(EntityRendererManager rendererManager) {
        super(rendererManager, new FailgullModel(), 0.25F);
    }

    @Override
    public ResourceLocation getEntityTexture(FailgullEntity e) {
        return FAILGULL_TEXTURE;
    }
}
