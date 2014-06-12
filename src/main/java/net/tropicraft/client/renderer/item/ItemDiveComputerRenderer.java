package net.tropicraft.client.renderer.item;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureCompass;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Direction;
import net.minecraft.util.MathHelper;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.client.IItemRenderer;
import net.tropicraft.item.scuba.ItemScubaChestplateGear;
import net.tropicraft.registry.TCItemRegistry;
import net.tropicraft.util.TropicraftUtils;

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
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        textureManager.bindTexture(TropicraftUtils.bindTextureGui("diveComputerBackground"));
        Tessellator tessellator = Tessellator.instance;
        GL11.glNormal3f(0.0F, 0.0F, -1.0F);
        tessellator.startDrawingQuads();
        byte b0 = 7;
        int other = 150;
        tessellator.addVertexWithUV((double)(0 - b0), (double)(other + b0), 0.0D, 0.0D, 1.0D);
        tessellator.addVertexWithUV((double)(other + b0), (double)(other + b0), 0.0D, 1.0D, 1.0D);
        tessellator.addVertexWithUV((double)(other + b0), (double)(0 - b0), 0.0D, 1.0D, 0.0D);
        tessellator.addVertexWithUV((double)(0 - b0), (double)(0 - b0), 0.0D, 0.0D, 0.0D);
        tessellator.draw();
        
        
        
        
        TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
        texturemanager.bindTexture(TextureMap.locationItemsTexture);
        TextureAtlasSprite textureatlassprite1 = ((TextureMap)texturemanager.getTexture(TextureMap.locationItemsTexture)).getAtlasSprite(Items.compass.getIconIndex(new ItemStack(Items.compass, 1, 0)).getIconName());

        if (textureatlassprite1 instanceof TextureCompass)
        {
      //      System.out.println("hey");
       /*     TextureCompass texturecompass = (TextureCompass)textureatlassprite1;
            double d0 = texturecompass.currentAngle;
            double d1 = texturecompass.angleDelta;
            texturecompass.currentAngle = 0.0D;
            texturecompass.angleDelta = 0.0D;
            texturecompass.updateCompass(player.worldObj, 0, 0, 0, false, true);
            texturecompass.currentAngle = d0;
            texturecompass.angleDelta = d1;*/
            
        }
        
        
        
        int i = player.worldObj.getLightBrightnessForSkyBlocks(MathHelper.floor_double(player.posX), 255, MathHelper.floor_double(player.posZ), 0);
        int j = i % 65536;
        int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j / 1.0F, (float)k / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().fontRenderer.drawString(String.format("%.0f", airRemaining), 96, 26, 0x00ccde);
        drawString(currentDepth, 68, 72, 0xffffffff);
        GL11.glPushMatrix();
        GL11.glScalef(0.6F, 0.6F, 1.0F);
        drawString(String.format("%.0f %s", timeRemaining, timeUnits), 42, 46, 0xF6EB12);
        GL11.glPopMatrix();
        GL11.glScalef(0.45F, 0.45F, 1.0F);
        drawString("Y", 179, 168, 0xffffff);
        drawString("psi", 269, 66, 0xffffff);
        drawString("Air", 218, 42, 0xffffff);
        drawString(TropicraftUtils.translateGUI("temperature") + ": " + airTemp + "", 20, 100, 0xffffffff);
        drawString(TropicraftUtils.translateGUI("maxDepth") + ": " + maxDepth, 20, 120, 0xffffffff);
        drawString(TropicraftUtils.translateGUI("timeRemaining"), 58, 42, 0xffffff);
        drawString(String.format("%s / %f", Direction.directions[heading], MathHelper.wrapAngleTo180_float(yaw)), 208, 100, 0xffffff);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
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
