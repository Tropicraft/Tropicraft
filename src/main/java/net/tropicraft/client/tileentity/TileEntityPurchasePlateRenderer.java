package net.tropicraft.client.tileentity;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.block.tileentity.TileEntityPurchasePlate;
import net.tropicraft.economy.ItemEntry;
import net.tropicraft.economy.ItemValues;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

public class TileEntityPurchasePlateRenderer extends TileEntitySpecialRenderer
{
    /**
     * Hash map of the entities that the mob spawner has rendered/rendering spinning inside of them
     */
    private Map entityHashMap = new HashMap();
    
    public long lifeTick = 0;
    public long lastWorldTick = 0;
    
    
    public TileEntityPurchasePlateRenderer() {
    	
    	lifeTick = 0;//rand.nextInt(20);
    }

    public void renderTileEntityMobSpawner(TileEntityPurchasePlate par1TileEntityMobSpawner, double par2, double par4, double par6, float par8)
    {
    	
    	/*if (ZCGame.instance() != null && ZCGame.instance().mapMan != null && ZCGame.instance().mapMan.editMode && ZCGame.instance().mapMan.infoOverlay) {
    		
    	}*/
    	
    	if (par1TileEntityMobSpawner.tradeState == 1) {
    		
    		if (par1TileEntityMobSpawner.showToolTip) {
    			renderLivingLabel("Offer Items: right click koa with item", par2+0.5F, par4 + 0.9F, par6+0.5F, 0);
    			renderLivingLabel("Cycle Items: right click plate", par2+0.5F, par4 + 0.8F, par6+0.5F, 0);
    			renderLivingLabel("Buy Item: left click plate", par2+0.5F, par4 + 0.7F, par6+0.5F, 0);
    		}
    		
    		//Item item = ItemValues.g.getItemBuyable(par1TileEntityMobSpawner.itemIndex);
    		String name = "???";
    		//if (item != null) {
	    		ItemEntry ie = ItemValues.itemsBuyable.get(par1TileEntityMobSpawner.itemIndex);
	    		ItemStack is = ie.item;
	    		if (is != null) {
	    			name = is.getDisplayName();// + " x" + ValuedItems.getBuyItemCount(item);//ValuedItems.getItemBuyable(par1TileEntityMobSpawner.itemIndex).getItemName();
	    			if (is.stackSize > 1) {
	    				name += " x" + is.stackSize;
	    			}
	    			if (name.startsWith("item.")) name = name.substring(5);
	    			renderLivingLabel("" + name, par2+0.5F, par4 + 0.45F, par6+0.5F, 0);
	    		}
    		//}
    		//renderLivingLabel("cost: " + ValuedItems.getBuyableItemCostByIndex(par1TileEntityMobSpawner.itemIndex), par2+0.5F, par4 + 0.0F, par6+0.5F, 0);
    	} else if (par1TileEntityMobSpawner.tradeState == 2) {
    		renderLivingLabel("Confirm Trade", par2+0.5F, par4 + 0.45F, par6+0.5F, 0);
    	}
    	
    	
    	
    	if (par1TileEntityMobSpawner.tradeState > 0) {
    		
    		renderBanner(par1TileEntityMobSpawner, par2, par4, par6, par8);
    		
	    	if (lastWorldTick != par1TileEntityMobSpawner.getWorldObj().getWorldTime()) {
	    		lastWorldTick = par1TileEntityMobSpawner.getWorldObj().getWorldTime();
	    		lifeTick += 1F + (0.5F * FMLClientHandler.instance().getClient().thePlayer.getDistance(par1TileEntityMobSpawner.xCoord, par1TileEntityMobSpawner.yCoord, par1TileEntityMobSpawner.zCoord) / 2);
	    		/*if (lifeTick >= 20) {
	    			lifeTick = 0;
	    		}*/
	    		
	    		//System.out.println(FMLClientHandler.instance().getClient().thePlayer.getDistance(par1TileEntityMobSpawner.xCoord, par1TileEntityMobSpawner.yCoord, par1TileEntityMobSpawner.zCoord) / 2);
	    		
	    		par1TileEntityMobSpawner.angle += 1F;
	    		if (par1TileEntityMobSpawner.angle >= 360F) par1TileEntityMobSpawner.angle -= 360F;
	    		
	    	}
	    	
	    	renderEntityItem2(par1TileEntityMobSpawner, par2, par4, par6, par8);
    	}
    	//GL11.glPushMatrix();
        //GL11.glTranslatef((float)par2 + 0.5F, (float)par4, (float)par6 + 0.5F);
        /*Entity var9 = (Entity)this.entityHashMap.get(par1TileEntityMobSpawner.getMobID());

        if (var9 == null)
        {
            var9 = EntityList.createEntityInWorld(par1TileEntityMobSpawner.getMobID(), (World)null);
            this.entityHashMap.put(par1TileEntityMobSpawner.getMobID(), var9);
        }

        if (var9 != null)
        {
            var9.setWorld(par1TileEntityMobSpawner.worldObj);
            float var10 = 0.4375F;
            GL11.glTranslatef(0.0F, 0.4F, 0.0F);
            GL11.glRotatef((float)(par1TileEntityMobSpawner.yaw2 + (par1TileEntityMobSpawner.yaw - par1TileEntityMobSpawner.yaw2) * (double)par8) * 10.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-30.0F, 1.0F, 0.0F, 0.0F);
            GL11.glTranslatef(0.0F, -0.4F, 0.0F);
            GL11.glScalef(var10, var10, var10);
            var9.setLocationAndAngles(par2, par4, par6, 0.0F, 0.0F);
            RenderManager.instance.renderEntityWithPosYaw(var9, 0.0D, 0.0D, 0.0D, 0.0F, par8);
        }*/

        //GL11.glPopMatrix();
    }
    
