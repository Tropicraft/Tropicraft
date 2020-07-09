package net.tropicraft.core.client.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
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

    public AirCompressorRenderer(final TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher, TropicraftBlocks.AIR_COMPRESSOR.get(), new EIHMachineModel<>(RenderType::getEntitySolid));
    }

    @Override
    protected Material getMaterial() {
        return TropicraftRenderUtils.getTEMaterial("drink_mixer");
    }
    @Override
    protected void animationTransform(AirCompressorTileEntity te, final MatrixStack stack, float partialTicks) {
        float progress = te.getBreatheProgress(partialTicks);
        float sin = 1 + MathHelper.cos(progress);
        float sc = 1 + 0.05f * sin;
        stack.translate(0, 1.5f, 0);
        stack.scale(sc, sc, sc);
        stack.translate(0, -1.5f, 0);
        if (progress < Math.PI) {
            float shake = MathHelper.sin(te.getBreatheProgress(partialTicks) * 10) * 8f;
            stack.rotate(Vector3f.YP.rotationDegrees(shake));
        }
    }

    @Override
    protected void renderIngredients(AirCompressorTileEntity te, MatrixStack stack, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
        if (te.isActive()) {
            stack.push();
            stack.translate(-0.5f, 0.5f, 0);
            stack.rotate(Vector3f.YP.rotationDegrees(90));
            // TODO this is likely wrong
            IVertexBuilder builder = ItemRenderer.getBuffer(buffer, RenderType.getEntityCutoutNoCull(ScubaArmorItem.getArmorTexture(te.getTank().getType())), true, false);
            tankModel.showChest = true;
            tankModel.renderScubaGear(stack, builder, combinedLightIn, combinedOverlayIn, false);
            stack.pop();
        }
    }
}