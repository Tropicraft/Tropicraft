package net.tropicraft.core.client.renderer.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.phys.Vec3;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.EIHMachineModel;
import net.tropicraft.core.common.block.tileentity.AirCompressorBlockEntity;
import net.tropicraft.core.common.item.scuba.ScubaArmorItem;
import net.tropicraft.core.common.item.scuba.ScubaType;
import org.jspecify.annotations.Nullable;

public class AirCompressorBlockEntityRenderer extends MachineBlockEntityRenderer<AirCompressorBlockEntity, AirCompressorBlockEntityRenderer.RenderState> {
    public static final SpriteId SPRITE = new SpriteId(TextureAtlas.LOCATION_BLOCKS, Tropicraft.id("block/te/drink_mixer"));

    private final Model.Simple tankModel;

    public AirCompressorBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(new EIHMachineModel(context.bakeLayer(TropicraftRenderLayers.AIRCOMPRESSOR_LAYER)), context.sprites());
        tankModel = new Model.Simple(context.bakeLayer(TropicraftRenderLayers.TANK_SCUBA_LAYER), RenderTypes::entityCutout);
    }

    @Override
    protected SpriteId getSprite() {
        return SPRITE;
    }

    @Override
    public RenderState createRenderState() {
        return new RenderState();
    }

    @Override
    public void extractRenderState(AirCompressorBlockEntity blockEntity, RenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        if (blockEntity.isActive()) {
            state.breatheProgress = blockEntity.getBreatheProgress(partialTicks);
            ScubaArmorItem tank = blockEntity.getTank();
            if (tank != null) {
                state.scubaType = tank.getScubaType();
            }
        }
    }

    @Override
    protected void animationTransform(RenderState state, PoseStack poseStack) {
        float progress = state.breatheProgress;
        float sin = 1 + Mth.cos(progress);
        float sc = 1 + 0.05f * sin;
        poseStack.translate(0, 1.5f, 0);
        poseStack.scale(sc, sc, sc);
        poseStack.translate(0, -1.5f, 0);
        if (progress < Math.PI) {
            float shake = Mth.sin(state.breatheProgress * 10) * 8.0f;
            poseStack.mulPose(Axis.YP.rotationDegrees(shake));
        }
    }

    @Override
    protected void submitIngredients(RenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector) {
        if (state.active) {
            poseStack.pushPose();
            poseStack.translate(-0.5f, 0.5f, 0);
            poseStack.mulPose(Axis.YP.rotationDegrees(90));
            Identifier texture = state.scubaType.textureLocation();
            submitNodeCollector.submitModel(tankModel, Unit.INSTANCE, poseStack, texture, state.lightCoords, OverlayTexture.NO_OVERLAY, EntityRenderState.NO_OUTLINE, null);
            poseStack.popPose();
        }
    }

    public static class RenderState extends MachineBlockEntityRenderer.RenderState {
        public float breatheProgress;
        public ScubaType scubaType = ScubaType.PINK;
    }
}