    public void renderEntityItem2(TileEntityPurchasePlate par1TileEntityMobSpawner, double par2, double par4, double par6, float par8) {
    	
    	GL11.glPushMatrix();
    	
    	float var10 = 0.3F;
    	

    	float yOffset = MathHelper.sin(((float)lifeTick + par8) / 10.0F + par1TileEntityMobSpawner.startOffset) * 0.03F + 0.0F;
    	
    	GL11.glTranslatef((float)par2 + 0.5F, (float)par4 - 0.1F + yOffset + 0.75F * var10, (float)par6 + 0.5F);
    	
    	float var12 = (((float)lifeTick + par8) / 20.0F + par1TileEntityMobSpawner.startOffset) * (180F / (float)Math.PI);
    	
    	GL11.glRotatef(-var12, 0.0F, 1.0F, 0.0F);
    	//GL11.glRotatef(45F, 0.0F, 0.0F, 1.0F);
    	
    	float var11 = var12;//par1TileEntityMobSpawner.angle;//(float)(1F * 360) / 16.0F;
        //GL11.glRotatef(-var11, 0.0F, 1.0F, 0.0F);
        
        //GL11.glRotatef((((float)lifeTick + par8) / 20.0F + par1TileEntityMobSpawner.startOffset) * (180F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
    	
        GL11.glScalef(var10, var10, var10);
        
        ItemStack is = ItemValues.itemsBuyable.get(par1TileEntityMobSpawner.itemIndex).item;
        if (is != null) {
        	RenderManager.instance.itemRenderer.renderItem(FMLClientHandler.instance().getClient().thePlayer, is, 0);
        } else {
        	System.out.println("tropicraft tradeblock renderable item is null, bug? itemindex: " + par1TileEntityMobSpawner.itemIndex);
        }
    	
    	GL11.glPopMatrix();
    }
    
    public void renderEntityItem() {
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, 1.0F, 0.0F);
        
