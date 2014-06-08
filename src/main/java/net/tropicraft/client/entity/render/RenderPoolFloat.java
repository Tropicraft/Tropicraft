package net.tropicraft.client.entity.render;

import static org.lwjgl.opengl.GL11.*;

import java.nio.FloatBuffer;

import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.entity.pool.EntityPoolFloat;
import net.tropicraft.util.TropicraftUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class RenderPoolFloat extends Render {
	public boolean compiled;

	/** The GL display list id */
	private int displayList;
	
	float red = 0.0F, green = 0.0F, blue = 0.0F, alpha = 1.0F;

	public RenderPoolFloat() {
		compiled = false;
	}

	@Override
	public void doRender(Entity var1, double var2, double var4, double var6,
			float var8, float var9) {
		renderPoolFloat((EntityPoolFloat)var1, var2, var4, var6, var8, var9);
	}

	private void renderPoolFloat(EntityPoolFloat entity, double x, double y, double z, float var8, float var9) {
		red = TropicraftUtils.getRed(entity.getColor());
		green = TropicraftUtils.getGreen(entity.getColor());
		blue = TropicraftUtils.getBlue(entity.getColor());

		glPushMatrix();
		glTranslated(x, y + 0.3, z);
		glRotated(90.0F, 1.0F, 0.0F, 0.0F);
		glDisable(GL_CULL_FACE);
	//	glDisable(GL_DEPTH_TEST);

		// Radius for a torus is a follows:
		// x(theta0, theta1) = (R + r * cos(theta1)) * cos (theta0)
		// y(theta0, theta1) = (R + r * cos(theta1)) * sin (theta0)
		// z(theta0, theta1) = r * sin(theta1)
		//
		// From wikipedia:
		// theta0, theta1 are angles which make a full circle, starting at 0 and ending at 2pi, so that their values start and end at the same point,
		// R is the distance from the center of the tube to the center of the torus,
		// r is the radius of the tube.

		if (!this.compiled) {
			this.compileDisplayList(1);
		}

	//	glEnable(GL_BLEND);
	//	glBlendFunc(GL_DST_COLOR, GL_ONE_MINUS_DST_COLOR);
		glColor4f(red, green, blue, alpha);
		glCallList(this.displayList);
/*		glBlendFunc(GL_ONE, GL_SRC_ALPHA);
		glColor4f(0.0F, 0.5F, 0.5F, 1.0F);
		glCallList(this.displayList);*/

	//	glEnable(GL_DEPTH_TEST);
	//	glDisable(GL_BLEND);
		glEnable(GL_CULL_FACE);
		glPopMatrix();
	}

	/**
	 * Compiles a GL display list for this model
	 */
	@SideOnly(Side.CLIENT)
	private void compileDisplayList(float par1)
	{
		this.displayList = GLAllocation.generateDisplayLists(1);
		glNewList(this.displayList, GL_COMPILE);
		int i, j, k, numc=8, numt=25;
		double s, t, xx, yy, zz, twopi;
		double radius = 0.3;
		double distance = 0.6;

		twopi = 2 * (double)Math.PI;
		for (i = 0; i < numc; i++) {
			glBegin(GL_QUAD_STRIP);
			for (j = 0; j <= numt; j++) {
				for (k = 1; k >= 0; k--) {
					s = (i + k) % numc + 0.5;
					t = j % numt;

					xx = (distance + radius * Math.cos(s * twopi / numc)) * Math.cos(t * twopi / numt);
					yy = (distance + radius * Math.cos(s * twopi / numc)) * Math.sin(t * twopi / numt);
					zz = radius * Math.sin(s * twopi / numc);
					glVertex3d(xx, yy, zz);
				}
			}
			glEnd();
		}

		glEndList();
		this.compiled = true;
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity var1) {
		return null;
	}

}
