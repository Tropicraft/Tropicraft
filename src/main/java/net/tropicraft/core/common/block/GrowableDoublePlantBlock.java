package net.tropicraft.core.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.DoublePlantBlock;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.RegistryObject;
import net.tropicraft.core.common.block.huge_plant.HugePlantBlock;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import net.minecraft.block.AbstractBlock.Properties;

public final class GrowableDoublePlantBlock extends DoublePlantBlock implements IGrowable {
    private final Supplier<RegistryObject<HugePlantBlock>> growInto;
    private Supplier<RegistryObject<? extends IItemProvider>> pickItem;

    public GrowableDoublePlantBlock(Properties properties, Supplier<RegistryObject<HugePlantBlock>> growInto) {
        super(properties);
        this.growInto = growInto;
    }

    public GrowableDoublePlantBlock setPickItem(Supplier<RegistryObject<? extends IItemProvider>> item) {
        this.pickItem = item;
        return this;
    }

    @Override
    public boolean isValidBonemealTarget(IBlockReader world, BlockPos pos, BlockState state, boolean isClient) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        BlockPos lowerPos = state.getValue(HALF) == DoubleBlockHalf.LOWER ? pos : pos.below();

        HugePlantBlock growBlock = this.growInto.get().get();
        BlockState growState = growBlock.defaultBlockState();
        if (growState.canSurvive(world, lowerPos)) {
            growBlock.placeAt(world, lowerPos, Constants.BlockFlags.BLOCK_UPDATE);
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
            return super.getDrops(state, builder);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        if (this.pickItem != null) {
            return new ItemStack(this.pickItem.get().get());
        }
        return super.getPickBlock(state, target, world, pos, player);
    }
}
