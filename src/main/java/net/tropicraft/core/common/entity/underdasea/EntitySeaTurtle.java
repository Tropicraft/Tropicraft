package net.tropicraft.core.common.entity.underdasea;

import java.util.ArrayList;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityTropicraftWaterBase;
import net.tropicraft.core.common.entity.underdasea.atlantoku.IAmphibian;
import net.tropicraft.core.registry.BlockRegistry;

public class EntitySeaTurtle extends EntityTropicraftWaterBase implements IAmphibian {

	public boolean isSeekingLand = false;
	public BlockPos currentNestSite = null;
	public BlockPos targetWaterSite = null;
	public boolean isLandPathing = false;
	public boolean isSeekingWater = false;
	public long timeSinceLastEgg = 0L;
	public PathNavigateGround png;

	public EntitySeaTurtle(World par1World) {
		super(par1World);
		setSize(0.9f, 0.4f);
		this.setSwimSpeeds(1f, 1f, 1f);
		this.setApproachesPlayers(true);
		png = new PathNavigateGround(this, par1World);
	}

	public EntitySeaTurtle(World world, int age) {
		super(world);
		setSize(0.3f, 0.3f);
		png = new PathNavigateGround(this, world);
	}

	@Override
	public boolean isNotColliding() {
		return true;
	}

	@Nullable
	public AxisAlignedBB getCollisionBox(Entity entityIn) {
		if (entityIn instanceof EntityPlayer) {
	//		entityIn.setPositionAndRotation(posX, posY+0.3f, posZ, rotationYaw, rotationPitch);
			entityIn.move(this.motionX, this.motionY, this.motionZ);
		}
		return this.getEntityBoundingBox();
	}

	@Nullable
	public AxisAlignedBB getCollisionBoundingBox() {
		return this.getEntityBoundingBox();
	}

