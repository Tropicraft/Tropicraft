package net.tropicraft.client.renderer.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.tropicraft.block.BlockTropicraftFlowerPot;
import net.tropicraft.block.tileentity.TileEntityTropicraftFlowerPot;
import net.tropicraft.info.TCRenderIDs;
import net.tropicraft.registry.TCBlockRegistry;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class FlowerPotRenderHandler implements ISimpleBlockRenderingHandler {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {

	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		renderFlowerPot(world, x, y, z, (BlockTropicraftFlowerPot)block, renderer);
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return false;
	}

	@Override
	public int getRenderId() {
		return TCRenderIDs.flowerPot;
	}

	private void renderFlowerPot(IBlockAccess world, int x, int y, int z, BlockTropicraftFlowerPot block, RenderBlocks rb) {
		rb.renderStandardBlock(block, x, y, z);
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
        float f = 1.0F;
        int l = block.colorMultiplier(world, x, y, z);
        IIcon icon = rb.getBlockIconFromSide(block, 0);
        float f1 = (float)(l >> 16 & 255) / 255.0F;
        float f2 = (float)(l >> 8 & 255) / 255.0F;
        float f3 = (float)(l & 255) / 255.0F;
        float f4;
        float f5;

        if (EntityRenderer.anaglyphEnable)
        {
            f4 = (f1 * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
            float f6 = (f1 * 30.0F + f2 * 70.0F) / 100.0F;
            f5 = (f1 * 30.0F + f3 * 70.0F) / 100.0F;
            f1 = f4;
            f2 = f6;
            f3 = f5;
        }

        tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);
        f4 = 0.1865F;
        rb.renderFaceXPos(block, (double)((float)x - 0.5F + f4), (double)y, (double)z, icon);
        rb.renderFaceXNeg(block, (double)((float)x + 0.5F - f4), (double)y, (double)z, icon);
        rb.renderFaceZPos(block, (double)x, (double)y, (double)((float)z - 0.5F + f4), icon);
        rb.renderFaceZNeg(block, (double)x, (double)y, (double)((float)z + 0.5F - f4), icon);
        rb.renderFaceYPos(block, (double)x, (double)((float)y - 0.5F + f4 + 0.1875F), (double)z, rb.getBlockIcon(Blocks.dirt));

        TileEntityTropicraftFlowerPot te = (TileEntityTropicraftFlowerPot)world.getTileEntity(x, y, z);

        if (te == null)
        	return;
        
        int var19 = te.getFlowerID();

        if (var19 != 0)
        {
            f5 = 0.0F;
            float var15 = 4.0F;
            float var16 = 0.0F;

            tessellator.addTranslation(f5 / 16.0F, var15 / 16.0F, var16 / 16.0F);

            if (var19 > 0 && var19 < 17) {
                rb.drawCrossedSquares(draw(TCBlockRegistry.flowers, var19 - 1), (double)x, (double)y, (double)z, 0.75F);
            } else
                if (var19 == 17) {
                    rb.drawCrossedSquares(draw(TCBlockRegistry.tallFlowers, 0), (double)x, (double)y, (double)z, 0.75F);
                    rb.drawCrossedSquares(draw(TCBlockRegistry.tallFlowers, 1), (double)x, (double)(y + 0.75), (double)z, 0.75F);
                } else
                    if (var19 == 18) {
                        rb.drawCrossedSquares(draw(TCBlockRegistry.pineapple, 7), (double)x, (double)y, (double)z, 0.75F);
                        rb.drawCrossedSquares(draw(TCBlockRegistry.pineapple, 8), (double)x, (double)(y + 0.75), (double)z, 0.75F);
                    } else
                        if (var19 > 18 && var19 < 25) {
                            rb.drawCrossedSquares(draw(TCBlockRegistry.saplings, var19 - 19), (double)x, (double)y, (double)z, 0.75F);
                        } else
                        	if (var19 >= 25 && var19 < 34) {
                        		rb.drawCrossedSquares(draw(Blocks.red_flower, var19 - 25), (double)x, (double)y, (double)z, 0.75F);
                        	} else
                        		if (var19 == 34) {
                        			rb.drawCrossedSquares(draw(Blocks.yellow_flower, var19 - 34), (double)x, (double)y, (double)z, 0.75F);
                        		}

            tessellator.addTranslation(-f5 / 16.0F, -var15 / 16.0F, -var16 / 16.0F);
        }	
	}
	
	private IIcon draw(Block block, int damage) {
		return block.getIcon(0, damage);
	}

}
