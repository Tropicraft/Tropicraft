package net.tropicraft.core.common.entity.placeable;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.tropicraft.core.common.item.TropicraftItems;

public class UmbrellaEntity extends FurnitureEntity {

    public UmbrellaEntity(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn, TropicraftItems.UMBRELLAS);
    }

    @Override
    public ItemStack getPickedResult(HitResult target) {
        return new ItemStack(TropicraftItems.UMBRELLAS.get(DyeColor.byId(getColor().getId())).get());
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        return getBoundingBox().inflate(3.0, 1.0, 3.0);
    }
}
