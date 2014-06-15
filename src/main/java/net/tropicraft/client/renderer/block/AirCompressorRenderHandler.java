package net.tropicraft.client.renderer.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.world.IBlockAccess;
import net.tropicraft.block.tileentity.TileEntityAirCompressor;
import net.tropicraft.info.TCRenderIDs;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class AirCompressorRenderHandler implements ISimpleBlockRenderingHandler {
    private TileEntityAirCompressor dummyTileEntity = new TileEntityAirCompressor();
    
    public AirCompressorRenderHandler() {
    }
    
    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID,
            RenderBlocks renderer) {
        GL11.glPushMatrix();
        GL11.glScalef(0.55f, 0.55f, 0.55f);
        TileEntityRendererDispatcher.instance.renderTileEntityAt(dummyTileEntity, 0.0, -0.5, 0.0, 0f);
        GL11.glPopMatrix();
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
            Block block, int modelId, RenderBlocks renderer) {
        return false;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelID) {
        return true;
    }

    @Override
    public int getRenderId() {
        return TCRenderIDs.airCompressor;
    }
}
