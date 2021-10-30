package net.tropicraft.core.client.entity.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.model.CowModel;
import com.mojang.math.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.core.common.entity.passive.CowktailEntity;

@OnlyIn(Dist.CLIENT)
public class CowktailLayer<T extends CowktailEntity> extends RenderLayer<T, CowModel<T>>
{
   public CowktailLayer(RenderLayerParent<T, CowModel<T>> rendererIn) {
      super(rendererIn);
   }

   public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
      if (!entitylivingbaseIn.isBaby() && !entitylivingbaseIn.isInvisible()) {
         BlockRenderDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRenderer();
         BlockState blockstate = entitylivingbaseIn.getCowktailType().getRenderState();
         int i = LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F);
         matrixStackIn.pushPose();
         matrixStackIn.translate((double)0.2F, (double)-0.35F, 0.5D);
         matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(-48.0F));
         matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
         matrixStackIn.translate(-0.5D, -0.5D, -0.5D);
         blockrendererdispatcher.renderSingleBlock(blockstate, matrixStackIn, bufferIn, packedLightIn, i);
         matrixStackIn.popPose();
         matrixStackIn.pushPose();
         matrixStackIn.translate((double)0.2F, (double)-0.35F, 0.5D);
         matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(42.0F));
         matrixStackIn.translate((double)0.1F, 0.0D, (double)-0.6F);
         matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(-48.0F));
         matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
         matrixStackIn.translate(-0.5D, -0.5D, -0.5D);
         blockrendererdispatcher.renderSingleBlock(blockstate, matrixStackIn, bufferIn, packedLightIn, i);
         matrixStackIn.popPose();
         matrixStackIn.pushPose();
         this.getParentModel().getHead().translateAndRotate(matrixStackIn);
         matrixStackIn.translate(0.0D, (double)-0.7F, (double)-0.2F);
         matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(-78.0F));
         matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
         matrixStackIn.translate(-0.5D, -0.5D, -0.5D);
         blockrendererdispatcher.renderSingleBlock(blockstate, matrixStackIn, bufferIn, packedLightIn, i);
         matrixStackIn.popPose();
      }
   }
}
