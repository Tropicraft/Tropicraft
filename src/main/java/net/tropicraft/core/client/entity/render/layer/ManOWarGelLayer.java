package net.tropicraft.core.client.entity.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.tropicraft.core.client.entity.model.ManOWarModel;
import net.tropicraft.core.client.entity.render.ManOWarRenderer;
import net.tropicraft.core.common.entity.underdasea.ManOWarEntity;

@OnlyIn(Dist.CLIENT)
public class ManOWarGelLayer extends RenderLayer<ManOWarEntity, ManOWarModel> {
    private final ManOWarModel model;

    public ManOWarGelLayer(ManOWarRenderer renderer, ManOWarModel model) {
        super(renderer);
        this.model = model;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, ManOWarEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!entity.isInvisible()) {
            getParentModel().copyPropertiesTo(model);
            model.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
            model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            VertexConsumer buffer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(entity)));
            model.renderToBuffer(poseStack, buffer, packedLight, LivingEntityRenderer.getOverlayCoords(entity, 0.0f));
        }
    }
}
