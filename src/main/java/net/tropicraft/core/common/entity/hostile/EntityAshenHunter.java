package net.tropicraft.core.common.entity.hostile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.tropicraft.core.common.entity.passive.EntityKoaBase;
import net.tropicraft.core.common.item.armor.ItemAshenMask;
import net.tropicraft.core.registry.ItemRegistry;
import net.tropicraft.core.registry.SoundRegistry;

public class EntityAshenHunter extends EntityAshen {

	public boolean hasGTFO;

	private final AIAshenShootDart aiArrowAttack = new AIAshenShootDart(this);
	
	private static final DataParameter<Boolean> SWINGING = EntityDataManager.<Boolean>createKey(EntityAshenHunter.class, DataSerializers.BOOLEAN);

	public EntityAshenHunter(World par1World) {
		super(par1World);
		swingProgress = 0;
		setActionState(2);
		actionPicker = 2;
		hasGTFO = false;
		this.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(ItemRegistry.dagger));
		// lostSight = 0;

		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new AIAshenChaseAndPickupLostMask(this, 1.0D));
		this.tasks.addTask(3, aiArrowAttack);
		this.tasks.addTask(4, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(5, new EntityAIMeleeAndRangedAttack(this, 1.0D, 20*2, 20*10, 5F));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(7, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		// TODO: Change predicate in last parameter below?
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
		this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityKoaBase.class, true));
	}
	
	@Override
	protected void entityInit() {
		super.entityInit();        
		this.getDataManager().register(SWINGING, Boolean.valueOf(false));
	}
	
	@Override
	public void swingArm(EnumHand hand) {
		//this.setSwinging(true);
		this.isSwingInProgress = true;
	}
	
	public void setSwinging(boolean isSwinging) {
		this.dataManager.set(SWINGING, Boolean.valueOf(isSwinging));
	}

	public boolean isSwinging() {
		return ((Boolean)this.dataManager.get(SWINGING)).booleanValue();
	}

	@Override
	public void onLivingUpdate() { 
		super.onLivingUpdate();

		if ((world.getDifficulty() == EnumDifficulty.PEACEFUL) && getActionState() != 1) {
			setActionState(0);
		} else if (getActionState() != 1) {
			setActionState(actionPicker);
		}
	}

	protected SoundEvent getAmbientSound() {
		return getAttackTarget() == null ? null : SoundRegistry.get("ashenLaugh");
	}

	@Override
	public boolean attackEntityAsMob(Entity p_70652_1_) {
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(getAttackStrength());
		return super.attackEntityAsMob(p_70652_1_);
	}

	@Override
	protected double getAttackStrength() {
		if (world == null) return 0;
		switch (world.getDifficulty()) {
		case EASY:
			return 1;
		case NORMAL:
			return 2;
		case HARD:
			return 3;
		default:
			return 0;
		}
	}

	/**
	 * Attack the specified entity using a ranged attack.
	 *  
	 * @param distanceFactor How far the target is, normalized and clamped between 0.1 and 1.0
	 */
	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor)
	{
        ItemStack headGear = target.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        if (headGear != null && headGear.getItem() != null) {
            if (headGear.getItem() instanceof ItemAshenMask)
                return;
        }
		EntityTippedArrow entitytippedarrow = new EntityTippedArrow(this.world, this);
		double d0 = target.posX - this.posX;
		double d1 = target.getEntityBoundingBox().minY + (double)(target.height / 3.0F) - entitytippedarrow.posY;
		double d2 = target.posZ - this.posZ;
		double d3 = (double)MathHelper.sqrt(d0 * d0 + d2 * d2);
		entitytippedarrow.setThrowableHeading(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, (float)(14 - this.world.getDifficulty().getDifficultyId() * 4));

		entitytippedarrow.setDamage(1);
		entitytippedarrow.setKnockbackStrength(0);

		ItemStack itemstack = new ItemStack(Items.TIPPED_ARROW);
		itemstack = PotionUtils.addPotionToItemStack(itemstack, PotionType.getPotionTypeForName("slowness"));
		entitytippedarrow.setPotionEffect(itemstack);

		entitytippedarrow.addEffect(new PotionEffect(MobEffects.SLOWNESS, 100, 10));

		this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
		this.world.spawnEntity(entitytippedarrow);
	}

}
