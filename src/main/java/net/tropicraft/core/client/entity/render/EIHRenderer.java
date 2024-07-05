package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.Constants;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.EIHModel;
import net.tropicraft.core.common.entity.neutral.EIHEntity;

import javax.annotation.Nullable;

public class EIHRenderer extends MobRenderer<EIHEntity, EIHModel> {

    private static final ResourceLocation TEXTURE_SLEEP = ResourceLocation.fromNamespaceAndPath(Constants.MODID, "textures/entity/eih/headtext.png");
    private static final ResourceLocation TEXTURE_AWARE = ResourceLocation.fromNamespaceAndPath(Constants.MODID, "textures/entity/eih/headawaretext.png");
    private static final ResourceLocation TEXTURE_ANGRY = ResourceLocation.fromNamespaceAndPath(Constants.MODID, "textures/entity/eih/headangrytext.png");

    public EIHRenderer(final EntityRendererProvider.Context context) {
        super(context, new EIHModel(context.bakeLayer(TropicraftRenderLayers.EIH_LAYER)), 1.2F);
    }

    @Override
    protected void scale(EIHEntity eih, PoseStack stack, float partialTickTime) {
        stack.scale(2.0F, 1.75F, 2.0F);
    }

    @Nullable
    @Override
    public ResourceLocation getTextureLocation(final EIHEntity eih) {
        if (eih.isAware()) {
            return TEXTURE_AWARE;
        } else if (eih.isAngry()) {
            return TEXTURE_ANGRY;
        } else {
            return TEXTURE_SLEEP;
        }
    }
}
