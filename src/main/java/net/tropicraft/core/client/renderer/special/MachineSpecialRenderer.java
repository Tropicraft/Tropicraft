package net.tropicraft.core.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.util.CommonColors;
import net.minecraft.util.Unit;
import org.joml.Vector3fc;

import java.util.function.Consumer;

public abstract class MachineSpecialRenderer implements NoDataSpecialModelRenderer {
    private final Model<Unit> model;
    private final SpriteGetter sprites;
    private final SpriteId sprite;

    public MachineSpecialRenderer(Model<Unit> model, SpriteGetter sprites, SpriteId sprite) {
        this.model = model;
        this.sprites = sprites;
        this.sprite = sprite;
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
        poseStack.pushPose();
        transform(poseStack);
        submitNodeCollector.submitModel(model, Unit.INSTANCE, poseStack, model.renderType(sprite.atlasLocation()), lightCoords, overlayCoords, CommonColors.WHITE, sprites.get(sprite), EntityRenderState.NO_OUTLINE, null);
        poseStack.popPose();
    }

    @Override
    public void getExtents(Consumer<Vector3fc> output) {
        PoseStack poseStack = new PoseStack();
        transform(poseStack);
        model.root().getExtentsForGui(poseStack, output);
    }

    private static void transform(PoseStack poseStack) {
        poseStack.translate(0.5f, 1.5f, 0.5f);
        poseStack.mulPose(Axis.XP.rotationDegrees(180));
        poseStack.mulPose(Axis.YP.rotationDegrees(-90));
    }
}
