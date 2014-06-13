package net.tropicraft.client.renderer.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureCompass;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Direction;
import net.minecraft.util.MathHelper;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.client.IItemRenderer;
import net.tropicraft.item.scuba.ItemScubaChestplateGear;
import net.tropicraft.util.TropicraftUtils;

import org.lwjgl.opengl.GL11;

public class ItemDiveComputerRenderer implements IItemRenderer {

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return type == ItemRenderType.FIRST_PERSON_MAP;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        EntityPlayer player = (EntityPlayer)data[0];
        TextureManager textureManager = (TextureManager)data[1];
        MapData mapData = (MapData)data[2];
       /* 
        if (!player.inventory.hasItem(TCItemRegistry.diveComputer))
            return;*/
        
      

        // TODO http://www.dansdiveshop.ca/dstore/images/cobalt.jpg
        // TODO show time of day / cool compass type thing?

        float airRemaining;

        float airTemp;
        
        float timeRemaining;

        ItemStack chestplate = player.getEquipmentInSlot(3);

        if (chestplate != null && chestplate.getItem() instanceof ItemScubaChestplateGear) {
            airRemaining = getTagCompound(chestplate).getInteger("AirRemaining");
        } else
            return;

        int currentDepth = chestplate.getTagCompound().getInteger("CurrentDepth");
        int maxDepth = getTagCompound(chestplate).getInteger("MaxDepth");
        airRemaining = chestplate.getTagCompound().getFloat("AirContained");
        
        ItemScubaChestplateGear gear = (ItemScubaChestplateGear)chestplate.getItem();
        
        timeRemaining = (airRemaining / (gear.getAirType(chestplate).getUsageRate()));
        
        String timeUnits = timeRemaining <= 60 ? "secs" : "mins";
        
        timeRemaining = timeRemaining <= 60 ? timeRemaining : timeRemaining / 60;

        airTemp = player.worldObj.getBiomeGenForCoords(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posZ)).temperature;

        int width = Minecraft.getMinecraft().displayWidth;
        int height = Minecraft.getMinecraft().displayHeight;

        float yaw = player.rotationYaw;
        int heading = MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        
        GL11.glPushMatrix();
    //    GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        
        
      /*  GL11.glPushMatrix();
        GL11.glScalef(0.4F, 0.4F, 1.0F);
        GL11.glTranslatef(130.0F, 160.0F, 1.0F);
        GL11.glRotatef(-1 * MathHelper.wrapAngleTo180_float(yaw), 0.0F, 0.0F, 0.1F);
        drawString("test", 60, 50, 0xff0000);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
        
        */
        
        
        textureManager.bindTexture(TropicraftUtils.bindTextureGui("diveComputerBackground"));
        Tessellator tessellator = Tessellator.instance;
        GL11.glNormal3f(0.0F, 0.0F, -1.0F);
        tessellator.startDrawingQuads();
        byte b0 = 16;
        int other = 140;
        tessellator.addVertexWithUV((double)(0 - b0), (double)(other + b0), 0.0D, 0.0D, 1.0D);
        tessellator.addVertexWithUV((double)(other + b0), (double)(other + b0), 0.0D, 1.0D, 1.0D);
        tessellator.addVertexWithUV((double)(other + b0), (double)(0 - b0), 0.0D, 1.0D, 0.0D);
        tessellator.addVertexWithUV((double)(0 - b0), (double)(0 - b0), 0.0D, 0.0D, 0.0D);
        tessellator.draw();
        
        GL11.glPushMatrix();
       // GL11.glNormal3f(1.0F, 1.0F, 0.0F);
        GL11.glTranslatef(50.0F, 50.0F, 0.0F);
        GL11.glRotatef(MathHelper.wrapAngleTo180_float(yaw), 0.0F, 0.0F, 1.0F);
        GL11.glTranslatef(0.0F, 0.0F, 0.0F);
        GL11.glScalef(1.0F, 1.0F, 1.0F);
        textureManager.bindTexture(TropicraftUtils.bindTextureGui("compassBackground"));
        tessellator.startDrawingQuads();
        b0 = 8;
        other = 60;
        tessellator.addVertexWithUV((double)(0 - b0), (double)(other + b0), 0.0D, 0.0D, 1.0D);
        tessellator.addVertexWithUV((double)(other + b0), (double)(other + b0), 0.0D, 1.0D, 1.0D);
        tessellator.addVertexWithUV((double)(other + b0), (double)(0 - b0), 0.0D, 1.0D, 0.0D);
        tessellator.addVertexWithUV((double)(0 - b0), (double)(0 - b0), 0.0D, 0.0D, 0.0D);
        tessellator.draw();
    //    GL11.glTranslatef(1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
        
        
      /*  int i = player.worldObj.getLightBrightnessForSkyBlocks(MathHelper.floor_double(player.posX), 255, MathHelper.floor_double(player.posZ), 0);
        int j = i % 65536;
        int k = i / 65536;*/
    //    GL11.glDisable(GL11.GL_LIGHTING);
    //    Tessellator.instance.setColorRGBA(255, 255, 255, 255);
     //   OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j / 1.0F, (float)k / 1.0F);
   //     RenderHelper.enableStandardItemLighting();
   //     OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().fontRenderer.drawString(String.format("%.0f", airRemaining), 96, 26, 0x00ccde);
        GL11.glPushMatrix();
        GL11.glScalef(1.3F, 1.3F, 1.0F);
        drawString(currentDepth, 53, 78, 0xbbbbff);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glScalef(0.6F, 0.6F, 1.0F);
        drawString(String.format("%.0f %s", timeRemaining, timeUnits), 42, 46, 0xF6EB12);
        drawString(TropicraftUtils.translateGUI("maxDepth") + ": " + maxDepth, 194, 148, 0xffffffff);
        drawString(airTemp + " F", 20, 148, 0xffffffff);
        GL11.glPopMatrix();
        GL11.glScalef(0.5F, 0.5F, 1.0F);
        drawString("Y", 169, 214, 0xffffff);
        drawString("psi", 242, 59, 0xffffff);
        drawString("Air", 210, 41, 0xffffff);
        drawString(TropicraftUtils.translateGUI("timeRemaining"), 50, 41, 0xffffff);
        drawString(String.format("%s / %f", Direction.directions[heading], MathHelper.wrapAngleTo180_float(yaw)), 92, 90, 0xffffff);
       // public static final String[] directions = new String[] {"SOUTH", "WEST", "NORTH", "EAST"};
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_BLEND);
    //    RenderHelper.disableStandardItemLighting();
     //   GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }
    
    /**
     * Retrives an existing nbt tag compound or creates a new one if it is null
     * @param stack
     * @return
     */
    public NBTTagCompound getTagCompound(ItemStack stack) {
        if (!stack.hasTagCompound())
            stack.setTagCompound(new NBTTagCompound());
        
        return stack.getTagCompound();
    }
    
    private void drawString(Object text, int x, int y, int color) {
        Minecraft.getMinecraft().fontRenderer.drawString(String.valueOf(text), x, y, color);
    }

}
