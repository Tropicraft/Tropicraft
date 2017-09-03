package net.tropicraft.core.common.entity.placeable;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.ColorHelper;
import net.tropicraft.core.registry.ItemRegistry;

public class EntityUmbrella extends Entity {

	private static final DataParameter<Integer> COLOR = EntityDataManager.<Integer>createKey(EntityChair.class, DataSerializers.VARINT);
	private static final DataParameter<Float> DAMAGE = EntityDataManager.<Float>createKey(EntityChair.class, DataSerializers.FLOAT);
	private static final DataParameter<Integer> FORWARD_DIRECTION = EntityDataManager.<Integer>createKey(EntityChair.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> TIME_SINCE_HIT = EntityDataManager.<Integer>createKey(EntityChair.class, DataSerializers.VARINT);

	private static final int DAMAGE_THRESHOLD = 40;

	private double umbrellaX;
	private double umbrellaY;
	private double umbrellaZ;
	private double umbrellaYaw;
	private double umbrellaPitch;

	@SideOnly(Side.CLIENT)
	private double velocityX;
	@SideOnly(Side.CLIENT)
	private double velocityY;
	@SideOnly(Side.CLIENT)
	private double velocityZ;

	private int idk;

	public EntityUmbrella(World world) {
		super(world);
		this.ignoreFrustumCheck = true;
		this.preventEntitySpawning = true;
		this.entityCollisionReduction = .95F;
		this.setSize(1F, 4F);
	}

	public EntityUmbrella(World world, double x, double y, double z, int color) {
		this(world);
		setPosition(x, y, z);
		motionX = 0.0D;
		motionY = 0.0D;
		motionZ = 0.0D;
		prevPosX = x;
		prevPosY = y;
		prevPosZ = z;
		setColor(color);
		idk = 4;
	}

	/**
	 * Destroy the umbrella blocks
	 */
	@Override
	public void setDead() {
		this.isDead = true;

		int i = (int)posX;
		int j = (int)posY;
		int k = (int)posZ;

		int y = j + 4;

		for (int x = i - 3; x <= i + 2; x++) {
			for (int z = k - 3; z <= k + 2; z++) {
				BlockPos pos = new BlockPos(i, y, k);
				world.setBlockToAir(pos);
				pos = null;
			}
		}
	}

	@Override
	protected void entityInit() {
		this.getDataManager().register(COLOR, Integer.valueOf(ColorHelper.DEFAULT_VALUE));
		this.getDataManager().register(DAMAGE, new Float(0));
		this.getDataManager().register(FORWARD_DIRECTION, new Integer(1));
		this.getDataManager().register(TIME_SINCE_HIT, new Integer(0));
	}

	@SideOnly(Side.CLIENT)
	public void setVelocity(double d, double d1, double d2) {
		velocityX = motionX = d;
		velocityY = motionY = d1;
		velocityZ = motionZ = d2;
	}
	
    /**
     * For vehicles, the first passenger is generally considered the controller and "drives" the vehicle. For example,
     * Pigs, Horses, and Boats are generally "steered" by the controlling passenger.
     */
    @Nullable
    public Entity getControllingPassenger() {
        List<Entity> list = this.getPassengers();
        return list.isEmpty() ? null : (Entity)list.get(0);
    }

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(this.getTimeSinceHit() > 0) {
			this.setTimeSinceHit(this.getTimeSinceHit() - 1);
		}

		if(this.getDamage() > 0) {
			this.setDamage(this.getDamage() - 1);
		}

		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;

		int i = 5;
		double d = 0.0D;

		for(int j = 0; j < i; j++) {
			AxisAlignedBB umbrellaBB = this.getEntityBoundingBox();
			double d5 = (umbrellaBB.minY + ((umbrellaBB.maxY - umbrellaBB.minY) * (double)(j + 0)) / (double)i) - 0.125D;
			double d9 = (umbrellaBB.minY + ((umbrellaBB.maxY - umbrellaBB.minY) * (double)(j + 1)) / (double)i) - 0.125D;
			AxisAlignedBB axisalignedbb = new AxisAlignedBB(umbrellaBB.minX, d5, umbrellaBB.minZ, umbrellaBB.maxX, d9, umbrellaBB.maxZ);
			if(world.isAABBInMaterial(axisalignedbb, Material.WATER)) {
				d += 1.0D / (double)i;
			}
		}

		if(world.isRemote) {
			if(idk > 0) {
				double d1 = posX + (umbrellaX - posX) / (double)idk;
				double d6 = posY + (umbrellaY - posY) / (double)idk;
				double d10 = posZ + (umbrellaZ - posZ) / (double)idk;
				double d14;
				for(d14 = umbrellaYaw - (double)rotationYaw; d14 < -180D; d14 += 360D) { }
				for(; d14 >= 180D; d14 -= 360D) { }
				rotationYaw += d14 / (double)idk;
				rotationPitch += (umbrellaPitch - (double)rotationPitch) / (double)idk;
				idk--;
				setPosition(d1, d6, d10);
				setRotation(rotationYaw, rotationPitch);
			}
		} else {
			motionX = motionY = motionZ = 0;
		}

		if(d < 1.0D)
		{
			double d3 = d * 2D - 1.0D;
			motionY += 0.039999999105930328D * d3;
		} else
		{
			if(motionY < 0.0D)
			{
				motionY /= 2D;
			}
			motionY += 0.0070000002160668373D;
		}

		if (this.onGround) {
			this.motionX = 0;
			this.motionY = 0;
			this.motionZ = 0;
		}

		this.move(this.motionX, this.motionY, this.motionZ);

		rotationPitch = 0.0F;
		double d13 = rotationYaw;
		double d16 = prevPosX - posX;
		double d17 = prevPosZ - posZ;
		if(d16 * d16 + d17 * d17 > 0.001D)
		{
			d13 = (float)((Math.atan2(d17, d16) * 180D) / 3.1415926535897931D);
		}
		double d19;
		for(d19 = d13 - (double)rotationYaw; d19 >= 180D; d19 -= 360D) { }
		for(; d19 < -180D; d19 += 360D) { }
		if(d19 > 20D)
		{
			d19 = 20D;
		}
		if(d19 < -20D)
		{
			d19 = -20D;
		}
		rotationYaw += d19;
		setRotation(rotationYaw, rotationPitch);
		List<?> list = world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(0.20000000298023224D, 0.2D, 0.20000000298023224D));
		if(list != null && list.size() > 0)
		{
			for(int j1 = 0; j1 < list.size(); j1++)
			{
				Entity entity = (Entity)list.get(j1);

				if(entity != this.getControllingPassenger() && entity.canBePushed() && (entity instanceof EntityUmbrella))
				{
					entity.applyEntityCollision(this);
				}
			}

		}
	}

	@Override
	public boolean attackEntityFrom(DamageSource damagesource, float i) {
		if (this.isEntityInvulnerable(damagesource)) {
			return false;
		}
		else if (!this.world.isRemote && !this.isDead) {
			this.setForwardDirection(-this.getForwardDirection());
			this.setTimeSinceHit(10);
			this.setDamage(this.getDamage() + i * 10.0F);
			this.setBeenAttacked();
			boolean flag = damagesource.getEntity() instanceof EntityPlayer && ((EntityPlayer)damagesource.getEntity()).capabilities.isCreativeMode;

			if (flag || this.getDamage() > DAMAGE_THRESHOLD) {
				Entity rider = this.getControllingPassenger();
				if (rider != null) {
					rider.startRiding(this);
				}

				if (!flag) {
					this.entityDropItem(new ItemStack(ItemRegistry.umbrella, 1,  getDamageFromColor()), 0.0F);
				}

				this.setDead();
			}

			return true;
		} else {
			return true;
		}
	}

