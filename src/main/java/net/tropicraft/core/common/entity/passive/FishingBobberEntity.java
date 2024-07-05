package net.tropicraft.core.common.entity.passive;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import net.tropicraft.core.common.entity.TropicraftEntities;

import javax.annotation.Nullable;

public class FishingBobberEntity extends Entity implements IEntityWithComplexSpawn {
    private static final EntityDataAccessor<Integer> DATA_HOOKED_ENTITY = SynchedEntityData.defineId(FishingBobberEntity.class, EntityDataSerializers.INT);
    private boolean inGround;
    private int ticksInGround;
    @Nullable
    private EntityKoaBase angler;
    private int ticksInAir;
    private int ticksCatchable;
    private int ticksCaughtDelay;
    private int ticksCatchableDelay;
    private float fishApproachAngle;
    @Nullable
    public Entity caughtEntity;
    private FishingBobberEntity.State currentState = FishingBobberEntity.State.FLYING;
    private int luck;
    private int lureSpeed;

    private FishingBobberEntity(Level p_i50219_1_, EntityKoaBase koaBase, int luck, int lureSpeed) {
        super(TropicraftEntities.FISHING_BOBBER.get(), p_i50219_1_);
        noCulling = true;
        angler = koaBase;
        angler.setLure(this);
        this.luck = Math.max(0, luck);
        this.lureSpeed = Math.max(0, lureSpeed);
    }

   /*@OnlyIn(Dist.CLIENT)
   public FishingBobberEntity(Level worldIn, EntityKoaBase p_i47290_2_, double x, double y, double z) {
      this(worldIn, p_i47290_2_, 0, 0);
      this.setPos(x, y, z);
      this.xo = this.getX();
      this.yo = this.getY();
      this.zo = this.getZ();
   }*/

    public FishingBobberEntity(EntityKoaBase p_i50220_1_, Level p_i50220_2_, int p_i50220_3_, int p_i50220_4_) {
        this(p_i50220_2_, p_i50220_1_, p_i50220_3_, p_i50220_4_);
        float f = angler.getXRot();
        float f1 = angler.getYRot();
        float f2 = Mth.cos(-f1 * ((float) Math.PI / 180.0f) - (float) Math.PI);
        float f3 = Mth.sin(-f1 * ((float) Math.PI / 180.0f) - (float) Math.PI);
        float f4 = -Mth.cos(-f * ((float) Math.PI / 180.0f));
        float f5 = Mth.sin(-f * ((float) Math.PI / 180.0f));
        double d0 = angler.getX() - (double) f3 * 0.3;
        double d1 = angler.getY() + (double) angler.getEyeHeight();
        double d2 = angler.getZ() - (double) f2 * 0.3;
        moveTo(d0, d1, d2, f1, f);
        Vec3 Vector3d = new Vec3(-f3, Mth.clamp(-(f5 / f4), -5.0f, 5.0f), -f2);
        double d3 = Vector3d.length();
        Vector3d = Vector3d.multiply(0.6 / d3 + 0.5 + random.nextGaussian() * 0.0045, 0.6 / d3 + 0.5 + random.nextGaussian() * 0.0045, 0.6 / d3 + 0.5 + random.nextGaussian() * 0.0045);
        setDeltaMovement(Vector3d);
        setYRot((float) (Mth.atan2(Vector3d.x, Vector3d.z) * (double) (180.0f / (float) Math.PI)));
        setXRot((float) (Mth.atan2(Vector3d.y, Mth.sqrt((float) distanceToSqr(Vector3d))) * (double) (180.0f / (float) Math.PI)));
        yRotO = getYRot();
        xRotO = getXRot();
    }

    public FishingBobberEntity(EntityType<FishingBobberEntity> type, Level world) {
        super(type, world);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_HOOKED_ENTITY, 0);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (DATA_HOOKED_ENTITY.equals(key)) {
            int i = getEntityData().get(DATA_HOOKED_ENTITY);
            caughtEntity = i > 0 ? level().getEntity(i - 1) : null;
        }

