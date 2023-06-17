package net.tropicraft.core.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public final class TropicraftLogBlock extends RotatedPillarBlock {
    private final Supplier<? extends RotatedPillarBlock> strippedBlock;

    public TropicraftLogBlock(Properties properties, Supplier<? extends RotatedPillarBlock> strippedBlock) {
        super(properties);
        this.strippedBlock = strippedBlock;
    }

    @Nullable
    @Override
    public BlockState getToolModifiedState(BlockState state, Level world, BlockPos pos, Player player, ItemStack stack, ToolAction toolAction) {
        if (toolAction == ToolActions.AXE_STRIP) {
            return this.strippedBlock.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
        }
        return null;
    }
}
