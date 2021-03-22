package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.Constants;
import net.tropicraft.core.client.entity.model.KoaModel;
import net.tropicraft.core.common.entity.passive.EntityKoaBase;

import javax.annotation.Nullable;

public class KoaRenderer extends BipedRenderer<EntityKoaBase, KoaModel> {

    private static final ResourceLocation MALE_FISHER = new ResourceLocation(Constants.MODID, "textures/entity/koa/koa_man_fisher.png");
    private static final ResourceLocation FEMALE_FISHER = new ResourceLocation(Constants.MODID, "textures/entity/koa/koa_woman_fisher.png");
    private static final ResourceLocation MALE_HUNTER = new ResourceLocation(Constants.MODID, "textures/entity/koa/koa_man_hunter.png");
    private static final ResourceLocation FEMALE_HUNTER = new ResourceLocation(Constants.MODID, "textures/entity/koa/koa_woman_hunter.png");

    public KoaRenderer(EntityRendererManager rendermanagerIn) {
        super(rendermanagerIn, new KoaModel(0), 0.5F);
        this.shadowOpaque = 0.5f;
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
    public ResourceLocation getEntityTexture(EntityKoaBase entity) {
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

    @Nullable
    @Override
    protected RenderType func_230496_a_(EntityKoaBase entity, boolean p_230496_2_, boolean p_230496_3_, boolean p_230496_4_) {
        return RenderType.getEntityCutout(getEntityTexture(entity));
    }
}
