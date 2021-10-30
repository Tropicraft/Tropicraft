package net.tropicraft.core.common.block;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.function.Supplier;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public final class TropicraftLogBlock extends RotatedPillarBlock {
    private final Supplier<RotatedPillarBlock> strippedBlock;

    public TropicraftLogBlock(Properties properties, Supplier<RotatedPillarBlock> strippedBlock) {
        super(properties);
        this.strippedBlock = strippedBlock;
    }

    @Nullable
    @Override
    public BlockState getToolModifiedState(BlockState state, Level world, BlockPos pos, Player player, ItemStack stack, ToolType toolType) {
        if (toolType == ToolType.AXE) {
            return this.strippedBlock.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
        }
        return null;
    }
}
