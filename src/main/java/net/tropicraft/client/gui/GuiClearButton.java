package net.tropicraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.core.client.TropicraftRenderUtils;

@SideOnly(Side.CLIENT)
public class GuiClearButton extends GuiButton {

	public int id;
	public int type;
	public String buttonImageLoc;
	public int color;

	public GuiClearButton(int i, int j, int k, String s) {
		this(i, j, k, 45, 45, s);
	}

    public GuiClearButton(int i, int j, int k, int l, int i1, String s) {
        super(i, j, k, l, i1, s);
        id = i;
    }

    public GuiClearButton(int i, int j, int k, int l, int i1, String s, int Type, String img, int TextColor) {
        super(i, j, k, l, i1, s);
		id = i;
		type = Type;
		buttonImageLoc = img;
		color = TextColor;
	}

	public void initGui() {

	}

	@Override
    public int getHoverState(boolean flag) {
		byte byte0 = 1;
		if(!enabled)
		{
			byte0 = 0;
		} else
			if(flag)
			{
				byte0 = 2;
			}
		return byte0;
	}

	@Override
	public void drawButton(Minecraft minecraft, int i, int j, float partialTicks) {
		if (!visible) {
			return;
		}

		boolean flag = i >= x && j >= y && i < x + width && j < y + height;

		FontRenderer fontrenderer = minecraft.fontRenderer;

		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();//glDisable(2929 /*GL_DEPTH_TEST*/);
		GlStateManager.pushMatrix();
		TropicraftRenderUtils.bindTextureGui(buttonImageLoc);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		int k = getHoverState(flag);	        	   
		drawTexturedModalRect(x, y, type * 22 + 168 + (k - 1) * 11 , 236, 11, 20);
		GlStateManager.popMatrix();
		mouseDragged(minecraft, i, j);

		if(!enabled) {
			fontrenderer.drawString(displayString, x , y + (height - 8) / 2, 0xffa0a0a0);
		} else if(flag) {
			fontrenderer.drawString(displayString, x , y + (height - 8) / 2, 0x990000);
		} else {
			fontrenderer.drawString(displayString, x,y + (height - 8) / 2, color);	            
		}
		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
		//GL11.glEnable(2896 /*GL_LIGHTING*/);
		//GL11.glEnable(2929 /*GL_DEPTH_TEST*/);           
	}


	@Override
    protected void mouseDragged(Minecraft minecraft, int i, int j)
	{
	}

	@Override
    public void mouseReleased(int i, int j)
	{
	}

	@Override
    public boolean mousePressed(Minecraft minecraft, int i, int j)
	{
		return enabled && visible && i >= x && j >= y && i < x + width && j < y + height;
	}
}