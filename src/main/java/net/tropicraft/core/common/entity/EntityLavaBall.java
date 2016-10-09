package net.tropicraft.core.common.entity;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityLavaBall extends Entity {

	public boolean setFire;
	public float size;
	public boolean held;
	public int lifeTimer;

	public EntityLavaBall(World world) {
		super(world);
		setSize(1F, 1F);
		setFire = false;
		held = false;
		size = 1;
		lifeTimer = 0;
	}

	public EntityLavaBall(World world, double i, double j, double k, double motX, double motY, double motZ) {
		super(world);
		setSize(1F, 1F);
		setFire = false;
		setLocationAndAngles(i, j, k, 0, 0);
		motionX = motX;
		motionY = motY;
		motionZ = motZ;
		size = 1;
		held = false;
		lifeTimer = 0;

	}
	public EntityLavaBall(World world, float startSize) {
		super(world);
		size = startSize;
		setSize(1F, 1F);
		setFire = false;
		held = true;
		lifeTimer = 0;
	}

	public boolean canBeCollidedWith() {
		return true;
	}

	public boolean canBePushed() {
		return true;
	}

	@SideOnly(Side.CLIENT)
	public void supahDrip() {
		float x = (float)this.posX;
		float y = (float)this.posY;
		float z = (float)this.posZ;

		if (worldObj.isRemote) {
			worldObj.spawnParticle(EnumParticleTypes.LAVA, x, y, z, motionX, -1.5F, motionZ, 0);
		}
	}

	public void onUpdate() {
		super.onUpdate();
		// System.out.println("laba ball: " + posX + " " + posY + " " + posZ);

		if (lifeTimer < 500)
			lifeTimer++;
		else
			this.setDead();

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
			if (worldObj.isRemote) {
				for (int i = 0; i < 5 + rand.nextInt(3); i++){
					supahDrip();
				}
			}
		}

		if (isCollidedHorizontally) {
			motionZ = 0;
			motionX = 0;
		}

		//TODO: Note below, these used to be tempLavaMoving - maybe they still need to be?
		int thisX = (int)Math.floor(posX);
		int thisY = (int)Math.floor(posY);
		int thisZ = (int)Math.floor(posZ);

		BlockPos posCurrent = new BlockPos(thisX, thisY, thisZ);
		BlockPos posBelow = posCurrent.down();
		IBlockState stateBelow = worldObj.getBlockState(posBelow);

		if (!worldObj.isAirBlock(posBelow) && stateBelow.getMaterial() != Material.LAVA && !held) {
			if (setFire) {
				worldObj.setBlockState(posCurrent, Blocks.LAVA.getDefaultState(), 3);
				isDead = true;
			}

			if (!setFire) {
				if (worldObj.isAirBlock(posCurrent.west())) {
					worldObj.setBlockState(posCurrent.west(), Blocks.LAVA.getDefaultState(), 2);
				}

				if (worldObj.isAirBlock(posCurrent.east())) {
					worldObj.setBlockState(posCurrent.east(), Blocks.LAVA.getDefaultState(), 2);
				}

				if (worldObj.isAirBlock(posCurrent.south())) {
					worldObj.setBlockState(posCurrent.south(), Blocks.LAVA.getDefaultState(), 2);
				}

				if (worldObj.isAirBlock(posCurrent.north())) {
					worldObj.setBlockState(posCurrent.north(), Blocks.LAVA.getDefaultState(), 2);
				}

				worldObj.setBlockState(posCurrent, Blocks.LAVA.getDefaultState(), 3);
				setFire = true;
			}
		}

		this.moveEntity(motionX, motionY, motionZ);
	}

	@Override
	protected void entityInit() {

	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
		this.lifeTimer = nbt.getInteger("lifeTimer");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {
		nbt.setInteger("lifeTimer", this.lifeTimer);
	}

}
