package net.tropicraft.core.common.entity.underdasea;

import java.util.ArrayList;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.tropicraft.core.common.Util;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityTropicraftWaterBase;
import net.tropicraft.core.common.entity.underdasea.atlantoku.IAmphibian;
import net.tropicraft.core.registry.BlockRegistry;

public class EntitySeaTurtle extends EntityTropicraftWaterBase implements IAmphibian {

	private static final long EGG_SITE_WAIT_TIME = 500L;
	private static final long EGG_INTERVAL_MINIMUM = 200L;
	private static final int NEST_SITE_SEARCH_ODDS = 1000;
	private static final int MAX_BLOCK_SCAN_RADIUS = 64;

	private static final DataParameter<Boolean> IS_MATURE = EntityDataManager.<Boolean>createKey(EntitySeaTurtle.class,
			DataSerializers.BOOLEAN);


	public boolean isSeekingLand = false;
	public BlockPos currentNestSite = null;
	public BlockPos targetWaterSite = null;
	public boolean isLandPathing = false;
	public boolean isSeekingWater = false;
	public long timeSinceLastEgg = 0L;
	public long eggSiteCooldown = EGG_SITE_WAIT_TIME;
	public PathNavigateGround png;

	public EntitySeaTurtle(World par1World) {
		super(par1World);
		setSize(0.9f, 0.4f);
		this.setSwimSpeeds(1f, 1f, 1f);
		this.setApproachesPlayers(true);
		png = new PathNavigateGround(this, par1World);
		this.stepHeight = 1f;
	}

	/** Constructor for baby */
	public EntitySeaTurtle(World world, int age) {
		this(world);
		setSize(0.1f, 0.1f);
	}

	@Override
	public void entityInit() {
		super.entityInit();

		this.getDataManager().register(IS_MATURE, false);
		this.assignRandomTexture();
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		this.setAir(30);
		if (this.getNavigator() == null) {
			return;
		}

		float renderSize = 0.3f + (((float) ticksExisted / 2000));
		if (renderSize > 1f)
			renderSize = 1f;
		if (this.ticksExisted % 40 == 1) {
			setSize(renderSize, renderSize * 0.5f);
		}

		if (this.isMature()) {
			renderSize = 1f;
		}

		if (world.isRemote) {
			return;
		}

		if (renderSize >= 1f) {
			setMature();
			timeSinceLastEgg++;
		}

		if (this.timeSinceLastEgg > EGG_INTERVAL_MINIMUM && !isSeekingLand
				&& rand.nextInt(NEST_SITE_SEARCH_ODDS) == 0 && world.getWorldTime() > 13000L && world.getWorldTime() < 23000L) {
			this.isSeekingLand = true;
			log("Seeking Land");
		}

		if (this.onGround && !this.isInWater() && !this.isLandPathing) {
			this.isLandPathing = true;
			this.isSeekingWater = true;
		}

		if (this.isSeekingLand) {
			if (this.currentNestSite == null) {
				if (rand.nextInt(NEST_SITE_SEARCH_ODDS) == 0) {
					this.currentNestSite = this.scanSuitableNests();

					if (this.currentNestSite != null) {
						log("Found suitable nest site.");
					} else {
						this.setRandomTargetHeading();
					}
				}
			} else {
				if (this.isInWater()) {
					this.setNoGravity(true);
					this.setNoAI(true);
					this.isLandPathing = false;
					this.setTargetHeading(this.currentNestSite.getX(), this.currentNestSite.getY() + 13,
							this.currentNestSite.getZ(), false);
					BlockPos above = this.getPosition().up(1);
					Vec3d angle = this.getHeading();
					double frontDist = 1.2f;

					Vec3d diff = new Vec3d(posX + (angle.xCoord * frontDist), posY, posZ + (angle.zCoord * frontDist));

					BlockPos ahead = new BlockPos((int) diff.xCoord, (int) posY - 1, (int) diff.zCoord);

					if (!world.getBlockState(above).getMaterial().isLiquid()
							&& world.getBlockState(ahead).getMaterial().isSolid()) {
					//	this.swimPitch = 15f;
						this.motionY += 0.2f;
						this.swimSpeedCurrent = 2f;
						log("Climbing up");
					}
				} else {
					this.isLandPathing = true;
					this.setNoGravity(false);
					this.setNoAI(false);
					if (this.getDistanceSq(this.currentNestSite.up(1)) > 3D) {
						if (this.ticksExisted % 10 == 0) {
							log("Trying to path to nest site");
							Util.tryMoveToXYZLongDist(this, this.currentNestSite.getX(),
									this.currentNestSite.getY(), this.currentNestSite.getZ(), 0.2f);
							if (this.getNavigator().noPath()) {
								log("Gave up, was "+this.getDistanceSq(this.currentNestSite)+" away");
								this.currentNestSite = null;
								this.isSeekingWater = true;
								this.isLandPathing = true;
								this.timeSinceLastEgg = 0L;
								this.isSeekingLand = false;
								this.eggSiteCooldown = EGG_SITE_WAIT_TIME;

							}
						}

					} else {
						// this.getNavigator().clearPathEntity();
						if (this.eggSiteCooldown == EGG_SITE_WAIT_TIME)
							log("Close enough to nest, waiting " + EGG_SITE_WAIT_TIME + " ticks to lay egg");

						this.eggSiteCooldown--;

						if (this.eggSiteCooldown <= 0) {
							log("egg laid!");

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
							this.eggSiteCooldown = EGG_SITE_WAIT_TIME;
						}

					}

				}

				if (this.currentNestSite == null) {
					this.setRandomTargetHeadingForce(30);
				}
			}
		}

		if (this.isSeekingWater) {
			if (this.targetWaterSite == null) {
				if (this.ticksExisted % 20 == 0) {
					this.targetWaterSite = this.scanForWater();
					log("Seeking water!");
					if (this.targetWaterSite == null) {
						log("No luck finding water :(");
					}
				}

			} else {
				this.isLandPathing = true;
				this.setNoAI(false);

				if (this.ticksExisted % 20 == 0) {
					this.setPathPriority(PathNodeType.WATER, 10f);
					this.setPathPriority(PathNodeType.BLOCKED, -1f);
					this.setPathPriority(PathNodeType.WALKABLE, 10f);

					log("Moving to water!");
					Util.tryMoveToXYZLongDist(this, targetWaterSite, 0.2f);

					if (this.getNavigator().noPath() || rand.nextInt(60) == 0) {
						this.targetWaterSite = this.scanForWater();

					}
				}
				if (this.isInWater()) {
					this.setRandomTargetHeadingForce(10);
					this.swimYaw = -this.getRotationYawHead();
					this.swimPitch = -15f;
					this.isLandPathing = false;
					this.isSeekingWater = false;
				}
			}
		}

		this.getNavigator().updatePath();
		this.getNavigator().onUpdateNavigation();
		if (!this.isInWater()) {
			this.swimYaw = this.rotationYaw;
			this.prevSwimYaw = this.prevRotationYaw;
		}
		this.syncSwimAngles();

	}

