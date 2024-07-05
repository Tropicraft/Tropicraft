package net.tropicraft.core.common.entity.ai.ashen;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;
import net.tropicraft.core.common.entity.hostile.AshenEntity;

import java.util.EnumSet;

public class AIAshenChaseAndPickupLostMask extends Goal {
    public final AshenEntity ashen;
    public LivingEntity target;
    public double speed = 1D;
    public final double maskGrabDistance = 3D;
    public int panicTime = 0;

    public AIAshenChaseAndPickupLostMask(AshenEntity ashen, double speed) {
        this.ashen = ashen;
        this.speed = speed;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return !ashen.hasMask() && ashen.maskToTrack != null;
    }

    @Override
    public boolean canContinueToUse() {
        return !ashen.hasMask() && ashen.maskToTrack != null && ashen.maskToTrack.isAlive();
    }

    @Override
    public void tick() {
        if (panicTime > 0) {
            panicTime--;

            if (ashen.level().getGameTime() % 10 == 0) {
                Vec3 vec3 = DefaultRandomPos.getPos(ashen, 10, 7);

                if (vec3 != null) {
                    ashen.getNavigation().moveTo(vec3.x, vec3.y, vec3.z, speed);
                }
            }
        } else {
            if (ashen.distanceToSqr(ashen.maskToTrack) <= maskGrabDistance) {
                if (ashen.maskToTrack.isAlive()/* && ashen.world.loadedEntityList.contains(ashen.maskToTrack)*/) {
                    ashen.pickupMask(ashen.maskToTrack);
                } else {
                    ashen.maskToTrack = null;
                }
            } else {
                if (ashen.level().getGameTime() % 40 == 0) {
                    ashen.getNavigation().moveTo(ashen.maskToTrack.getX(), ashen.maskToTrack.getY(), ashen.maskToTrack.getZ(), speed);
                }
            }
        }
    }

    @Override
    public void start() {
        super.start();
        panicTime = 120;
    }

    @Override
    public void stop() {
        this.target = null;
    }
}