	@Override
	public void applyEntityCollision(Entity entityIn) {
		//System.out.println(entityIn);
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	@Override
	public double getMountedYOffset() {
		return (double) height * 0.75D - 1F + 0.7F;
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		this.stepHeight = 1f;
		this.onGroundSpeedFactor = 10f;
		if (world.isRemote) {
			// super.onLivingUpdate();

			return;
		}
		timeSinceLastEgg++;

		if (this.timeSinceLastEgg > 2000L && !isSeekingLand) {
			this.isSeekingLand = true;
			System.out.println("Now looking for some land");
		}
		if (this.onGround && !this.isInWater() && !this.isLandPathing) {
			this.isLandPathing = true;
			this.isSeekingWater = true;
		}
		this.setAir(30);

		if (this.isSeekingWater) {
			if (this.targetWaterSite == null) {
				this.targetWaterSite = this.scanForWater();
				System.out.println("Looking for water!");
			} else {
				// this.setTargetHeading(this.targetWaterSite.getX(),
				// this.targetWaterSite.getY()+5, this.targetWaterSite.getZ(), false);
				// if(this.getNavigator() != null)
				// System.out.println("Moving to water!");
				this.isLandPathing = true;
				this.setNoAI(false);
				this.setPathPriority(PathNodeType.WATER, 1f);
				this.getMoveHelper().setMoveTo(this.targetWaterSite.getX(), this.targetWaterSite.getY(),
						this.targetWaterSite.getZ(), 0.1f);
				this.moveEntityWithHeading(1f, -1f);
				this.getMoveHelper().onUpdateMoveHelper();
				this.setMoveForward(.2f);
				// this.rotationYawHead = this.rotationYaw;
				// this.getMoveHelper().strafe(0.2f, 0f);
				if (this.isInWater()) {
					// this.isLandPathing = false;
					// this.isSeekingWater = false;
				}
			}
		}
		if (this.isSeekingLand) {
			if (this.currentNestSite == null) {
				if (rand.nextInt(200) == 0) {
					this.currentNestSite = this.scanSuitableNests();
				}
			} else {
				if (this.isInWater()) {
					this.setNoGravity(true);
					this.setNoAI(true);
					this.isLandPathing = false;
					// System.out.println("Suitable nest located! "+this.moveForward);
					this.setTargetHeading(this.currentNestSite.getX(), this.currentNestSite.getY() + 13,
							this.currentNestSite.getZ(), false);
					BlockPos above = this.getPosition().up(1);
					Vec3d angle = this.getHeading();
					double frontDist = 0.5f;

					Vec3d diff = new Vec3d(posX + (angle.xCoord * frontDist), posY + angle.yCoord,
							posZ + (angle.zCoord * frontDist));

					BlockPos ahead = new BlockPos((int) diff.xCoord, (int) posY-1, (int) diff.zCoord);

					if (!world.getBlockState(above).getMaterial().isLiquid()
							&& world.getBlockState(ahead).getMaterial().isSolid()) {
						if(!this.isInWater()) {
						// this.motionY -= 0.1f;
						}else {
							//this.jump();
							//this.motionY += 1f;
						}
						
						//this.move(0, 1, 0);
						//this.setPosition(posX, posY+1, posZ);
					//	 this.getMoveHelper().strafe(1f, 0f);
					//	 this.getMoveHelper().onUpdateMoveHelper();
						//System.out.println("oy");
						//this.jumpHelper.doJump();
						// this.setMoveForward(-1f);
					}
				} else {
					this.isLandPathing = true;

					this.setNoGravity(false);
					this.setNoAI(false);

					if (this.getNavigator().noPath()) {
						// this.motionX = 0f;
						// this.motionZ = 0f;
					}

					// this.moveForward = 0.5f;
					// this.setAIMoveSpeed(1f);
					// this.onGround = true;
					// System.out.println("Pathing?");
					if (this.getDistanceSq(this.currentNestSite) > 1.2D) {
						// this.setTargetHeading(this.currentNestSite.getX(),
						// this.currentNestSite.getY(), this.currentNestSite.getZ(), false);

						this.getMoveHelper().setMoveTo(this.currentNestSite.getX(), this.currentNestSite.getY(),
								this.currentNestSite.getZ(), 0.1f);
						/*
						 * if(this.timeSinceLastEgg > 10000D) {
						 * System.out.println("Missed chance to egg");
						 * this.getNavigator().clearPathEntity(); this.currentNestSite = null;
						 * this.isSeekingWater = true; this.isLandPathing = true; this.timeSinceLastEgg
						 * = 0L; this.isSeekingLand = false;
						 * 
						 * super.onLivingUpdate(); return; }
						 */
					//	motionX = 0.1f * Math.sin(this.rotationYaw * (Math.PI / 180.0));
					//	motionZ = 0.1f * Math.cos(this.rotationYaw * (Math.PI / 180.0));
					
						this.getMoveHelper().onUpdateMoveHelper();
						// this.getNavigator().tryMoveToXYZ(this.currentNestSite.getX(),
						// this.currentNestSite.getY(), this.currentNestSite.getZ(), 1f);
					} else {
						this.getNavigator().clearPathEntity();
						EntityTurtleEgg egg = new EntityTurtleEgg(this.world);
						egg.setPosition(this.currentNestSite.getX() + 0.5f, this.currentNestSite.getY() + 1,
								this.currentNestSite.getZ() + 0.5f);
						world.spawnEntity(egg);
						this.motionX = 0;
						this.motionZ = 0;
						this.currentNestSite = null;
						this.isSeekingWater = true;
						this.isLandPathing = true;
						this.timeSinceLastEgg = 0L;
						this.isSeekingLand = false;

					}

				}
			}
		}

	}

	@Override
	public void moveEntityWithHeading(float forward, float strafe) {
		super.moveEntityWithHeading(forward, strafe);
	}

	public BlockPos scanSuitableNests() {
		ArrayList<BlockPos> potentials = new ArrayList<BlockPos>();
		int scanSize = 16;
		for (int x = 0; x < scanSize; x++) {
			for (int y = 0; y < scanSize; y++) {

				for (int z = 0; z < scanSize; z++) {
					BlockPos sandCheck = this.getPosition().add(-(scanSize / 2) + x, -(scanSize / 4) + y,
							-(scanSize / 2) + z);
					BlockPos airCheck = sandCheck.up(1);

					if (world.getBlockState(sandCheck).getBlock().equals(BlockRegistry.sands)) {
						// We have a sand block
						if (world.getBlockState(airCheck).getMaterial().equals(Material.AIR)) {
							// We have a sand block below an air block!
							potentials.add(sandCheck);
						}
					}
				}
			}
		}
		if (potentials.size() > 0) {
			return potentials.get(rand.nextInt(potentials.size()));
		}
		return null;
	}

	public BlockPos scanForWater() {
		ArrayList<BlockPos> potentials = new ArrayList<BlockPos>();
		int scanSize = 16;
		for (int x = 0; x < scanSize; x++) {
			for (int y = 0; y < scanSize; y++) {
				for (int z = 0; z < scanSize; z++) {
					BlockPos waterCheck = this.getPosition().add(-(scanSize / 2) + x, -(scanSize / 2) + y,
							-(scanSize / 2) + z);
					BlockPos airCheck = waterCheck.up(1);

					if (world.getBlockState(waterCheck).getMaterial().equals(Material.WATER)) {
						// We have a water block
						if (world.getBlockState(airCheck).getMaterial().equals(Material.AIR)) {
							// We have a water block below an air block!
							potentials.add(waterCheck);
						}
					}
				}
			}
		}
		if (potentials.size() > 0) {
			return potentials.get(rand.nextInt(potentials.size()));
		}
		return null;
	}

	public float getMoveForward() {
		return this.moveForward;
	}

	@Override
	public boolean setTargetHeading(double posX, double posY, double posZ, boolean waterChecks) {
		if (this.currentNestSite != null) {
			return super.setTargetHeading(this.currentNestSite.getX(), this.currentNestSite.getY() + 1,
					this.currentNestSite.getZ(), false);

		}
		return super.setTargetHeading(posX, posY, posZ, waterChecks);
	}

	@Override
	public boolean isOnLadder() {
		return this.isSeekingLand && this.isInWater();
	}

	@Override
	public boolean isAIDisabled() {
		return this.isLandPathing;
	}

	@Override
	public PathNavigate getNavigator() {
		if (this.isLandPathing) {
			return png;
		}
		return super.getNavigator();
	}

}
