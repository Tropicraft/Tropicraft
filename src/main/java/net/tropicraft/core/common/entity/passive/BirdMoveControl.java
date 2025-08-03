package net.tropicraft.core.common.entity.passive;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;

public class BirdMoveControl extends MoveControl {
    public BirdMoveControl(Mob mob) {
        super(mob);
    }

    @Override
    public void tick() {
        if (operation == Operation.MOVE_TO) {
            operation = Operation.WAIT;

            double deltaX = wantedX - mob.getX();
            double deltaY = wantedY - mob.getY();
            double deltaZ = wantedZ - mob.getZ();
            if (Mth.lengthSquared(deltaX, deltaY, deltaZ) < 2.5e-7f) {
                mob.setYya(0.0f);
                mob.setZza(0.0f);
                return;
            }

            boolean movingVertically = Math.abs(deltaY) > 0.05f;
            double distanceHorizontal = Mth.length(deltaX, deltaZ);
            boolean movingHorizontally = Math.abs(distanceHorizontal) > 0.05f;

            // Prefer to hop on the ground - only start flying if we actually need to
            if (!mob.onGround() || movingVertically) {
                mob.setNoGravity(true);
            }

            float speed = (float) (speedModifier * mob.getAttributeValue(mob.onGround() ? Attributes.MOVEMENT_SPEED : Attributes.FLYING_SPEED));

            if (movingHorizontally) {
                mob.setSpeed(speed);
                mob.setYRot((float) (Mth.atan2(deltaZ, deltaX) * Mth.RAD_TO_DEG) - 90.0f);
            }
            if (movingVertically) {
                mob.setYya((float) (Math.signum(deltaY) * speed * 0.5f));
            }

            if (movingVertically || movingHorizontally) {
                mob.setXRot((float) -(Mth.atan2(deltaY, distanceHorizontal) * Mth.RAD_TO_DEG));
            }
        } else {
            mob.setNoGravity(false);
            mob.setYya(0.0f);
            mob.setZza(0.0f);
        }
    }
}
