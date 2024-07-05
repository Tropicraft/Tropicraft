package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.BeachFloatModel;
import net.tropicraft.core.common.entity.placeable.BeachFloatEntity;

public class BeachFloatRenderer extends FurnitureRenderer<BeachFloatEntity> {

    public BeachFloatRenderer(EntityRendererProvider.Context context) {
        super(context, "beach_float", new BeachFloatModel(context.bakeLayer(TropicraftRenderLayers.BEACH_FLOAT_LAYER)));
        shadowRadius = 0.5f;
    }

    @Override
    protected double getYOffset() {
        return super.getYOffset() + 1.2;
    }

    @Override
    protected void setupTransforms(PoseStack stack) {
        stack.mulPose(Axis.YP.rotationDegrees(-180));
    }

    @Override
    protected Axis getRockingAxis() {
        return Axis.XP;
    }

    @Override
    protected float getRockAmount() {
        return 25;
    }
}
