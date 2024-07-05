package net.tropicraft.core.common.dimension.feature.tree;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.tropicraft.core.common.block.FruitingBranchBlock;

import java.util.List;

public class BranchTreeDecorator extends TreeDecorator {
    public static final MapCodec<BranchTreeDecorator> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Codec.floatRange(0.0f, 1.0f).fieldOf("probability").forGetter(d -> d.probability),
            BlockStateProvider.CODEC.fieldOf("branch").forGetter(d -> d.branch),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("min_height").forGetter(d -> d.minHeight)
    ).apply(i, BranchTreeDecorator::new));

    private final float probability;
    private final BlockStateProvider branch;
    private final int minHeight;

    public BranchTreeDecorator(float probability, BlockStateProvider branch, int minHeight) {
        this.probability = probability;
        this.branch = branch;
        this.minHeight = minHeight;
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return TropicraftTreeDecorators.BRANCH.get();
    }

    @Override
    public void place(TreeDecorator.Context context) {
        RandomSource random = context.random();
        List<BlockPos> logs = context.logs();
        int bottomLogY = logs.getFirst().getY();
        for (BlockPos log : logs) {
            if (log.getY() - bottomLogY < minHeight) {
                continue;
            }
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                if (random.nextFloat() <= probability) {
                    BlockPos pos = log.relative(direction.getOpposite());
                    if (context.isAir(pos)) {
                        context.setBlock(pos, branch.getState(random, pos).trySetValue(FruitingBranchBlock.FACING, direction.getOpposite()));
                    }
                }
            }
        }
    }
}
