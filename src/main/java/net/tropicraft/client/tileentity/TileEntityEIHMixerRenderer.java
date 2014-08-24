package net.tropicraft.client.tileentity;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.tropicraft.block.tileentity.TileEntityEIHMixer;
import net.tropicraft.client.block.model.ModelBambooMug;
import net.tropicraft.client.entity.model.ModelEIHMixer;
import net.tropicraft.item.ItemCocktail;
import net.tropicraft.util.TropicraftUtils;

import org.lwjgl.opengl.GL11;

public class TileEntityEIHMixerRenderer extends TileEntitySpecialRenderer {

  /**
     * EIHMixer model instance
     */
    private ModelEIHMixer modelMixer = new ModelEIHMixer();
    private ModelBambooMug modelBambooMug = new ModelBambooMug();
    private EntityItem dummyEntityItem = new EntityItem((World)null, 0.0, 0.0, 0.0, new ItemStack(Items.sugar));
    private RenderItem renderItem = new RenderItem();
    
    public TileEntityEIHMixerRenderer() {
        renderItem.setRenderManager(RenderManager.instance);
    }
    
    /**
     * All rendering of EIH mixer done here
     * @param te TileEntityEIHMixer instance
     * @param x xCoord
     * @param y yCoord
     * @param z zCoord
     * @param partialTicks partial ticks
     */
    private void renderEIHMixer(TileEntityEIHMixer te, double x, double y, double z, float partialTicks) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x+0.5f,(float)y+1.5f,(float)z+0.5f);
        GL11.glRotatef(180f, 1f, 0f, 1f);
        
        if (te.getWorldObj() == null) {
            GL11.glRotatef(180f, 0f, 1f, 0f);
        } else {
            int meta = te.getBlockMetadata();

            if (meta == 2) {
                GL11.glRotatef(0f, 0f, 1f, 0f);
            } else if (meta == 3) {
                GL11.glRotatef(180f, 0f, 1f, 0f);
            } else if (meta == 4) {
                GL11.glRotatef(270f, 0f, 1f, 0f);
            } else if (meta == 5) {
                GL11.glRotatef(90f, 0f, 1f, 0f);
            }
        }
        
        if (te.isMixing()) {
            float angle = MathHelper.sin((float)(25f*2f*Math.PI*te.ticks/te.TICKS_TO_MIX))*15f;
            GL11.glRotatef(angle, 0f, 1f, 0f);
        }
        
        TropicraftUtils.bindTextureTE("eihmixer");
        modelMixer.renderEIHMixer();
        ItemStack[] ingredients = te.getIngredients();
        
        if (!te.isDoneMixing()) {
            if (ingredients[0] != null) {
                GL11.glPushMatrix();
                GL11.glRotatef(180f, 1f, 0f, 1f);
                GL11.glTranslatef(0.3f, -0.5f, 0.05f);
                GL11.glRotatef(RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
                dummyEntityItem.setEntityItemStack(ingredients[0]);
                renderItem.doRender(dummyEntityItem, 0.0, 0.0, 0.0, 0f, 0f);
                GL11.glPopMatrix();
            }

            if (ingredients[1] != null) {
                GL11.glPushMatrix();
                GL11.glRotatef(180f, 1f, 0f, 1f);
                GL11.glTranslatef(-0.3f, -0.5f, 0.05f);
                GL11.glRotatef(RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
                dummyEntityItem.setEntityItemStack(ingredients[1]);
                renderItem.doRender(dummyEntityItem, 0.0, 0.0, 0.0, 0f, 0f);
                GL11.glPopMatrix();
            }
        }
        
        if (te.isMixing()) {
            GL11.glPushMatrix();
            GL11.glTranslatef(-0.2f, -0.25f, 0.0f);
            if (te.isDoneMixing()) {
                modelBambooMug.renderLiquid = true;
                modelBambooMug.liquidColor = ItemCocktail.getCocktailColor(te.result);
            } else {
                modelBambooMug.renderLiquid = false;
            }
            TropicraftUtils.bindTextureTE("bamboomug");
            modelBambooMug.renderBambooMug();
            GL11.glPopMatrix();
        }
        
        GL11.glPopMatrix();
    }

    /**
     * Bridge method, calls renderEIHMixer
     */
    @Override
    public void renderTileEntityAt(TileEntity var1, double var2, double var4,
            double var6, float var8) {
        renderEIHMixer((TileEntityEIHMixer)var1, var2, var4, var6, var8);
    }
}
