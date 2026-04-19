package net.tropicraft.core.client.renderer.special;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.EIHMachineModel;
import net.tropicraft.core.client.renderer.tileentity.AirCompressorBlockEntityRenderer;

public class AirCompressorSpecialRenderer extends MachineSpecialRenderer {
    public AirCompressorSpecialRenderer(EIHMachineModel model, SpriteGetter sprites, SpriteId sprite) {
        super(model, sprites, sprite);
    }

    public record Unbaked() implements MachineSpecialRenderer.Unbaked {
        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(Unbaked::new);

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public SpecialModelRenderer<Void> bake(BakingContext context) {
            EIHMachineModel model = new EIHMachineModel(context.entityModelSet().bakeLayer(TropicraftRenderLayers.AIRCOMPRESSOR_LAYER));
            return new AirCompressorSpecialRenderer(model, context.sprites(), AirCompressorBlockEntityRenderer.SPRITE);
        }
    }
}
