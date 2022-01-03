package net.tropicraft.core.common.dimension.carver;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.UnderwaterCaveWorldCarver;
import net.tropicraft.core.common.block.TropicraftBlocks;

import java.util.Random;

public class TropicsUnderwaterCaveCarver extends UnderwaterCaveWorldCarver {

    public TropicsUnderwaterCaveCarver(Codec<CaveCarverConfiguration> codec) {
        super(codec);
        this.replaceableBlocks = ImmutableSet.<Block> builder().addAll(this.replaceableBlocks)
                .add(TropicraftBlocks.CORAL_SAND.get())
                .add(TropicraftBlocks.FOAMY_SAND.get())
                .add(TropicraftBlocks.MINERAL_SAND.get())
                .add(TropicraftBlocks.PACKED_PURIFIED_SAND.get())
                .add(TropicraftBlocks.PURIFIED_SAND.get())
                .add(TropicraftBlocks.VOLCANIC_SAND.get())
                .add(TropicraftBlocks.MUD.get(), TropicraftBlocks.MUD_WITH_PIANGUAS.get())
                .build();
    }
    
    @Override
    protected float getThickness(Random rand) {
        float f = rand.nextFloat() * 3.0F + rand.nextFloat();
        if (rand.nextInt(10) == 0) {
           f *= rand.nextFloat() * rand.nextFloat() * 5.0F + 1.0F;
        }

        return f;
    }
}
