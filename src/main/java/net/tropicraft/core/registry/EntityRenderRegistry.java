package net.tropicraft.core.registry;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.tropicraft.core.client.entity.render.RenderEIH;
import net.tropicraft.core.client.entity.render.RenderIguana;
import net.tropicraft.core.client.entity.render.RenderTreeFrog;
import net.tropicraft.core.client.entity.render.RenderTropiCreeper;
import net.tropicraft.core.client.entity.render.RenderTropiSkeleton;
import net.tropicraft.core.common.entity.hostile.EntityEIH;
import net.tropicraft.core.common.entity.hostile.EntityTreeFrogBase;
import net.tropicraft.core.common.entity.hostile.EntityTropiCreeper;
import net.tropicraft.core.common.entity.hostile.EntityTropiSkeleton;
import net.tropicraft.core.common.entity.passive.EntityIguana;

public class EntityRenderRegistry {

	public static void init() {
		registerEntityRender(EntityEIH.class, new RenderEIH());
		registerEntityRender(EntityTropiCreeper.class, new RenderTropiCreeper());
		registerEntityRender(EntityIguana.class, new RenderIguana());
		registerEntityRender(EntityTreeFrogBase.class, new RenderTreeFrog());
		registerEntityRender(EntityTropiSkeleton.class, new RenderTropiSkeleton());
	}

    private static void registerEntityRender(Class<? extends Entity> entityClass, Render render) {
        RenderingRegistry.registerEntityRenderingHandler(entityClass, render);
    }	
	
}
