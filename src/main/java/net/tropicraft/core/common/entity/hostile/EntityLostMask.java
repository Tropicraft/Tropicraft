package net.tropicraft.core.common.entity.hostile;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.tropicraft.core.common.enums.AshenMasks;
import net.tropicraft.core.registry.ItemRegistry;

public class EntityLostMask extends Entity {

	//Client side, used for mask bob
	public int type = -1;
	public float bobber;
	private double launchedSpeed = 1D;
	
	private static final DataParameter<Integer> MASK_TYPE = EntityDataManager.<Integer>createKey(EntityLostMask.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> ROTATOR_X = EntityDataManager.<Integer>createKey(EntityLostMask.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> ROTATOR_Y = EntityDataManager.<Integer>createKey(EntityLostMask.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> ROTATOR_Z = EntityDataManager.<Integer>createKey(EntityLostMask.class, DataSerializers.VARINT);

	public EntityLostMask(World par1World) {
		super(par1World);
		this.setSize(1f, 1f);
	}

	/**
	 * Spawns a LostMask into the world at a given position and angle
	 * @param world World object
	 * @param color Mask type
	 * @param x X position
	 * @param y Y position
	 * @param z Z position
	 * @param angle Use the "attackers" rotationYaw
	 */
	public EntityLostMask(World world, int type, double x, double y, double z, double angle) {
		this(world);
		this.setPosition(x, y, z);		
		motionX = Math.cos(Math.toRadians(angle + 90))*launchedSpeed;
		motionZ = Math.sin(Math.toRadians(angle + 90))*launchedSpeed;
		double subAngle = MathHelper.wrapDegrees(angle);
		double subAngle2 = subAngle + (180 - subAngle)*2;
		this.rotationYaw = (float) (subAngle2);
		this.type = type;
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public boolean canBePushed() {
		return true;
	}

	@Override 
	public void onUpdate() {
		if(!world.isRemote) {
			if(this.ticksExisted == 1 && type >= 0) {
				this.setType(type);
			}
		}
		if (onGround) {
			this.motionX *= .5F;
			this.motionZ *= .5F;
			this.motionY = 0;
		}

		if (this.checkForWater(0)) {
			this.motionY =.02F;
			this.motionX *= .95F;
			this.motionZ *= .95F;
		}

		else{
			int xMod = world.rand.nextInt(10);
			int yMod = world.rand.nextInt(30);
			int zMod = world.rand.nextInt(10);
			int[] a = this.getRotator();
			a[0] += xMod;
			a[1] += yMod;
			a[2] += zMod;
			this.setRotator(a);
			motionY -= .05f;
		}
		this.move(motionX, motionY, motionZ);		
	}
	@Override
	protected void entityInit() {
		this.dataManager.register(MASK_TYPE, Integer.valueOf(0));
		this.dataManager.register(ROTATOR_X, Integer.valueOf(0));
		this.dataManager.register(ROTATOR_Y, Integer.valueOf(0));
		this.dataManager.register(ROTATOR_Z, Integer.valueOf(0));
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
		this.setType(nbt.getInteger("MaskType"));

	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {
		nbt.setInteger("MaskType", this.getType());

	}
	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
		if (this.isEntityInvulnerable(par1DamageSource)) {
			return false;
		} else {
			if (!this.isDead && !this.world.isRemote) {
				this.setDead();
				this.setBeenAttacked();
				EntityPlayer entityplayer = null;

				if (par1DamageSource.getEntity() instanceof EntityPlayer) {
					entityplayer = (EntityPlayer)par1DamageSource.getEntity();
				}

				if (entityplayer != null && entityplayer.capabilities.isCreativeMode) {
					return true;
				}

				this.dropItemStack();
			}

			return true;
		}
	}

	public void dropItemStack() {
		this.entityDropItem(new ItemStack(ItemRegistry.maskMap.get(AshenMasks.VALUES[this.getType()]), 1, getType()), 0.0F);
	}

	private void setRotator(int[] a) {
		this.dataManager.set(ROTATOR_X, Integer.valueOf(a[0]));
		this.dataManager.set(ROTATOR_Y, Integer.valueOf(a[1]));
		this.dataManager.set(ROTATOR_Z, Integer.valueOf(a[2]));
	}

	public int[] getRotator() {
		return new int[]{this.dataManager.get(ROTATOR_X).intValue(), this.dataManager.get(ROTATOR_Y).intValue(), this.dataManager.get(ROTATOR_Z).intValue()};
	}

	private void setType(int i) {
		this.dataManager.set(MASK_TYPE, Integer.valueOf(i));
	}

	public boolean checkForWater(int offset) {
		BlockPos pos = new BlockPos(posX, posY + offset, posZ);
		IBlockState state = world.getBlockState(pos);
		return state.getMaterial() == Material.WATER;
	}

	public int getMode() {
		return 0;
	}
	
	public int getType() {
		return this.dataManager.get(MASK_TYPE).intValue();
	}

	public int getDirection() {
		return 0;
	}
}