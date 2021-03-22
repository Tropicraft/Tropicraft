package net.tropicraft.core.common.dimension.carver;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.world.gen.carver.CaveWorldCarver;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import net.tropicraft.core.common.block.TropicraftBlocks;

import java.util.Random;

public class TropicsCaveCarver extends CaveWorldCarver {

    public TropicsCaveCarver(Codec<ProbabilityConfig> codec) {
        super(codec, 256);
        this.carvableBlocks = ImmutableSet.<Block> builder().addAll(this.carvableBlocks)
                .add(TropicraftBlocks.CORAL_SAND.get())
                .add(TropicraftBlocks.FOAMY_SAND.get())
                .add(TropicraftBlocks.MINERAL_SAND.get())
                .add(TropicraftBlocks.PACKED_PURIFIED_SAND.get())
                .add(TropicraftBlocks.PURIFIED_SAND.get())
                .add(TropicraftBlocks.VOLCANIC_SAND.get()).build();
    }

    @Override
    protected int func_230361_b_(Random rand) {
        if (rand.nextInt(5) == 0) {
            return rand.nextInt(240 + 8); // Add some evenly distributed caves in, in addition to the ones biased towards lower Y
        }
        return rand.nextInt(rand.nextInt(240) + 8);
    }
}
