package net.tropicraft.entity.underdasea;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.tropicraft.registry.TCItemRegistry;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityStarfish extends EntityEchinoderm implements IEntityAdditionalSpawnData {
	public static final float BABY_WIDTH = 0.25f;
	public static final float ADULT_WIDTH = 1f;
	public static final float BABY_HEIGHT = 0.1f;
	public static final float ADULT_HEIGHT = 0.2f;
	public static final float BABY_YOFFSET = 0.03125f;
	public static final float ADULT_YOFFSET = 0.03125f;

	/** The type of starfish. */
	private StarfishType starfishType;
	
	public EntityStarfish(World world) {
		super(world);
		setStarfishType(StarfishType.values()[rand.nextInt(StarfishType.values().length)]);
		this.experienceValue = 5;
	}
	
	public EntityStarfish(World world, boolean baby) {
		super(world, baby);
		setStarfishType(StarfishType.values()[rand.nextInt(StarfishType.values().length)]);
		this.experienceValue = 5;
	}
	
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
	}
	
	public StarfishType getStarfishType() {
		return starfishType;
	}
	
	public void setStarfishType(StarfishType starfishType) {
		this.starfishType = starfishType;
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		nbt.setByte("StarfishType", (byte)getStarfishType().ordinal());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		setStarfishType(StarfishType.values()[nbt.getByte("StarfishType")]);
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		buffer.writeByte(starfishType.ordinal());
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {
		starfishType = StarfishType.values()[additionalData.readByte()];
	}

	@Override
	public EntityEchinodermEgg createEgg() {
		return new EntityStarfishEgg(worldObj, this.starfishType);
	}

	@Override
	public float getBabyWidth() {
		return BABY_WIDTH;
	}

	@Override
	public float getAdultWidth() {
		return ADULT_WIDTH;
	}

	@Override
	public float getBabyHeight() {
		return BABY_HEIGHT;
	}

	@Override
	public float getAdultHeight() {
		return ADULT_HEIGHT;
	}

	@Override
	public float getBabyYOffset() {
		return BABY_YOFFSET;
	}

	@Override
	public float getAdultYOffset() {
		return ADULT_YOFFSET;
	}

	@Override
	public boolean isPotentialMate(EntityEchinoderm other) {
		return super.isPotentialMate(other) && ((EntityStarfish)other).getStarfishType() == getStarfishType();
	}
	
	@Override
	public void onDeath(DamageSource d) {
		super.onDeath(d);
		if (!this.worldObj.isRemote) {
			this.entityDropItem(new ItemStack(TCItemRegistry.shells, 1, 4), 0);
		}
	}
}