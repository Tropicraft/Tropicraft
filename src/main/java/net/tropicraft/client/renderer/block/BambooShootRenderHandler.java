package net.tropicraft.client.renderer.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.tropicraft.block.BlockBambooShoot;
import net.tropicraft.info.TCRenderIDs;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class BambooShootRenderHandler implements ISimpleBlockRenderingHandler {
    public BambooShootRenderHandler() {
    }
    
    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {

    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        
        if (modelId == getRenderId()) {
            Tessellator tessellator = Tessellator.instance;
            tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
            int l = block.colorMultiplier(world, x, y, z);
            float f = (float)(l >> 16 & 255) / 255.0F;
            float f1 = (float)(l >> 8 & 255) / 255.0F;
            float f2 = (float)(l & 255) / 255.0F;
            float f4;

            if (EntityRenderer.anaglyphEnable)
            {
                float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
                f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
                float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
                f = f3;
                f1 = f4;
                f2 = f5;
            }

            tessellator.setColorOpaque_F(f, f1, f2);
            
            float sixteenth = 1f/16f;
            IIcon sideTex = BlockBambooShoot.getBambooIcon("side");
            IIcon topTex = BlockBambooShoot.getBambooIcon("top");
            IIcon bottomTex = BlockBambooShoot.getBambooIcon("bottom");
            IIcon indentTex = BlockBambooShoot.getBambooIcon("indent");
            IIcon leafTex = BlockBambooShoot.getBambooIcon("leaf");
            IIcon leafFlippedTex = BlockBambooShoot.getBambooIcon("leafFlipped");

            // indent 1
            renderer.renderFaceYNeg(block, x+sixteenth/2f, y, z+sixteenth/2f, bottomTex);
            renderer.renderFaceXPos(block, x - 3.5*sixteenth, y, z+sixteenth/2f, indentTex);
            renderer.renderFaceXNeg(block, x + 3.5*sixteenth, y, z+sixteenth/2f, indentTex);
            renderer.renderFaceZPos(block, x+sixteenth/2f, y, z - 3.5*sixteenth, indentTex);
            renderer.renderFaceZNeg(block, x+sixteenth/2f, y, z + 3.5*sixteenth, indentTex);

            // bottom base
            renderer.renderFaceYNeg(block, x, y + sixteenth, z, topTex);
            renderer.renderFaceXPos(block, x - 3*sixteenth, y + sixteenth, z, sideTex);
            renderer.renderFaceXNeg(block, x + 3*sixteenth, y + sixteenth, z, sideTex);
            renderer.renderFaceZPos(block, x, y + sixteenth, z - 3*sixteenth, sideTex);
            renderer.renderFaceZNeg(block, x, y + sixteenth, z + 3*sixteenth, sideTex);
            renderer.renderFaceYPos(block, x, y - 8*sixteenth, z, topTex);
            
            // indent 2
            renderer.renderFaceXPos(block, x - 3.5*sixteenth, y + 8*sixteenth, z+sixteenth/2f, indentTex);
            renderer.renderFaceXNeg(block, x + 3.5*sixteenth, y + 8*sixteenth, z+sixteenth/2f, indentTex);
            renderer.renderFaceZPos(block, x+sixteenth/2f, y + 8*sixteenth, z - 3.5*sixteenth, indentTex);
            renderer.renderFaceZNeg(block, x+sixteenth/2f, y + 8*sixteenth, z + 3.5*sixteenth, indentTex);

            // top base
            renderer.renderFaceYNeg(block, x, y + 9*sixteenth, z, topTex);
            renderer.renderFaceXPos(block, x - 3*sixteenth, y + 9*sixteenth, z, sideTex);
            renderer.renderFaceXNeg(block, x + 3*sixteenth, y + 9*sixteenth, z, sideTex);
            renderer.renderFaceZPos(block, x, y + 9*sixteenth, z - 3*sixteenth, sideTex);
            renderer.renderFaceZNeg(block, x, y + 9*sixteenth, z + 3*sixteenth, sideTex);
            renderer.renderFaceYPos(block, x, y, z, topTex);
            
            float leafOffset1 = 5.5f*sixteenth;
            float leafOffset2 = 9f*sixteenth;
            float leafOffsetY = 3.5f*sixteenth;
            
            // leaf
            if (y % 2 == 0) {
            	renderer.renderFaceXPos(block, x-leafOffset1, y+leafOffsetY, z+leafOffset2, leafTex);
            	renderer.renderFaceXNeg(block, x+leafOffset1, y+leafOffsetY, z+leafOffset2, leafTex);
            	renderer.renderFaceXPos(block, x-leafOffset1, y+leafOffsetY, z-leafOffset2, leafFlippedTex);
            	renderer.renderFaceXNeg(block, x+leafOffset1, y+leafOffsetY, z-leafOffset2, leafFlippedTex);
            } else {
            	renderer.renderFaceZPos(block, x+leafOffset2, y+leafOffsetY, z-leafOffset1, leafTex);
            	renderer.renderFaceZNeg(block, x+leafOffset2, y+leafOffsetY, z+leafOffset1, leafTex);
            	renderer.renderFaceZPos(block, x-leafOffset2, y+leafOffsetY, z-leafOffset1, leafFlippedTex);
            	renderer.renderFaceZNeg(block, x-leafOffset2, y+leafOffsetY, z+leafOffset1, leafFlippedTex);
            	
            }
        }
        
        return false;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return false;
    }

    @Override
    public int getRenderId() {
        return TCRenderIDs.bambooShoot;
    }

}
