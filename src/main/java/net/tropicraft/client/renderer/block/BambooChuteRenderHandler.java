package net.tropicraft.client.renderer.block;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import net.tropicraft.client.block.model.ModelBamboo;
import net.tropicraft.info.TCRenderIDs;
import net.tropicraft.util.TropicraftUtils;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class BambooChuteRenderHandler implements ISimpleBlockRenderingHandler {

    private ModelBamboo model;
    
    public BambooChuteRenderHandler() {
        model = new ModelBamboo();
    }
    
    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {

    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        
        if (modelId == getRenderId()) {
            GL11.glPushMatrix();
       //     Tessellator.instance.draw();
       //     TropicraftUtils.bindTextureBlock("bamboo");
       //     model.render(null, 0, 0, 0, 0, 0, 0.0625f);
       //     Tessellator.instance.startDrawingQuads();
            GL11.glPopMatrix();
        }
        
        return false;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return false;
    }

    @Override
    public int getRenderId() {
        return TCRenderIDs.bambooChute;
    }

}
