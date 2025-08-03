package net.tropicraft.core.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SeagrassBlock;
import net.minecraft.world.level.block.TallSeagrassBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class CustomTallSeagrassBlock extends TallSeagrassBlock {
    private final Supplier<? extends SeagrassBlock> drop;

    public CustomTallSeagrassBlock(Properties p, Supplier<? extends SeagrassBlock> drop) {
        super(p);
        this.drop = drop;
    }

    @Override
    public MapCodec<TallSeagrassBlock> codec() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData, Player player) {
        return new ItemStack(drop.get());
    }
}
