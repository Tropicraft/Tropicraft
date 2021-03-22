package net.tropicraft.core.client.entity.render.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.CowModel;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.core.common.entity.passive.CowktailEntity;

@OnlyIn(Dist.CLIENT)
public class CowktailLayer<T extends CowktailEntity> extends LayerRenderer<T, CowModel<T>>
{
   public CowktailLayer(IEntityRenderer<T, CowModel<T>> rendererIn) {
      super(rendererIn);
   }

   public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
      if (!entitylivingbaseIn.isChild() && !entitylivingbaseIn.isInvisible()) {
         BlockRendererDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
         BlockState blockstate = entitylivingbaseIn.getCowktailType().getRenderState();
         int i = LivingRenderer.getPackedOverlay(entitylivingbaseIn, 0.0F);
         matrixStackIn.push();
         matrixStackIn.translate((double)0.2F, (double)-0.35F, 0.5D);
         matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-48.0F));
         matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
         matrixStackIn.translate(-0.5D, -0.5D, -0.5D);
         blockrendererdispatcher.renderBlock(blockstate, matrixStackIn, bufferIn, packedLightIn, i);
         matrixStackIn.pop();
         matrixStackIn.push();
         matrixStackIn.translate((double)0.2F, (double)-0.35F, 0.5D);
         matrixStackIn.rotate(Vector3f.YP.rotationDegrees(42.0F));
         matrixStackIn.translate((double)0.1F, 0.0D, (double)-0.6F);
         matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-48.0F));
         matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
         matrixStackIn.translate(-0.5D, -0.5D, -0.5D);
         blockrendererdispatcher.renderBlock(blockstate, matrixStackIn, bufferIn, packedLightIn, i);
         matrixStackIn.pop();
         matrixStackIn.push();
         this.getEntityModel().getHead().translateRotate(matrixStackIn);
         matrixStackIn.translate(0.0D, (double)-0.7F, (double)-0.2F);
         matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-78.0F));
         matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
         matrixStackIn.translate(-0.5D, -0.5D, -0.5D);
         blockrendererdispatcher.renderBlock(blockstate, matrixStackIn, bufferIn, packedLightIn, i);
         matrixStackIn.pop();
      }
   }
}
