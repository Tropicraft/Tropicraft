package net.tropicraft.registry;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.tropicraft.client.entity.render.RenderChair;
import net.tropicraft.client.entity.render.RenderPoolFloat;
import net.tropicraft.client.entity.render.RenderUmbrella;
import net.tropicraft.entity.placeable.EntityChair;
import net.tropicraft.entity.placeable.EntityUmbrella;
import net.tropicraft.entity.pool.EntityPoolFloat;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class TCRenderRegistry {

	public static void init() {
		registerRender(EntityChair.class, new RenderChair());
		registerRender(EntityUmbrella.class, new RenderUmbrella());
		registerRender(EntityPoolFloat.class, new RenderPoolFloat());
	}
	
	private static void registerRender(Class<? extends Entity> entityClass, Render render) {
		RenderingRegistry.registerEntityRenderingHandler(entityClass, render);
	}	
}