        super.onSyncedDataUpdated(key);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean shouldRenderAtSqrDistance(double distance) {
        double d0 = 64.0;
        return distance < 4096.0;
    }

    /**
     * Inflated so it will still render when looking at koa but not fishing lure
     *
     * @return
     */
    @Override
    public AABB getBoundingBoxForCulling() {
        return getBoundingBox().inflate(8, 5.0, 8);
    }

    @Override
    public void lerpTo(double x, double y, double z, float yaw, float pitch, int posRotationIncrements) {
    }

    @Override
    public void tick() {
        super.tick();

        if (angler == null) {
            if (tickCount > 40) {
                remove(RemovalReason.DISCARDED);
            }
            return;
        }

        if (level().isClientSide || !shouldStopFishing()) {
            if (inGround) {
                ++ticksInGround;
                if (ticksInGround >= 1200) {
                    remove(RemovalReason.DISCARDED);
                    return;
                }
            }

            float f = 0.0f;
            BlockPos blockpos = blockPosition();
            FluidState ifluidstate = level().getFluidState(blockpos);
            if (ifluidstate.is(FluidTags.WATER)) {
                f = ifluidstate.getHeight(level(), blockpos);
            }

            if (currentState == State.FLYING) {
                if (caughtEntity != null) {
                    setDeltaMovement(Vec3.ZERO);
                    currentState = State.HOOKED_IN_ENTITY;
                    return;
                }

                if (f > 0.0f) {
                    setDeltaMovement(getDeltaMovement().multiply(0.3, 0.2, 0.3));
                    currentState = State.BOBBING;
                    return;
                }

                if (!level().isClientSide) {
                    checkCollision();
                }

                if (!inGround && !onGround() && !horizontalCollision) {
                    ++ticksInAir;
                } else {
                    ticksInAir = 0;
                    setDeltaMovement(Vec3.ZERO);
                }
            } else {
                if (currentState == State.HOOKED_IN_ENTITY) {
                    if (caughtEntity != null) {
                        if (caughtEntity.isRemoved()) {
                            caughtEntity = null;
                            currentState = State.FLYING;
                        } else {
                            setPos(caughtEntity.getX(), caughtEntity.getBoundingBox().minY + (double) caughtEntity.getBbHeight() * 0.8, caughtEntity.getZ());
                            setPos(getX(), getY(), getZ());
                        }
                    }

                    return;
                }

                if (currentState == State.BOBBING) {
                    Vec3 Vector3d = getDeltaMovement();
                    double d0 = getY() + Vector3d.y - (double) blockpos.getY() - (double) f;
                    if (Math.abs(d0) < 0.01) {
                        d0 += Math.signum(d0) * 0.1;
                    }

                    setDeltaMovement(Vector3d.x * 0.9, Vector3d.y - d0 * (double) random.nextFloat() * 0.2, Vector3d.z * 0.9);
                    if (!level().isClientSide && f > 0.0f) {
                        catchingFish(blockpos);
                    }
                }
            }

            if (!ifluidstate.is(FluidTags.WATER)) {
                setDeltaMovement(getDeltaMovement().add(0.0, -0.03, 0.0));
            }

            move(MoverType.SELF, getDeltaMovement());
            updateRotation();
            double d1 = 0.92;
            setDeltaMovement(getDeltaMovement().scale(0.92));
            setPos(getX(), getY(), getZ());
        }
    }

    private boolean shouldStopFishing() {
        ItemStack itemstack = angler.getMainHandItem();
        ItemStack itemstack1 = angler.getOffhandItem();
        boolean flag = itemstack.getItem() instanceof net.minecraft.world.item.FishingRodItem;
        boolean flag1 = itemstack1.getItem() instanceof net.minecraft.world.item.FishingRodItem;
        if (!angler.isRemoved() && angler.isAlive() && (flag || flag1) && !(distanceToSqr(angler) > 1024.0)) {
            return false;
        } else {
            remove(RemovalReason.DISCARDED);
            return true;
        }
    }

