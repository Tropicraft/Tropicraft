package net.tropicraft.core.client.tileentity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.block.model.ModelAirCompressor;
import net.tropicraft.core.client.entity.model.ModelScubaTank;
import net.tropicraft.core.common.block.tileentity.TileEntityAirCompressor;
import net.tropicraft.core.registry.BlockRegistry;
import net.tropicraft.core.registry.ItemRegistry;

public class TileEntityAirCompressorRenderer extends TileEntityMachineRenderer<TileEntityAirCompressor> {
    
    private final ModelScubaTank tankModel = new ModelScubaTank();

    public TileEntityAirCompressorRenderer() {
        super(BlockRegistry.airCompressor, new ModelAirCompressor());
    }
    
    @Override
    protected void animationTransform(TileEntityAirCompressor te, float partialTicks) {
        float progress = te.getBreatheProgress(partialTicks);
        float sin = 1 + MathHelper.cos(progress);
        float sc = 1 + 0.05f * sin;
        GlStateManager.translate(0, 1.5f, 0);
        GlStateManager.scale(sc, sc, sc);
        GlStateManager.translate(0, -1.5f, 0);
        if (progress < Math.PI) {
            float shake = MathHelper.sin(te.getProgress(partialTicks) * 750) * 8f;
            GlStateManager.rotate(shake, 0, 1, 0);
        }
    }

    @Override
    public void renderIngredients(TileEntityAirCompressor te) {
        if (te.isActive()) {
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.1F, 1.1F, 1.1F);
            GlStateManager.translate(-0.35f, 0.9f, 0.4f);
            GlStateManager.rotate(180, 1, 0, 0);
            TropicraftRenderUtils.bindTextureArmor(te.getTankStack().getItem() == ItemRegistry.pinkScubaTank ? "scuba_gear_pink" : "scuba_gear_yellow");
            tankModel.render();
            GlStateManager.popMatrix();
        }
    }
}
