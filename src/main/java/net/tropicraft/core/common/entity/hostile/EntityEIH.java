package net.tropicraft.core.common.entity.hostile;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.tropicraft.core.common.entity.EntityLandHostile;
import net.tropicraft.core.registry.BlockRegistry;
import net.tropicraft.core.registry.ItemRegistry;
import net.tropicraft.core.registry.SoundRegistry;

public class EntityEIH extends EntityLandHostile implements IMob {
	//0 = sleep, 1 = aware, 2 = angry
	private static final DataParameter<Integer> STATE = EntityDataManager.<Integer>createKey(EntityEIH.class, DataSerializers.VARINT);
	public int STATE_SLEEP = 0;
	//this state was never actually used
	public int STATE_AWARE = 1;
	public int STATE_ANGRY = 2;

	public EntityEIH(World world) {
		super(world);
		setSize(1.2F, 3.8F);
		this.isImmuneToFire = true;
		this.experienceValue = 10;

	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();

		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(7);
		this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(100.0D);
	}

	@Override
	protected void initEntityAI() {
		super.initEntityAI();

		this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, false) {
			public boolean shouldExecute() {
				if (getState() != STATE_ANGRY) return false;
				return super.shouldExecute();
			}
		});

		EntityAILeapAtTarget leap = new EntityAILeapAtTarget(this, 0.4F);
		leap.setMutexBits(0);
		this.tasks.addTask(3, leap);

		this.tasks.addTask(5, new EntityAIWander(this, 0.8D) {
			public boolean shouldExecute() {
				if (getState() != STATE_ANGRY) return false;
				return super.shouldExecute();
			}
		});

		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.getDataManager().register(STATE, Integer.valueOf(0));
	}

	public int getState() {
		setSize(1.2F, 3.25F);

		return this.getDataManager().get(STATE);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if(source.equals(DamageSource.OUT_OF_WORLD)) {
			return super.attackEntityFrom(source, amount);
		}
		this.getDataManager().set(STATE, STATE_ANGRY);

		if (source.getImmediateSource() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) source.getImmediateSource();
			ItemStack heldItem = player.getHeldItemMainhand();
			if (!heldItem.isEmpty() && heldItem.getItem().canHarvestBlock(Blocks.IRON_ORE.getDefaultState())) {
				return super.attackEntityFrom(source, amount);
			} else {
				this.playSound(SoundRegistry.get("headlaughing"), this.getSoundVolume(), this.getSoundPitch());
				this.setRevengeTarget(player);
			}
		}

		return true;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);

		compound.setByte("state", (byte)getState());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);

		this.getDataManager().set(STATE, (int)compound.getByte("state"));
	}

	@Override
	protected SoundEvent getAmbientSound() {
		//aware was never properly used, so I've adjusted this code to use short noise for angry but without target
		if (getState() == STATE_ANGRY && getAttackTarget() != null) {
			return rand.nextInt(10) == 0 ? SoundRegistry.get("headmed") : null;
		} else if (getState() == STATE_ANGRY) {
			return rand.nextInt(10) == 0 ? SoundRegistry.get("headshort") : null;
		} else {
			return null;
		}
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource) {
		return SoundRegistry.get("headpain");
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundRegistry.get("headdeath");
	}

	@Override
	protected float getSoundVolume() {
		return 0.4F;
	}
	
	@Nullable
	public AxisAlignedBB getCollisionBox(Entity entityIn) {
		return this.getEntityBoundingBox();
	}

	@Nullable
	public AxisAlignedBB getCollisionBoundingBox() {
		return this.getEntityBoundingBox();
	}

    /**
     * drops the loot of this entity upon death
     */
    @Override
    protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source) {
        int numDrops = 3 + this.rand.nextInt(1 + lootingModifier);

        if (!world.isRemote) {
            this.dropItem(Item.getItemFromBlock(BlockRegistry.chunk), numDrops);
        }
    }
}
