package net.tropicraft.core.common.block;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.tropicraft.Tropicraft;

public class TropicraftWoodTypes {
    public static final WoodType MAHOGANY = register("mahogany");
    public static final WoodType PALM = register("palm");
    public static final WoodType BAMBOO = register("bamboo");
    public static final WoodType THATCH = register("thatch");
    public static final WoodType MANGROVE = register("mangrove");

    private static WoodType register(String name) {
        return WoodType.register(new WoodType(Tropicraft.location(name).toString(), BlockSetType.OAK));
    }
}
