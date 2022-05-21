package net.tropicraft.core.common.dimension.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.tropicraft.core.common.block.huge_plant.HugePlantBlock;

public final class HugePlantFeature extends Feature<SimpleBlockConfiguration> {
    public HugePlantFeature(Codec<SimpleBlockConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<SimpleBlockConfiguration> context) {
        SimpleBlockConfiguration config = context.config();
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        BlockState state = config.toPlace().getState(context.random(), origin);
        if (state.getBlock() instanceof HugePlantBlock hugePlant && state.canSurvive(level, origin)) {
            hugePlant.placeAt(level, origin, Block.UPDATE_CLIENTS);
            return true;
        }

        return false;
    }
}
