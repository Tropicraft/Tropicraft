package net.tropicraft.core.common.dimension.feature.tree.mangrove;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.tropicraft.core.common.Util;
import net.tropicraft.core.common.dimension.feature.tree.TropicraftTreeDecorators;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.BiConsumer;

import static net.minecraft.world.level.levelgen.feature.TreeFeature.validTreePos;

public class PneumatophoresTreeDecorator extends TreeDecorator {
    public static final Codec<PneumatophoresTreeDecorator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Registry.BLOCK.fieldOf("roots_block").forGetter(c -> c.rootsBlock),
            Codec.INT.fieldOf("min_count").forGetter(c -> c.minCount),
            Codec.INT.fieldOf("max_count").forGetter(c -> c.maxCount),
            Codec.INT.fieldOf("spread").forGetter(c -> c.spread)
    ).apply(instance, PneumatophoresTreeDecorator::new));

    private final Block rootsBlock;
    private final int minCount;
    private final int maxCount;
    private final int spread;

    public PneumatophoresTreeDecorator(Block rootsBlock, int minCount, int maxCount, int spread) {
        this.rootsBlock = rootsBlock;
        this.minCount = minCount;
        this.maxCount = maxCount;
        this.spread = spread;
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return TropicraftTreeDecorators.PNEUMATOPHORES.get();
    }

    @Override
    public void place(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, Random pRandom, List<BlockPos> pLogPositions, List<BlockPos> pLeafPositions) {
        BlockPos origin = Util.findLowestBlock(pLogPositions);
        if (origin == null) return;

        int spread = this.spread;
        int count = pRandom.nextInt(this.maxCount - this.minCount + 1) + this.minCount;
        int maxTopY = origin.getY() + 3;
        int minBottomY = origin.getY() - 6;

        BlockPos.MutableBlockPos mutablePos = origin.mutable();
        while (MangroveTrunkPlacer.isWaterAt(pLevel, mutablePos) && mutablePos.getY() < maxTopY) {
            mutablePos.move(Direction.UP);
        }

        int topY = mutablePos.getY();

        for (int i = 0; i < count; i++) {
            int dx = pRandom.nextInt(spread) - pRandom.nextInt(spread);
            int dz = pRandom.nextInt(spread) - pRandom.nextInt(spread);
            if (dx == 0 && dz == 0) continue;
            mutablePos.setWithOffset(origin, dx, 0, dz);

            // Don't generate pneumatophores if there isn't a solid block in the column to attach onto
            boolean canGenerate = false;
            int minY = minBottomY;
            for (int y = topY; y >= minBottomY; y--) {
                mutablePos.setY(y);

                if (!validTreePos(pLevel, mutablePos)) {
                    canGenerate = true;
                    minY = y;
                    break;
                }
            }

            if (!canGenerate) {
                continue;
            }

            int y = topY;
            while (y >= minY) {
                mutablePos.setY(y--);
                MangroveTrunkPlacer.setRootsAt(pLevel, pBlockSetter, mutablePos, this.rootsBlock);
            }
        }
    }
}
