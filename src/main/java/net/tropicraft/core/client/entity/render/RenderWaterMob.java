package net.tropicraft.core.client.entity.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.client.entity.model.ModelVMonkey;
import net.tropicraft.core.common.entity.underdasea.EntityTropicraftWaterMob;

import org.lwjgl.opengl.GL11;

public abstract class RenderWaterMob extends RenderLiving<EntityTropicraftWaterMob> {

	private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");

	public RenderWaterMob(ModelBase modelbase, float f) {
		super(Minecraft.getMinecraft().getRenderManager(), modelbase, f);
	}

}