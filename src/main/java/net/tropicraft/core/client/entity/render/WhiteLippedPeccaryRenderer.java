package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.Constants;
import net.tropicraft.core.client.ClientSetup;
import net.tropicraft.core.client.entity.model.WhiteLippedPeccaryModel;
import net.tropicraft.core.common.entity.passive.WhiteLippedPeccaryEntity;

public class WhiteLippedPeccaryRenderer extends MobRenderer<WhiteLippedPeccaryEntity, WhiteLippedPeccaryModel<WhiteLippedPeccaryEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MODID, "textures/entity/white_lipped_peccary.png");

    public WhiteLippedPeccaryRenderer(EntityRendererProvider.Context context) {
        super(context, new WhiteLippedPeccaryModel<>(context.bakeLayer(ClientSetup.WHITE_LIPPED_PECCARY_LAYER)), 0.5F);
    }

    @Override
    protected void scale(WhiteLippedPeccaryEntity entity, PoseStack matrixStack, float partialTicks) {
        matrixStack.scale(0.9F, 0.9F, 0.9F);
    }

    @Override
    public ResourceLocation getTextureLocation(WhiteLippedPeccaryEntity entity) {
        return TEXTURE;
    }
}
