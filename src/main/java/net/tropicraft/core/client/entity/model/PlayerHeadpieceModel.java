package net.tropicraft.core.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.LivingEntity;
import com.mojang.math.Vector3f;
import net.tropicraft.core.client.entity.TropicraftSpecialRenderHelper;

public class PlayerHeadpieceModel extends HumanoidModel<LivingEntity> {
	private int textureIndex;
	private final double xOffset, yOffset;
	protected TropicraftSpecialRenderHelper renderer;
	private static float scale;
	final ModelPart headpiece;

	public PlayerHeadpieceModel(ModelPart modelPart, final int textureIndex) {
		this(modelPart,0, textureIndex, 0, 0);
	}

	public PlayerHeadpieceModel(ModelPart modelPart, final int textureIndex, final double xOffset, final double yOffset) {
		this(modelPart,0, textureIndex, xOffset, yOffset);
	}

	public PlayerHeadpieceModel(ModelPart modelPart, float scale, final int textureIndex, final double xOffset, final double yOffset) {
		super(modelPart, RenderType::entityCutoutNoCull);
		this.textureIndex = textureIndex;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.scale = scale;
		renderer = new TropicraftSpecialRenderHelper();

		headpiece = modelPart.getChild(PartNames.HAT);
	}

	/*

	public PlayerHeadpieceModel(ModelPart modelPart) {
		super(modelPart, RenderLayer::getEntityCutoutNoCull);
	}

	 */

	public static LayerDefinition create() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();

		modelPartData.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
		//modelPartData.addChild("hat", ModelPartBuilder.create(), ModelTransform.NONE);
		modelPartData.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO);
		modelPartData.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.ZERO);
		modelPartData.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.ZERO);
		modelPartData.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.ZERO);
		modelPartData.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.ZERO);

		CubeDeformation dilation_hat = new CubeDeformation(scale + 0.5f);
		PartDefinition hat = modelPartData.addOrReplaceChild(PartNames.HAT,
				CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, dilation_hat),
				PartPose.ZERO);

		return LayerDefinition.create(modelData, 64, 32);
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
		stack.mulPose(Vector3f.YP.rotation(head.yRot));
		stack.mulPose(Vector3f.XP.rotation(head.xRot));

		// Flip mask to face away from the player
		stack.mulPose(Vector3f.YP.rotationDegrees(180));

		// put it in the middle in front of the face
		stack.translate(0.0F - xOffset, 0.112f + 0.0625f - yOffset, 0.2501F);

   		// renderMask handles the rendering of the mask model, but it doesn't set the texture.
		// Setting the texture is handled in the item class.
		renderer.renderMask(stack, bufferIn, this.textureIndex, packedLightIn, packedOverlayIn);
		
		stack.popPose();
	}
	
	
}
