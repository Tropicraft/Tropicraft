package net.tropicraft.core.client.renderer.special;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.util.Unit;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.EIHMachineModel;
import net.tropicraft.core.client.renderer.tileentity.DrinkMixerBlockEntityRenderer;

public class DrinkMixerSpecialRenderer extends MachineSpecialRenderer {
    public DrinkMixerSpecialRenderer(Model<Unit> model, SpriteGetter sprites, SpriteId sprite) {
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
            EIHMachineModel model = new EIHMachineModel(context.entityModelSet().bakeLayer(TropicraftRenderLayers.EIHMACHINE_LAYER));
            return new DrinkMixerSpecialRenderer(model, context.sprites(), DrinkMixerBlockEntityRenderer.SPRITE);
        }
    }
}
