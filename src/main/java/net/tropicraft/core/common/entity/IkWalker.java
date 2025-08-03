package net.tropicraft.core.common.entity;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import org.joml.Vector2d;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class IkWalker {
    private static final double STEP_DISTANCE = 0.25;
    // Even if another foot is currently stepping, we're too far - step now!
    private static final double STEP_NOW_DISTANCE = 0.4;
    private static final double RESET_DISTANCE = 1.5;

    private static final double MIN_SETTLE_DISTANCE = 0.05;

    private final int stepLengthTicks;
    private final int stepIntervalTicks;
    private final int settleDelayTicks;
    private final float stepAhead;

    private final List<Foot> feet = new ArrayList<>();

    private int stepDelayTicks;

    public IkWalker(int stepLengthTicks, int stepIntervalTicks, int settleDelayTicks, float stepAhead) {
        this.stepLengthTicks = stepLengthTicks;
        this.stepIntervalTicks = stepIntervalTicks;
        this.settleDelayTicks = settleDelayTicks;
        this.stepAhead = stepAhead;
    }

    public Foot addFoot(float forward, float right) {
        Foot foot = new Foot(forward, right);
        feet.add(foot);
        return foot;
    }

    public void reset(Mob entity) {
        EntitySpace entitySpace = EntitySpace.from(entity);
        for (Foot foot : feet) {
            foot.reset(entitySpace);
        }
    }

    public void update(Mob entity) {
        boolean moving = entity.walkAnimation.isMoving();
        EntitySpace entitySpace = EntitySpace.from(entity);

        boolean waitingToStep = stepDelayTicks > 0;
        if (waitingToStep) {
            stepDelayTicks--;
        }

        boolean anyStepping = false;
        for (Foot foot : feet) {
            if (entity.onGround()) {
                foot.updateOnGround(entitySpace, moving, waitingToStep, anyStepping);
            } else {
                foot.updateInAir(entitySpace);
            }
            anyStepping |= foot.isStepping();
        }

        if (anyStepping) {
            stepDelayTicks = stepIntervalTicks;
        }
    }

    public class Foot {
        private final float forward;
        private final float right;

        private final Vector2d oldPosition = new Vector2d();
        private final Vector2d position = new Vector2d();
        @Nullable
        private Vector2d lastStepPosition;

        private int stepTicks;
        private int stepLengthTicks;

        private int settleDelayTicks;

        private Foot(float forward, float right) {
            this.forward = forward;
            this.right = right;
        }

        private void reset(EntitySpace entitySpace) {
            moveTo(entitySpace.toWorldSpace(forward, right));
            oldPosition.set(position);
        }

        private void updateOnGround(EntitySpace entitySpace, boolean moving, boolean waitingToStep, boolean anyFootStepping) {
    		oldPosition.set(position);

			if (lastStepPosition != null && ++stepTicks >= stepLengthTicks) {
				lastStepPosition = null;
				stepTicks = 0;
			}

			if (settleDelayTicks > 0) {
				settleDelayTicks--;
			}

			Vector2d settleTarget = entitySpace.toWorldSpace(forward, right);

			double distanceSq = position.distanceSquared(settleTarget);
			if (distanceSq > RESET_DISTANCE * RESET_DISTANCE) {
				moveTo(settleTarget);
				return;
			}

			if (isStepping() || anyFootStepping) {
				return;
			}

			if (distanceSq > STEP_NOW_DISTANCE * STEP_NOW_DISTANCE) {
				maybeStepTo(settleTarget, distanceSq, 0.0, IkWalker.this.stepLengthTicks / 3);
				return;
			}

			if (!waitingToStep) {
				if (moving) {
					Vector2d stepTarget = entitySpace.toWorldSpace(forward + stepAhead, right);
					maybeStepTo(stepTarget, distanceSq, STEP_DISTANCE, IkWalker.this.stepLengthTicks);
				} else if (settleDelayTicks == 0) {
					maybeStepTo(settleTarget, distanceSq, MIN_SETTLE_DISTANCE, IkWalker.this.stepLengthTicks);
				}
			}
		}

		private void updateInAir(EntitySpace entitySpace) {
			oldPosition.set(position);
			moveTo(entitySpace.toWorldSpace(forward, right));
		}

		private void maybeStepTo(Vector2d target, double distanceSq, double minDistance, int lengthTicks) {
			if (distanceSq < minDistance * minDistance) {
				return;
			}
			lastStepPosition = new Vector2d(position);
			oldPosition.set(target);
			position.set(target);
			stepLengthTicks = lengthTicks;
			settleDelayTicks = IkWalker.this.settleDelayTicks;
		}

		private void moveTo(Vector2d target) {
			position.set(target);
			lastStepPosition = null;
			stepTicks = 0;
			settleDelayTicks = IkWalker.this.settleDelayTicks;
		}

		private Vector2d stepPosition(float partialTicks) {
			return oldPosition.lerp(position, partialTicks, new Vector2d());
		}

		public boolean isStepping() {
			return lastStepPosition != null;
		}

		public Vector3f solveModelPosition(EntitySpace entitySpace, float partialTicks) {
			Vector3f stepPos = entitySpace.toLocalSpace(stepPosition(partialTicks));
			Vector3f pos;
			if (lastStepPosition != null) {
				Vector3f lastStep = entitySpace.toLocalSpace(lastStepPosition);
				pos = step(lastStep, stepPos, (stepTicks + partialTicks) / stepLengthTicks);
			} else {
				pos = stepPos;
			}
			return pos.mul(1.0f, 1.0f, -1.0f);
		}

		private static Vector3f step(Vector3f from, Vector3f to, float progress) {
			Vector3f midpoint = from.add(to, new Vector3f()).mul(0.5f);
			Vector3f delta = from.sub(midpoint);
			Vector3f axis = delta.cross(0.0f, 1.0f, 0.0f, new Vector3f()).normalize();
			return delta.rotateAxis(progress * Mth.PI, axis.x, axis.y, axis.z).add(midpoint);
		}
	}

	public record EntitySpace(
			double x, double z,
			float sin, float cos
	) {
		public EntitySpace(double x, double z, float yRot) {
			this(x, z, Mth.sin(-yRot * Mth.DEG_TO_RAD), Mth.cos(-yRot * Mth.DEG_TO_RAD));
		}

		public static EntitySpace from(Mob entity) {
			return new EntitySpace(entity.getX(), entity.getZ(), entity.yBodyRot);
		}

		public static EntitySpace from(Mob entity, float partialTicks) {
			return new EntitySpace(
					Mth.lerp(partialTicks, entity.xo, entity.getX()),
					Mth.lerp(partialTicks, entity.zo, entity.getZ()),
					Mth.lerp(partialTicks, entity.yBodyRotO, entity.yBodyRot)
			);
		}

		public Vector2d toWorldSpace(float forward, float right) {
			return new Vector2d(
					-right * cos + forward * sin,
					forward * cos - -right * sin
			).add(x, z);
		}

		public Vector3f toLocalSpace(Vector2d worldPos) {
			float relativeX = (float) (worldPos.x - x);
			float relativeZ = (float) (worldPos.y - z);
			return new Vector3f(
					relativeX * cos + relativeZ * -sin,
					0.0f,
					relativeZ * cos - relativeX * -sin
			);
		}
	}
}
