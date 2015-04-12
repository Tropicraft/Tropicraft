package net.tropicraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.tropicraft.util.TropicraftUtils;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiClearButton extends GuiButton {

	protected int width;
	protected int height;
	public int xPosition;
	public int yPosition;
	public String displayString;
	public int id;
	public boolean enabled;
	public boolean drawButton;
	public int type;
	public String buttonImageLoc;
	public int color;

	public GuiClearButton(int i, int j, int k, String s) {
		this(i, j, k, 45, 45, s);
	}

	public GuiClearButton(int i, int j, int k, int l, int i1, String s) {
		super(i,j,k,l,i1,"");
		width = 200;
		height = 20;
		enabled = true;
		drawButton = true;
		id = i;
		xPosition = j;
		yPosition = k;
		width = l;
		height = i1;
		displayString = s;   

	}

	public GuiClearButton(int i, int j, int k, int l, int i1, String s, int Type, String img, int TextColor) {
		super(i,j,k,l,i1,"");
		width = 200;
		height = 20;
		enabled = true;
		drawButton = true;
		id = i;
		xPosition = j;
		yPosition = k;
		width = l;
		height = i1;
		displayString = s;
		type = Type;
		buttonImageLoc = img;
		color = TextColor;

	}

	public void initGui() {

	}

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

	public void drawButton(Minecraft minecraft, int i, int j) {
		if(!drawButton) {
			return;
		}
		
		boolean flag = i >= xPosition && j >= yPosition && i < xPosition + width && j < yPosition + height;

		FontRenderer fontrenderer = minecraft.fontRenderer;

		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(2896 /*GL_LIGHTING*/);
		GL11.glDisable(2929 /*GL_DEPTH_TEST*/);
		GL11.glPushMatrix();
		TropicraftUtils.bindTextureGui(buttonImageLoc);
		//        GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, minecraft.renderEngine.getTexture(gui));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int k = getHoverState(flag);	        	   
		GL11.glScalef(1.35F, 1.35F, 1.35F);	           
		drawTexturedModalRect((int)(xPosition/1.35), (int)(yPosition/1.35), type*16 + 128 + (k -1)*8 , 240, 8, 16);
		GL11.glPopMatrix();
		mouseDragged(minecraft, i, j);
		//TODO    minecraft.renderEngine.resetBoundTexture();
		if(!enabled)
		{
			fontrenderer.drawString( displayString, xPosition , yPosition + (height - 8) / 2, 0xffa0a0a0);
		} else
			if(flag)
			{
				fontrenderer.drawString(displayString, xPosition , yPosition + (height - 8) / 2, 0x990000);
			} else
			{
				fontrenderer.drawString(displayString, xPosition,yPosition + (height - 8) / 2, color);	            
			}
		//GL11.glScalef(1.0F, 1.0F, 1.0F);       
		GL11.glEnable(2896 /*GL_LIGHTING*/);
		GL11.glEnable(2929 /*GL_DEPTH_TEST*/);           
	}


	protected void mouseDragged(Minecraft minecraft, int i, int j)
	{
	}

	public void mouseReleased(int i, int j)
	{
	}

	public boolean mousePressed(Minecraft minecraft, int i, int j)
	{
		return enabled && drawButton && i >= xPosition && j >= yPosition && i < xPosition + width && j < yPosition + height;
	}
}