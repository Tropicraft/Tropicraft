package net.tropicraft.core.common.entity.ai.ashen;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.vector.Vector3d;
import net.tropicraft.core.common.entity.hostile.AshenEntity;

import java.util.EnumSet;

public class AIAshenChaseAndPickupLostMask extends Goal {
    public AshenEntity ashen;
    public LivingEntity target;
    public double speed = 1D;
    public double maskGrabDistance = 3D;
    public int panicTime = 0;

    public AIAshenChaseAndPickupLostMask(AshenEntity ashen, double speed) {
        this.ashen = ashen;
        this.speed = speed;
        this.setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean shouldExecute() {
        return !ashen.hasMask() && ashen.maskToTrack != null;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !ashen.hasMask() && ashen.maskToTrack != null && ashen.maskToTrack.isAlive();
    }

    @Override
    public void tick() {
        if (panicTime > 0) {
            panicTime--;

            if (ashen.world.getGameTime() % 10 == 0) {
                Vector3d vec3 = RandomPositionGenerator.findRandomTarget(ashen, 10, 7);

                if (vec3 != null) {
                    ashen.getNavigator().tryMoveToXYZ(vec3.x, vec3.y, vec3.z, speed);
                }
            }
        } else {
            if (ashen.getDistanceSq(ashen.maskToTrack) <= maskGrabDistance) {
                if (ashen.maskToTrack.isAlive()/* && ashen.world.loadedEntityList.contains(ashen.maskToTrack)*/) {
                    ashen.pickupMask(ashen.maskToTrack);
                } else {
                    ashen.maskToTrack = null;
                }
            } else {
                if (ashen.world.getGameTime() % 40 == 0) {
                    ashen.getNavigator().tryMoveToXYZ(ashen.maskToTrack.getPosX(), ashen.maskToTrack.getPosY(), ashen.maskToTrack.getPosZ(), speed);
                }
            }
        }
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        panicTime = 120;
    }

    @Override
    public void resetTask() {
        this.target = null;
    }
}
