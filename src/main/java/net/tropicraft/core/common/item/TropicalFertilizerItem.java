package net.tropicraft.core.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;
import java.util.Random;

public class TropicalFertilizerItem extends BoneMealItem {

    public TropicalFertilizerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockState state = context.getLevel().getBlockState(context.getClickedPos());
        if (state.getBlock() == Blocks.GRASS_BLOCK) {
            if (!context.getLevel().isClientSide) {
                // Logic from GrassBlock#grow, with probability for grass significantly reduced
                BlockPos blockpos = context.getClickedPos().above();
                BlockState blockstate = Blocks.GRASS.defaultBlockState();
                Level level = context.getLevel();
                Random rand = level.getRandom();
                for (int i = 0; i < 128; ++i) {
                    BlockPos blockpos1 = blockpos;
                    int j = 0;

                    while (true) {
                        if (j >= i / 16) {
                            BlockState blockstate2 = level.getBlockState(blockpos1);
                            if (blockstate2.getBlock() == blockstate.getBlock() && rand.nextInt(10) == 0) {
                                if (level instanceof ServerLevel) {
                                    ((BonemealableBlock) blockstate.getBlock()).performBonemeal((ServerLevel) level, rand, blockpos1, blockstate2);
                                }
                            }

                            if (!blockstate2.isAir()) {
                                break;
                            }

                            PlacedFeature placedFeature;
                            BlockState blockstate1;
                            if (rand.nextInt(8) > 0) { // Modification here, == changed to > to invert chances
                                List<ConfiguredFeature<?, ?>> list = level.getBiome(blockpos1).getGenerationSettings().getFlowerFeatures();
                                if (list.isEmpty()) {
                                    break;
                                }

                                // TODO this is so ugly and hacky, pls
                                ConfiguredFeature<?, ?> pFlowerFeature = list.get(0);
                                placedFeature = ((RandomPatchConfiguration) list.get(0).config()).feature().get();
                            } else {
                                placedFeature = VegetationPlacements.GRASS_BONEMEAL;
                            }

                            if (level instanceof ServerLevel serverLevel) {
                                placedFeature.place(serverLevel, serverLevel.getChunkSource().getGenerator(), rand, blockpos1);
                            }
                            break;
                        }

                        blockpos1 = blockpos1.offset(rand.nextInt(3) - 1, (rand.nextInt(3) - 1) * rand.nextInt(3) / 2, rand.nextInt(3) - 1);
                        if (level.getBlockState(blockpos1.below()).getBlock() != Blocks.GRASS_BLOCK || level.getBlockState(blockpos1).isCollisionShapeFullBlock(level, blockpos1)) {
                            break;
                        }

                        ++j;
                    }
                }
            }
            return InteractionResult.SUCCESS;
        }
        return super.useOn(context);
    }

}
