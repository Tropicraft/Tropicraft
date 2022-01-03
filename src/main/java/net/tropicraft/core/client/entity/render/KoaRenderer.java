package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.tropicraft.Constants;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.KoaModel;
import net.tropicraft.core.common.entity.passive.EntityKoaBase;

import javax.annotation.Nullable;

public class KoaRenderer extends HumanoidMobRenderer<EntityKoaBase, KoaModel> {

    private static final ResourceLocation MALE_FISHER = new ResourceLocation(Constants.MODID, "textures/entity/koa/koa_man_fisher.png");
    private static final ResourceLocation FEMALE_FISHER = new ResourceLocation(Constants.MODID, "textures/entity/koa/koa_woman_fisher.png");
    private static final ResourceLocation MALE_HUNTER = new ResourceLocation(Constants.MODID, "textures/entity/koa/koa_man_hunter.png");
    private static final ResourceLocation FEMALE_HUNTER = new ResourceLocation(Constants.MODID, "textures/entity/koa/koa_woman_hunter.png");

    public KoaRenderer(final EntityRendererProvider.Context context) {
        super(context, new KoaModel(context.bakeLayer(TropicraftRenderLayers.KOA_HUNTER_LAYER)), 0.5F);
        this.shadowStrength = 0.5f;
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
    public ResourceLocation getTextureLocation(EntityKoaBase entity) {
        if (entity.getGender() == EntityKoaBase.Genders.MALE) {
            if (entity.getRole() == EntityKoaBase.Roles.HUNTER) {
                return MALE_HUNTER;
            }
            return MALE_FISHER;
        } else {
            if (entity.getRole() == EntityKoaBase.Roles.HUNTER) {
                return FEMALE_HUNTER;
            }
            return FEMALE_FISHER;
        }
    }

    @Override
    public Vec3 getRenderOffset(EntityKoaBase pEntity, float pPartialTicks) {
        if (pEntity.isSitting()) {
            if (pEntity.isBaby()) {
                return new Vec3(0, -0.3, 0);
            } else {
                return new Vec3(0, -0.7, 0);
            }
        }
        return super.getRenderOffset(pEntity, pPartialTicks);
    }

    @Nullable
    @Override
    protected RenderType getRenderType(EntityKoaBase entity, boolean p_230496_2_, boolean p_230496_3_, boolean p_230496_4_) {
        return RenderType.entityCutout(getTextureLocation(entity));
    }
}
