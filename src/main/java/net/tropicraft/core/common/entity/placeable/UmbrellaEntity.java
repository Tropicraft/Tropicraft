package net.tropicraft.core.common.entity.placeable;

import net.minecraft.entity.EntityType;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.tropicraft.core.common.item.TropicraftItems;

public class UmbrellaEntity extends FurnitureEntity {

    public UmbrellaEntity(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn, TropicraftItems.UMBRELLAS);
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return new ItemStack(TropicraftItems.UMBRELLAS.get(DyeColor.byId(getColor().getId())).get());
    }
}
