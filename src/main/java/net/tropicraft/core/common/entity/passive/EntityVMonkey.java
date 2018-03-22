package net.tropicraft.core.common.entity.passive;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.tropicraft.core.common.drinks.Drink;
import net.tropicraft.core.common.entity.EntityLandTameable;
import net.tropicraft.core.common.entity.ai.*;
import net.tropicraft.core.common.item.ItemCocktail;
import net.tropicraft.core.registry.ItemRegistry;

import javax.annotation.Nullable;

public class EntityVMonkey extends EntityLandTameable implements IEntityFollower /* implements IMob*/ {

	public boolean isClimbing = false;

	private static final DataParameter<Boolean> IS_ANGRY = EntityDataManager.<Boolean>createKey(EntityVMonkey.class, DataSerializers.BOOLEAN);
    //TODO private static final DataParameter<Boolean> IS_CLIMBING = EntityDataManager.<Boolean>createKey(EntityVMonkey.class, DataSerializers.BOOLEAN);

    private EntityLivingBase followingEntity;

    public static final Predicate followPredicate = new Predicate<EntityLivingBase>()
    {
        public boolean apply(@Nullable EntityLivingBase ent)
        {
            if (ent == null) return false;
            if (!(ent instanceof EntityPlayer)) return false;

            EntityPlayer player = (EntityPlayer)ent;
            ItemStack heldMain = player.getHeldItemMainhand();
            ItemStack heldOff = player.getHeldItemOffhand();

            if (!heldMain.isEmpty() && heldMain.getItem() instanceof ItemCocktail) {
                if (ItemCocktail.getDrink(heldMain) == Drink.pinaColada) {
                    return true;
                }
            }

            if (!heldOff.isEmpty() && heldOff.getItem() instanceof ItemCocktail) {
                if (ItemCocktail.getDrink(heldOff) == Drink.pinaColada) {
                    return true;
                }
            }

            return false;
        }
    };

	public EntityVMonkey(World world) {
		super(world);
		followingEntity = null;
		setSize(0.8F, 0.8F);
		this.experienceValue = 4;
	}

    @Override
    protected void entityInit() {
        super.entityInit();
        this.getDataManager().register(IS_ANGRY, Boolean.valueOf(false));
        //this.getDataManager().register(IS_CLIMBING, Boolean.valueOf(false));
    }

//    public boolean isClimbing() {
//        return this.getDataManager().get(IS_CLIMBING);
//    }
//
//    public void setClimbing(boolean isClimbing) {
//        this.getDataManager().set(IS_CLIMBING, isClimbing);
//    }

    public boolean isAngry() {
		return this.getDataManager().get(IS_ANGRY);
	}

	public void setAngry(boolean isAngry) {
		this.getDataManager().set(IS_ANGRY, isAngry);
	}

	@Override
	protected void initEntityAI() {
		super.initEntityAI();
        this.aiSit = new EntityAIMonkeySit(this);
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(3, new EntityAIMonkeyFollowNearestWithCondition(this, 1.0D, 2.0F, 10.0F, followPredicate));
		this.tasks.addTask(3, new EntityAIMonkeyLeap(this, 0.4F));
		this.tasks.addTask(2, new EntityAIStealDrink(this));
		this.tasks.addTask(2, new EntityAISitAndDrink(this));
		this.tasks.addTask(4, new EntityAIMonkeySitInChair(this, this.aiSit));
        this.tasks.addTask(4, this.aiSit);
		this.tasks.addTask(6, new EntityAIAttackMelee(this, 1.0D, true));
		this.tasks.addTask(7, new EntityAIFollowOwner(this, 1.0D, 10.0F, 2.0F));
		this.tasks.addTask(8, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(9, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
		this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
		this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true, new Class[0]));
	}

    public boolean selfHoldingDrink(Drink drink) {
        ItemStack heldItem = getHeldItemMainhand();
        if (!heldItem.isEmpty() && heldItem.getItem() instanceof ItemCocktail) {
            return ItemCocktail.getDrink(heldItem) == drink;
        }
        return false;
    }

	public void resetRideCooldown() {
	    this.rideCooldown = 0;
    }

