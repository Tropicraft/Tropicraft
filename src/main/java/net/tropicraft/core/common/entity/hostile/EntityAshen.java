package net.tropicraft.core.common.entity.hostile;

import java.util.Random;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public abstract class EntityAshen extends EntityMob implements IRangedAttackMob {

	private static final DataParameter<Integer> MASK_TYPE = EntityDataManager.<Integer>createKey(EntityAshen.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> ACTION_STATE = EntityDataManager.<Integer>createKey(EntityAshen.class, DataSerializers.VARINT);

	public float bobber;
	public int bobberHelper;

	/* 0 = peaceful, 1 = lost mask, 2 = hostile */
	public int actionPicker;

	public EntityLostMask maskToTrack;
	public Entity itemToTrack;

	public EntityAshen(World par1World) {
		super(par1World);
		setSize(0.5F, 1.3F);      
		setMaskType(new Random().nextInt(7));
		actionPicker = 0;
	}

	@Override
	protected void entityInit() {
		super.entityInit();        
		this.getDataManager().register(MASK_TYPE, Integer.valueOf(0));
		this.getDataManager().register(ACTION_STATE, Integer.valueOf(0));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.24D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(getAttackStrength());
	}

	protected abstract double getAttackStrength();

	public void setMaskType(int type) {
		this.dataManager.set(MASK_TYPE, Integer.valueOf(type));
	}

	public int getMaskType() {
		return ((Integer)this.dataManager.get(MASK_TYPE)).intValue();
	}

	public void setActionState(int state) {
		this.dataManager.set(ACTION_STATE, Integer.valueOf(state));
	}

	public int getActionState() {
		return ((Integer)this.dataManager.get(ACTION_STATE)).intValue();
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setShort("MaskType", (short)getMaskType());
		nbttagcompound.setShort("ActionState", (short)getActionState());
	}
	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		setMaskType(nbttagcompound.getShort("MaskType"));
		setActionState(nbttagcompound.getShort("ActionState"));
	}

	public boolean hasMask() {
		return getActionState() != 1;
	}

	public void dropMask() {
		setActionState(1);
		maskToTrack = new EntityLostMask(world, getMaskType(), posX, posY, posZ, rotationYaw);
		world.spawnEntity(maskToTrack);
	}

	public void pickupMask(EntityLostMask mask) {
		setActionState(2);
		maskToTrack = null;
		setMaskType(mask.type);
		mask.setDead();
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amt) {
		boolean wasHit = super.attackEntityFrom(source, amt);

		if (!world.isRemote) {
			if (hasMask() && wasHit && !source.equals(DamageSource.outOfWorld)) {
				dropMask();
			}
		}

		return wasHit;
	}
}
