package net.tropicraft.core.client.tileentity;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.math.MathHelper;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.EIHMachineModel;
import net.tropicraft.core.client.scuba.ModelScubaGear;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.block.tileentity.AirCompressorTileEntity;
import net.tropicraft.core.common.item.scuba.ScubaArmorItem;

public class AirCompressorRenderer extends MachineRenderer<AirCompressorTileEntity> {
    
    private final ModelScubaGear tankModel = new ModelScubaGear(0, EquipmentSlotType.CHEST); // Can't reuse the main one with a different scale

    public AirCompressorRenderer() {
        super(TropicraftBlocks.AIR_COMPRESSOR.get(), new EIHMachineModel<>());
    }
    
    @Override
    protected void animationTransform(AirCompressorTileEntity te, float partialTicks) {
        float progress = te.getBreatheProgress(partialTicks);
        float sin = 1 + MathHelper.cos(progress);
        float sc = 1 + 0.05f * sin;
        GlStateManager.translatef(0, 1.5f, 0);
        GlStateManager.scalef(sc, sc, sc);
        GlStateManager.translatef(0, -1.5f, 0);
        if (progress < Math.PI) {
            float shake = MathHelper.sin(te.getBreatheProgress(partialTicks) * 10) * 8f;
            GlStateManager.rotatef(shake, 0, 1, 0);
        }
    }

    @Override
    public void renderIngredients(AirCompressorTileEntity te) {
        if (te.isActive()) {
            GlStateManager.pushMatrix();
            GlStateManager.translatef(-0.5f, 0.5f, 0);
            GlStateManager.rotatef(90, 0, 1, 0);
            TropicraftRenderUtils.bindTexture(ScubaArmorItem.getArmorTexture(te.getTank().getType()));
            tankModel.renderScubaGear(0.0625f, false, false, true, false);
            GlStateManager.popMatrix();
        }
    }
}