	@Override
	public void moveEntityWithHeading(float forward, float strafe) {
		super.moveEntityWithHeading(forward, strafe);
	}

	public BlockPos scanSuitableNests() {
		ArrayList<BlockPos> potentials = new ArrayList<BlockPos>();
		int scanSize = MAX_BLOCK_SCAN_RADIUS;
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
			log("Completed a scan for blocks with results");
			return potentials.get(rand.nextInt(potentials.size()));
		}
		log("Completed a scan for blocks without results");

		return null;
	}

	public BlockPos scanForWater() {
		ArrayList<BlockPos> potentials = new ArrayList<BlockPos>();
		int scanSize = MAX_BLOCK_SCAN_RADIUS;
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
			log("Completed a scan for waterblocks with results");

			double closest = -1;
			BlockPos closestBlock = null;
			for (BlockPos b : potentials) {
				if (this.getDistanceSq(b) < closest || closest == -1) {
					if (rand.nextInt(5) == 0) {
						closestBlock = b;
					}
				}
			}
			if (closestBlock != null) {
				return closestBlock;
			}
			return potentials.get(rand.nextInt(potentials.size()));
		}
		log("Completed a scan for waterblocks without results");

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

	public boolean isMature() {
		return this.getDataManager().get(IS_MATURE);
	}

	public void setMature() {
		if (!isMature()) {
			this.getDataManager().set(IS_MATURE, true);
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound n) {
		n.setBoolean("isMature", isMature());
		super.writeEntityToNBT(n);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound n) {
		if (n.getBoolean("isMature")) {
			setMature();
		}
		super.readEntityFromNBT(n);
	}

	@Override
	public boolean isNotColliding() {
		return true;
	}

	@Nullable
	public AxisAlignedBB getCollisionBox(Entity entityIn) {

		return this.getEntityBoundingBox();
	}

	@Nullable
	public AxisAlignedBB getCollisionBoundingBox() {
		return null;
	}

	@Override
	public void applyEntityCollision(Entity entityIn) {
		// log(entityIn);
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
	public boolean isOnLadder() {
		return this.isSeekingLand && this.isInWater();
	}

	@Override
	public boolean isAIDisabled() {
		return !this.isLandPathing;
	}

	@Override
	public PathNavigate getNavigator() {
		if (this.isLandPathing) {
			return png;
		}
		return super.getNavigator();
	}

	private void log(String s) {
		// System.out.println(s);
	}

}
