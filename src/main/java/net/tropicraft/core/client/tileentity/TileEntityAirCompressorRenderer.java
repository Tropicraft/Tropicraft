package net.tropicraft.core.client.tileentity;

import net.tropicraft.core.client.block.model.ModelAirCompressor;
import net.tropicraft.core.common.block.tileentity.TileEntityAirCompressor;

public class TileEntityAirCompressorRenderer extends TileEntityMachineRenderer<TileEntityAirCompressor> {

    public TileEntityAirCompressorRenderer() {
        super(new ModelAirCompressor());
    }

    @Override
    public void renderIngredients(TileEntityAirCompressor te) {}
}
