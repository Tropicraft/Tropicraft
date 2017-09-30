package net.tropicraft.core.common.entity.projectile;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.core.common.entity.damage.TCDamageSource;
import net.tropicraft.core.common.entity.hostile.EntityAshen;

public class EntityDart extends Entity implements IProjectile {

	/** Number of seconds desired * number of ticks per second = number of ticks total 8) */
	private static final int MAX_HIT_TIME = 10 * 20;
	
	private static final DataParameter<Boolean> IS_HIT = EntityDataManager.<Boolean>createKey(EntityDart.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> HIT_TIME = EntityDataManager.<Integer>createKey(EntityDart.class, DataSerializers.VARINT);
	
	private int ticksInGround;
	private int ticksInAir;
	public Entity shootingEntity;
	private short dartType;
	public boolean doesDartBelongToPlayer;
	public int dartShake;
	private boolean hasRidden;
	public static Potion[] potions;
	private double damage;

	private int xTile;
	private int yTile;
	private int zTile;
	private Block inTile;
	private int inData;
	protected boolean inGround;
	protected int timeInGround;
	/** Seems to be some sort of timer for animating an arrow. */
	public int arrowShake;
	
    @SuppressWarnings("unchecked")
	private static final Predicate<Entity> DART_TARGETS = Predicates.and(new Predicate[] {EntitySelectors.NOT_SPECTATING, EntitySelectors.IS_ALIVE, new Predicate<Entity>()
    {
        public boolean apply(@Nullable Entity p_apply_1_) {
            return p_apply_1_.canBeCollidedWith();
        }
    }});

	public EntityDart(World world) {
		super(world);
		this.setSize(0.5F, 0.5F);
		this.ticksInAir = 0;
		this.ticksInGround = 0;
	}

	public EntityDart(World world, double x, double y, double z) {
		this(world);
		this.setPosition(x, y, z);
	}

	public EntityDart(World world, Entity shooter, float derp, short damage) {
		this(world);
		this.shootingEntity = shooter;
		this.dartType = damage;
		this.setLocationAndAngles(shooter.posX, shooter.posY + (double)shooter.getEyeHeight(), shooter.posZ, shooter.rotationYaw, shooter.rotationPitch);
		this.posX -= (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
		this.posY -= 0.10000000149011612D;
		this.posZ -= (double)(MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
		this.setPosition(this.posX, this.posY, this.posZ);
		this.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
		this.motionZ = (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
		this.motionY = (double)(-MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI));
		this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, derp * 1.5F, 1.0F);
	}

	/**
	 * Checks if the entity is in range to render.
	 */
	 @SideOnly(Side.CLIENT)
	@Override
	public boolean isInRangeToRenderDist(double distance) {
		 double d0 = this.getEntityBoundingBox().getAverageEdgeLength() * 10.0D;

		 if (Double.isNaN(d0)) {
			 d0 = 1.0D;
		 }

		 d0 = d0 * 64.0D * getRenderDistanceWeight();
		 return distance < d0 * d0;
	 }

	 @Override
	 public void setThrowableHeading(double x, double y, double z, float rotation1, float rotation2) {
		 // Vector normalization
		 float f2 = MathHelper.sqrt(x * x + y * y + z * z);
		 x /= (double)f2;
		 y /= (double)f2;
		 z /= (double)f2;
		 // Add some random
		 x += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double)rotation2;
		 y += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double)rotation2;
		 z += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double)rotation2;
		 x *= (double)rotation1;
		 y *= (double)rotation1;
		 z *= (double)rotation1;
		 this.motionX = x;
		 this.motionY = y;
		 this.motionZ = z;
		 float f3 = MathHelper.sqrt(x * x + z * z);
		 this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(x, z) * 180.0D / Math.PI);
		 this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(y, (double)f3) * 180.0D / Math.PI);
		 this.ticksInGround = 0;
	 }

	 /**
	  * NOTE: Mostly taken from EntityArrow.onUpdate (1.7.2)
	  */
	 @Override
	 public void onUpdate() {
		 super.onUpdate();

		 if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
		 {
			 float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
			 this.prevRotationYaw = this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));
			 this.prevRotationPitch = this.rotationPitch = (float)(MathHelper.atan2(this.motionY, (double)f) * (180D / Math.PI));
		 }

		 BlockPos blockpos = new BlockPos(this.xTile, this.yTile, this.zTile);
		 IBlockState iblockstate = this.world.getBlockState(blockpos);
		 Block block = iblockstate.getBlock();

		 if (iblockstate.getMaterial() != Material.AIR)
		 {
			 AxisAlignedBB axisalignedbb = iblockstate.getCollisionBoundingBox(this.world, blockpos);

			 if (axisalignedbb != Block.NULL_AABB && axisalignedbb.offset(blockpos).isVecInside(new Vec3d(this.posX, this.posY, this.posZ)))
			 {
				 this.inGround = true;
			 }
		 }

		 if (this.arrowShake > 0) {
			 --this.arrowShake;
		 }

		 if (this.inGround) {
			 int j = block.getMetaFromState(iblockstate);

			 if (block == this.inTile && j == this.inData) {
				 ++this.ticksInGround;

				 if (this.ticksInGround >= 1200)
				 {
					 this.setDead();
				 }
			 }
			 else
			 {
				 this.inGround = false;
				 this.motionX *= (double)(this.rand.nextFloat() * 0.2F);
				 this.motionY *= (double)(this.rand.nextFloat() * 0.2F);
				 this.motionZ *= (double)(this.rand.nextFloat() * 0.2F);
				 this.ticksInGround = 0;
				 this.ticksInAir = 0;
			 }

			 ++this.timeInGround;
		 } else {
			 ++this.ticksInAir;
			 Vec3d vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
			 Vec3d vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
			 RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d1, vec3d, false, true, false);
			 vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
			 vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

			 if (raytraceresult != null) {
				 vec3d = new Vec3d(raytraceresult.hitVec.xCoord, raytraceresult.hitVec.yCoord, raytraceresult.hitVec.zCoord);
			 }

			 Entity entity = this.findEntityOnPath(vec3d1, vec3d);

			 if (entity != null) {
				 raytraceresult = new RayTraceResult(entity);
			 }

			 if (raytraceresult != null && raytraceresult.entityHit != null && raytraceresult.entityHit instanceof EntityPlayer) {
				 EntityPlayer entityplayer = (EntityPlayer)raytraceresult.entityHit;

				 if (this.shootingEntity instanceof EntityPlayer && !((EntityPlayer)this.shootingEntity).canAttackPlayer(entityplayer)) {
					 raytraceresult = null;
				 }
			 }

			 if (raytraceresult != null) {
				 this.onHit(raytraceresult);
			 }

			 this.posX += this.motionX;
			 this.posY += this.motionY;
			 this.posZ += this.motionZ;
			 float f2 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
			 this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

			 for (this.rotationPitch = (float)(Math.atan2(this.motionY, (double)f2) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
			 {
				 ;
			 }

			 while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
			 {
				 this.prevRotationPitch += 360.0F;
			 }

			 while (this.rotationYaw - this.prevRotationYaw < -180.0F)
			 {
				 this.prevRotationYaw -= 360.0F;
			 }

			 while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
			 {
				 this.prevRotationYaw += 360.0F;
			 }

			 this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
			 this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
			 float f3 = 0.99F;
			 float f1 = 0.05F;

			 if (this.isInWater())
			 {
				 for (int l = 0; l < 4; ++l)
				 {
					 float f4 = 0.25F;
					 this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * (double)f4, this.posY - this.motionY * (double)f4, this.posZ - this.motionZ * (double)f4, this.motionX, this.motionY, this.motionZ, new int[0]);
				 }

				 f3 = 0.8F;
			 }

			 if (this.isWet())
			 {
				 this.extinguish();
			 }

			 this.motionX *= (double)f3;
			 this.motionY *= (double)f3;
			 this.motionZ *= (double)f3;
			 this.motionY -= (double)f1;
			 this.setPosition(this.posX, this.posY, this.posZ);
			 this.doBlockCollisions();
		 }
	 }

	 protected void doBlockCollisions() {
		 AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
		 BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain(axisalignedbb.minX + 0.001D, axisalignedbb.minY + 0.001D, axisalignedbb.minZ + 0.001D);
		 BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos1 = BlockPos.PooledMutableBlockPos.retain(axisalignedbb.maxX - 0.001D, axisalignedbb.maxY - 0.001D, axisalignedbb.maxZ - 0.001D);
		 BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos2 = BlockPos.PooledMutableBlockPos.retain();

		 if (this.world.isAreaLoaded(blockpos$pooledmutableblockpos, blockpos$pooledmutableblockpos1))
		 {
			 for (int i = blockpos$pooledmutableblockpos.getX(); i <= blockpos$pooledmutableblockpos1.getX(); ++i)
			 {
				 for (int j = blockpos$pooledmutableblockpos.getY(); j <= blockpos$pooledmutableblockpos1.getY(); ++j)
				 {
					 for (int k = blockpos$pooledmutableblockpos.getZ(); k <= blockpos$pooledmutableblockpos1.getZ(); ++k)
					 {
						 blockpos$pooledmutableblockpos2.setPos(i, j, k);
						 IBlockState iblockstate = this.world.getBlockState(blockpos$pooledmutableblockpos2);

						 try
						 {
							 iblockstate.getBlock().onEntityCollidedWithBlock(this.world, blockpos$pooledmutableblockpos2, iblockstate, this);
						 }
						 catch (Throwable throwable)
						 {
							 CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Colliding entity with block");
							 CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being collided with");
							 CrashReportCategory.addBlockInfo(crashreportcategory, blockpos$pooledmutableblockpos2, iblockstate);
							 throw new ReportedException(crashreport);
						 }
					 }
				 }
			 }
		 }

		 blockpos$pooledmutableblockpos.release();
		 blockpos$pooledmutableblockpos1.release();
		 blockpos$pooledmutableblockpos2.release();
	 }

	 @SideOnly(Side.CLIENT)
	 public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9)
	 {
		 this.setPosition(par1, par3, par5);
		 this.setRotation(par7, par8);
	 }

	 @Override
	 @SideOnly(Side.CLIENT)
	 public void setVelocity(double par1, double par3, double par5)
	 {
		 this.motionX = par1;
		 this.motionY = par3;
		 this.motionZ = par5;

		 if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
		 {
			 float f = MathHelper.sqrt(par1 * par1 + par5 * par5);
			 this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(par1, par5) * 180.0D / Math.PI);
			 this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(par3, (double)f) * 180.0D / Math.PI);
			 this.prevRotationPitch = this.rotationPitch;
			 this.prevRotationYaw = this.rotationYaw;
			 this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
			 this.ticksInGround = 0;
		 }
	 }

	 public void onCollideWithPlayer(EntityPlayer par1EntityPlayer) {
	 }

	 @Override
	 protected boolean canTriggerWalking() {
		 return false;
	 }

	 @Override
	 protected void entityInit() {
		 potions = new Potion[]{MobEffects.BLINDNESS, MobEffects.POISON, MobEffects.SLOWNESS, MobEffects.NAUSEA, MobEffects.HUNGER, MobEffects.WEAKNESS};
		 this.dataManager.register(IS_HIT, Boolean.valueOf(false));
		 this.dataManager.register(HIT_TIME, Integer.valueOf(MAX_HIT_TIME));
	 }

	 @Override
	 public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		 nbttagcompound.setByte("shake", (byte) dartShake);
		 nbttagcompound.setByte("inGround", (byte) (inGround ? 1 : 0));
		 nbttagcompound.setBoolean("player", doesDartBelongToPlayer);
		 nbttagcompound.setBoolean("hit", getIsHit());
		 nbttagcompound.setShort("hitTime", (short) getHitTimer());
		 nbttagcompound.setShort("dartType", dartType);
	 }

	 @Override
	 public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		 dartShake = nbttagcompound.getByte("shake") & 0xff;
		 inGround = nbttagcompound.getByte("inGround") == 1;
		 doesDartBelongToPlayer = nbttagcompound.getBoolean("player");
		 setIsHit(nbttagcompound.getBoolean("hit"));
		 setHitTimer(nbttagcompound.getShort("hitTime"));
		 dartType = nbttagcompound.getShort("dartType");
	 }

	 public void setIsHit(boolean set) {
		 this.dataManager.set(IS_HIT, Boolean.valueOf(set));
	 }

	 public boolean getIsHit() {
		 return ((Boolean)this.dataManager.get(IS_HIT)).booleanValue();
	 }

	 public void setHitTimer(int hitTime) {
		 this.dataManager.set(HIT_TIME, Integer.valueOf(hitTime));
	 }

	 public int getHitTimer() {
		 return ((Integer)this.dataManager.get(HIT_TIME)).intValue();
	 }

	 /**
	  * Stolen from @EntityArrow
	  * @param start
	  * @param end
	  * @return
	  */
	 @Nullable
	 protected Entity findEntityOnPath(Vec3d start, Vec3d end) {
		 Entity entity = null;
		 List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expandXyz(1.0D), DART_TARGETS);
		 double d0 = 0.0D;

		 for (int i = 0; i < list.size(); ++i)
		 {
			 Entity entity1 = (Entity)list.get(i);

			 if (entity1 != this.shootingEntity || this.ticksInAir >= 5)
			 {
				 AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expandXyz(0.30000001192092896D);
				 RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(start, end);

				 if (raytraceresult != null)
				 {
					 double d1 = start.squareDistanceTo(raytraceresult.hitVec);

					 if (d1 < d0 || d0 == 0.0D)
					 {
						 entity = entity1;
						 d0 = d1;
					 }
				 }
			 }
		 }

		 return entity;
	 }

	 /**
	  * Called when the arrow hits a block or an entity
	  */
	 protected void onHit(RayTraceResult raytraceResultIn) {
		 Entity entity = raytraceResultIn.entityHit;

		 if (entity != null) {
			 float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
			 int i = MathHelper.ceil((double)f * this.damage);

			 DamageSource damagesource;

			 if (this.shootingEntity == null) {
				 damagesource = TCDamageSource.causeDartDamage(this, this);
			 } else {
				 damagesource = TCDamageSource.causeDartDamage(this, this.shootingEntity);
			 }

			 if (this.isBurning() && !(entity instanceof EntityEnderman)) {
				 entity.setFire(5);
			 }

			 if (entity.attackEntityFrom(damagesource, (float)2)) {
				 if (entity instanceof EntityLivingBase) {
					 EntityLivingBase entitylivingbase = (EntityLivingBase)entity;

					 if (!this.world.isRemote) {
						 entitylivingbase.setArrowCountInEntity(entitylivingbase.getArrowCountInEntity() + 1);

						 // TODO: Re-implement if we somehow add paralysis darts back in
						 if (this.dartType == 0) {
							 //                    		System.out.println("client?: " + worldObj.isRemote);
							 //                    		//do custom paralyse
							 //                    		if (entitylivingbase instanceof EntityPlayerMP) {
							 //                    			//Player, send to client
							 //                    			
							 //                    			NBTTagCompound nbt = new NBTTagCompound();
							 //                    			nbt.setString("packetCommand", "effectAdd");
							 //                    			nbt.setInteger("effectID", this.dartType);
							 //                    			nbt.setInteger("effectTime", 100);
							 //                    			Tropicraft.eventChannel.sendTo(PacketHelper.getNBTPacket(nbt, Tropicraft.eventChannelName), (EntityPlayerMP) entitylivingbase);
							 //                    		} else {
							 //                    			//AI
							 //                    			EffectHelper.addEntry(entitylivingbase, 100);
							 //                    		}
						 } else {	
							 entitylivingbase.addPotionEffect(new PotionEffect(potions[this.dartType], MAX_HIT_TIME, 1));
						 }
					 }

					 if (this.shootingEntity != null && entitylivingbase != this.shootingEntity && entitylivingbase instanceof EntityPlayer && this.shootingEntity instanceof EntityPlayerMP) {
						 ((EntityPlayerMP)this.shootingEntity).connection.sendPacket(new SPacketChangeGameState(6, 0.0F));
					 }
				 }

				 this.playSound(SoundEvents.ENTITY_ARROW_HIT, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));

				 if (!(entity instanceof EntityEnderman)) {
					 this.setDead();
				 }
			 } else {
				 this.motionX *= -0.10000000149011612D;
				 this.motionY *= -0.10000000149011612D;
				 this.motionZ *= -0.10000000149011612D;
				 this.rotationYaw += 180.0F;
				 this.prevRotationYaw += 180.0F;
				 this.ticksInAir = 0;

				 if (!this.world.isRemote && this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ < 0.0010000000474974513D) {
					 this.setDead();
				 }
			 }
		 } else {
			 BlockPos blockpos = raytraceResultIn.getBlockPos();
			 this.xTile = blockpos.getX();
			 this.yTile = blockpos.getY();
			 this.zTile = blockpos.getZ();
			 IBlockState iblockstate = this.world.getBlockState(blockpos);
			 this.inTile = iblockstate.getBlock();
			 this.inData = this.inTile.getMetaFromState(iblockstate);
			 this.motionX = (double)((float)(raytraceResultIn.hitVec.xCoord - this.posX));
			 this.motionY = (double)((float)(raytraceResultIn.hitVec.yCoord - this.posY));
			 this.motionZ = (double)((float)(raytraceResultIn.hitVec.zCoord - this.posZ));
			 float f2 = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
			 this.posX -= this.motionX / (double)f2 * 0.05000000074505806D;
			 this.posY -= this.motionY / (double)f2 * 0.05000000074505806D;
			 this.posZ -= this.motionZ / (double)f2 * 0.05000000074505806D;
			 this.playSound(SoundEvents.ENTITY_ARROW_HIT, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
			 this.inGround = true;
			 this.arrowShake = 7;

			 if (iblockstate.getMaterial() != Material.AIR) {
				 this.inTile.onEntityCollidedWithBlock(this.world, blockpos, iblockstate, this);
			 }
		 }
	 }
}
