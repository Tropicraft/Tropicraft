package net.tropicraft.core.client.entity.render;

import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.tropicraft.Constants;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.render.layer.CowktailLayer;
import net.tropicraft.core.common.entity.passive.CowktailEntity;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class CowktailRenderer extends MobRenderer<CowktailEntity, CowModel<CowktailEntity>> {
    private static final Map<CowktailEntity.Type, ResourceLocation> textures = Util.make(Maps.newHashMap(), (map) -> {
        map.put(CowktailEntity.Type.IRIS, ResourceLocation.fromNamespaceAndPath(Constants.MODID, "textures/entity/cowktail/iris_cowktail.png"));
        map.put(CowktailEntity.Type.ANEMONE, ResourceLocation.fromNamespaceAndPath(Constants.MODID, "textures/entity/cowktail/anemone_cowktail.png"));
    });

    public CowktailRenderer(EntityRendererProvider.Context context) {
        super(context, new CowModel<>(context.bakeLayer(TropicraftRenderLayers.COWKTAIL_LAYER)), 0.7F);
        addLayer(new CowktailLayer<>(this));
    }

    /**
     * Returns the location of an entity's texture.
     */
    @Override
    public ResourceLocation getTextureLocation(CowktailEntity entity) {
        return textures.get(entity.getCowktailType());
    }
}
