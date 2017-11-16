package net.tropicraft.core.common.entity.underdasea.atlantoku;

import java.util.List;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.core.registry.ItemRegistry;

public class EntityHook extends Entity {
	private static final DataParameter<Integer> DATA_HOOKED_ID = EntityDataManager
			.<Integer>createKey(EntityHook.class, DataSerializers.VARINT);
	private static final DataParameter<String> DATA_ANGLER_UUID = EntityDataManager.<String>createKey(EntityHook.class,
			DataSerializers.STRING);

	private BlockPos pos;
	private Block inTile;
	private boolean inGround;
	public EntityPlayer angler;
	private int ticksInGround;
	private int ticksInAir;

	@SideOnly(Side.CLIENT)
	private double clientMotionX;
	@SideOnly(Side.CLIENT)
	private double clientMotionY;
	@SideOnly(Side.CLIENT)
	private double clientMotionZ;

	public EntityHook(World worldIn) {
		super(worldIn);
		this.pos = new BlockPos(-1, -1, -1);
		this.setSize(0.25F, 0.25F);
		this.ignoreFrustumCheck = true;
	}

	@SideOnly(Side.CLIENT)
	public EntityHook(World worldIn, double x, double y, double z, EntityPlayer anglerIn) {
		this(worldIn);
		this.setPosition(x, y, z);
		this.ignoreFrustumCheck = true;
		this.angler = anglerIn;
		// this.angler.setLure(this);

	}

	public EntityHook(World worldIn, EntityPlayer fishingPlayer) {
		super(worldIn);
		this.pos = new BlockPos(-1, -1, -1);
		this.ignoreFrustumCheck = true;
		this.angler = fishingPlayer;
		// this.angler.setLure(this);
		this.setSize(0.25F, 0.25F);
		this.setLocationAndAngles(fishingPlayer.posX, fishingPlayer.posY + (double) fishingPlayer.getEyeHeight(),
				fishingPlayer.posZ, fishingPlayer.rotationYaw, fishingPlayer.rotationPitch);
		this.posX -= (double) (MathHelper.cos(this.rotationYaw * 0.017453292F) * 0.16F);
		this.posY -= 0.10000000149011612D;
		this.posZ -= (double) (MathHelper.sin(this.rotationYaw * 0.017453292F) * 0.16F);
		this.setPosition(this.posX, this.posY, this.posZ);
		float f = 0.8F;
		this.motionX = (double) (-MathHelper.sin(this.rotationYaw * 0.017453292F)
				* MathHelper.cos(this.rotationPitch * 0.017453292F) * f);
		this.motionZ = (double) (MathHelper.cos(this.rotationYaw * 0.017453292F)
				* MathHelper.cos(this.rotationPitch * 0.017453292F) * f);
		this.motionY = (double) (-MathHelper.sin(this.rotationPitch * 0.017453292F) * 0.4F);
		this.handleHookCasting(this.motionX, this.motionY, this.motionZ, 1.5F, 1.0F);
	}

	protected void entityInit() {
		this.getDataManager().register(DATA_HOOKED_ID, Integer.valueOf(0));
		this.getDataManager().register(DATA_ANGLER_UUID, "");
	}
	
	public void setHooked(Entity ent) {
		if(ent == null) {
			this.getDataManager().set(DATA_HOOKED_ID, 0);
			return;
		}
		if(ent instanceof EntityTropicraftWaterBase) {
			EntityTropicraftWaterBase fish = (EntityTropicraftWaterBase)ent;
			fish.setHook(this);
		}
		this.getDataManager().set(DATA_HOOKED_ID, ent.getEntityId());
	}
	
	public Entity getHooked() {
		return world.getEntityByID(this.getDataManager().get(DATA_HOOKED_ID));
	}

	public void setAngler(EntityPlayer p) {
		if (p == null) {
			this.angler = null;
			this.getDataManager().set(DATA_ANGLER_UUID, "");
			return;
		}
		this.getDataManager().set(DATA_ANGLER_UUID, p.getCachedUniqueIdString());
		this.angler = p;
	}

