package net.tropicraft.entity.underdasea;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public abstract class EntityEchinodermEgg extends EntityLiving {
	public static final int HATCH_TICKS = 2*60*20; // 2 minutes

	/**
	 * Ticks left until hatching.
	 */
	private int hatchTime;

	public EntityEchinodermEgg(World par1World) {
		super(par1World);
		hatchTime = HATCH_TICKS;
		setSize(0.25f, 0.25f);
		yOffset = 0.25f;
	}
	
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(5.0D);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		hatchTime = compound.getInteger("HatchTime");
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setInteger("HatchTime", hatchTime);
	}
	
	@Override
	public void updateEntityActionState() {
		super.updateEntityActionState();
		isJumping = false;
	}

	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amt) {
		if (source == DamageSource.inWall) {
			return false;
		}
		
		return super.attackEntityFrom(source, amt);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if (worldObj.isRemote) {
			motionY = 0;
			return;
		}
		
		this.noClip = this.func_145771_j(this.posX, (this.boundingBox.minY + this.boundingBox.maxY) / 2.0D, this.posZ);
		
		if (hatchTime > 0) {
			hatchTime--;
		} else {
			setDead();
			EntityEchinoderm baby = createBaby();
			double newX = posX;
			double newY = posY;
			double newZ = posZ;
			baby.setLocationAndAngles(newX, newY, newZ, 0f, 0f);
			worldObj.spawnEntityInWorld(baby);
		}
	}

	public abstract EntityEchinoderm createBaby();
}