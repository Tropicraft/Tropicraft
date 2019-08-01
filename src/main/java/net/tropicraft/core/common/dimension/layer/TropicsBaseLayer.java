package net.tropicraft.core.common.dimension.layer;

import net.minecraft.world.gen.area.IAreaFactory;
import net.minecraft.world.gen.area.LazyArea;
import net.minecraft.world.gen.layer.Layer;

public class TropicsBaseLayer extends Layer {

    public TropicsBaseLayer(IAreaFactory<LazyArea> areaFactory) {
        super(areaFactory);
    }
}
