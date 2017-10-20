package net.tropicraft.core.client.entity.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.Info;
import net.tropicraft.core.client.entity.model.ModelTropiSkeleton;
import net.tropicraft.core.client.entity.render.layers.LayerHeldItemTropiSkelly;
import net.tropicraft.core.common.entity.hostile.EntityTropiSkeleton;

@SideOnly(Side.CLIENT)
public class RenderTropiSkeleton extends RenderBiped<EntityTropiSkeleton> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(Info.MODID + ":textures/entity/tropiskeleton.png");

    public RenderTropiSkeleton() {
        super(Minecraft.getMinecraft().getRenderManager(), new ModelTropiSkeleton(), 0.5F);
        this.layerRenderers.clear();
        this.addLayer(new LayerHeldItemTropiSkelly(this));
    }

    @Override
    public void transformHeldFull3DItemLayer() {
        GlStateManager.translate(0.09375F, 0.1875F, 0.0F);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
    protected ResourceLocation getEntityTexture(EntityTropiSkeleton entity) {
        return TEXTURE;
    }
}