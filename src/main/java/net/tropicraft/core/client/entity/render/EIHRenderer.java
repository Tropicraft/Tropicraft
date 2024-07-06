package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.EIHModel;
import net.tropicraft.core.common.entity.neutral.EIHEntity;

public class EIHRenderer extends MobRenderer<EIHEntity, EIHModel> {

    private static final ResourceLocation TEXTURE_SLEEP = Tropicraft.location("textures/entity/eih/headtext.png");
    private static final ResourceLocation TEXTURE_AWARE = Tropicraft.location("textures/entity/eih/headawaretext.png");
    private static final ResourceLocation TEXTURE_ANGRY = Tropicraft.location("textures/entity/eih/headangrytext.png");

    public EIHRenderer(EntityRendererProvider.Context context) {
        super(context, new EIHModel(context.bakeLayer(TropicraftRenderLayers.EIH_LAYER)), 1.2f);
    }

    @Override
    protected void scale(EIHEntity eih, PoseStack stack, float partialTickTime) {
        stack.scale(2.0f, 1.75f, 2.0f);
    }

    @Override
    public ResourceLocation getTextureLocation(EIHEntity eih) {
        if (eih.isAware()) {
            return TEXTURE_AWARE;
        } else if (eih.isAngry()) {
            return TEXTURE_ANGRY;
        } else {
            return TEXTURE_SLEEP;
        }
    }
}
