package net.tropicraft.core.common.entity.placeable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.tropicraft.core.common.entity.BambooItemFrame;
import net.tropicraft.core.common.entity.TropicraftEntities;

import javax.annotation.Nullable;

public class WallItemEntity extends BambooItemFrame {

    public WallItemEntity(EntityType<? extends WallItemEntity> entityType, Level world) {
        super(entityType, world);
    }

    public WallItemEntity(Level worldIn, BlockPos pos, Direction on) {
        super(TropicraftEntities.WALL_ITEM.get(), worldIn, pos, on);
    }

    @Override
    protected void dropItem(@Nullable Entity entityIn, boolean p_146065_2_) {
        super.dropItem(entityIn, false);
        remove(RemovalReason.DISCARDED);
    }

    @Override
    public void playPlacementSound() {
    }

    @Override
    public ItemStack getPickedResult(HitResult target) {
        return getItem();
    }
}
