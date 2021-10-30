package net.tropicraft.core.common.dimension.feature.block_placer;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.blockplacer.BlockPlacer;
import net.minecraft.world.gen.blockplacer.BlockPlacerType;
import net.minecraftforge.common.util.Constants;
import net.tropicraft.core.common.block.huge_plant.HugePlantBlock;

import java.util.Random;

public final class HugePlantBlockPlacer extends BlockPlacer {
    public static final HugePlantBlockPlacer INSTANCE = new HugePlantBlockPlacer();
    public static final Codec<HugePlantBlockPlacer> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    public void place(IWorld world, BlockPos pos, BlockState state, Random random) {
        HugePlantBlock block = (HugePlantBlock) state.getBlock();
        block.placeAt(world, pos, Constants.BlockFlags.BLOCK_UPDATE);
    }

    @Override
    protected BlockPlacerType<?> type() {
        return TropicraftBlockPlacerTypes.HUGE_PLANT.get();
    }
}
