package net.tropicraft.core.common.dimension.layer;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.area.IAreaFactory;
import net.minecraft.world.gen.area.LazyArea;
import net.minecraft.world.gen.layer.Layer;

public class TropicraftLookupLayer extends Layer {
    private final LazyArea area;

    public TropicraftLookupLayer(IAreaFactory<LazyArea> areaFactory) {
        super(() -> null);
        this.area = areaFactory.make();
    }

    // the default layer delegated to Forge's registry which isn't populated from dynamic registries
    // we look up our produced biome ids from the same registry that we use here, so this is safe
    @Override
    public Biome func_242936_a(Registry<Biome> biomes, int x, int z) {
        int id = this.area.getValue(x, z);

        Biome biome = biomes.getByValue(id);
        if (biome == null) {
            throw new IllegalStateException("Unknown biome id emitted by layers: " + id);
        }

        return biome;
    }
}
