package net.tropicraft.core.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.LivingEntity;
import net.tropicraft.core.client.entity.TropicraftSpecialRenderHelper;

public class PlayerHeadpieceModel extends HumanoidModel<LivingEntity> {
	private final int textureIndex;
	private final double xOffset, yOffset;
	private final TropicraftSpecialRenderHelper renderer;

	public PlayerHeadpieceModel(ModelPart modelPart, final int textureIndex, final double xOffset, final double yOffset) {
		super(modelPart, RenderType::entityCutoutNoCull);
		this.textureIndex = textureIndex;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		renderer = new TropicraftSpecialRenderHelper();
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
		//root.addChild("hat", ModelPartBuilder.create(), ModelTransform.NONE);
		root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO);
		root.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.ZERO);
		root.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.ZERO);
		root.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.ZERO);
		root.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.ZERO);

		CubeDeformation dilation_hat = new CubeDeformation(0.5f);
		root.addOrReplaceChild(PartNames.HAT,
				CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, dilation_hat),
				PartPose.ZERO);

		return LayerDefinition.create(mesh, 64, 32);
	}

	public static PlayerHeadpieceModel createModel(ModelLayerLocation entityModelLayer, EntityModelSet entityModelLoader, final int textureIndex, final double xOffset, final double yOffset) {
		return new PlayerHeadpieceModel(entityModelLoader == null ?
				create().bakeRoot() :
				entityModelLoader.bakeLayer(entityModelLayer), textureIndex, xOffset, yOffset);
	}

	@Override
	public void renderToBuffer(PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		stack.pushPose();

		if (crouching) {
			stack.translate(0, 0.25f, 0);
		}

		// Set head rotation to mask
		stack.mulPose(Axis.YP.rotation(head.yRot));
		stack.mulPose(Axis.XP.rotation(head.xRot));

		// Flip mask to face away from the player
		stack.mulPose(Axis.YP.rotationDegrees(180));

		// put it in the middle in front of the face
		stack.translate(0.0F - xOffset, 0.112f + 0.0625f - yOffset, 0.2501F);

		// renderMask handles the rendering of the mask model, but it doesn't set the texture.
		// Setting the texture is handled in the item class.
		renderer.renderMask(stack, bufferIn, this.textureIndex, packedLightIn, packedOverlayIn);

		stack.popPose();
	}
}