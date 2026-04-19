package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.monster.spider.SpiderModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.entity.render.state.TropiSpiderRenderState;
import net.tropicraft.core.common.entity.hostile.TropiSpiderEntity;

public class TropiSpiderRenderer extends MobRenderer<TropiSpiderEntity, TropiSpiderRenderState, SpiderModel> {
    private static final Identifier ADULT_TEXTURE_LOCATION = Tropicraft.id("textures/entity/spideradult.png");
    private static final Identifier MOTHER_TEXTURE_LOCATION = Tropicraft.id("textures/entity/spidermother.png");
    private static final Identifier CHILD_TEXTURE_LOCATION = Tropicraft.id("textures/entity/spiderchild.png");

    public TropiSpiderRenderer(EntityRendererProvider.Context context) {
        super(context, new SpiderModel(context.bakeLayer(ModelLayers.SPIDER)), 0.8f);
        shadowStrength = 0.5f;
    }

    @Override
    public TropiSpiderRenderState createRenderState() {
        return new TropiSpiderRenderState();
    }

    @Override
    public void extractRenderState(TropiSpiderEntity entity, TropiSpiderRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.type = entity.getSpiderType();
    }

    // TODO: Replace with separate models
    @Override
    protected void scale(TropiSpiderRenderState state, PoseStack poseStack) {
        float scale = switch (state.type) {
            case ADULT -> 1.0f;
            case MOTHER -> 1.2f;
            case CHILD -> 0.5f;
        };
        shadowRadius = scale;
        poseStack.scale(scale, scale, scale);
    }

    @Override
    public Identifier getTextureLocation(TropiSpiderRenderState state) {
        return switch (state.type) {
            case CHILD -> CHILD_TEXTURE_LOCATION;
            case MOTHER -> MOTHER_TEXTURE_LOCATION;
            case ADULT -> ADULT_TEXTURE_LOCATION;
        };
    }
}
