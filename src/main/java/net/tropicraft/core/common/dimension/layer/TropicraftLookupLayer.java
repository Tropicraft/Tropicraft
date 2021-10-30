package net.tropicraft.core.common.dimension.layer;

import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.newbiome.area.AreaFactory;
import net.minecraft.world.level.newbiome.area.LazyArea;
import net.minecraft.world.level.newbiome.layer.Layer;

public class TropicraftLookupLayer extends Layer {
    private final LazyArea area;

    public TropicraftLookupLayer(AreaFactory<LazyArea> areaFactory) {
        super(() -> null);
        this.area = areaFactory.make();
    }

    // the default layer delegated to Forge's registry which isn't populated from dynamic registries
    // we look up our produced biome ids from the same registry that we use here, so this is safe
    @Override
    public Biome get(Registry<Biome> biomes, int x, int z) {
        int id = this.area.get(x, z);

        Biome biome = biomes.byId(id);
        if (biome == null) {
            throw new IllegalStateException("Unknown biome id emitted by layers: " + id);
        }

        return biome;
    }
}
