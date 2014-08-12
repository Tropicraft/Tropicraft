package net.tropicraft.client.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.model.ModelLargeChest;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.tropicraft.block.BlockBambooChest;
import net.tropicraft.block.tileentity.TileEntityKoaChest;
import net.tropicraft.util.TropicraftUtils;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityKoaChestRenderer extends TileEntitySpecialRenderer
{
    private ModelChest chestModel;
    private ModelChest chestModelLarge;

    public TileEntityKoaChestRenderer()
    {
        chestModel = new ModelChest();
        chestModelLarge = new ModelLargeChest();
    }

    public void renderChest(TileEntityKoaChest tileentitybamboochest, double d, double d1, double d2, float f) {
        int i;
        if (tileentitybamboochest.getWorldObj() == null)
        {
            i = 0;
        }
        else
        {
            Block block = tileentitybamboochest.getBlockType();
            i = tileentitybamboochest.getBlockMetadata();
            /*if (block != null && i == 0)
            {
                ((BlockBambooChest)block).unifyAdjacentChests(tileentitybamboochest.getWorldObj(), tileentitybamboochest.xCoord, tileentitybamboochest.yCoord, tileentitybamboochest.zCoord);
                i = tileentitybamboochest.getBlockMetadata();
            }
            tileentitybamboochest.checkForAdjacentChests();*/
            
            //copied from vanilla chest renderer for 1.7.x
            if (block instanceof BlockChest && i == 0)
            {
                try
                {
                ((BlockChest)block).func_149954_e(tileentitybamboochest.getWorldObj(), tileentitybamboochest.xCoord, tileentitybamboochest.yCoord, tileentitybamboochest.zCoord);
                }
                catch (ClassCastException e)
                {
                    FMLLog.severe("Attempted to render a chest at %d,  %d, %d that was not a chest", tileentitybamboochest.xCoord, tileentitybamboochest.yCoord, tileentitybamboochest.zCoord);
                }
                i = tileentitybamboochest.getBlockMetadata();
            }

            tileentitybamboochest.checkForAdjacentChests();
        }
        if (tileentitybamboochest.adjacentChestZNeg != null || tileentitybamboochest.adjacentChestXNeg != null)
        {
            return;
        }
        ModelChest modelchest;
        if (tileentitybamboochest.adjacentChestXPos != null || tileentitybamboochest.adjacentChestZPos != null)
        {
            modelchest = chestModelLarge;
            TropicraftUtils.bindTextureBlock("largechest");
        }
        else
        {
            modelchest = chestModel;
            TropicraftUtils.bindTextureBlock("chest");
        }
        GL11.glPushMatrix();
        GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslatef((float)d, (float)d1 + 1.0F, (float)d2 + 1.0F);
        GL11.glScalef(1.0F, -1F, -1F);
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        int j = 0;
        if (i == 2)
        {
            j = 180;
        }
        if (i == 3)
        {
            j = 0;
        }
        if (i == 4)
        {
            j = 90;
        }
        if (i == 5)
        {
            j = -90;
        }
        if (i == 2 && tileentitybamboochest.adjacentChestXPos != null)
        {
            GL11.glTranslatef(1.0F, 0.0F, 0.0F);
        }
        if (i == 5 && tileentitybamboochest.adjacentChestZPos != null)
        {
            GL11.glTranslatef(0.0F, 0.0F, -1F);
        }
        GL11.glRotatef(j, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        float f1 = tileentitybamboochest.prevLidAngle + (tileentitybamboochest.lidAngle - tileentitybamboochest.prevLidAngle) * f;
        //System.out.println(tileentitybamboochest.lidAngle);
        if (tileentitybamboochest.adjacentChestZNeg != null)
        {
            float f2 = tileentitybamboochest.adjacentChestZNeg.prevLidAngle + (tileentitybamboochest.adjacentChestZNeg.lidAngle - tileentitybamboochest.adjacentChestZNeg.prevLidAngle) * f;
            if (f2 > f1)
            {
                f1 = f2;
            }
            
        }
        if (tileentitybamboochest.adjacentChestXNeg != null)
        {
            float f3 = tileentitybamboochest.adjacentChestXNeg.prevLidAngle + (tileentitybamboochest.adjacentChestXNeg.lidAngle - tileentitybamboochest.adjacentChestXNeg.prevLidAngle) * f;
            if (f3 > f1)
            {
                f1 = f3;
            }
        }
        f1 = 1.0F - f1;
        f1 = 1.0F - f1 * f1 * f1;
        //System.out.println(f1);
        modelchest.chestLid.rotateAngleX = -((f1 * 3.141593F) / 2.0F);
        modelchest.renderAll();
        GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2,
            float f)
    {
        renderChest((TileEntityKoaChest)tileentity, d, d1, d2, f);
    }
}
