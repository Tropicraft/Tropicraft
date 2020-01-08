package net.tropicraft.core.common.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.fish.AbstractFishEntity;
import net.minecraft.entity.passive.fish.TropicalFishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.FishBucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;
import net.tropicraft.core.common.entity.TropicraftEntities;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class TropicraftFishBucketItem<T extends AbstractFishEntity> extends FishBucketItem {
    private final Supplier<? extends EntityType<T>> fishType;

    public TropicraftFishBucketItem(final Supplier<? extends EntityType<T>> type, Fluid fluid, Properties props) {
        super(type.get(), fluid, props);
        this.fishType = type;
    }

    public void onLiquidPlaced(World p_203792_1_, ItemStack p_203792_2_, BlockPos p_203792_3_) {
        if (!p_203792_1_.isRemote) {
            this.placeFish(p_203792_1_, p_203792_2_, p_203792_3_);
        }
    }

    private void placeFish(World p_205357_1_, ItemStack p_205357_2_, BlockPos p_205357_3_) {
        Entity lvt_4_1_ = this.fishType.get().spawn(p_205357_1_, p_205357_2_, (PlayerEntity)null, p_205357_3_, SpawnReason.BUCKET, true, false);
        if (lvt_4_1_ != null) {
            ((AbstractFishEntity)lvt_4_1_).setFromBucket(true);
        }

    }
}
