package net.tropicraft.core.common.entity.hostile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.Vec3d;

public class AIAshenChaseAndPickupLostMask extends EntityAIBase {

	public EntityAshen ashen;
	public EntityLivingBase target;
	public double speed = 1D;
	public double maskGrabDistance = 3D;
	public int panicTime = 0;

	public AIAshenChaseAndPickupLostMask(EntityAshen ashen, double speed) {
		this.ashen = ashen;
		this.speed = speed;
		this.setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		return !ashen.hasMask();
	}

	@Override
	public boolean continueExecuting() {
		if (ashen.maskToTrack == null) return false;

		if (panicTime > 0) {
			panicTime--;

			if (this.ashen.worldObj.getTotalWorldTime() % 10 == 0) {
				Vec3d vec3 = RandomPositionGenerator.findRandomTarget(this.ashen, 10, 7);

				if (vec3 == null)
				{
					return false;
				}
				else
				{

					this.ashen.getNavigator().tryMoveToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord, this.speed);
					return true;
				}
			}

		} else {
			if (ashen.getDistanceSqToEntity(ashen.maskToTrack) <= maskGrabDistance) {
				ashen.pickupMask(ashen.maskToTrack);
			} else {
				if (this.ashen.worldObj.getTotalWorldTime() % 40 == 0) {
					this.ashen.getNavigator().tryMoveToXYZ(ashen.maskToTrack.posX, ashen.maskToTrack.posY, ashen.maskToTrack.posZ, this.speed);
				}
			}
		}

		return this.shouldExecute() || !this.ashen.getNavigator().noPath();
	}

	@Override
	public void startExecuting() {
		super.startExecuting();
		panicTime = 120;
	}

	public void resetTask() {
		this.target = null;
	}
}
