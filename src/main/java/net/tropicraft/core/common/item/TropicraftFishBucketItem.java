package net.tropicraft.core.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class TropicraftFishBucketItem<T extends AbstractFish> extends MobBucketItem {
    private final Supplier<? extends EntityType<T>> fishType;

    public TropicraftFishBucketItem(final Supplier<? extends EntityType<T>> type, Fluid fluid, Properties props) {
        super(type, () -> fluid, () -> SoundEvents.BUCKET_FILL_FISH, props);
        this.fishType = type;
    }

    @Override
    public void checkExtraContent(@Nullable Player player, Level level, ItemStack stack, BlockPos pos) {
        if (!level.isClientSide) {
            this.placeFish((ServerLevel) level, stack, pos);
        }
    }

    private void placeFish(ServerLevel world, ItemStack stack, BlockPos pos) {
        Entity fishy = fishType.get().spawn(world, stack, null, pos, MobSpawnType.BUCKET, true, false);
        if (fishy != null) {
            ((AbstractFish) fishy).setFromBucket(true);
        }

    }
}
