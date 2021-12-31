package net.tropicraft.core.client.entity.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.core.client.entity.model.ManOWarModel;
import net.tropicraft.core.client.entity.render.ManOWarRenderer;
import net.tropicraft.core.common.entity.underdasea.ManOWarEntity;

@OnlyIn(Dist.CLIENT)
public class ManOWarGelLayer extends RenderLayer<ManOWarEntity, ManOWarModel> {
    private final ManOWarRenderer mowRenderer;
    private final ManOWarModel mowModel;// = new ManOWarModel(0, 20, false);

    public ManOWarGelLayer(ManOWarRenderer manOWarRenderer, final ManOWarModel mowModel) {
        super(manOWarRenderer);
        mowRenderer = manOWarRenderer;
        this.mowModel = mowModel; // TODO 1.17 use bigger size
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, ManOWarEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!entity.isInvisible()) {
            getParentModel().copyPropertiesTo(mowModel);
            mowModel.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
            mowModel.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entityTranslucent(getTextureLocation(entity)));
            mowModel.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, LivingEntityRenderer.getOverlayCoords(entity, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}