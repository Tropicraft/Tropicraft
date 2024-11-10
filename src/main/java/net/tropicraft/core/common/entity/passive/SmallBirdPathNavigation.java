package net.tropicraft.core.common.entity.passive;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.FlyNodeEvaluator;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SmallBirdPathNavigation extends FlyingPathNavigation {
	private static final float ADVANCE_THRESHOLD = 0.35f;

	public SmallBirdPathNavigation(Mob mob, Level level) {
		super(mob, level);
	}

	@Override
	protected PathFinder createPathFinder(int maxVisitedNodes) {
		nodeEvaluator = new FlyNodeEvaluator();
		return new PathFinder(nodeEvaluator, maxVisitedNodes) {
			@Override
			protected float distance(Node first, Node second) {
				// Assign a higher cost to traveling vertically - try to travel in a straight line
				return first.distanceToXZ(second) + Math.abs(first.y - second.y) * 4.0f;
			}
		};
	}

	@Override
	@Nullable
	protected Path createPath(Set<BlockPos> targets, int regionOffset, boolean offsetUpward, int accuracy, float followRange) {
		Path path = super.createPath(targets, regionOffset, offsetUpward, accuracy, followRange);
		if (path != null && !path.canReach() && accuracy == 0) {
			return injectTargetNode(targets, path);
		}
		return path;
	}

	// Big hack: not sure why the pathfinder doesn't include the target node, but it means we often miss it
	private Path injectTargetNode(Set<BlockPos> targets, Path path) {
		for (BlockPos target : targets) {
			Node endNode = path.getEndNode();
			if (endNode != null && endNode.distanceManhattan(target) == 1) {
				List<Node> newNodes = new ArrayList<>(path.getNodeCount() + 1);
				for (int i = 0; i < path.getNodeCount(); i++) {
					newNodes.add(path.getNode(i));
				}
				// None of the node metadata is important, so just clone it
				newNodes.add(endNode.cloneAndMove(target.getX(), target.getY(), target.getZ()));
				return new Path(newNodes, target, true);
			}
		}
		return path;
	}

	@Override
	protected void followThePath() {
		Vec3i nextBlock = path.getNextNodePos();
		Vec3 target = mob.onGround() ? Vec3.atBottomCenterOf(nextBlock) : Vec3.atCenterOf(nextBlock);
		double deltaX = Math.abs(mob.getX() - target.x);
		double deltaY = Math.abs(mob.getY() - target.y);
		double deltaZ = Math.abs(mob.getZ() - target.z);
		if (deltaX <= ADVANCE_THRESHOLD && deltaZ <= ADVANCE_THRESHOLD && deltaY <= ADVANCE_THRESHOLD) {
			path.advance();
		}
		doStuckDetection(getTempMobPos());
	}
}
