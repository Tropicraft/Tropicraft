package net.tropicraft.core.common.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IGrowable;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DecoratedFeatureConfig;
import net.minecraft.world.gen.feature.FlowersFeature;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.Random;

public class TropicalFertilizerItem extends BoneMealItem {

    public TropicalFertilizerItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        BlockState state = context.getWorld().getBlockState(context.getPos());
        if (state.getBlock() == Blocks.GRASS_BLOCK) {
            if (!context.getWorld().isRemote) {
                // Logic from GrassBlock#grow, with probability for grass significantly reduced
                BlockPos blockpos = context.getPos().up();
                BlockState blockstate = Blocks.GRASS.getDefaultState();
                World world = context.getWorld();
                Random rand = world.getRandom();
                for (int i = 0; i < 128; ++i) {
                    BlockPos blockpos1 = blockpos;
                    int j = 0;

                    while (true) {
                        if (j >= i / 16) {
                            BlockState blockstate2 = world.getBlockState(blockpos1);
                            if (blockstate2.getBlock() == blockstate.getBlock() && rand.nextInt(10) == 0) {
                                if (world instanceof ServerWorld) {
                                    ((IGrowable) blockstate.getBlock()).grow((ServerWorld) world, rand, blockpos1, blockstate2);
                                }
                            }

                            if (!blockstate2.isAir(world, blockpos1)) {
                                break;
                            }

                            BlockState blockstate1;
                            if (rand.nextInt(8) > 0) { // Modification here, == changed to > to invert chances
                                List<ConfiguredFeature<?, ?>> list = world.getBiome(blockpos1).getFlowers();
                                if (list.isEmpty()) {
                                    break;
                                }

                                // TODO this is so ugly and hacky, pls
                                blockstate1 = ((FlowersFeature) ((DecoratedFeatureConfig) (list.get(0)).config).feature.feature).getFlowerToPlace(rand, blockpos1, null);
                            } else {
                                blockstate1 = blockstate;
                            }

                            if (blockstate1.isValidPosition(world, blockpos1)) {
                                world.setBlockState(blockpos1, blockstate1, 3);
                            }
                            break;
                        }

                        blockpos1 = blockpos1.add(rand.nextInt(3) - 1, (rand.nextInt(3) - 1) * rand.nextInt(3) / 2, rand.nextInt(3) - 1);
                        if (world.getBlockState(blockpos1.down()).getBlock() != Blocks.GRASS_BLOCK || world.getBlockState(blockpos1).isCollisionShapeOpaque(world, blockpos1)) {
                            break;
                        }

                        ++j;
                    }
                }
            }
            return ActionResultType.SUCCESS;
        }
        return super.onItemUse(context);
    }
}
