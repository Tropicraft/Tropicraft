package net.tropicraft.core.common.dimension.feature.tree.mangrove;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.tropicraft.core.common.Util;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.dimension.feature.tree.TropicraftTreeDecorators;

import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public class PianguasTreeDecorator extends TreeDecorator {
    public static final PianguasTreeDecorator REGULAR = new PianguasTreeDecorator(8, 4);
    public static final PianguasTreeDecorator SMALL = new PianguasTreeDecorator(2, 2);

    public static final Codec<PianguasTreeDecorator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("count").forGetter(c -> c.count),
            Codec.INT.fieldOf("spread").forGetter(c -> c.spread)
    ).apply(instance, PianguasTreeDecorator::new));

    private final int count;
    private final int spread;

    public PianguasTreeDecorator(int count, int spread) {
        this.count = count;
        this.spread = spread;
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return TropicraftTreeDecorators.PIANGUAS.get();
    }

    @Override
    public void place(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter, Random random, List<BlockPos> logs, List<BlockPos> leaves) {
        BlockPos lowestLog = Util.findLowestBlock(logs);
        if (lowestLog == null) return;

        for (int i = 0; i < this.count; i++) {
            int x = lowestLog.getX() + random.nextInt(this.spread) - random.nextInt(this.spread);
            int z = lowestLog.getZ() + random.nextInt(this.spread) - random.nextInt(this.spread);
            int y = lowestLog.getY() - random.nextInt(this.spread);

            BlockPos local = new BlockPos(x, y, z);
            if (level.isStateAtPosition(local, s-> s.is(TropicraftBlocks.MUD.get()))) {
                blockSetter.accept(local, TropicraftBlocks.MUD_WITH_PIANGUAS.get().defaultBlockState());
            }
        }
    }
}