//	@Override
//	@SideOnly(Side.CLIENT)
//	public void setPositionAndRotation2(double d, double d1, double d2, float f, float f1, int i) {
//		umbrellaX = d;
//		umbrellaY = d1;
//		umbrellaZ = d2;
//		umbrellaYaw = f;
//		umbrellaPitch = f1;
//		motionX = 0;
//		motionY = 0;
//		motionZ = 0;
//		idk = i + 1;
//	}

	/**
	 * @return Returns the damage value associated with the color of this umbrella
	 */
	 public int getDamageFromColor() {
		return ColorHelper.getDamageFromColor(this.getColor());
	}

	@Override
	public AxisAlignedBB getCollisionBox(Entity entity) {
		return entity.getEntityBoundingBox();
	}

	@Override
	public double getMountedYOffset() {
		return 0.0D;
	}

	@Override
	public void performHurtAnimation() {
		this.setForwardDirection(-1 * this.getForwardDirection());
		this.setTimeSinceHit(10);
		this.setDamage(this.getDamage() * 10.0F);
	}

	/**
	 * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
	 * prevent them from trampling crops
	 */
	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	@SideOnly(Side.CLIENT)
	public float getShadowSize() {
		return 2.0F;
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
		this.setColor(Integer.valueOf(nbt.getInteger("COLOR")));
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {
		nbt.setInteger("COLOR", Integer.valueOf(this.getColor()));
	}

	public void setColor(int color) {
		this.dataManager.set(COLOR, Integer.valueOf(color));
	}

	public void setColor(float red, float green, float blue) {
		this.dataManager.set(COLOR, Integer.valueOf(ColorHelper.getColor(red, green, blue)));
	}

	public int getColor() {
		return ((Integer)this.dataManager.get(COLOR)).intValue();
	}

	/**
	 * Sets the forward direction of the entity.
	 */
	public void setForwardDirection(int dir) {
		this.dataManager.set(FORWARD_DIRECTION, Integer.valueOf(dir));
	}

	/**
	 * Gets the forward direction of the entity.
	 */
	public int getForwardDirection() {
		return ((Integer)this.dataManager.get(FORWARD_DIRECTION)).intValue();
	}

	/**
	 * Sets the damage taken from the last hit.
	 */
	public void setDamage(float damageTaken) {
		this.dataManager.set(DAMAGE, Float.valueOf(damageTaken));
	}

	/**
	 * Gets the damage taken from the last hit.
	 */
	public float getDamage() {
		return ((Float)this.dataManager.get(DAMAGE)).floatValue();
	}

	/**
	 * Sets the time to count down from since the last time entity was hit.
	 */
	public void setTimeSinceHit(int timeSinceHit) {
		this.dataManager.set(TIME_SINCE_HIT, Integer.valueOf(timeSinceHit));
	}

	/**
	 * Gets the time since the last hit.
	 */
	public int getTimeSinceHit() {
		return ((Integer)this.dataManager.get(TIME_SINCE_HIT)).intValue();
	}
}
