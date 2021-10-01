package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.Constants;
import net.tropicraft.core.client.entity.model.FiddlerCrabModel;
import net.tropicraft.core.common.entity.passive.FiddlerCrabEntity;

public class FiddlerCrabRenderer extends MobRenderer<FiddlerCrabEntity, FiddlerCrabModel<FiddlerCrabEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MODID, "textures/entity/fiddler_crab.png");

    public FiddlerCrabRenderer(EntityRendererManager manager) {
        super(manager, new FiddlerCrabModel<>(), 0.3F);
    }

    @Override
    public ResourceLocation getEntityTexture(FiddlerCrabEntity entity) {
        return TEXTURE;
    }
}
