package net.tropicraft.client.entity.render;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureCompass;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.MapData;
import net.tropicraft.entity.EntityTCItemFrame;
import net.tropicraft.registry.TCBlockRegistry;

import org.lwjgl.opengl.GL11;

public class RenderTCItemFrame extends Render {

    private final RenderBlocks renderBlocksInstance = new RenderBlocks();
    private static final ResourceLocation mapBackgroundTextures = new ResourceLocation("textures/map/map_background.png");

    public void func_82404_a(EntityTCItemFrame par1EntityItemFrame, double par2, double par4, double par6, float par8, float par9) {
        GL11.glPushMatrix();
        float var10 = (float)(par1EntityItemFrame.posX - par2) - 0.5F;
        float var11 = (float)(par1EntityItemFrame.posY - par4) - 0.5F;
        float var12 = (float)(par1EntityItemFrame.posZ - par6) - 0.5F;
        int var13 = par1EntityItemFrame.xPosition + Direction.offsetX[par1EntityItemFrame.hangingDirection];
        int var14 = par1EntityItemFrame.yPosition;
        int var15 = par1EntityItemFrame.zPosition + Direction.offsetZ[par1EntityItemFrame.hangingDirection];
        GL11.glTranslatef((float)var13 - var10, (float)var14 - var11, (float)var15 - var12);
        this.renderFrameItemAsBlock(par1EntityItemFrame);
        this.func_147915_b(par1EntityItemFrame);
        GL11.glPopMatrix();
    }

    /**
     * Render the item frame's item as a block.
     */
    private void renderFrameItemAsBlock(EntityItemFrame par1EntityItemFrame) {
        //RenderItemFrame
        GL11.glPushMatrix();
        //GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderManager.renderEngine.getTexture("/mods/TropicraftMod/textures/blocks/bamboobundle.png"));
        this.renderManager.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        //GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderManager.renderEngine.getTexture("/terrain.png"));
        GL11.glRotatef(par1EntityItemFrame.rotationYaw, 0.0F, 1.0F, 0.0F);
        Block var2 = TCBlockRegistry.bambooBundle;
        float var3 = 0.0625F;
        float var4 = 0.75F;
        float var5 = var4 / 2.0F;
        GL11.glPushMatrix();
        this.renderBlocksInstance.overrideBlockBounds(0.0D, (double)(0.5F - var5 + 0.0625F), (double)(0.5F - var5 + 0.0625F), (double)(var3 * 0.5F), (double)(0.5F + var5 - 0.0625F), (double)(0.5F + var5 - 0.0625F));
        this.renderBlocksInstance.setOverrideBlockTexture(renderBlocksInstance.getBlockIcon(TCBlockRegistry.bambooBundle));
        this.renderBlocksInstance.renderBlockAsItem(var2, 0, 1.0F);
        this.renderBlocksInstance.clearOverrideBlockTexture();
        this.renderBlocksInstance.unlockBlockBounds();
        GL11.glPopMatrix();
        this.renderBlocksInstance.setOverrideBlockTexture(renderBlocksInstance.getBlockIcon(TCBlockRegistry.bambooBundle));
        GL11.glPushMatrix();
        this.renderBlocksInstance.overrideBlockBounds(0.0D, (double)(0.5F - var5), (double)(0.5F - var5), (double)(var3 + 1.0E-4F), (double)(var3 + 0.5F - var5), (double)(0.5F + var5));
        this.renderBlocksInstance.renderBlockAsItem(var2, 0, 1.0F);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        this.renderBlocksInstance.overrideBlockBounds(0.0D, (double)(0.5F + var5 - var3), (double)(0.5F - var5), (double)(var3 + 1.0E-4F), (double)(0.5F + var5), (double)(0.5F + var5));
        this.renderBlocksInstance.renderBlockAsItem(var2, 0, 1.0F);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        this.renderBlocksInstance.overrideBlockBounds(0.0D, (double)(0.5F - var5), (double)(0.5F - var5), (double)var3, (double)(0.5F + var5), (double)(var3 + 0.5F - var5));
        this.renderBlocksInstance.renderBlockAsItem(var2, 0, 1.0F);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        this.renderBlocksInstance.overrideBlockBounds(0.0D, (double)(0.5F - var5), (double)(0.5F + var5 - var3), (double)var3, (double)(0.5F + var5), (double)(0.5F + var5));
        this.renderBlocksInstance.renderBlockAsItem(var2, 0, 1.0F);
        GL11.glPopMatrix();
        this.renderBlocksInstance.unlockBlockBounds();
        this.renderBlocksInstance.clearOverrideBlockTexture();
        GL11.glPopMatrix();
    }
    
    private void func_147915_b(EntityItemFrame p_147915_1_)
    {
        GL11.glPushMatrix();
        GL11.glRotatef(p_147915_1_.rotationYaw, 0.0F, 1.0F, 0.0F);
        this.renderManager.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        Block block = Blocks.planks;
        float f = 0.0625F;
        float f1 = 1.0F;
        float f2 = f1 / 2.0F;
        GL11.glPushMatrix();
        this.renderBlocksInstance.overrideBlockBounds(0.0D, (double)(0.5F - f2 + 0.0625F), (double)(0.5F - f2 + 0.0625F), (double)f, (double)(0.5F + f2 - 0.0625F), (double)(0.5F + f2 - 0.0625F));
        this.renderBlocksInstance.setOverrideBlockTexture(TCBlockRegistry.bambooBundle.getIcon(0, 0));
        this.renderBlocksInstance.renderBlockAsItem(block, 0, 1.0F);
        this.renderBlocksInstance.clearOverrideBlockTexture();
        this.renderBlocksInstance.unlockBlockBounds();
        GL11.glPopMatrix();
        this.renderBlocksInstance.setOverrideBlockTexture(Blocks.planks.getIcon(1, 2));
        GL11.glPushMatrix();
        this.renderBlocksInstance.overrideBlockBounds(0.0D, (double)(0.5F - f2), (double)(0.5F - f2), (double)(f + 1.0E-4F), (double)(f + 0.5F - f2), (double)(0.5F + f2));
        this.renderBlocksInstance.renderBlockAsItem(block, 0, 1.0F);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        this.renderBlocksInstance.overrideBlockBounds(0.0D, (double)(0.5F + f2 - f), (double)(0.5F - f2), (double)(f + 1.0E-4F), (double)(0.5F + f2), (double)(0.5F + f2));
        this.renderBlocksInstance.renderBlockAsItem(block, 0, 1.0F);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        this.renderBlocksInstance.overrideBlockBounds(0.0D, (double)(0.5F - f2), (double)(0.5F - f2), (double)f, (double)(0.5F + f2), (double)(f + 0.5F - f2));
        this.renderBlocksInstance.renderBlockAsItem(block, 0, 1.0F);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        this.renderBlocksInstance.overrideBlockBounds(0.0D, (double)(0.5F - f2), (double)(0.5F + f2 - f), (double)f, (double)(0.5F + f2), (double)(0.5F + f2));
        this.renderBlocksInstance.renderBlockAsItem(block, 0, 1.0F);
        GL11.glPopMatrix();
        this.renderBlocksInstance.unlockBlockBounds();
        this.renderBlocksInstance.clearOverrideBlockTexture();
        GL11.glPopMatrix();
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
        this.func_82404_a((EntityTCItemFrame)par1Entity, par2, par4, par6, par8, par9);
    }

    @Override

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity entity) {
        return null;
    }
}
