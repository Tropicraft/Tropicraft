package net.tropicraft.core.common.block;

import net.minecraft.world.level.block.state.properties.WoodType;
import net.tropicraft.Constants;

public class TropicraftWoodTypes extends WoodType {

    public static final WoodType MAHOGANY = register(new TropicraftWoodTypes("mahogany"));
    public static final WoodType PALM = register(new TropicraftWoodTypes("palm"));
    public static final WoodType BAMBOO = register(new TropicraftWoodTypes("bamboo"));
    public static final WoodType THATCH = register(new TropicraftWoodTypes("thatch"));

    public static final WoodType MANGROVE_RED = register(new TropicraftWoodTypes("mangrove_red"));
    public static final WoodType MANGROVE_LIGHT = register(new TropicraftWoodTypes("mangrove_light"));
    public static final WoodType MANGROVE_BLACK = register(new TropicraftWoodTypes("mangrove_black"));

    protected TropicraftWoodTypes(String pName) {
        super(Constants.id(pName).toString());
    }
}
