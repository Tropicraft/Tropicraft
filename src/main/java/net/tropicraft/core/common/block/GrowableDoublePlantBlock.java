package net.tropicraft.core.common.block;

import com.mojang.serialization.MapCodec;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.HitResult;
import net.tropicraft.core.common.block.huge_plant.HugePlantBlock;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public final class GrowableDoublePlantBlock extends DoublePlantBlock implements BonemealableBlock {
    private final Supplier<BlockEntry<HugePlantBlock>> growInto;
    @Nullable
    private Supplier<RegistryEntry<? extends ItemLike, ? extends ItemLike>> pickItem;

    public GrowableDoublePlantBlock(Properties properties, Supplier<BlockEntry<HugePlantBlock>> growInto) {
        super(properties);
        this.growInto = growInto;
    }

    public GrowableDoublePlantBlock setPickItem(Supplier<RegistryEntry<? extends ItemLike, ? extends ItemLike>> item) {
        pickItem = item;
        return this;
    }

    @Override
    public MapCodec<? extends DoublePlantBlock> codec() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
        BlockPos lowerPos = state.getValue(HALF) == DoubleBlockHalf.LOWER ? pos : pos.below();

        HugePlantBlock growBlock = growInto.get().get();
        BlockState growState = growBlock.defaultBlockState();
        if (growState.canSurvive(world, lowerPos)) {
            growBlock.placeAt(world, lowerPos, Block.UPDATE_CLIENTS);
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
            return super.getDrops(state, params);
        } else {
            return List.of();
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        if (pickItem != null) {
            return new ItemStack(pickItem.get().get());
        }
        return super.getCloneItemStack(state, target, level, pos, player);
    }
}
