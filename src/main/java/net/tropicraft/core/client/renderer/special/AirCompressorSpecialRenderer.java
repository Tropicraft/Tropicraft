package net.tropicraft.core.client.renderer.special;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.resources.model.Material;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.EIHMachineModel;
import net.tropicraft.core.client.renderer.tileentity.AirCompressorBlockEntityRenderer;

public class AirCompressorSpecialRenderer extends MachineSpecialRenderer {
    public AirCompressorSpecialRenderer(EIHMachineModel model, Material material) {
        super(model, material);
    }

    public record Unbaked() implements SpecialModelRenderer.Unbaked {
        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(Unbaked::new);

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public SpecialModelRenderer<?> bake(EntityModelSet modelSet) {
            EIHMachineModel model = new EIHMachineModel(modelSet.bakeLayer(TropicraftRenderLayers.AIRCOMPRESSOR_LAYER));
            return new AirCompressorSpecialRenderer(model, AirCompressorBlockEntityRenderer.MATERIAL);
        }
    }
}
