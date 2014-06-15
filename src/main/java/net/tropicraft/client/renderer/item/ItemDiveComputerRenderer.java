package net.tropicraft.client.renderer.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
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
import net.minecraft.world.World;
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

        // TODO http://www.dansdiveshop.ca/dstore/images/cobalt.jpg
        // TODO show time of day / cool compass type thing?

        float airRemaining;

        float airTemp;

        float timeRemaining;

        int blocksAbove, blocksBelow;

        ItemStack chestplate = player.getEquipmentInSlot(3);

        if (chestplate != null && chestplate.getItem() instanceof ItemScubaChestplateGear) {
            airRemaining = getTagCompound(chestplate).getInteger("AirRemaining");
        } else
            return;

        int currentDepth = MathHelper.floor_double(player.posY);
        int maxDepth = getTagCompound(chestplate).getInteger("MaxDepth");
        airRemaining = chestplate.getTagCompound().getFloat("AirContained");

        blocksAbove = chestplate.getTagCompound().getInteger("WaterBlocksAbove");
        blocksBelow = chestplate.getTagCompound().getInteger("WaterBlocksBelow");

        ItemScubaChestplateGear gear = (ItemScubaChestplateGear)chestplate.getItem();

        timeRemaining = (airRemaining / (gear.getAirType(chestplate).getUsageRate()));

        String timeUnits = timeRemaining <= 60 ? "secs" : "mins";

        timeRemaining = timeRemaining <= 60 ? timeRemaining : timeRemaining / 60;

        airTemp = player.worldObj.getBiomeGenForCoords(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posZ)).temperature;

        int width = Minecraft.getMinecraft().displayWidth;
        int height = Minecraft.getMinecraft().displayHeight;

        float yaw = player.rotationYaw;
        int heading = MathHelper.floor_double((double)(yaw * 4.0F / 360.0F) + 0.5D) & 3;

        GL11.glPushMatrix();
        //   RenderHelper.enableStandardItemLighting();
        //   RenderHelper.enableGUIStandardItemLighting();
        // Tessellator.instance.setBrightness(255555);
        //  Tessellator.instance.setColorRGBA(255, 255, 255, 255);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_ALPHA_TEST);

        textureManager.bindTexture(TropicraftUtils.getTextureGui("diveComputerBackground"));
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
        GL11.glNormal3f(0.0F, 0.0F, 1.0F);
        GL11.glScalef(0.45F, 0.45F, 1.0F);
        GL11.glTranslatef(150.0F, 150.0F, 0.0F);
        GL11.glRotatef(yaw + 180, 0.0F, 0.0F, -1.0F);
        textureManager.bindTexture(TropicraftUtils.getTextureGui("compassBackground"));
        tessellator.startDrawingQuads();
        int offset = -75;
        other = 150;
        tessellator.addVertexWithUV((double)(0 + offset), (double)(other + offset), 0.0D, 0.0D, 1.0D);
        tessellator.addVertexWithUV((double)(other + offset), (double)(other + offset), 0.0D, 1.0D, 1.0D);
        tessellator.addVertexWithUV((double)(other + offset), (double)(0 + offset), 0.0D, 1.0D, 0.0D);
        tessellator.addVertexWithUV((double)(0 + offset), (double)(0 + offset), 0.0D, 0.0D, 0.0D);
        tessellator.draw();
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

        GL11.glPushMatrix();
        GL11.glScalef(1.3F, 1.3F, 1.0F);
        Minecraft.getMinecraft().fontRenderer.drawString(String.format("%.0f", airRemaining), 70, 14, 0x00ccde);
        drawString(currentDepth, 46, 79, 0xbbbbff);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glScalef(0.6F, 0.6F, 1.0F);
        if (isFullyUnderwater(player)) {
            drawString("Blocks Above", 2, 58, 0xabcdef);
            drawString(blocksAbove, 33, 66, 0xffaabb);

            drawString("Blocks Below", 168, 58, 0xabcdef);
            drawString(blocksBelow, 200, 66, 0xffaabb);
        }
        drawString(String.format("%.0f %s", timeRemaining, timeUnits), 29, 30, 0xF6EB12);
        drawString(TropicraftUtils.translateGUI("maxDepth") + ": " + maxDepth, 194, 150, 0xffffffff);
        drawString(airTemp + " F", 6, 150, 0xffffffff);
        GL11.glPopMatrix();
        GL11.glScalef(0.5F, 0.5F, 1.0F);
        drawString("Y", 151, 215, 0xffffff);
        drawString("psi", 245, 47, 0xffffff);
        drawString("Air", 206, 24, 0xffffff);
        drawString(TropicraftUtils.translateGUI("timeRemaining"), 34, 24, 0xffffff);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        //  RenderHelper.enableStandardItemLighting();
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }

    private boolean isFullyUnderwater(EntityPlayer player) {
        int x = MathHelper.ceiling_double_int(player.posX);
        int y = MathHelper.ceiling_double_int(player.posY);
        int z = MathHelper.ceiling_double_int(player.posZ);

        return player.worldObj.getBlock(x, y, z).getMaterial().isLiquid();
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