	public EntityPlayer getAngler() {
		if(this.getDataManager().get(DATA_ANGLER_UUID).length() == 0){
			return null;
		}
		return world.getPlayerEntityByUUID(UUID.fromString(this.getDataManager().get(DATA_ANGLER_UUID)));
	}

	/**
	 * Checks if the entity is in range to render.
	 */
	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRenderDist(double distance) {
		double d0 = this.getEntityBoundingBox().getAverageEdgeLength() * 4.0D;

		if (Double.isNaN(d0)) {
			d0 = 4.0D;
		}

		d0 = d0 * 64.0D;
		return distance < d0 * d0;
	}

	public void handleHookCasting(double posX, double posY, double posZ, float p_146035_7_, float p_146035_8_) {
		float f = MathHelper.sqrt(posX * posX + posY * posY + posZ * posZ);
		posX = posX / (double) f;
		posY = posY / (double) f;
		posZ = posZ / (double) f;
		posX = posX + this.rand.nextGaussian() * 0.007499999832361937D * (double) p_146035_8_;
		posY = posY + this.rand.nextGaussian() * 0.007499999832361937D * (double) p_146035_8_;
		posZ = posZ + this.rand.nextGaussian() * 0.007499999832361937D * (double) p_146035_8_;
		posX = posX * (double) p_146035_7_;
		posY = posY * (double) p_146035_7_;
		posZ = posZ * (double) p_146035_7_;
		this.motionX = posX;
		this.motionY = posY;
		this.motionZ = posZ;
		float f1 = MathHelper.sqrt(posX * posX + posZ * posZ);
		this.rotationYaw = (float) (MathHelper.atan2(posX, posZ) * (180D / Math.PI));
		this.rotationPitch = (float) (MathHelper.atan2(posY, (double) f1) * (180D / Math.PI));
		this.prevRotationYaw = this.rotationYaw;
		this.prevRotationPitch = this.rotationPitch;
		this.ticksInGround = 0;
	}

