package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.Constants;
import net.tropicraft.core.client.entity.model.TropiCreeperModel;
import net.tropicraft.core.common.entity.passive.TropiCreeperEntity;

@OnlyIn(Dist.CLIENT)
public class TropiCreeperRenderer extends MobRenderer<TropiCreeperEntity, TropiCreeperModel> {
    private static final ResourceLocation CREEPER_TEXTURE = new ResourceLocation(Constants.MODID, "textures/entity/tropicreeper.png");

    public TropiCreeperRenderer(EntityRendererManager rendererManager) {
        super(rendererManager, new TropiCreeperModel(), 0.5F);
    }

    public ResourceLocation getEntityTexture(TropiCreeperEntity e) {
        return CREEPER_TEXTURE;
    }
}
