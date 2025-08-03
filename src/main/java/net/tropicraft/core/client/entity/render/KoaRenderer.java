package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.KoaModel;
import net.tropicraft.core.client.entity.render.state.KoaRenderState;
import net.tropicraft.core.common.entity.passive.EntityKoaBase;

import javax.annotation.Nullable;

public class KoaRenderer extends HumanoidMobRenderer<EntityKoaBase, KoaRenderState, KoaModel> {

    private static final ResourceLocation MALE_FISHER = Tropicraft.location("textures/entity/koa/koa_man_fisher.png");
    private static final ResourceLocation FEMALE_FISHER = Tropicraft.location("textures/entity/koa/koa_woman_fisher.png");
    private static final ResourceLocation MALE_HUNTER = Tropicraft.location("textures/entity/koa/koa_man_hunter.png");
    private static final ResourceLocation FEMALE_HUNTER = Tropicraft.location("textures/entity/koa/koa_woman_hunter.png");

    public KoaRenderer(EntityRendererProvider.Context context) {
        super(context, new KoaModel(context.bakeLayer(TropicraftRenderLayers.KOA_HUNTER_LAYER)), new KoaModel(context.bakeLayer(TropicraftRenderLayers.KOA_HUNTER_BABY_LAYER)), 0.5f);
        shadowStrength = 0.5f;
    }

    @Override
    public KoaRenderState createRenderState() {
        return new KoaRenderState();
    }

    @Override
    public void extractRenderState(EntityKoaBase entity, KoaRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.isDancing = entity.isDancing();
        state.isSitting = entity.isSitting();
        state.gender = entity.getGender();
        state.role = entity.getRole();
    }

    @Override
    public ResourceLocation getTextureLocation(KoaRenderState state) {
        return switch (state.gender) {
            case MALE -> switch (state.role) {
                case HUNTER -> MALE_HUNTER;
                case FISHERMAN -> MALE_FISHER;
            };
            case FEMALE -> switch (state.role) {
                case HUNTER -> FEMALE_HUNTER;
                case FISHERMAN -> FEMALE_FISHER;
            };
        };
    }

    @Override
    public Vec3 getRenderOffset(KoaRenderState state) {
        if (state.isSitting) {
            if (state.isBaby) {
                return new Vec3(0, -0.3, 0);
            } else {
                return new Vec3(0, -0.7, 0);
            }
        }
        return super.getRenderOffset(state);
    }

    @Nullable
    @Override
    protected RenderType getRenderType(KoaRenderState state, boolean p_230496_2_, boolean p_230496_3_, boolean p_230496_4_) {
        return RenderType.entityCutout(getTextureLocation(state));
    }
}
