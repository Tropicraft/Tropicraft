package net.tropicraft.core.client.entity.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.ModelSeaTurtle;
import net.tropicraft.core.common.entity.underdasea.EntitySeaTurtle;

public class RenderSeaTurtle extends RenderLiving<EntitySeaTurtle> {

    public ModelSeaTurtle turtle;
    
    public RenderSeaTurtle(ModelBase modelbase, float f) {
        super(Minecraft.getMinecraft().getRenderManager(), modelbase, f);
        
        turtle = (ModelSeaTurtle) modelbase;
    }

    @Override
    protected ResourceLocation getEntityTexture(EntitySeaTurtle entity) {
        return TropicraftRenderUtils.bindTextureEntity("turtle/seaTurtle");
    }

}