	/**
	 * Set the position and rotation values directly without any clamping.
	 */
	@SideOnly(Side.CLIENT)
	public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch,
			int posRotationIncrements, boolean teleport) {
		this.motionX = this.clientMotionX;
		this.motionY = this.clientMotionY;
		this.motionZ = this.clientMotionZ;
	}

	/**
	 * Updates the velocity of the entity to a new value.
	 */
	@SideOnly(Side.CLIENT)
	public void setVelocity(double x, double y, double z) {
		this.motionX = x;
		this.motionY = y;
		this.motionZ = z;
		this.clientMotionX = this.motionX;
		this.clientMotionY = this.motionY;
		this.clientMotionZ = this.motionZ;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		super.onUpdate();

		if (world.isRemote) {
			this.angler = this.getAngler();
		}
		if (this.angler == null) {
			setDead();
			return;
		}

		
		if(!world.isRemote) {
			ItemStack itemstack = this.angler.getHeldItemMainhand();

			if (this.angler.isDead || !this.angler.isEntityAlive() || itemstack.isEmpty()
					|| itemstack.getItem() != ItemRegistry.fishingRod
					|| this.getDistanceSqToEntity(this.angler) > 1024.0D) {
				this.setDead();
				// this.angler.setLure(null);
				return;
			}
		
		}

		if (this.inGround) {
			if (this.world.getBlockState(this.pos).getBlock() == this.inTile) {
				++this.ticksInGround;

				if (this.ticksInGround == 1200) {
					this.setDead();
				}

				return;
			}

			this.inGround = false;
			this.motionX *= (double) (this.rand.nextFloat() * 0.2F);
			this.motionY *= (double) (this.rand.nextFloat() * 0.2F);
			this.motionZ *= (double) (this.rand.nextFloat() * 0.2F);
			this.ticksInGround = 0;
			this.ticksInAir = 0;
		} else {
			++this.ticksInAir;
		}

		if (!this.world.isRemote) {
			Vec3d vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
			Vec3d vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
			RayTraceResult result = this.world.rayTraceBlocks(vec3d1, vec3d);
			vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
			vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

			if (result != null) {
				vec3d = new Vec3d(result.hitVec.xCoord, result.hitVec.yCoord, result.hitVec.zCoord);
			}

			Entity entity = null;
			List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this,
					this.getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expandXyz(1.0D));
			double d0 = 0.0D;

			for (int j = 0; j < list.size(); ++j) {
				Entity entity1 = (Entity) list.get(j);

				if (this.canBeHooked(entity1) && (entity1 != this.angler || this.ticksInAir >= 5)) {
					AxisAlignedBB axisalignedbb1 = entity1.getEntityBoundingBox().expandXyz(0.30000001192092896D);
					RayTraceResult raytraceresult1 = axisalignedbb1.calculateIntercept(vec3d1, vec3d);

					if (raytraceresult1 != null) {
						double d1 = vec3d1.squareDistanceTo(raytraceresult1.hitVec);

						if (d1 < d0 || d0 == 0.0D) {
							entity = entity1;
							d0 = d1;
						}
					}
				}
			}

			if (entity != null) {
				result = new RayTraceResult(entity);
			}

			if (result != null) {
				if (result.entityHit != null) {
					if(this.getHooked() == null)
					this.setHooked(result.entityHit);
				} else {
					this.inGround = true;
				}
			}
		}

		if (!this.inGround) {

			if (this.world.isAABBInMaterial(this.getEntityBoundingBox().offset(0, 0.0D, 0), Material.WATER)) {
				if (this.motionY < -.05f) {
					this.motionY = -.05f;
				}
				this.motionY += 0.01D;
			} else {
				if(this.motionY > 0) {
					this.motionY *= 0.9D;
				}
				this.motionY -= 0.02D;
			}

			this.motionX *= 0.9D;
			this.motionZ *= 0.9D;

			this.move(this.motionX, this.motionY, this.motionZ);
			this.setPosition(this.posX, this.posY, this.posZ);
			
			if(this.getHooked() != null) {
				this.setPosition(this.getHooked().posX, this.getHooked().posY+(this.getHooked().height*0.5f), this.getHooked().posZ);
			}
		}
	}

	protected boolean canBeHooked(Entity e) {
		if(e instanceof EntityTropicraftWaterBase) {
			return((EntityTropicraftWaterBase)e).isFishable();
		}
		return false;
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound compound) {
		compound.setInteger("xTile", this.pos.getX());
		compound.setInteger("yTile", this.pos.getY());
		compound.setInteger("zTile", this.pos.getZ());
		ResourceLocation resourcelocation = (ResourceLocation) Block.REGISTRY.getNameForObject(this.inTile);
		compound.setString("inTile", resourcelocation == null ? "" : resourcelocation.toString());
		compound.setByte("inGround", (byte) (this.inGround ? 1 : 0));
	}

	public void readEntityFromNBT(NBTTagCompound compound) {
		this.pos = new BlockPos(compound.getInteger("xTile"), compound.getInteger("yTile"),
				compound.getInteger("zTile"));

		if (compound.hasKey("inTile", 8)) {
			this.inTile = Block.getBlockFromName(compound.getString("inTile"));
		} else {
			this.inTile = Block.getBlockById(compound.getByte("inTile") & 255);
		}

		this.inGround = compound.getByte("inGround") == 1;
	}

	@SideOnly(Side.CLIENT)
	public void handleStatusUpdate(byte id) {
		if (id == 31 && this.world.isRemote && this.getHooked() instanceof EntityPlayer
				&& ((EntityPlayer) this.getHooked()).isUser()) {
			this.bringInHookedEntity();
		}

		super.handleStatusUpdate(id);
	}

	public void bringInHookedEntity() {
		double d0 = this.angler.posX - this.posX;
		double d1 = this.angler.posY - this.posY;
		double d2 = this.angler.posZ - this.posZ;
		double d3 = (double) MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
		this.getHooked().motionX += d0 * 0.05D;
		this.getHooked().motionY += d1 * 0.05D + (double) MathHelper.sqrt(d3) * 0.08D;
		this.getHooked().motionZ += d2 * 0.05D;
	}

	public void setDead() {
		super.setDead();

		if (this.angler != null) {
			if(!world.isRemote) {
				RodLink.destroyLink(this, this.angler);
			}
		}
	}
}