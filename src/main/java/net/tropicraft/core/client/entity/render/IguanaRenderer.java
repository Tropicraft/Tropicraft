package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.Constants;
import net.tropicraft.core.client.entity.model.IguanaModel;
import net.tropicraft.core.common.entity.neutral.IguanaEntity;

public class IguanaRenderer extends MobRenderer<IguanaEntity, IguanaModel> {
    private static final String IGOR = "igor";

    private static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation(Constants.MODID, "textures/entity/iggy.png");
    private static final ResourceLocation IGOR_TEXTURE = new ResourceLocation(Constants.MODID, "textures/entity/iggy_igor.png");

    public IguanaRenderer(final EntityRenderDispatcher rendererManager) {
        super(rendererManager, new IguanaModel(), 0.5F);
        this.shadowStrength = 0.5f;
    }

    @Override
    public ResourceLocation getTextureLocation(final IguanaEntity entity) {
        if (entity.getName().getString().equalsIgnoreCase(IGOR)) {
            return IGOR_TEXTURE;
        }

        return DEFAULT_TEXTURE;
    }
}
