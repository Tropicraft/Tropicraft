package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.TreeFrogModel;
import net.tropicraft.core.client.entity.render.state.TreeFrogRenderState;
import net.tropicraft.core.common.entity.neutral.TreeFrogEntity;

public class TreeFrogRenderer extends MobRenderer<TreeFrogEntity, TreeFrogRenderState, TreeFrogModel> {

    public TreeFrogRenderer(EntityRendererProvider.Context context) {
        super(context, new TreeFrogModel(context.bakeLayer(TropicraftRenderLayers.TREE_FROG_LAYER)), 0.5f);
        shadowStrength = 0.5f;
        shadowRadius = 0.3f;
    }

    @Override
    public TreeFrogRenderState createRenderState() {
        return new TreeFrogRenderState();
    }

    @Override
    public void extractRenderState(TreeFrogEntity entity, TreeFrogRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.type = entity.getFrogType();
    }

    @Override
    public Identifier getTextureLocation(TreeFrogRenderState state) {
        return Tropicraft.id("textures/entity/treefrog/treefrog" + state.type.getColor() + ".png");
    }
}
