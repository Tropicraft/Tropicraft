package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.TreeFrogModel;
import net.tropicraft.core.common.entity.neutral.TreeFrogEntity;

public class TreeFrogRenderer extends MobRenderer<TreeFrogEntity, TreeFrogModel> {

    public TreeFrogRenderer(EntityRendererProvider.Context context) {
        super(context, new TreeFrogModel(context.bakeLayer(TropicraftRenderLayers.TREE_FROG_LAYER)), 0.5f);
        shadowStrength = 0.5f;
        shadowRadius = 0.3f;
    }

    @Override
    public ResourceLocation getTextureLocation(TreeFrogEntity entity) {
        return Tropicraft.location("textures/entity/treefrog/treefrog" + entity.getColor() + ".png");
    }
}
