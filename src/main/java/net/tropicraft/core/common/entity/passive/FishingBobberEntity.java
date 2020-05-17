package net.tropicraft.core.common.entity.passive;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class FishingBobberEntity extends Entity {
   private static final DataParameter<Integer> DATA_HOOKED_ENTITY = EntityDataManager.createKey(FishingBobberEntity.class, DataSerializers.VARINT);
   private boolean inGround;
   private int ticksInGround;
   private final EntityKoaBase angler;
   private int ticksInAir;
   private int ticksCatchable;
   private int ticksCaughtDelay;
   private int ticksCatchableDelay;
   private float fishApproachAngle;
   public Entity caughtEntity;
   private FishingBobberEntity.State currentState = FishingBobberEntity.State.FLYING;
   private final int luck;
   private final int lureSpeed;

   private FishingBobberEntity(World p_i50219_1_, EntityKoaBase koaBase, int luck, int lureSpeed) {
      super(EntityType.FISHING_BOBBER, p_i50219_1_);
      this.ignoreFrustumCheck = true;
      this.angler = koaBase;
      this.angler.setLure(this);
      this.luck = Math.max(0, luck);
      this.lureSpeed = Math.max(0, lureSpeed);
   }

   @OnlyIn(Dist.CLIENT)
   public FishingBobberEntity(World worldIn, EntityKoaBase p_i47290_2_, double x, double y, double z) {
      this(worldIn, p_i47290_2_, 0, 0);
      this.setPosition(x, y, z);
      this.prevPosX = this.getPosX();
      this.prevPosY = this.getPosY();
      this.prevPosZ = this.getPosZ();
   }

   public FishingBobberEntity(EntityKoaBase p_i50220_1_, World p_i50220_2_, int p_i50220_3_, int p_i50220_4_) {
      this(p_i50220_2_, p_i50220_1_, p_i50220_3_, p_i50220_4_);
      float f = this.angler.rotationPitch;
      float f1 = this.angler.rotationYaw;
      float f2 = MathHelper.cos(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
      float f3 = MathHelper.sin(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
      float f4 = -MathHelper.cos(-f * ((float)Math.PI / 180F));
      float f5 = MathHelper.sin(-f * ((float)Math.PI / 180F));
      double d0 = this.angler.getPosX() - (double)f3 * 0.3D;
      double d1 = this.angler.getPosY() + (double)this.angler.getEyeHeight();
      double d2 = this.angler.getPosZ() - (double)f2 * 0.3D;
      this.setLocationAndAngles(d0, d1, d2, f1, f);
      Vec3d vec3d = new Vec3d((double)(-f3), (double)MathHelper.clamp(-(f5 / f4), -5.0F, 5.0F), (double)(-f2));
      double d3 = vec3d.length();
      vec3d = vec3d.mul(0.6D / d3 + 0.5D + this.rand.nextGaussian() * 0.0045D, 0.6D / d3 + 0.5D + this.rand.nextGaussian() * 0.0045D, 0.6D / d3 + 0.5D + this.rand.nextGaussian() * 0.0045D);
      this.setMotion(vec3d);
      this.rotationYaw = (float)(MathHelper.atan2(vec3d.x, vec3d.z) * (double)(180F / (float)Math.PI));
      this.rotationPitch = (float)(MathHelper.atan2(vec3d.y, (double)MathHelper.sqrt(getDistanceSq(vec3d))) * (double)(180F / (float)Math.PI));
      this.prevRotationYaw = this.rotationYaw;
      this.prevRotationPitch = this.rotationPitch;
   }

   protected void registerData() {
      this.getDataManager().register(DATA_HOOKED_ENTITY, 0);
   }

   public void notifyDataManagerChange(DataParameter<?> key) {
      if (DATA_HOOKED_ENTITY.equals(key)) {
         int i = this.getDataManager().get(DATA_HOOKED_ENTITY);
         this.caughtEntity = i > 0 ? this.world.getEntityByID(i - 1) : null;
      }

      super.notifyDataManagerChange(key);
   }

   /**
    * Checks if the entity is in range to render.
    */
   @OnlyIn(Dist.CLIENT)
   public boolean isInRangeToRenderDist(double distance) {
      double d0 = 64.0D;
      return distance < 4096.0D;
   }

   /**
    * Sets a target for the client to interpolate towards over the next few ticks
    */
   @OnlyIn(Dist.CLIENT)
   public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
   }

   /**
    * Called to update the entity's position/logic.
    */
   public void tick() {
      super.tick();

      if (this.angler == null) {
         if (this.ticksExisted > 40) {
            remove();
         }
         return;
      }

      if (this.angler == null) {
         this.remove();
      } else if (this.world.isRemote || !this.shouldStopFishing()) {
         if (this.inGround) {
            ++this.ticksInGround;
            if (this.ticksInGround >= 1200) {
               this.remove();
               return;
            }
         }

         float f = 0.0F;
         BlockPos blockpos = new BlockPos(this);
         IFluidState ifluidstate = this.world.getFluidState(blockpos);
         if (ifluidstate.isTagged(FluidTags.WATER)) {
            f = ifluidstate.getActualHeight(this.world, blockpos);
         }

         if (this.currentState == FishingBobberEntity.State.FLYING) {
            if (this.caughtEntity != null) {
               this.setMotion(Vec3d.ZERO);
               this.currentState = FishingBobberEntity.State.HOOKED_IN_ENTITY;
               return;
            }

            if (f > 0.0F) {
               this.setMotion(this.getMotion().mul(0.3D, 0.2D, 0.3D));
               this.currentState = FishingBobberEntity.State.BOBBING;
               return;
            }

            if (!this.world.isRemote) {
               this.checkCollision();
            }

            if (!this.inGround && !this.onGround && !this.collidedHorizontally) {
               ++this.ticksInAir;
            } else {
               this.ticksInAir = 0;
               this.setMotion(Vec3d.ZERO);
            }
         } else {
            if (this.currentState == FishingBobberEntity.State.HOOKED_IN_ENTITY) {
               if (this.caughtEntity != null) {
                  if (this.caughtEntity.removed) {
                     this.caughtEntity = null;
                     this.currentState = FishingBobberEntity.State.FLYING;
                  } else {
                     setPosition(caughtEntity.getPosX(), caughtEntity.getBoundingBox().minY + (double) caughtEntity.getHeight() * 0.8D, caughtEntity.getPosZ());
                     setPosition(getPosX(), getPosY(), getPosZ());
                  }
               }

               return;
            }

            if (this.currentState == FishingBobberEntity.State.BOBBING) {
               Vec3d vec3d = this.getMotion();
               double d0 = this.getPosY() + vec3d.y - (double)blockpos.getY() - (double)f;
               if (Math.abs(d0) < 0.01D) {
                  d0 += Math.signum(d0) * 0.1D;
               }

               this.setMotion(vec3d.x * 0.9D, vec3d.y - d0 * (double)this.rand.nextFloat() * 0.2D, vec3d.z * 0.9D);
               if (!this.world.isRemote && f > 0.0F) {
                  this.catchingFish(blockpos);
               }
            }
         }

         if (!ifluidstate.isTagged(FluidTags.WATER)) {
            this.setMotion(this.getMotion().add(0.0D, -0.03D, 0.0D));
         }

         this.move(MoverType.SELF, this.getMotion());
         this.updateRotation();
         double d1 = 0.92D;
         this.setMotion(this.getMotion().scale(0.92D));
         this.setPosition(getPosX(), getPosY(), getPosZ());
      }
   }

   private boolean shouldStopFishing() {
      ItemStack itemstack = this.angler.getHeldItemMainhand();
      ItemStack itemstack1 = this.angler.getHeldItemOffhand();
      boolean flag = itemstack.getItem() instanceof net.minecraft.item.FishingRodItem;
      boolean flag1 = itemstack1.getItem() instanceof net.minecraft.item.FishingRodItem;
      if (!this.angler.removed && this.angler.isAlive() && (flag || flag1) && !(this.getDistanceSq(this.angler) > 1024.0D)) {
         return false;
      } else {
         this.remove();
         return true;
      }
   }

   private void updateRotation() {
      Vec3d vec3d = this.getMotion();
      float f = MathHelper.sqrt(getDistanceSq(vec3d));
      this.rotationYaw = (float)(MathHelper.atan2(vec3d.x, vec3d.z) * (double)(180F / (float)Math.PI));

      for(this.rotationPitch = (float)(MathHelper.atan2(vec3d.y, (double)f) * (double)(180F / (float)Math.PI)); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
         ;
      }

      while(this.rotationPitch - this.prevRotationPitch >= 180.0F) {
         this.prevRotationPitch += 360.0F;
      }

      while(this.rotationYaw - this.prevRotationYaw < -180.0F) {
         this.prevRotationYaw -= 360.0F;
      }

      while(this.rotationYaw - this.prevRotationYaw >= 180.0F) {
         this.prevRotationYaw += 360.0F;
      }

      this.rotationPitch = MathHelper.lerp(0.2F, this.prevRotationPitch, this.rotationPitch);
      this.rotationYaw = MathHelper.lerp(0.2F, this.prevRotationYaw, this.rotationYaw);
   }

   private void checkCollision() {
      RayTraceResult raytraceresult = ProjectileHelper.rayTrace(this, this.getBoundingBox().expand(this.getMotion()).grow(1.0D), (p_213856_1_) -> {
         return !p_213856_1_.isSpectator() && (p_213856_1_.canBeCollidedWith() || p_213856_1_ instanceof ItemEntity) && (p_213856_1_ != this.angler || this.ticksInAir >= 5);
      }, RayTraceContext.BlockMode.COLLIDER, true);
      if (raytraceresult.getType() != RayTraceResult.Type.MISS) {
         if (raytraceresult.getType() == RayTraceResult.Type.ENTITY) {
            this.caughtEntity = ((EntityRayTraceResult)raytraceresult).getEntity();
            this.setHookedEntity();
         } else {
            this.inGround = true;
         }
      }

   }

   private void setHookedEntity() {
      this.getDataManager().set(DATA_HOOKED_ENTITY, this.caughtEntity.getEntityId() + 1);
   }

   private void catchingFish(BlockPos p_190621_1_) {
      ServerWorld serverworld = (ServerWorld)this.world;
      int i = 1;
      BlockPos blockpos = p_190621_1_.up();
      if (this.rand.nextFloat() < 0.25F && this.world.isRainingAt(blockpos)) {
         ++i;
      }

      if (this.rand.nextFloat() < 0.5F && !this.world.canBlockSeeSky(blockpos)) {
         --i;
      }

      if (this.ticksCatchable > 0) {
         --this.ticksCatchable;
         if (this.ticksCatchable <= 0) {
            this.ticksCaughtDelay = 0;
            this.ticksCatchableDelay = 0;
         } else {
            this.setMotion(this.getMotion().add(0.0D, -0.2D * (double)this.rand.nextFloat() * (double)this.rand.nextFloat(), 0.0D));
         }
      } else if (this.ticksCatchableDelay > 0) {
         this.ticksCatchableDelay -= i;
         if (this.ticksCatchableDelay > 0) {
            this.fishApproachAngle = (float)((double)this.fishApproachAngle + this.rand.nextGaussian() * 4.0D);
            float f = this.fishApproachAngle * ((float)Math.PI / 180F);
            float f1 = MathHelper.sin(f);
            float f2 = MathHelper.cos(f);
            double d0 = this.getPosX() + (double)(f1 * (float)this.ticksCatchableDelay * 0.1F);
            double d1 = (float)MathHelper.floor(this.getBoundingBox().minY) + 1.0F;
            double d2 = this.getPosX() + (double)(f2 * (float)this.ticksCatchableDelay * 0.1F);
            Block block = serverworld.getBlockState(new BlockPos(d0, d1 - 1.0D, d2)).getBlock();
            if (serverworld.getBlockState(new BlockPos((int)d0, (int)d1 - 1, (int)d2)).getMaterial() == net.minecraft.block.material.Material.WATER) {
               if (this.rand.nextFloat() < 0.15F) {
                  serverworld.spawnParticle(ParticleTypes.BUBBLE, d0, d1 - (double)0.1F, d2, 1, (double)f1, 0.1D, (double)f2, 0.0D);
               }

               float f3 = f1 * 0.04F;
               float f4 = f2 * 0.04F;
               serverworld.spawnParticle(ParticleTypes.FISHING, d0, d1, d2, 0, (double)f4, 0.01D, (double)(-f3), 1.0D);
               serverworld.spawnParticle(ParticleTypes.FISHING, d0, d1, d2, 0, (double)(-f4), 0.01D, (double)f3, 1.0D);
            }
         } else {
            Vec3d vec3d = this.getMotion();
            this.setMotion(vec3d.x, (double)(-0.4F * MathHelper.nextFloat(this.rand, 0.6F, 1.0F)), vec3d.z);
            this.playSound(SoundEvents.ENTITY_FISHING_BOBBER_SPLASH, 0.25F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
            double d3 = this.getBoundingBox().minY + 0.5D;
            serverworld.spawnParticle(ParticleTypes.BUBBLE, getPosX(), d3, getPosZ(), (int)(1.0F + this.getWidth() * 20.0F), this.getWidth(), 0.0D, this.getWidth(), 0.2F);
            serverworld.spawnParticle(ParticleTypes.FISHING, getPosX(), d3, getPosZ(), (int)(1.0F + this.getWidth() * 20.0F), this.getWidth(), 0.0D, this.getWidth(), 0.2F);
            this.ticksCatchable = MathHelper.nextInt(this.rand, 20, 40);
         }
      } else if (this.ticksCaughtDelay > 0) {
         this.ticksCaughtDelay -= i;
         float f5 = 0.15F;
         if (this.ticksCaughtDelay < 20) {
            f5 = (float)((double)f5 + (double)(20 - this.ticksCaughtDelay) * 0.05D);
         } else if (this.ticksCaughtDelay < 40) {
            f5 = (float)((double)f5 + (double)(40 - this.ticksCaughtDelay) * 0.02D);
         } else if (this.ticksCaughtDelay < 60) {
            f5 = (float)((double)f5 + (double)(60 - this.ticksCaughtDelay) * 0.01D);
         }

         if (this.rand.nextFloat() < f5) {
            float f6 = MathHelper.nextFloat(this.rand, 0.0F, 360.0F) * ((float)Math.PI / 180F);
            float f7 = MathHelper.nextFloat(this.rand, 25.0F, 60.0F);
            double d4 = getPosX() + (double)(MathHelper.sin(f6) * f7 * 0.1F);
            double d5 = (float)MathHelper.floor(this.getBoundingBox().minY) + 1.0F;
            double d6 = getPosZ() + (double)(MathHelper.cos(f6) * f7 * 0.1F);
            Block block1 = serverworld.getBlockState(new BlockPos(d4, d5 - 1.0D, d6)).getBlock();
            if (serverworld.getBlockState(new BlockPos(d4, d5 - 1.0D, d6)).getMaterial() == net.minecraft.block.material.Material.WATER) {
               serverworld.spawnParticle(ParticleTypes.SPLASH, d4, d5, d6, 2 + this.rand.nextInt(2), (double)0.1F, 0.0D, (double)0.1F, 0.0D);
            }
         }

         if (this.ticksCaughtDelay <= 0) {
            this.fishApproachAngle = MathHelper.nextFloat(this.rand, 0.0F, 360.0F);
            this.ticksCatchableDelay = MathHelper.nextInt(this.rand, 20, 80);
         }
      } else {
         this.ticksCaughtDelay = MathHelper.nextInt(this.rand, 100, 600);
         this.ticksCaughtDelay -= this.lureSpeed * 20 * 5;
      }

   }

   public void writeAdditional(CompoundNBT compound) {
   }

   /**
    * (abstract) Protected helper method to read subclass entity data from NBT.
    */
   public void readAdditional(CompoundNBT compound) {
   }

   /**
    * Handler for {@link World#setEntityState}
    */
   @OnlyIn(Dist.CLIENT)
   public void handleStatusUpdate(byte id) {
      if (id == 31 && this.world.isRemote && this.caughtEntity instanceof PlayerEntity && ((PlayerEntity)this.caughtEntity).isUser()) {
         this.bringInHookedEntity();
      }

      super.handleStatusUpdate(id);
   }

   protected void bringInHookedEntity() {
      if (angler != null) {
         Vec3d vec3d = (new Vec3d(angler.getPosX() - getPosX(), angler.getPosY() - getPosY(), angler.getPosZ() - getPosZ())).scale(0.1D);
         caughtEntity.setMotion(caughtEntity.getMotion().add(vec3d));
      }
   }

   /**
    * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
    * prevent them from trampling crops
    */
   protected boolean canTriggerWalking() {
      return false;
   }

   /**
    * Queues the entity for removal from the world on the next tick.
    */
   public void remove() {
      super.remove();
      if (this.angler != null) {
         this.angler.setLure(null);
      }

   }

   @Nullable
   public EntityKoaBase getAngler() {
      return this.angler;
   }

   /**
    * Returns false if this Entity is a boss, true otherwise.
    */
   public boolean isNonBoss() {
      return false;
   }

   public IPacket<?> createSpawnPacket() {
      Entity entity = this.getAngler();
      return new SSpawnObjectPacket(this, entity == null ? this.getEntityId() : entity.getEntityId());
   }

   static enum State {
      FLYING,
      HOOKED_IN_ENTITY,
      BOBBING;
   }
}