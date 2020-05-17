package net.tropicraft.core.common.entity.projectile;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

public class LavaBallEntity extends Entity
{

	public boolean setFire;
	public float size;
	public boolean held;
	public int lifeTimer;

	public double accelerationX;
	public double accelerationY;
	public double accelerationZ;

	public LavaBallEntity(EntityType<? extends LavaBallEntity> type, World world) {
		super(type, world);
		setFire = false;
		held = false;
		size = 1;
		lifeTimer = 0;
	}

	public LavaBallEntity(EntityType<? extends LavaBallEntity> type,World world, double i, double j, double k, double motX, double motY, double motZ) {
		super(type, world);
		setFire = false;
		setLocationAndAngles(i, j, k, 0, 0);
		accelerationX = motX;
		accelerationY = motY;
		accelerationZ = motZ;
		size = 1;
		held = false;
		lifeTimer = 0;
	}

	public LavaBallEntity(EntityType<? extends LavaBallEntity> type, World world, float startSize) {
		super(type, world);
		size = startSize;
		setFire = false;
		held = true;
		lifeTimer = 0;
	}

	@Override
    public boolean canBeCollidedWith() {
		return true;
	}

	@Override
    public boolean canBePushed() {
		return true;
	}

	@OnlyIn(Dist.CLIENT)
	public void supahDrip() {
		float x = (float) getPosX();
		float y = (float) getPosY();
		float z = (float) getPosZ();

		if (world.isRemote) {
			world.addParticle(ParticleTypes.LAVA, x, y, z, this.getMotion().x, -1.5F, this.getMotion().z);
		}
	}

	@Override
	protected void registerData()
	{

	}

	@Override
    public void tick() {
		super.tick();
		// System.out.println("laba ball: " + posX + " " + posY + " " + posZ);

		if (lifeTimer < 500)
		{
			lifeTimer++;
		}
		else
		{
			this.remove();
		}

		double motionX = this.getMotion().x;
		double motionY = this.getMotion().y;
		double motionZ = this.getMotion().z;

		if (size < 1) {
			size += .025;
		}

		if (onGround) {
			motionZ *= .95;
			motionX *= .95;
		}

		motionY *= .99;

		if (!onGround) {
			motionY -=.05F;
			if (world.isRemote) {
				for (int i = 0; i < 5 + rand.nextInt(3); i++){
					supahDrip();
				}
			}
		}

		if (collidedHorizontally) {
			motionZ = 0;
			motionX = 0;
		}

		//TODO: Note below, these used to be tempLavaMoving - maybe they still need to be?
		int thisX = (int)Math.floor(getPosX());
		int thisY = (int)Math.floor(getPosY());
		int thisZ = (int)Math.floor(getPosZ());

		BlockPos posCurrent = new BlockPos(thisX, thisY, thisZ);
		BlockPos posBelow = posCurrent.down();
		BlockState stateBelow = world.getBlockState(posBelow);

		if (!world.isAirBlock(posBelow) && stateBelow.getMaterial() != Material.LAVA && !held) {
			if (setFire) {
				world.setBlockState(posCurrent, Blocks.LAVA.getDefaultState(), 3);
				this.remove();
			}

			if (!setFire) {
				if (world.isAirBlock(posCurrent.west())) {
					world.setBlockState(posCurrent.west(), Blocks.LAVA.getDefaultState(), 2);
				}

				if (world.isAirBlock(posCurrent.east())) {
					world.setBlockState(posCurrent.east(), Blocks.LAVA.getDefaultState(), 2);
				}

				if (world.isAirBlock(posCurrent.south())) {
					world.setBlockState(posCurrent.south(), Blocks.LAVA.getDefaultState(), 2);
				}

				if (world.isAirBlock(posCurrent.north())) {
					world.setBlockState(posCurrent.north(), Blocks.LAVA.getDefaultState(), 2);
				}

				world.setBlockState(posCurrent, Blocks.LAVA.getDefaultState(), 3);
				setFire = true;
			}
		}

		Vec3d motion = new Vec3d(motionX + this.accelerationX, motionY + this.accelerationY, motionZ + this.accelerationZ);
		this.setMotion(motion);

		this.move(MoverType.SELF, motion);
	}

	// TODO: Need this again? 1.14
	/*@Override
	protected void entityInit() {

	}*/

	@Override
	protected void readAdditional(CompoundNBT nbt) {
		this.lifeTimer = nbt.getInt("lifeTimer");
	}

	@Override
	protected void writeAdditional(CompoundNBT nbt) {
		nbt.putInt("lifeTimer", this.lifeTimer);
	}

	@Override
	public float getCollisionBorderSize() {
		return 1.0F;
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}