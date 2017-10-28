package net.tropicraft.core.common.enums;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.IStringSerializable;

public enum BlockHardnessValues {
    THATCH(0.2F, 5.0F),
    BAMBOO(0.2F, 5.0F),
    CHUNK(6.0F, 30F),
    PALM(2.0F, 5.0F),
    MAHOGANY(2.0F, 5.0F);
    
    public float hardness;
    public float resistance;
    
    BlockHardnessValues(float hardness, float resistance) {
        this.hardness = hardness;
        this.resistance = resistance;
    }

}
