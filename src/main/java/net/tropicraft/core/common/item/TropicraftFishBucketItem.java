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

import java.util.function.Supplier;

public class TropicraftFishBucketItem<T extends AbstractFishEntity> extends FishBucketItem {
    private final Supplier<? extends EntityType<T>> fishType;

    public TropicraftFishBucketItem(final Supplier<? extends EntityType<T>> type, Fluid fluid, Properties props) {
        super(type, () -> fluid, props);
        this.fishType = type;
    }

    public void onLiquidPlaced(World p_203792_1_, ItemStack p_203792_2_, BlockPos p_203792_3_) {
        if (!p_203792_1_.isRemote) {
            this.placeFish(p_203792_1_, p_203792_2_, p_203792_3_);
        }
    }

    private void placeFish(World p_205357_1_, ItemStack p_205357_2_, BlockPos p_205357_3_) {
        Entity fishy = fishType.get().spawn(p_205357_1_, p_205357_2_, null, p_205357_3_, SpawnReason.BUCKET, true, false);
        if (fishy != null) {
            ((AbstractFishEntity) fishy).setFromBucket(true);
        }

    }
}
