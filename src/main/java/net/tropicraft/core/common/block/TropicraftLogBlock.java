package net.tropicraft.core.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public final class TropicraftLogBlock extends RotatedPillarBlock {
    private final Supplier<? extends RotatedPillarBlock> strippedBlock;

    public TropicraftLogBlock(Properties properties, Supplier<? extends RotatedPillarBlock> strippedBlock) {
        super(properties);
        this.strippedBlock = strippedBlock;
    }

    @Override
    public MapCodec<TropicraftLogBlock> codec() {
        throw new UnsupportedOperationException();
    }

    @Nullable
    @Override
    public BlockState getToolModifiedState(final BlockState state, final UseOnContext context, final ItemAbility itemAbility, final boolean simulate) {
        if (itemAbility == ItemAbilities.AXE_STRIP) {
            return this.strippedBlock.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
        }
        return null;
    }
}
