package net.tropicraft.core.client.entity.render.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.core.client.entity.model.ManOWarModel;
import net.tropicraft.core.client.entity.render.ManOWarRenderer;
import net.tropicraft.core.common.entity.underdasea.ManOWarEntity;

@OnlyIn(Dist.CLIENT)
public class ManOWarGelLayer extends LayerRenderer<ManOWarEntity, ManOWarModel> {
    private final ManOWarRenderer mowRenderer;
    private final ManOWarModel mowModel = new ManOWarModel(0, 20, false);

    public ManOWarGelLayer(ManOWarRenderer manOWarRenderer) {
        super(manOWarRenderer);
        mowRenderer = manOWarRenderer;
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, ManOWarEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!entity.isInvisible()) {
            getEntityModel().copyModelAttributesTo(mowModel);
            mowModel.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTicks);
            mowModel.setRotationAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEntityTranslucent(getEntityTexture(entity)));
            mowModel.render(matrixStackIn, ivertexbuilder, packedLightIn, LivingRenderer.getPackedOverlay(entity, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}