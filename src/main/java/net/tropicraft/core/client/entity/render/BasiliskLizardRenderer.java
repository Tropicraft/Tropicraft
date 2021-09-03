package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.Constants;
import net.tropicraft.core.client.entity.model.BasiliskLizardModel;
import net.tropicraft.core.common.entity.passive.basilisk.BasiliskLizardEntity;

@OnlyIn(Dist.CLIENT)
public class BasiliskLizardRenderer extends MobRenderer<BasiliskLizardEntity, BasiliskLizardModel<BasiliskLizardEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MODID, "textures/entity/basilisk_lizard.png");

    public BasiliskLizardRenderer(EntityRendererManager manager) {
        super(manager, new BasiliskLizardModel<>(), 0.3F);
    }

    @Override
    public ResourceLocation getEntityTexture(BasiliskLizardEntity entity) {
        return TEXTURE;
    }
}
