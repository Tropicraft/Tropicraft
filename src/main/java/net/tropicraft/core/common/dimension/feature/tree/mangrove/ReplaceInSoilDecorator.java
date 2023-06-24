package net.tropicraft.core.common.dimension.feature.tree.mangrove;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedBlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.tropicraft.core.common.Util;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.dimension.feature.tree.TropicraftTreeDecorators;

public class ReplaceInSoilDecorator extends TreeDecorator {
    public static final Codec<ReplaceInSoilDecorator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("count").forGetter(c -> c.count),
            Codec.INT.fieldOf("spread").forGetter(c -> c.spread),
            RuleBasedBlockStateProvider.CODEC.fieldOf("state_provider").forGetter(c -> c.stateProvider),
            BlockPredicate.CODEC.fieldOf("target").forGetter(c -> c.target)
    ).apply(instance, ReplaceInSoilDecorator::new));

    private final int count;
    private final int spread;
    private final RuleBasedBlockStateProvider stateProvider;
    private final BlockPredicate target;

    public ReplaceInSoilDecorator(int count, int spread, RuleBasedBlockStateProvider stateProvider, BlockPredicate target) {
        this.count = count;
        this.spread = spread;
        this.stateProvider = stateProvider;
        this.target = target;
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return TropicraftTreeDecorators.REPLACE_IN_SOIL.get();
    }

    @Override
    public void place(Context context) {
        BlockPos lowestLog = Util.findLowestBlock(context.logs());
        if (lowestLog == null) return;

        LevelSimulatedReader level = context.level();
        RandomSource random = context.random();
        for (int i = 0; i < this.count; i++) {
            int x = lowestLog.getX() + random.nextInt(this.spread) - random.nextInt(this.spread);
            int z = lowestLog.getZ() + random.nextInt(this.spread) - random.nextInt(this.spread);
            int y = lowestLog.getY() - random.nextInt(this.spread);

            BlockPos local = new BlockPos(x, y, z);
            if (level.isStateAtPosition(local, s-> s.is(TropicraftBlocks.MUD.get()))) {
                context.setBlock(local, TropicraftBlocks.MUD_WITH_PIANGUAS.get().defaultBlockState());
            }
        }
    }
}
