package net.tropicraft.core.client.entity.render;

import com.google.common.collect.Maps;
import java.util.Map;

import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.model.CowModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.Util;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.Constants;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.render.layer.CowktailLayer;
import net.tropicraft.core.common.entity.passive.CowktailEntity;

@OnlyIn(Dist.CLIENT)
public class CowktailRenderer extends MobRenderer<CowktailEntity, CowModel<CowktailEntity>>
{
	private static final Map<CowktailEntity.Type, ResourceLocation> textures = Util.make(Maps.newHashMap(), (map) -> {
		map.put(CowktailEntity.Type.IRIS, new ResourceLocation(Constants.MODID, "textures/entity/cowktail/iris_cowktail.png"));
		map.put(CowktailEntity.Type.ANEMONE, new ResourceLocation(Constants.MODID, "textures/entity/cowktail/anemone_cowktail.png"));
	});

	public CowktailRenderer(final EntityRendererProvider.Context context) {
		super(context, new CowModel<>(context.bakeLayer(TropicraftRenderLayers.COWKTAIL_LAYER)), 0.7F);
		this.addLayer(new CowktailLayer<>(this));
	}

	/**
	 * Returns the location of an entity's texture.
	 */
	public ResourceLocation getTextureLocation(CowktailEntity entity) {
		return textures.get(entity.getCowktailType());
	}
}