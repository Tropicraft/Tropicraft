package net.tropicraft.core.common.dimension.feature.tree;

import com.google.common.collect.Iterables;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.shapes.BitSetDiscreteVoxelShape;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;
import net.tropicraft.core.common.block.TropicraftLeavesBlock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.OptionalInt;
import java.util.Set;

public class TropicraftLeavesFixer {
	private static final List<BlockPos> VANILLA_OFFSETS = Arrays.stream(Direction.values())
			.map(d -> new BlockPos(d.getStepX(), d.getStepY(), d.getStepZ()))
			.toList();

	public static boolean updateLeaves(LevelAccessor level, Set<BlockPos> logs, Set<BlockPos> leaves, BlockState leavesBlock) {
		boolean extendedDecay = leavesBlock.getBlock() instanceof TropicraftLeavesBlock;
		return BoundingBox.encapsulatingPositions(Iterables.concat(logs, leaves)).map(box -> {
			DiscreteVoxelShape leavesShape = buildShapeAndAssignDistances(level, box, logs, extendedDecay ? TropicraftLeavesBlock.AROUND_OFFSETS : VANILLA_OFFSETS);
			StructureTemplate.updateShapeAtEdge(level, Block.UPDATE_ALL, leavesShape, box.minX(), box.minY(), box.minZ());
			return true;
		}).orElse(false);
	}

	private static DiscreteVoxelShape buildShapeAndAssignDistances(LevelAccessor level, BoundingBox box, Set<BlockPos> logs, List<BlockPos> neighborOffsets) {
		DiscreteVoxelShape voxelShape = new BitSetDiscreteVoxelShape(box.getXSpan(), box.getYSpan(), box.getZSpan());

		List<Set<BlockPos>> queuesByDistance = new ArrayList<>(LeavesBlock.DECAY_DISTANCE);
		for (int i = 0; i < LeavesBlock.DECAY_DISTANCE; i++) {
			queuesByDistance.add(new HashSet<>());
		}

		BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

		int currentDistance = 0;
		queuesByDistance.get(currentDistance).addAll(logs);

		while (currentDistance < LeavesBlock.DECAY_DISTANCE) {
			while (!queuesByDistance.get(currentDistance).isEmpty()) {
				Iterator<BlockPos> iterator = queuesByDistance.get(currentDistance).iterator();
				BlockPos blockPos = iterator.next();
				iterator.remove();
				if (!box.isInside(blockPos)) {
					continue;
				}

				if (currentDistance != 0) {
					BlockState leafState = level.getBlockState(blockPos);
					setBlockKnownShape(level, blockPos, leafState.setValue(BlockStateProperties.DISTANCE, currentDistance));
				}
				voxelShape.fill(blockPos.getX() - box.minX(), blockPos.getY() - box.minY(), blockPos.getZ() - box.minZ());

				for (BlockPos offset : neighborOffsets) {
					mutablePos.setWithOffset(blockPos, offset);
					if (!box.isInside(mutablePos)) {
						continue;
					}
					if (!voxelShape.isFull(mutablePos.getX() - box.minX(), mutablePos.getY() - box.minY(), mutablePos.getZ() - box.minZ())) {
						BlockState neighborState = level.getBlockState(mutablePos);
						OptionalInt neighborDistance = TropicraftLeavesBlock.getOptionalDistanceAt(neighborState);
						if (neighborDistance.isPresent()) {
							int newDistance = Math.min(neighborDistance.getAsInt(), currentDistance + 1);
							if (newDistance < LeavesBlock.DECAY_DISTANCE) {
								queuesByDistance.get(newDistance).add(mutablePos.immutable());
								currentDistance = Math.min(currentDistance, newDistance);
							}
						}
					}
				}
			}

			currentDistance++;
		}

		return voxelShape;
	}

	private static void setBlockKnownShape(LevelWriter level, BlockPos pos, BlockState state) {
		level.setBlock(pos, state, Block.UPDATE_ALL | Block.UPDATE_KNOWN_SHAPE);
	}
}
