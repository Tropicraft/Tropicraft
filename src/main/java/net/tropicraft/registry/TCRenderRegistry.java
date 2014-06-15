package net.tropicraft.registry;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.tropicraft.block.tileentity.TileEntityBambooChest;
import net.tropicraft.client.entity.model.ModelSeahorse;
import net.tropicraft.client.entity.render.RenderChair;
import net.tropicraft.client.entity.render.RenderPoolFloat;
import net.tropicraft.client.entity.render.RenderSeahorse;
import net.tropicraft.client.entity.render.RenderUmbrella;
import net.tropicraft.client.tileentity.TileEntityBambooChestRenderer;
import net.tropicraft.entity.placeable.EntityChair;
import net.tropicraft.entity.placeable.EntityUmbrella;
import net.tropicraft.entity.pool.EntityPoolFloat;
import net.tropicraft.entity.underdasea.EntitySeahorse;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class TCRenderRegistry {

	public static void initEntityRenderers() {
		registerEntityRender(EntityChair.class, new RenderChair());
		registerEntityRender(EntityUmbrella.class, new RenderUmbrella());
		registerEntityRender(EntityPoolFloat.class, new RenderPoolFloat());
		registerEntityRender(EntitySeahorse.class, new RenderSeahorse(new ModelSeahorse(), 0.75F));
	}
	
	public static void initTileEntityRenderers() {
	    registerTileEntityRenderer(TileEntityBambooChest.class, new TileEntityBambooChestRenderer());
	}
	
	private static void registerTileEntityRenderer(Class<? extends TileEntity> tileEntityClass, 
	        TileEntitySpecialRenderer specialRenderer) {
	    ClientRegistry.bindTileEntitySpecialRenderer(tileEntityClass, specialRenderer);
	}
	
	private static void registerEntityRender(Class<? extends Entity> entityClass, Render render) {
		RenderingRegistry.registerEntityRenderingHandler(entityClass, render);
	}	
}