    public boolean followingHoldingPinaColada() {
        if (getFollowingEntity() == null) {
            return false;
        }
        if (!(getFollowingEntity() instanceof EntityPlayer)) return false;

        EntityLivingBase player = getFollowingEntity();
        ItemStack heldMain = player.getHeldItemMainhand();
        ItemStack heldOff = player.getHeldItemOffhand();

        if (!heldMain.isEmpty() && heldMain.getItem() instanceof ItemCocktail) {
            if (ItemCocktail.getDrink(heldMain) == Drink.pinaColada) {
                return true;
            }
        }

        if (!heldOff.isEmpty() && heldOff.getItem() instanceof ItemCocktail) {
            if (ItemCocktail.getDrink(heldOff) == Drink.pinaColada) {
                return true;
            }
        }

        return false;
    }

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();

		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3);

		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
	}

	@Override
	public EntityAgeable createChild(EntityAgeable ageable) {
		return null;
	}

	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		if (this.isTamed())
		{
			if (this.isOwner(player) && !this.world.isRemote)
			{
			    System.out.println("hello world" + ((EntityAIMonkeySit)this.aiSit).getSitting());
				this.aiSit.setSitting(!this.isSitting());
				this.isJumping = false;
				this.navigator.clearPath();
				this.setAttackTarget((EntityLivingBase)null);
				this.setAngry(false);
			}
		}
		else if (!stack.isEmpty() && stack.getItem() == ItemRegistry.cocktail)
		{
			if (!player.capabilities.isCreativeMode)
			{
				stack.shrink(1);
			}

			if (!this.world.isRemote)
			{
				if (this.rand.nextInt(3) == 0)
				{
					this.setTamed(true);
					this.navigator.clearPath();
					this.setAttackTarget((EntityLivingBase)null);
					this.aiSit.setSitting(true);
					this.setHealth(20.0F);
					this.setOwnerId(player.getUniqueID());
					this.playTameEffect(true);
					this.world.setEntityState(this, (byte)7);
				}
				else
				{
					this.playTameEffect(false);
					this.world.setEntityState(this, (byte)6);
				}
			}

			return true;
		}

		return super.processInteract(player, hand);
	}

	@Override
	public boolean isBreedingItem(ItemStack stack) {
		return false;
	}

	@Override
	protected boolean canDespawn()
	{
		return !this.isTamed() && this.ticksExisted > 2400;
	}

	@Override
	public boolean shouldAttackEntity(EntityLivingBase p_142018_1_, EntityLivingBase p_142018_2_)
	{
		if (!(p_142018_1_ instanceof EntityCreeper) && !(p_142018_1_ instanceof EntityGhast))
		{
			if (p_142018_1_ instanceof EntityVMonkey)
			{
				EntityVMonkey entitywolf = (EntityVMonkey)p_142018_1_;

				if (entitywolf.isTamed() && entitywolf.getOwner() == p_142018_2_)
				{
					return false;
				}
			}

			return p_142018_1_ instanceof EntityPlayer && p_142018_2_ instanceof EntityPlayer && !((EntityPlayer)p_142018_2_).canAttackPlayer((EntityPlayer)p_142018_1_) ? false : !(p_142018_1_ instanceof EntityHorse) || !((EntityHorse)p_142018_1_).isTame();
		}
		else
		{
			return false;
		}
	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn)
	{
		boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float)((int)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()));

		if (flag)
		{
			this.applyEnchantments(this, entityIn);
		}

		return flag;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if (this.isEntityInvulnerable(source))
		{
			return false;
		}
		else
		{
			Entity entity = source.getTrueSource();

			if (this.aiSit != null)
			{
				this.aiSit.setSitting(false);
			}

			if (entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArrow))
			{
				amount = (amount + 1.0F) / 2.0F;
			}

			return super.attackEntityFrom(source, amount);
		}
	}

    @Override
    public EntityLivingBase getFollowingEntity() {
        return this.followingEntity;
    }

    @Override
    public void setFollowingEntity(EntityLivingBase entity) {
        this.followingEntity = entity;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
		compound.setBoolean("isAngry", isAngry());
        //compound.setBoolean("isClimbing", isClimbing());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
		this.getDataManager().set(IS_ANGRY, compound.getBoolean("isAngry"));
        //this.getDataManager().set(IS_CLIMBING, compound.getBoolean("isClimbing"));
    }
}
