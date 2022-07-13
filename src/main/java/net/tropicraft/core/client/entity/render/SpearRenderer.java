package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.tropicraft.core.common.entity.projectile.SpearEntity;

public class SpearRenderer<T extends SpearEntity> extends EntityRenderer<T> {
	private ItemRenderer itemRenderer;

	public SpearRenderer(EntityRendererProvider.Context p_174008_) {
		super(p_174008_);
		this.itemRenderer = Minecraft.getInstance().getItemRenderer();
	}

	@Override
	public void render(T entityIn, float entityYaw, float partialTicks, PoseStack stackIn, MultiBufferSource bufferIn, int packedLightIn) {
		stackIn.pushPose();

		stackIn.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(partialTicks, entityIn.yRotO, entityIn.getYRot())));
		stackIn.mulPose(Vector3f.XP.rotationDegrees(-Mth.lerp(partialTicks, entityIn.xRotO, entityIn.getXRot())));
		stackIn.mulPose(Vector3f.YP.rotationDegrees(-45.0F));
		stackIn.mulPose(Vector3f.XP.rotationDegrees(90.0F));
		
		stackIn.scale(2.5F, 2.5F, 2.5F);

		BakedModel bakedmodel = this.itemRenderer.getModel(entityIn.getPickupItem(), entityIn.level, (LivingEntity) null, entityIn.getId());

		this.itemRenderer.render(entityIn.getPickupItem(), ItemTransforms.TransformType.GROUND, false, stackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, bakedmodel);
		stackIn.popPose();
		super.render(entityIn, entityYaw, partialTicks, stackIn, bufferIn, packedLightIn);
	}

	@Override
	public ResourceLocation getTextureLocation(T p_115034_) {
		return TextureAtlas.LOCATION_BLOCKS;
	}
}