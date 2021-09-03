package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.Constants;
import net.tropicraft.core.client.entity.model.JaguarModel;
import net.tropicraft.core.common.entity.neutral.JaguarEntity;

@OnlyIn(Dist.CLIENT)
public class JaguarRenderer extends MobRenderer<JaguarEntity, JaguarModel<JaguarEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MODID, "textures/entity/jaguar.png");

    public JaguarRenderer(EntityRendererManager manager) {
        super(manager, new JaguarModel<>(), 0.7F);
    }

    @Override
    public ResourceLocation getEntityTexture(JaguarEntity entity) {
        return TEXTURE;
    }
}
