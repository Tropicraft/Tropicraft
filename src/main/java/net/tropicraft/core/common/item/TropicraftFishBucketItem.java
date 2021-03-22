package net.tropicraft.core.common.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.fish.AbstractFishEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.FishBucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.function.Supplier;

import net.minecraft.item.Item.Properties;

public class TropicraftFishBucketItem<T extends AbstractFishEntity> extends FishBucketItem {
    private final Supplier<? extends EntityType<T>> fishType;

    public TropicraftFishBucketItem(final Supplier<? extends EntityType<T>> type, Fluid fluid, Properties props) {
        super(type, () -> fluid, props);
        this.fishType = type;
    }

    @Override
    public void onLiquidPlaced(World world, ItemStack stack, BlockPos pos) {
        if (!world.isRemote) {
            this.placeFish((ServerWorld) world, stack, pos);
        }
    }

    private void placeFish(ServerWorld world, ItemStack stack, BlockPos pos) {
        Entity fishy = fishType.get().spawn(world, stack, null, pos, SpawnReason.BUCKET, true, false);
        if (fishy != null) {
            ((AbstractFishEntity) fishy).setFromBucket(true);
        }

    }
}
