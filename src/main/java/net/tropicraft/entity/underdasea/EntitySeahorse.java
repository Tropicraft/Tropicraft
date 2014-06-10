package net.tropicraft.entity.underdasea;

import net.minecraft.block.material.Material;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntitySeahorse extends EntityWaterMob {

	private static final int DATAWATCHER_COLOR = 30;

	public EntitySeahorse(World par1World) {
		super(par1World);
		this.setSize(0.75F, 1.1F);
	}

	public EntitySeahorse(World par1World, int color) {
		this (par1World);
		this.setColor((byte)color);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
	}

	protected void updateEntityActionState()
	{
		if (this.fleeingTick > 0 && --this.fleeingTick == 0)
		{
			IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
			iattributeinstance.removeModifier(field_110181_i);
		}

		this.updateWanderPath();

	//	super.updateEntityActionState();
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	@Override
	protected String getLivingSound() {
		return null;
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound() {
		return null;
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	@Override
	protected String getDeathSound() {
		return null;
	}

	/**
	 * Returns the volume for the sounds this mob makes.
	 */
	@Override
	protected float getSoundVolume() {
		return 0.4F;
	}


	@Override
	public void entityInit() {
		this.dataWatcher.addObject(DATAWATCHER_COLOR, Byte.valueOf((byte)0));
		super.entityInit();
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		nbt.setByte("Color", Byte.valueOf(getColor()));
		super.writeEntityToNBT(nbt);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		setColor(nbt.getByte("Color"));
		super.readEntityFromNBT(nbt);
	}

	public byte getColor() {
		return this.dataWatcher.getWatchableObjectByte(DATAWATCHER_COLOR);
	}

	public void setColor(byte color) {
		this.dataWatcher.updateObject(DATAWATCHER_COLOR, Byte.valueOf(color));
	}

	public String getColorName() {
		switch (getColor()) {
		case 0:
			return "razz";
		case 1:
			return "blue";
		case 2:
			return "cyan";
		case 3:
			return "yellow";
		case 4:
			return "green";
		case 5:
			return "orange";
		default:
			return "razz";
		}
	}

	/**
	 * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
	 * prevent them from trampling crops
	 */
	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	/**
	 * Checks if this entity is inside water (if inWater field is true as a result of handleWaterMovement() returning
	 * true)
	 */
	@Override
	public boolean isInWater() {
		return this.worldObj.handleMaterialAcceleration(this.boundingBox.expand(0.0D, -0.6000000238418579D, 0.0D), Material.water, this);
	}

	/**
	 * Checks if the entity's current position is a valid location to spawn this entity.
	 */
	@Override
	public boolean getCanSpawnHere() {
		return this.posY > 45.0D && this.posY < 63.0D && super.getCanSpawnHere();
	}
}
