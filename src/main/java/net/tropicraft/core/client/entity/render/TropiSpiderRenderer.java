package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SpiderRenderer;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.entity.hostile.TropiSpiderEntity;

public class TropiSpiderRenderer extends SpiderRenderer<TropiSpiderEntity> {
    private static final ResourceLocation ADULT_TEXTURE_LOCATION = Tropicraft.location("textures/entity/spideradult.png");
    private static final ResourceLocation MOTHER_TEXTURE_LOCATION = Tropicraft.location("textures/entity/spidermother.png");
    private static final ResourceLocation CHILD_TEXTURE_LOCATION = Tropicraft.location("textures/entity/spiderchild.png");

    public TropiSpiderRenderer(EntityRendererProvider.Context context) {
        super(context);
        shadowStrength = 0.5f;
    }

    @Override
    public void render(TropiSpiderEntity spider, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
        stack.pushPose();
        float scale = getScale(spider);
        shadowRadius = scale;
        stack.scale(scale, scale, scale);
        super.render(spider, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
        stack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(TropiSpiderEntity entity) {
        return switch (entity.getSpiderType()) {
            case CHILD -> CHILD_TEXTURE_LOCATION;
            case MOTHER -> MOTHER_TEXTURE_LOCATION;
            case ADULT -> ADULT_TEXTURE_LOCATION;
        };
    }

    private float getScale(TropiSpiderEntity spider) {
        float scale = 1.0f;
        if (spider.getSpiderType() == TropiSpiderEntity.Type.CHILD) {
            scale = 0.5f;
        }
        if (spider.getSpiderType() == TropiSpiderEntity.Type.MOTHER) {
            scale = 1.2f;
        }
        return scale;
    }
}