    private void updateRotation() {
        Vec3 Vector3d = getDeltaMovement();
        float f = Mth.sqrt((float) distanceToSqr(Vector3d));
        setYRot((float) (Mth.atan2(Vector3d.x, Vector3d.z) * (double) (180.0f / (float) Math.PI)));

        for (setXRot((float) (Mth.atan2(Vector3d.y, f) * (double) (180.0f / (float) Math.PI))); getXRot() - xRotO < -180.0f; xRotO -= 360.0f) {
        }

        while (getXRot() - xRotO >= 180.0f) {
            xRotO += 360.0f;
        }

        while (getYRot() - yRotO < -180.0f) {
            yRotO -= 360.0f;
        }

        while (getYRot() - yRotO >= 180.0f) {
            yRotO += 360.0f;
        }

        setXRot(Mth.lerp(0.2f, xRotO, getXRot()));
        setYRot(Mth.lerp(0.2f, yRotO, getYRot()));
    }

    private void checkCollision() {
        HitResult result = ProjectileUtil.getHitResultOnMoveVector(this, entity -> {
            return !entity.isSpectator() && (entity.isPickable() || entity instanceof ItemEntity) && (entity != angler || ticksInAir >= 5);
        });
        if (result.getType() != HitResult.Type.MISS) {
            if (result.getType() == HitResult.Type.ENTITY) {
                caughtEntity = ((EntityHitResult) result).getEntity();
                setHookedEntity();
            } else {
                inGround = true;
            }
        }
    }

    private void setHookedEntity() {
        getEntityData().set(DATA_HOOKED_ENTITY, caughtEntity.getId() + 1);
    }

