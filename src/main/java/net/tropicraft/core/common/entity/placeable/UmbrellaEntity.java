package net.tropicraft.core.common.entity.placeable;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import net.tropicraft.core.common.item.TropicraftItems;

public class UmbrellaEntity extends FurnitureEntity {

    public UmbrellaEntity(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn, TropicraftItems.UMBRELLAS);
    }
}
