package net.tropicraft.core.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;

import javax.annotation.Nullable;

public class TropicraftFishBucketItem<T extends AbstractFish> extends MobBucketItem {
    private final EntityType<T> fishType;

    public TropicraftFishBucketItem(EntityType<T> type, Fluid fluid, Properties props) {
        super(type, fluid, SoundEvents.BUCKET_FILL_FISH, props);
        fishType = type;
    }

    @Override
    public void checkExtraContent(@Nullable Player player, Level level, ItemStack stack, BlockPos pos) {
        if (!level.isClientSide) {
            placeFish((ServerLevel) level, stack, pos);
        }
    }

    private void placeFish(ServerLevel world, ItemStack stack, BlockPos pos) {
        T fishy = fishType.spawn(world, stack, null, pos, MobSpawnType.BUCKET, true, false);
        if (fishy != null) {
            fishy.setFromBucket(true);
        }
    }
}
