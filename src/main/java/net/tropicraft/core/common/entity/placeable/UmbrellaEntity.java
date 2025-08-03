package net.tropicraft.core.common.entity.placeable;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.tropicraft.core.common.item.TropicraftItems;

public class UmbrellaEntity extends FurnitureEntity {

    public UmbrellaEntity(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn, TropicraftItems.UMBRELLAS);
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(TropicraftItems.UMBRELLAS.get(getColor()).get());
    }
}
