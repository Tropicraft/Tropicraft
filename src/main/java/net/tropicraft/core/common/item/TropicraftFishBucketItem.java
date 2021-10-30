package net.tropicraft.core.common.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.item.FishBucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

import java.util.function.Supplier;

import net.minecraft.world.item.Item.Properties;

public class TropicraftFishBucketItem<T extends AbstractFish> extends FishBucketItem {
    private final Supplier<? extends EntityType<T>> fishType;

    public TropicraftFishBucketItem(final Supplier<? extends EntityType<T>> type, Fluid fluid, Properties props) {
        super(type, () -> fluid, props);
        this.fishType = type;
    }

    @Override
    public void checkExtraContent(Level world, ItemStack stack, BlockPos pos) {
        if (!world.isClientSide) {
            this.placeFish((ServerLevel) world, stack, pos);
        }
    }

    private void placeFish(ServerLevel world, ItemStack stack, BlockPos pos) {
        Entity fishy = fishType.get().spawn(world, stack, null, pos, MobSpawnType.BUCKET, true, false);
        if (fishy != null) {
            ((AbstractFish) fishy).setFromBucket(true);
        }

    }
}
