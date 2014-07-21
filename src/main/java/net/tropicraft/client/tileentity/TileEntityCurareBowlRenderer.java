package net.tropicraft.client.tileentity;

import java.util.List;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.tropicraft.block.tileentity.TileEntityCurareBowl;
import net.tropicraft.client.block.model.ModelCurareBowl;
import net.tropicraft.util.TropicraftUtils;

import org.lwjgl.opengl.GL11;

public class TileEntityCurareBowlRenderer extends TileEntitySpecialRenderer {

    ModelCurareBowl modelBowl = new ModelCurareBowl();
    private EntityItem dummyEntityItem = new EntityItem(null, 0.0, 0.0, 0.0, new ItemStack(Items.sugar));
    private RenderItem renderItem = new RenderItem();

    public TileEntityCurareBowlRenderer() {
        renderItem.setRenderManager(RenderManager.instance);
    }

    private void renderBowl(TileEntityCurareBowl bowl, double x, double y, double z, float partialTicks) {
        GL11.glPushMatrix();
        GL11.glScalef(1F, 1F, 1F);
        GL11.glTranslatef((float)x+0.5f,(float)y+1.5f,(float)z+0.5f);
        GL11.glRotatef(180f, 1f, 0f, 1f);
        List<ItemStack> ingredients = bowl.getIngredientList();
        
        if (ingredients != null) {
            int count = ingredients.size();
            
            float turnRate = 1f;
            float startAngle;
            
            if (bowl.pestleTicks > 0) {
                int ticksPerPestleClick = TileEntityCurareBowl.TICKS_PER_PESTLE_CLICK;
                float progress = bowl.pestleTicks/(float)ticksPerPestleClick;
                startAngle = (float) (turnRate*2*Math.PI*progress);
            } else {
                startAngle = 0f;
            }

            for (int i = 0; i < count; i++) {
                GL11.glPushMatrix();
                float angle = (float)(2*Math.PI*((float)i)/count);
                float radius = 0.666f;
                float offsetx = MathHelper.sin(startAngle+angle)*radius;
                float offsetz = MathHelper.cos(startAngle+angle)*radius;
                GL11.glTranslatef(-0.05f, 1.4f, 0.0f);
                GL11.glScalef(0.333f, 0.333f, 0.333f);
                GL11.glTranslatef(offsetx, 0, offsetz);
                GL11.glRotatef(90f, 0f, 0f, 1f);
                GL11.glRotatef(180f, 1f, 0f, 1f);
                GL11.glRotatef(RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
                dummyEntityItem.setEntityItemStack(ingredients.get(i));
                renderItem.doRender(dummyEntityItem, 0.0, 0.0, 0.0, 0f, 0f);
                GL11.glPopMatrix();
            }
        }

        TropicraftUtils.bindTextureBlock("curareBowlModel");
        modelBowl.renderBowl();
        GL11.glPopMatrix();
    }

    @Override
    public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8) {
        renderBowl((TileEntityCurareBowl)var1, var2, var4, var6, var8);
    }
}
