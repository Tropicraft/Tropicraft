package net.tropicraft.core.common.entity.placeable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.tropicraft.core.common.entity.BambooItemFrame;
import net.tropicraft.core.common.entity.TropicraftEntities;

import org.jspecify.annotations.Nullable;

public class WallItemEntity extends BambooItemFrame {

    public WallItemEntity(EntityType<? extends WallItemEntity> entityType, Level world) {
        super(entityType, world);
    }

    public WallItemEntity(Level worldIn, BlockPos pos, Direction on) {
        super(TropicraftEntities.WALL_ITEM.get(), worldIn, pos, on);
    }

    @Override
    public void dropItem(ServerLevel level, @Nullable Entity entity) {
        super.dropItem(level, entity);
        remove(RemovalReason.DISCARDED);
    }

    @Override
    public void playPlacementSound() {
    }

    @Override
    public ItemStack getPickResult() {
        return getItem();
    }
}