        float scale = 0.0078125F;
        GL11.glScalef(scale, scale, scale);
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
        ResourceLocation res = new ResourceLocation("textures/map/map_background.png");
        Minecraft.getMinecraft().renderEngine.bindTexture(res);
        Tessellator tessellator = Tessellator.instance;
        GL11.glNormal3f(0.0F, 0.0F, -1.0F);
        tessellator.startDrawingQuads();
        byte margin = 7;
        tessellator.addVertexWithUV((double)(0 - margin), (double)(128 + margin), 0.0D, 0.0D, 1.0D);
        tessellator.addVertexWithUV((double)(128 + margin), (double)(128 + margin), 0.0D, 1.0D, 1.0D);
        tessellator.addVertexWithUV((double)(128 + margin), (double)(0 - margin), 0.0D, 1.0D, 0.0D);
        tessellator.addVertexWithUV((double)(0 - margin), (double)(0 - margin), 0.0D, 0.0D, 0.0D);
        tessellator.draw();
        //MapData data = Item.map.getMapData(entity.item, ModLoader.getMinecraftInstance().theWorld);
        //this.mapRenderer.renderMap(mod_ZombieCraft.mc.thePlayer, mod_ZombieCraft.mc.renderEngine, data);
        GL11.glPopMatrix();
    }

    public void renderTileEntityAt(TileEntity par1TileEntity, double par2, double par4, double par6, float par8)
    {
        this.renderTileEntityMobSpawner((TileEntityPurchasePlate)par1TileEntity, par2, par4, par6, par8);
    }
    
    protected void renderBanner(TileEntityPurchasePlate par1TileEntityMobSpawner, double par3, double par5, double par7, float par8) {
    	
    	String par2Str = "Credit: " + par1TileEntityMobSpawner.credit;
    	String par2Str2 = "Cost: " + ItemValues.itemsBuyable.get(par1TileEntityMobSpawner.itemIndex).value;
    	
    	FontRenderer var11 = RenderManager.instance.getFontRenderer();
        float var12 = 1.2F;
        float var13 = 0.016F * var12;
        
        float angle;
        float x = 0.5F;
        float z = 0.0F;
        
        for (angle = -180; angle <= 90; angle += 90) {
        
        	x = z = 0F;
        	
        	if (angle == -180) {
        		z = 0.501F;
        	} else if (angle == -90) {
        		x = 0.501F;
        	} else if (angle == 0) {
        		z = -0.501F;
        	} else if (angle == 90) {
        		x = -0.501F;
        	}
        	
        	//Mmmmm copypasta
        	
        	//Cost
	        GL11.glPushMatrix();
	        GL11.glTranslatef((float)par3 + 0.5F + x, (float)par5 - 0.1F, (float)par7 + 0.5F + z);
	        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
	        GL11.glRotatef(angle, 0.0F, 1.0F, 0.0F);
	        //GL11.glRotatef(-RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
	        //GL11.glRotatef(RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);
	        GL11.glScalef(-var13, -var13, var13);
	        GL11.glDisable(GL11.GL_LIGHTING);
	        GL11.glDepthMask(false);
	        //GL11.glDisable(GL11.GL_DEPTH_TEST);
	        GL11.glEnable(GL11.GL_BLEND);
	        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	        Tessellator var14 = Tessellator.instance;
	        byte var15 = 0;
	        GL11.glDisable(GL11.GL_TEXTURE_2D);
	        var14.startDrawingQuads();
	        int var16 = var11.getStringWidth(par2Str2) / 2;
	        var14.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
	        var14.addVertex((double)(-var16 - 1), (double)(-1 + var15), 0.0D);
	        var14.addVertex((double)(-var16 - 1), (double)(8 + var15), 0.0D);
	        var14.addVertex((double)(var16 + 1), (double)(8 + var15), 0.0D);
	        var14.addVertex((double)(var16 + 1), (double)(-1 + var15), 0.0D);
	        var14.draw();
	        GL11.glEnable(GL11.GL_TEXTURE_2D);
	        var11.drawString(par2Str2, -var11.getStringWidth(par2Str2) / 2, var15, 553648127);
	        GL11.glEnable(GL11.GL_DEPTH_TEST);
	        GL11.glDepthMask(true);
	        var11.drawString(par2Str2, -var11.getStringWidth(par2Str2) / 2, var15, -1);
	        GL11.glEnable(GL11.GL_LIGHTING);
	        GL11.glDisable(GL11.GL_BLEND);
	        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	        GL11.glPopMatrix();
	        
	        //Credit
	        GL11.glPushMatrix();
	        GL11.glTranslatef((float)par3 + 0.5F + x, (float)par5 - 0.3F, (float)par7 + 0.5F + z);
	        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
	        GL11.glRotatef(angle, 0.0F, 1.0F, 0.0F);
	        //GL11.glRotatef(-RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
	        //GL11.glRotatef(RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);
	        GL11.glScalef(-var13, -var13, var13);
	        GL11.glDisable(GL11.GL_LIGHTING);
	        GL11.glDepthMask(false);
	        //GL11.glDisable(GL11.GL_DEPTH_TEST);
	        GL11.glEnable(GL11.GL_BLEND);
	        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	        var14 = Tessellator.instance;
	        var15 = 0;
	        GL11.glDisable(GL11.GL_TEXTURE_2D);
	        var14.startDrawingQuads();
	        var16 = var11.getStringWidth(par2Str) / 2;
	        var14.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
	        var14.addVertex((double)(-var16 - 1), (double)(-1 + var15), 0.0D);
	        var14.addVertex((double)(-var16 - 1), (double)(8 + var15), 0.0D);
	        var14.addVertex((double)(var16 + 1), (double)(8 + var15), 0.0D);
	        var14.addVertex((double)(var16 + 1), (double)(-1 + var15), 0.0D);
	        var14.draw();
	        GL11.glEnable(GL11.GL_TEXTURE_2D);
	        var11.drawString(par2Str, -var11.getStringWidth(par2Str) / 2, var15, 553648127);
	        GL11.glEnable(GL11.GL_DEPTH_TEST);
	        GL11.glDepthMask(true);
	        var11.drawString(par2Str, -var11.getStringWidth(par2Str) / 2, var15, -1);
	        GL11.glEnable(GL11.GL_LIGHTING);
	        GL11.glDisable(GL11.GL_BLEND);
	        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	        GL11.glPopMatrix();
        }
    }
    
    protected void renderLivingLabel(String par2Str, double par3, double par5, double par7, int par9)
    {
        //float var10 = par1EntityLiving.getDistanceToEntity(this.renderManager.livingPlayer);

    	//GL11.glPushMatrix();
    	//FMLClientHandler.instance().getClient().fontRenderer.drawString("test", 10, 10, 5);
    	//GL11.glPopMatrix();
    	
    	//if (true) return;
    	
        //if (var10 <= (float)par9)
        //{
            FontRenderer var11 = RenderManager.instance.getFontRenderer();
            float var12 = 0.8F;
            float var13 = 0.016666668F * var12;
            GL11.glPushMatrix();
            GL11.glTranslatef((float)par3 + 0.0F, (float)par5 + 0.2F, (float)par7);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);
            GL11.glScalef(-var13, -var13, var13);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDepthMask(false);
            //GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            Tessellator var14 = Tessellator.instance;
            byte var15 = 0;
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            var14.startDrawingQuads();
            int var16 = var11.getStringWidth(par2Str) / 2;
            var14.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
            var14.addVertex((double)(-var16 - 1), (double)(-1 + var15), 0.0D);
            var14.addVertex((double)(-var16 - 1), (double)(8 + var15), 0.0D);
            var14.addVertex((double)(var16 + 1), (double)(8 + var15), 0.0D);
            var14.addVertex((double)(var16 + 1), (double)(-1 + var15), 0.0D);
            var14.draw();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            var11.drawString(par2Str, -var11.getStringWidth(par2Str) / 2, var15, 553648127);
            //GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(true);
            var11.drawString(par2Str, -var11.getStringWidth(par2Str) / 2, var15, -1);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
        //}
    }
}
