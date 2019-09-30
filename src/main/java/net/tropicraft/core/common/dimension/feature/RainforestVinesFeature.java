package net.tropicraft.core.common.dimension.feature;

import java.util.Random;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.VineBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class RainforestVinesFeature extends Feature<NoFeatureConfig> {

    private static final Direction[] DIRECTIONS = Direction.values();

    public RainforestVinesFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> p_i51418_1_) {
        super(p_i51418_1_);
    }

    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(pos);

        for (int i = pos.getY(); i < worldIn.getWorld().getDimension().getHeight(); ++i) {
            blockpos$mutableblockpos.setPos(pos);
            blockpos$mutableblockpos.move(rand.nextInt(4) - rand.nextInt(4), 0, rand.nextInt(4) - rand.nextInt(4));
            blockpos$mutableblockpos.setY(i);
            if (worldIn.isAirBlock(blockpos$mutableblockpos)) {
                for (Direction direction : DIRECTIONS) {
                    blockpos$mutableblockpos.move(direction);
                    BlockState attaching = worldIn.getBlockState(blockpos$mutableblockpos);
                    if ((attaching.getBlock() == Blocks.GRASS_BLOCK && rand.nextInt(4) == 0) || attaching.isIn(BlockTags.LEAVES)) {
                        if (direction != Direction.DOWN && VineBlock.canAttachTo(worldIn, blockpos$mutableblockpos, direction)) {
                            blockpos$mutableblockpos.move(direction.getOpposite());
                            int len = rand.nextInt(3) + 2;
                            for (int j = 0; j < len && worldIn.isAirBlock(blockpos$mutableblockpos); j++) {
                                worldIn.setBlockState(blockpos$mutableblockpos, Blocks.VINE.getDefaultState().with(VineBlock.getPropertyFor(direction), Boolean.valueOf(true)), 2);
                                blockpos$mutableblockpos.move(Direction.DOWN);
                            }
                            break;
                        }
                    }
                }
            }
        }

        return true;
    }
}
