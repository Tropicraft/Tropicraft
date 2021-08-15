package net.tropicraft.core.common.dimension.feature.tree;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import net.tropicraft.core.common.block.TropicraftBlocks;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class PiangasTreeDecorator extends TreeDecorator {
    public static final PiangasTreeDecorator REGULAR = new PiangasTreeDecorator(8, 4);
    public static final PiangasTreeDecorator SMALL = new PiangasTreeDecorator(2, 2);

    public static final Codec<PiangasTreeDecorator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("count").forGetter(c -> c.count),
            Codec.INT.fieldOf("spread").forGetter(c -> c.spread)
    ).apply(instance, PiangasTreeDecorator::new));

    private final int count;
    private final int spread;

    public PiangasTreeDecorator(int count, int spread) {
        this.count = count;
        this.spread = spread;
    }

    @Override
    protected TreeDecoratorType<?> getDecoratorType() {
        return TropicraftTreeDecorators.PIANGUAS.get();
    }

    @Override
    public void func_225576_a_(ISeedReader world, Random random, List<BlockPos> logs, List<BlockPos> leaves, Set<BlockPos> placed, MutableBoundingBox box) {
        if (logs.size() == 0) {
            return;
        }

        // Find lowest log position
        BlockPos pos = logs.get(0);
        for (BlockPos log : logs) {
            if (pos.getY() > log.getY()) {
                pos = log;
            }
        }

        for (int i = 0; i < this.count; i++) {
            int x = pos.getX() + random.nextInt(this.spread) - random.nextInt(this.spread);
            int z = pos.getZ() + random.nextInt(this.spread) - random.nextInt(this.spread);
            int y = pos.getY() - random.nextInt(this.spread);

            BlockPos local = new BlockPos(x, y, z);
            if (world.getBlockState(local).matchesBlock(TropicraftBlocks.MUD.get())) {
                func_227423_a_(world, local, TropicraftBlocks.MUD_WITH_PIANGUAS.get().getDefaultState(), placed, box);
            }
        }
    }
}
