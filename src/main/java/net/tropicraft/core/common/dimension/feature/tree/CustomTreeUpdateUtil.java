package net.tropicraft.core.common.dimension.feature.tree;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.shapes.BitSetDiscreteVoxelShape;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;
import net.tropicraft.core.common.block.experimental.TropicraftExperimentalLeaveBlock;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class CustomTreeUpdateUtil {

    public static DiscreteVoxelShape updateLeaves(LevelAccessor pLevel, BoundingBox pBoundingBox, Set<BlockPos> pLogPositions, Set<BlockPos> pFoliagePositions) {
        AtomicReference<BlockState> leafState = new AtomicReference<>();

        pFoliagePositions.stream().findAny().ifPresentOrElse(blockPos1 -> leafState.set(pLevel.getBlockState(blockPos1)), () -> {
            throw new UnsupportedOperationException("It seems that a empty set was passed for FoliagePositions for a given feature!");
        });

        int maxDistance;
        IntegerProperty distanceProperty;

        if(leafState.get().getBlock() instanceof TropicraftExperimentalLeaveBlock block){
            maxDistance = block.maxDistanceBeforeDecay;
            distanceProperty = block.CUSTOM_DISTANCE;
        } else {
            maxDistance = 7;
            distanceProperty = BlockStateProperties.DISTANCE;
        }

        List<Set<BlockPos>> list = Lists.newArrayList();
        DiscreteVoxelShape discretevoxelshape = new BitSetDiscreteVoxelShape(pBoundingBox.getXSpan(), pBoundingBox.getYSpan(), pBoundingBox.getZSpan());

        for(int j = 0; j < maxDistance - 1; ++j) {
            list.add(Sets.newHashSet());
        }

        for(BlockPos blockpos : Lists.newArrayList(pFoliagePositions)) {
            if (pBoundingBox.isInside(blockpos)) {
                discretevoxelshape.fill(blockpos.getX() - pBoundingBox.minX(), blockpos.getY() - pBoundingBox.minY(), blockpos.getZ() - pBoundingBox.minZ());
            }
        }

        for(BlockPos blockpos1 : Lists.newArrayList(pLogPositions)) {
            if (pBoundingBox.isInside(blockpos1)) {
                discretevoxelshape.fill(blockpos1.getX() - pBoundingBox.minX(), blockpos1.getY() - pBoundingBox.minY(), blockpos1.getZ() - pBoundingBox.minZ());
            }

            for(BlockPos neighbourPos : BlockPos.betweenClosed(blockpos1.offset(-1,-1,-1), blockpos1.offset(1,1,1))){
                if (!pLogPositions.contains(neighbourPos)) {
                    BlockState blockstate = pLevel.getBlockState(neighbourPos);

                    if (blockstate.hasProperty(distanceProperty)) {
                        list.get(0).add(neighbourPos.immutable());
                        setBlockKnownShape(pLevel, neighbourPos, blockstate.setValue(distanceProperty, 1));
                        if (pBoundingBox.isInside(neighbourPos)) {
                            discretevoxelshape.fill(neighbourPos.getX() - pBoundingBox.minX(), neighbourPos.getY() - pBoundingBox.minY(), neighbourPos.getZ() - pBoundingBox.minZ());
                        }
                    }
                }
            }
        }

        for(int l = 1; l < maxDistance - 1; ++l) {
            Set<BlockPos> set = list.get(l - 1);
            Set<BlockPos> set1 = list.get(l);

            for(BlockPos blockpos2 : set) {
                if (pBoundingBox.isInside(blockpos2)) {
                    discretevoxelshape.fill(blockpos2.getX() - pBoundingBox.minX(), blockpos2.getY() - pBoundingBox.minY(), blockpos2.getZ() - pBoundingBox.minZ());
                }

                for(BlockPos neighbourPos : BlockPos.betweenClosed(blockpos2.offset(-1,-1,-1), blockpos2.offset(1,1,1))) {
                    if (!set.contains(neighbourPos) && !set1.contains(neighbourPos)) {
                        BlockState blockstate1 = pLevel.getBlockState(neighbourPos);
                        if (blockstate1.hasProperty(distanceProperty)) {
                            int k = blockstate1.getValue(distanceProperty);
                            if (k > l + 1) {
                                BlockState blockstate2 = blockstate1.setValue(distanceProperty, l + 1);
                                setBlockKnownShape(pLevel, neighbourPos, blockstate2);
                                if (pBoundingBox.isInside(neighbourPos)) {
                                    discretevoxelshape.fill(neighbourPos.getX() - pBoundingBox.minX(), neighbourPos.getY() - pBoundingBox.minY(), neighbourPos.getZ() - pBoundingBox.minZ());
                                }

                                set1.add(neighbourPos.immutable());
                            }
                        }
                    }
                }
            }
        }

        return discretevoxelshape;
    }

    private static void setBlockKnownShape(LevelWriter pLevel, BlockPos pPos, BlockState pState) {
        pLevel.setBlock(pPos, pState, 19);
    }
}
