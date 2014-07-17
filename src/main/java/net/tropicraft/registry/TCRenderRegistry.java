package net.tropicraft.registry;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.tropicraft.block.tileentity.TileEntityAirCompressor;
import net.tropicraft.block.tileentity.TileEntityBambooChest;
import net.tropicraft.client.entity.model.ModelEIH;
import net.tropicraft.client.entity.model.ModelIguana;
import net.tropicraft.client.entity.model.ModelSeahorse;
import net.tropicraft.client.entity.model.ModelTreeFrog;
import net.tropicraft.client.entity.render.RenderChair;
import net.tropicraft.client.entity.render.RenderDart;
import net.tropicraft.client.entity.render.RenderEIH;
import net.tropicraft.client.entity.render.RenderIguana;
import net.tropicraft.client.entity.render.RenderPoolFloat;
import net.tropicraft.client.entity.render.RenderSeahorse;
import net.tropicraft.client.entity.render.RenderTreeFrog;
import net.tropicraft.client.entity.render.RenderUmbrella;
import net.tropicraft.client.tileentity.TileEntityAirCompressorRenderer;
import net.tropicraft.client.tileentity.TileEntityBambooChestRenderer;
import net.tropicraft.entity.hostile.EntityEIH;
import net.tropicraft.entity.hostile.EntityTreeFrog;
import net.tropicraft.entity.passive.EntityIguana;
import net.tropicraft.entity.placeable.EntityChair;
import net.tropicraft.entity.placeable.EntityUmbrella;
import net.tropicraft.entity.pool.EntityPoolFloat;
import net.tropicraft.entity.projectile.EntityDart;
import net.tropicraft.entity.projectile.EntityPoisonBlot;
import net.tropicraft.entity.underdasea.EntitySeahorse;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class TCRenderRegistry {

    /**
     * Register all Entity*.class -> Render*.class mappings
     */
	public static void initEntityRenderers() {
		registerEntityRender(EntityChair.class, new RenderChair());
		registerEntityRender(EntityUmbrella.class, new RenderUmbrella());
		registerEntityRender(EntityPoolFloat.class, new RenderPoolFloat());
		registerEntityRender(EntitySeahorse.class, new RenderSeahorse(new ModelSeahorse(), 0.75F));
		registerEntityRender(EntityDart.class, new RenderDart());
		registerEntityRender(EntityIguana.class, new RenderIguana(new ModelIguana(), 0.75F));
		registerEntityRender(EntityEIH.class, new RenderEIH(new ModelEIH(), 0.75F));
		registerEntityRender(EntityTreeFrog.class, new RenderTreeFrog(new ModelTreeFrog(), 0.5F));
		registerEntityRender(EntityPoisonBlot.class, new RenderPoisonBlot());
	}
	
	/**
	 * Register all tile entity special render mappings
	 */
	public static void initTileEntityRenderers() {
	    registerTileEntityRenderer(TileEntityBambooChest.class, new TileEntityBambooChestRenderer());
	    registerTileEntityRenderer(TileEntityAirCompressor.class, new TileEntityAirCompressorRenderer());
	}
	
	private static void registerTileEntityRenderer(Class<? extends TileEntity> tileEntityClass, 
	        TileEntitySpecialRenderer specialRenderer) {
	    ClientRegistry.bindTileEntitySpecialRenderer(tileEntityClass, specialRenderer);
	}
	
	private static void registerEntityRender(Class<? extends Entity> entityClass, Render render) {
		RenderingRegistry.registerEntityRenderingHandler(entityClass, render);
	}	
}
