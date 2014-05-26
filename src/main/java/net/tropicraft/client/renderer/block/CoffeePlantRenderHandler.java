package net.tropicraft.client.renderer.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import net.tropicraft.block.BlockCoffeePlant;
import net.tropicraft.info.TCRenderIDs;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class CoffeePlantRenderHandler implements ISimpleBlockRenderingHandler {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId,
			RenderBlocks renderer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
			Block block, int modelId, RenderBlocks renderer) {
		renderer.overrideBlockTexture = ((BlockCoffeePlant)block).stemIcon;
        renderer.renderCrossedSquares(block, x, y, z);
        renderer.overrideBlockTexture = null;
        renderer.renderStandardBlock(block, x, y, z);
        return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return false;
	}

	@Override
	public int getRenderId() {
		return TCRenderIDs.coffeePlant;
	}

}
