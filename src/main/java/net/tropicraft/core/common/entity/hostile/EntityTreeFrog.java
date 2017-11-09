package net.tropicraft.core.common.entity.hostile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.tropicraft.core.common.entity.EntityLand;
import net.tropicraft.core.common.entity.projectile.EntityPoisonBlot;

public class EntityTreeFrog extends EntityLand implements IMob, IRangedAttackMob {

	private static final DataParameter<Integer> TYPE = EntityDataManager.<Integer>createKey(EntityTreeFrog.class, DataSerializers.VARINT);

	public int jumpDelay = 0;
	public boolean initialSet = false;
	private int attackTime;
	private EntityAINearestAttackableTarget<EntityPlayer> hostileAI;

	public EntityTreeFrog(World world) {
		super(world);
		setSize(0.8F, 0.8F);
		this.entityCollisionReduction = 0.8F;
		this.experienceValue = 5;

	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(TYPE, Integer.valueOf(rand.nextInt(4)));
	}

	public EntityTreeFrog(World world, byte type) {
		this(world);
		this.setType(type);
	}
	
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
		if(!initialSet) {
			setType(rand.nextInt(Type.values().length));
		}
		return super.onInitialSpawn(difficulty, livingdata);
	}

	@Override
	protected void initEntityAI() {
		super.initEntityAI();

		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIAttackRanged(this, 1.0D, 60, 10.0F));
		this.tasks.addTask(2, new EntityAIWander(this, 1.0D));
		hostileAI = new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, true);
		this.targetTasks.addTask(1, hostileAI);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();

		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25);
	}

	@Override
	protected void updateAITasks() {
		super.updateAITasks();
		if(getType().equals(Type.GREEN)) {
			targetTasks.removeTask(hostileAI);
		}
		if (!getNavigator().noPath() || this.getAttackTarget() != null) {
			if (onGround || isInWater()) {
				if (jumpDelay > 0)
					jumpDelay--;
				if (jumpDelay <= 0) {
					jumpDelay = 5 + rand.nextInt(4);

					// this.jump();
					// this.motionY += -0.01D + rand.nextDouble() * 0.1D;
					double speed = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
					if (speed > 0.02D) {
						this.motionY += 0.4D;

						this.motionX *= 1.1D;
						this.motionZ *= 1.1D;
					}
				}
			}
		}

		if (attackTime > 0)
			attackTime--;
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase entity, float f) {

		if (f < 4F && !world.isRemote && attackTime == 0 && world.getDifficulty() != EnumDifficulty.PEACEFUL) {
			double d = entity.posX - posX;
			double d1 = entity.posZ - posZ;

			EntityPoisonBlot entitypoisonblot = new EntityPoisonBlot(world, this);
			entitypoisonblot.posY += 1.3999999761581421D;
			double d2 = (entity.posY + (double) entity.getEyeHeight()) - 0.20000000298023224D - entitypoisonblot.posY;
			float f1 = MathHelper.sqrt(d * d + d1 * d1) * 0.2F;
			// world.playSoundAtEntity(this, "frogspit", 1.0F, 1.0F / (rand.nextFloat() *
			// 0.4F + 0.8F));
			world.spawnEntity(entitypoisonblot);
			entitypoisonblot.setThrowableHeading(d, d2 + (double) f1, d1, 0.6F, 12F);
			attackTime = 50;

			rotationYaw = (float) ((Math.atan2(d1, d) * 180D) / 3.1415927410125732D) - 90F;
			// hasAttacked = true;
		}

	}

	public Type getType() {
		Type[] types = Type.values();
		for(Type t : types) {
			if(t.ordinal() == this.getDataManager().get(TYPE)) {
				return t;
			}
		}
		return null;
	}

	public void setType(int i) {
		this.getDataManager().set(TYPE, i);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound n) {
		n.setInteger("frogType", getType().ordinal());
		super.writeEntityToNBT(n);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound n) {
		setType(n.getInteger("frogType"));
		super.readEntityFromNBT(n);
	}

	public static enum Type {
		GREEN("green"),
		RED("red"),
		BLUE("blue"),
		YELLOW("yellow");
		
		final String color;

		private Type(String s) {
			this.color = s;
		}
		
		public String getColor() {
			return color;
		}
	}
}
