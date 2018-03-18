package net.tropicraft.core.client.entity.render.layers;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.core.client.entity.model.ModelManOWar;
import net.tropicraft.core.client.entity.render.RenderManOWar;
import net.tropicraft.core.common.entity.underdasea.EntityManOWar;

@SideOnly(Side.CLIENT)
public class LayerManOWarGel implements LayerRenderer<EntityManOWar>
{
    private final RenderManOWar mowRenderer;
    private final ModelBase mowModel = new ModelManOWar(0, 20, false);

    public LayerManOWarGel(RenderManOWar manOWarRenderer)
    {
        this.mowRenderer = manOWarRenderer;
    }

    @Override
    public void doRenderLayer(EntityManOWar entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (!entity.isInvisible()) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableNormalize();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            this.mowModel.setModelAttributes(this.mowRenderer.getMainModel());
            this.mowModel.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            GlStateManager.disableBlend();
            GlStateManager.disableNormalize();
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
}