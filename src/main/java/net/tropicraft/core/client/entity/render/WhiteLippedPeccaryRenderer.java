package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.Constants;
import net.tropicraft.core.client.entity.model.WhiteLippedPeccaryModel;
import net.tropicraft.core.common.entity.passive.WhiteLippedPeccaryEntity;

public class WhiteLippedPeccaryRenderer extends MobRenderer<WhiteLippedPeccaryEntity, WhiteLippedPeccaryModel<WhiteLippedPeccaryEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MODID, "textures/entity/white_lipped_peccary.png");

    public WhiteLippedPeccaryRenderer(EntityRendererManager manager) {
        super(manager, new WhiteLippedPeccaryModel<>(), 0.5F);
    }

    @Override
    protected void scale(WhiteLippedPeccaryEntity entity, MatrixStack matrixStack, float partialTicks) {
        matrixStack.scale(0.9F, 0.9F, 0.9F);
    }

    @Override
    public ResourceLocation getTextureLocation(WhiteLippedPeccaryEntity entity) {
        return TEXTURE;
    }
}
