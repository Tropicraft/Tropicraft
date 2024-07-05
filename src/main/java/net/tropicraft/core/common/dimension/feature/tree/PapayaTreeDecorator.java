package net.tropicraft.core.common.dimension.feature.tree;

import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.CocoaBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.tropicraft.core.common.block.PapayaBlock;
import net.tropicraft.core.common.block.TropicraftBlocks;

public final class PapayaTreeDecorator extends TreeDecorator {
    public static final MapCodec<PapayaTreeDecorator> CODEC = MapCodec.unit(new PapayaTreeDecorator());

    @Override
    protected TreeDecoratorType<?> type() {
        return TropicraftTreeDecorators.PAPAYA.get();
    }

    @Override
    public void place(Context context) {
        ObjectArrayList<BlockPos> logs = context.logs();
        int y = logs.getLast().getY();

        for (BlockPos log : logs) {
            if (log.getY() > y - 4) {
                RandomSource random = context.random();
                if (random.nextInt(2) == 0) {
                    Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);

                    BlockPos pos = log.relative(direction);

                    if (context.isAir(pos)) {
                        BlockState blockstate = TropicraftBlocks.PAPAYA.get().defaultBlockState()
                                .setValue(PapayaBlock.AGE, random.nextInt(2))
                                .setValue(CocoaBlock.FACING, direction.getOpposite());

                        context.setBlock(pos, blockstate);
                    }
                }
            }
        }
    }
}
