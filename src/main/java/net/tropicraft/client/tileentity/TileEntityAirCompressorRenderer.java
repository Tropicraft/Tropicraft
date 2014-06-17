package net.tropicraft.client.tileentity;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.tropicraft.block.tileentity.TileEntityAirCompressor;
import net.tropicraft.client.entity.model.ModelAirCompressor;
import net.tropicraft.client.entity.model.ModelScubaTank;
import net.tropicraft.util.TropicraftUtils;

import org.lwjgl.opengl.GL11;

public class TileEntityAirCompressorRenderer extends TileEntitySpecialRenderer {

    private ModelAirCompressor model = new ModelAirCompressor();
    private ModelScubaTank modelScubaTank = new ModelScubaTank();

    public TileEntityAirCompressorRenderer() {

    }

    /**
     * All rendering of Air Compressor done here
     * @param te TileEntityEIHMixer instance
     * @param x xCoord
     * @param y yCoord
     * @param z zCoord
     * @param partialTicks partial ticks
     */
    private void renderAirCompressor(TileEntityAirCompressor te, double x, double y, double z, float partialTicks) {
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

        float angle = Float.MIN_VALUE;
        if (te.isCompressing()) {
            angle = MathHelper.sin((float)(5f * 2f * Math.PI * te.getTickRatio())) * 15f;
//            float angle = MathHelper.wrapAngleTo180_float((float) (Math.PI * te.getTickRatio()));
            //System.out.println(angle);
            GL11.glRotatef(angle, 0f, 1f, 0f);
        }
     //   System.out.println(te.isCompressing());
        if ((angle <= -13 && angle != Float.MIN_VALUE) || angle >= 13)
            TropicraftUtils.bindTextureTE("airCompressorBlow");
        else
            TropicraftUtils.bindTextureTE("airCompressor");
        model.renderAirCompressor();

        if (te.isCompressing()) {
            GL11.glPushMatrix();
            GL11.glScalef(1.1F, 1.1F, 1.1F);
           // GL11.glScalef(10.0F, 10.0F, 10.0F);
            GL11.glTranslatef(-0.35f, 0.9f, 0.4f);
/*            if (te.isDoneMixing()) {
                modelBambooMug.renderLiquid = true;
                modelBambooMug.liquidColor = ItemCocktail.getCocktailColor(te.result);
            } else {
                modelBambooMug.renderLiquid = false;
            }*/
            GL11.glRotatef(180, 1, 0, 0);
            TropicraftUtils.bindTextureArmor("scubaGearPink");
            modelScubaTank.renderBambooMug();
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
        renderAirCompressor((TileEntityAirCompressor)var1, var2, var4, var6, var8);
    }

}