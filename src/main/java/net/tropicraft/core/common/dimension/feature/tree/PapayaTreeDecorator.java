package net.tropicraft.core.common.dimension.feature.tree;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CocoaBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import net.tropicraft.core.common.block.PapayaBlock;
import net.tropicraft.core.common.block.TropicraftBlocks;

import java.util.List;
import java.util.Random;
import java.util.Set;

public final class PapayaTreeDecorator extends TreeDecorator {
    public static final Codec<PapayaTreeDecorator> CODEC = Codec.unit(new PapayaTreeDecorator());

    protected TreeDecoratorType<?> getDecoratorType() {
        return TropicraftTreeDecorators.PAPAYA.get();
    }

    public void func_225576_a_(ISeedReader world, Random rand, List<BlockPos> logs, List<BlockPos> p_225576_4_, Set<BlockPos> p_225576_5_, MutableBoundingBox p_225576_6_) {
        int y = logs.get(logs.size() - 1).getY();

        for (BlockPos log : logs) {
            if (log.getY() > y - 4) {
                if (rand.nextInt(2) == 0) {
                    Direction direction = Direction.Plane.HORIZONTAL.random(rand);

                    BlockPos pos = log.offset(direction);

                    if (Feature.isAirAt(world, pos)) {
                        System.out.println(pos);

                        BlockState blockstate = TropicraftBlocks.PAPAYA.get().getDefaultState()
                                .with(PapayaBlock.AGE, rand.nextInt(2))
                                .with(CocoaBlock.HORIZONTAL_FACING, direction.getOpposite());

                        world.setBlockState(pos, blockstate, 3);
//                        this.func_227423_a_(world, pos, blockstate, p_225576_5_, p_225576_6_);
                        System.out.println(world.getBlockState(pos).getBlock());
                    }
                }
            }
        }
    }
}
