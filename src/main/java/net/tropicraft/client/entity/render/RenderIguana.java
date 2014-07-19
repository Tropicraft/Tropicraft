package net.tropicraft.client.entity.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.entity.passive.EntityIguana;
import net.tropicraft.util.TropicraftUtils;

public class RenderIguana extends RenderLiving {

    public RenderIguana(ModelBase modelbase, float f) {
        super(modelbase, f);
    }

    public void renderIguana(EntityIguana entityiguana, double d, double d1, double d2,
            float f, float f1) {
        super.doRender(entityiguana, d, d1, d2, f, f1);
    }

    @Override
    public void doRender(EntityLiving entityliving, double d, double d1, double d2,
            float f, float f1) {
        renderIguana((EntityIguana) entityliving, d, d1, d2, f, f1);
    }

    @Override
    public void doRender(Entity entity, double d, double d1, double d2,
            float f, float f1) {
        renderIguana((EntityIguana) entity, d, d1, d2, f, f1);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return TropicraftUtils.bindTextureEntity("iguana/iggytexture");
    }
}