    private void catchingFish(BlockPos p_190621_1_) {
        ServerLevel level = (ServerLevel) level();
        int i = 1;
        BlockPos blockpos = p_190621_1_.above();
        if (random.nextFloat() < 0.25f && level().isRainingAt(blockpos)) {
            ++i;
        }

        if (random.nextFloat() < 0.5f && !level().canSeeSkyFromBelowWater(blockpos)) {
            --i;
        }

        if (ticksCatchable > 0) {
            --ticksCatchable;
            if (ticksCatchable <= 0) {
                ticksCaughtDelay = 0;
                ticksCatchableDelay = 0;
            } else {
                setDeltaMovement(getDeltaMovement().add(0.0, -0.2 * (double) random.nextFloat() * (double) random.nextFloat(), 0.0));
            }
        } else if (ticksCatchableDelay > 0) {
            ticksCatchableDelay -= i;
            if (ticksCatchableDelay > 0) {
                fishApproachAngle = (float) ((double) fishApproachAngle + random.nextGaussian() * 4.0);
                float f = fishApproachAngle * ((float) Math.PI / 180.0f);
                float f1 = Mth.sin(f);
                float f2 = Mth.cos(f);
                double d0 = getX() + (double) (f1 * (float) ticksCatchableDelay * 0.1f);
                double d1 = (float) Mth.floor(getBoundingBox().minY) + 1.0f;
                double d2 = getX() + (double) (f2 * (float) ticksCatchableDelay * 0.1f);
                if (level.getFluidState(BlockPos.containing(d0, d1 - 1, d2)).is(FluidTags.WATER)) {
                    if (random.nextFloat() < 0.15f) {
                        level.sendParticles(ParticleTypes.BUBBLE, d0, d1 - (double) 0.1f, d2, 1, f1, 0.1, f2, 0.0);
                    }

                    float f3 = f1 * 0.04f;
                    float f4 = f2 * 0.04f;
                    level.sendParticles(ParticleTypes.FISHING, d0, d1, d2, 0, f4, 0.01, -f3, 1.0);
                    level.sendParticles(ParticleTypes.FISHING, d0, d1, d2, 0, -f4, 0.01, f3, 1.0);
                }
            } else {
                Vec3 Vector3d = getDeltaMovement();
                setDeltaMovement(Vector3d.x, -0.4f * Mth.nextFloat(random, 0.6f, 1.0f), Vector3d.z);
                playSound(SoundEvents.FISHING_BOBBER_SPLASH, 0.25f, 1.0f + (random.nextFloat() - random.nextFloat()) * 0.4f);
                double d3 = getBoundingBox().minY + 0.5;
                level.sendParticles(ParticleTypes.BUBBLE, getX(), d3, getZ(), (int) (1.0f + getBbWidth() * 20.0f), getBbWidth(), 0.0, getBbWidth(), 0.2f);
                level.sendParticles(ParticleTypes.FISHING, getX(), d3, getZ(), (int) (1.0f + getBbWidth() * 20.0f), getBbWidth(), 0.0, getBbWidth(), 0.2f);
                ticksCatchable = Mth.nextInt(random, 20, 40);
            }
        } else if (ticksCaughtDelay > 0) {
            ticksCaughtDelay -= i;
            float f5 = 0.15f;
            if (ticksCaughtDelay < 20) {
                f5 = (float) ((double) f5 + (double) (20 - ticksCaughtDelay) * 0.05);
            } else if (ticksCaughtDelay < 40) {
                f5 = (float) ((double) f5 + (double) (40 - ticksCaughtDelay) * 0.02);
            } else if (ticksCaughtDelay < 60) {
                f5 = (float) ((double) f5 + (double) (60 - ticksCaughtDelay) * 0.01);
            }

            if (random.nextFloat() < f5) {
                float f6 = Mth.nextFloat(random, 0.0f, 360.0f) * ((float) Math.PI / 180.0f);
                float f7 = Mth.nextFloat(random, 25.0f, 60.0f);
                double d4 = getX() + (double) (Mth.sin(f6) * f7 * 0.1f);
                double d5 = (float) Mth.floor(getBoundingBox().minY) + 1.0f;
                double d6 = getZ() + (double) (Mth.cos(f6) * f7 * 0.1f);
                if (level.getFluidState(BlockPos.containing(d4, d5 - 1.0, d6)).is(FluidTags.WATER)) {
                    level.sendParticles(ParticleTypes.SPLASH, d4, d5, d6, 2 + random.nextInt(2), 0.1f, 0.0, 0.1f, 0.0);
                }
            }

            if (ticksCaughtDelay <= 0) {
                fishApproachAngle = Mth.nextFloat(random, 0.0f, 360.0f);
                ticksCatchableDelay = Mth.nextInt(random, 20, 80);
            }
        } else {
            ticksCaughtDelay = Mth.nextInt(random, 100, 600);
            ticksCaughtDelay -= lureSpeed * 20 * 5;
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte id) {
        if (id == 31 && level().isClientSide && caughtEntity instanceof Player && ((Player) caughtEntity).isLocalPlayer()) {
            bringInHookedEntity();
        }

        super.handleEntityEvent(id);
    }

    protected void bringInHookedEntity() {
        if (angler != null) {
            Vec3 Vector3d = (new Vec3(angler.getX() - getX(), angler.getY() - getY(), angler.getZ() - getZ())).scale(0.1);
            caughtEntity.setDeltaMovement(caughtEntity.getDeltaMovement().add(Vector3d));
        }
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.NONE;
    }

    /**
     * Queues the entity for removal from the world on the next tick.
     */
    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
        if (angler != null) {
            angler.setLure(null);
        }
    }

    @Nullable
    public EntityKoaBase getAngler() {
        return angler;
    }

    @Override
    public boolean canChangeDimensions(Level oldLevel, Level newLevel) {
        return false;
    }

    @Override
    public void writeSpawnData(RegistryFriendlyByteBuf data) {
        Entity entity = getAngler();
        data.writeInt(entity == null ? -1 : entity.getId());
    }

    @Override
    public void readSpawnData(RegistryFriendlyByteBuf data) {
        int anglerID = data.readInt();
        if (anglerID != -1) {
            Entity entity = level().getEntity(anglerID);
            if (entity instanceof EntityKoaBase) {
                angler = (EntityKoaBase) entity;
            }
        }
    }

    enum State {
        FLYING,
        HOOKED_IN_ENTITY,
        BOBBING
    }

    public static AttributeSupplier.Builder createAttributes() {
        return AbstractFish.createAttributes()
                .add(Attributes.MAX_HEALTH, 10.0);
    }
}
