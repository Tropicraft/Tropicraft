package net.tropicraft.client.renderer.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.tropicraft.block.tileentity.TileEntityBambooChest;
import net.tropicraft.info.TCRenderIDs;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class BambooChestRenderHandler implements ISimpleBlockRenderingHandler {

    private TileEntity dummyTileEnt;

    public BambooChestRenderHandler() {
        dummyTileEnt = new TileEntityBambooChest();
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
        if (modelID == getRenderId()) {
            TileEntityRendererDispatcher.instance.renderTileEntityAt(dummyTileEnt, 0.0D, 0.0D, 0.0D, 0.0F);
            GL11.glEnable(org.lwjgl.opengl.GL12.GL_RESCALE_NORMAL);
            return;
        }
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
            Block block, int modelId, RenderBlocks renderer) {
        return false;
    }
    
    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return TCRenderIDs.bambooChest;
    }
}