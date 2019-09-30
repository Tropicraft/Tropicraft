package net.tropicraft.core.common.dimension.carver;

import java.util.function.Function;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.Dynamic;

import net.minecraft.block.Block;
import net.minecraft.world.gen.carver.UnderwaterCanyonWorldCarver;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import net.tropicraft.core.common.block.TropicraftBlocks;

public class TropicsUnderwaterCanyonCarver extends UnderwaterCanyonWorldCarver {

    public TropicsUnderwaterCanyonCarver(Function<Dynamic<?>, ? extends ProbabilityConfig> p_i49922_1_) {
        super(p_i49922_1_);
        this.carvableBlocks = ImmutableSet.<Block>builder().addAll(this.carvableBlocks)
                .add(TropicraftBlocks.CORAL_SAND.get())
                .add(TropicraftBlocks.FOAMY_SAND.get())
                .add(TropicraftBlocks.MINERAL_SAND.get())
                .add(TropicraftBlocks.PACKED_PURIFIED_SAND.get())
                .add(TropicraftBlocks.PURIFIED_SAND.get())
                .add(TropicraftBlocks.VOLCANIC_SAND.get())
                .build();
    }
}
