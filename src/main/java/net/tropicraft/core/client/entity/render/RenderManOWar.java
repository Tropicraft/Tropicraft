package net.tropicraft.core.client.entity.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.ModelManOWar;
import net.tropicraft.core.client.entity.render.layers.LayerManOWarGel;
import net.tropicraft.core.common.entity.underdasea.EntityManOWar;

@SideOnly(Side.CLIENT)
public class RenderManOWar extends RenderLiving<EntityManOWar> {

    public RenderManOWar(ModelBase modelbase, float f) {
        super(Minecraft.getMinecraft().getRenderManager(), modelbase, f);
        mainModel = (ModelManOWar) modelbase;
        this.addLayer(new LayerManOWarGel(this));
    }
    
    /**
     * Renders the desired {@code T} type Entity.
     */
    @Override
    public void doRender(EntityManOWar entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        ((ModelManOWar) mainModel).isOnGround = entity.onGround;
    }

	@Override
	protected ResourceLocation getEntityTexture(EntityManOWar entity) {
		return TropicraftRenderUtils.bindTextureEntity("manowar");
	}
}