package net.tropicraft.core.common.entity.placeable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.tropicraft.core.common.item.TropicraftItems;

import javax.annotation.Nullable;
import java.util.List;

public class ChairEntity extends FurnitureEntity {
    // TODO add drips after being wet
    // TODO make it so monkies can sit in the chair ouo
    private static final EntityDataAccessor<Byte> COMESAILAWAY = SynchedEntityData.defineId(ChairEntity.class, EntityDataSerializers.BYTE);

    /** Is any entity sitting in the chair? */
    public boolean isChairEmpty = true;

    /** Acceleration */
    private double speedMultiplier = 0.1;

    private float rotationDelta = 0;

    private static final float ROTATION_SPEED = 2.5F;
    private static final float FRICTION = 0.05F;

    public ChairEntity(EntityType<ChairEntity> type, Level world) {
        super(type, world, TropicraftItems.CHAIRS);
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void tick() {
        super.tick();
        double waterHeight = 0.0;

        if (this.getComeSailAway()) {
            waterHeight = findWaterHeight(waterHeight);
        }

        double speed = getDeltaMovement().length();
        if (this.getComeSailAway() && speed > 0.2625) {
            double forwardX = Math.cos(this.getYRot() * Mth.DEG_TO_RAD);
            double forwardZ = Math.sin(this.getYRot() * Mth.DEG_TO_RAD);

            for (int i = 0; i < 1.0D + speed * 60.0D; ++i) {
                double d5 = random.nextFloat() * 2.0F - 1.0F;
                double d6 = (double) (random.nextInt(2) * 2 - 1) * 0.7D;
                double particleX;
                double particleZ;

                if (random.nextBoolean()) {
                    particleX = getX() - forwardX * d5 * 0.8D + forwardZ * d6;
                    particleZ = getZ() - forwardZ * d5 * 0.8D - forwardX * d6;
                } else {
                    particleX = getX() + forwardX + forwardZ * d5 * 0.7D;
                    particleZ = getZ() + forwardZ - forwardX * d5 * 0.7D;
                }
                level.addParticle(ParticleTypes.SPLASH, particleX, getY() - 0.125D, particleZ, getDeltaMovement().x, getDeltaMovement().y, getDeltaMovement().z);
            }
        }

        if (!this.level.isClientSide || this.getComeSailAway()) {
            if (waterHeight < 1.0D) {
                double d2 = waterHeight * 2.0 - 1.0;
                setDeltaMovement(getDeltaMovement().add(0, 0.04 * d2, 0));
            } else {
                if (this.getDeltaMovement().y < 0.0) {
                    setDeltaMovement(getDeltaMovement().multiply(1, 0.5, 1));
                }

                setDeltaMovement(getDeltaMovement().add(0, 0.007, 0));
            }

            if (this.getComeSailAway() && this.getControllingPassenger() instanceof LivingEntity passenger) {
                float yRot = passenger.getYRot() + -passenger.xxa * 90.0F;
                double moveX = -Math.sin(yRot * Mth.DEG_TO_RAD) * this.speedMultiplier * passenger.zza * 0.05;
                double moveZ = Math.cos(yRot * Mth.DEG_TO_RAD) * this.speedMultiplier * passenger.zza * 0.05;
                setDeltaMovement(getDeltaMovement().add(moveX, 0, moveZ));
            }

            double newSpeed = getDeltaMovement().length();
            if (newSpeed > 0.45) {
                double friction = 0.45 / newSpeed;
                setDeltaMovement(getDeltaMovement().multiply(friction, 1, friction));
                newSpeed = 0.45;
            }

            if (newSpeed > speed && this.speedMultiplier < 0.45D) {
                this.speedMultiplier += (0.45D - this.speedMultiplier) / 45.0D;

                if (this.speedMultiplier > 0.45D) {
                    this.speedMultiplier = 0.45D;
                }
            } else {
                this.speedMultiplier -= (this.speedMultiplier - 0.10D) / 45.0D;

                if (this.speedMultiplier < 0.10D) {
                    this.speedMultiplier = 0.10D;
                }
            }

            if (this.getComeSailAway())
                for (int i = 0; i < 4; ++i) {
                    int x = Mth.floor(this.getX() + ((double)(i % 2) - 0.5D) * 0.8D);
                    int z = Mth.floor(this.getZ() + ((double)(i / 2) - 0.5D) * 0.8D);
                    for (int j1 = 0; j1 < 2; ++j1) {
                        int k = Mth.floor(this.getY()) + j1;
                        BlockPos pos = new BlockPos(x, k, z);
                        Block block = this.level.getBlockState(pos).getBlock();
                        if (block == Blocks.SNOW) {
                            this.level.destroyBlock(pos, true);
                            this.horizontalCollision = false;
                        } else if (block == Blocks.LILY_PAD) {
                            this.level.destroyBlock(pos, true);
                            this.horizontalCollision = false;
                        }
                    }
                }

            if (this.onGround) {
                setDeltaMovement(getDeltaMovement().multiply(0.5, 1.0, 0.5));
            }

            this.move(MoverType.SELF, getDeltaMovement());

            // This will never trigger since d10 will only ever get up to 0.45 >:D *evil laugh*
            // In other words, when come sail away, there is no stopping this sucker
            if (this.getComeSailAway()) {
                setDeltaMovement(getDeltaMovement().multiply(0.99, 0.95, 0.99));
            }

            this.setXRot(0.0F);
            float targetYRot = this.getYRot();
            double deltaX = this.xo - this.getX();
            double deltaZ = this.zo - this.getZ();

            if (deltaX * deltaX + deltaZ * deltaZ > 0.001D) {
                targetYRot = (float)(Math.atan2(deltaZ, deltaX) * 180.0D / Math.PI);
            }

            double yRotStep = Mth.wrapDegrees(targetYRot - (double)this.getYRot());
            yRotStep = Mth.clamp(yRotStep, -20.0, 20.0);

            this.setYRot((float) (this.getYRot() + yRotStep));
            this.setRot(this.getYRot(), this.getXRot());

            if (!this.level.isClientSide) {
                List<Entity> entities = this.level.getEntities(this, this.getBoundingBox().inflate(0.2, 0.0, 0.2));
                for (Entity entity : entities) {
                    if (entity != this.getControllingPassenger() && entity.isPushable() && entity instanceof ChairEntity) {
                        entity.push(this);
                    }
                }

                if (this.getControllingPassenger() != null && !this.getControllingPassenger().isAlive()) {
                    this.ejectPassengers();
                }
            }
        } else {
            this.move(MoverType.SELF, getDeltaMovement());
        }

        this.rotationDelta *= FRICTION;

        if (level.isClientSide && getControllingPassenger() instanceof Player controller) {
            this.rotationDelta += -controller.xxa * ROTATION_SPEED;
            setYRot(getYRot() + rotationDelta);
        }
    }

    private double findWaterHeight(double waterHeight) {
        AABB bounds = this.getBoundingBox();
        int steps = 5;
        for (int i = 0; i < steps; i++) {
            double start = Mth.lerp((double) i / steps, bounds.minY, bounds.maxY) - 0.125;
            double end = Mth.lerp((double) (i + 1) / steps, bounds.minY, bounds.maxY) - 0.125;
            AABB testBounds = new AABB(bounds.minX, start, bounds.minZ, bounds.maxX, end, bounds.maxZ);
            if (level.containsAnyLiquid(testBounds)) {
                waterHeight += 1.0 / (double) steps;
            }
        }
        return waterHeight;
    }

    @Override
    protected boolean preventMotion() {
        return !getComeSailAway();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(COMESAILAWAY, (byte) 0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.setComeSailAway(nbt.getBoolean("COME_SAIL_AWAY"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("COME_SAIL_AWAY", getComeSailAway());
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (!level.isClientSide && !player.isShiftKeyDown()) {
            player.startRiding(this);
            return InteractionResult.SUCCESS;
        }

        return !player.isPassengerOfSameVehicle(this) ? InteractionResult.SUCCESS : InteractionResult.PASS;
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    @Override
    public boolean isPickable() {
        return isAlive();
    }

    @Override
    @Nullable
    public Entity getControllingPassenger() {
        List<Entity> list = this.getPassengers();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public double getPassengersRidingOffset() {
        return 0.11;
    }

    @Override
    public void positionRider(Entity passenger) {
        if (this.hasPassenger(passenger)) {
            Vec3 xzOffset = new Vec3(0, 0, -0.125).yRot((float) Math.toRadians(-getYRot()));
            passenger.setPos(getX() + xzOffset.x, getY() + getPassengersRidingOffset() + passenger.getMyRidingOffset(), getZ() + xzOffset.z);
            passenger.setYRot(passenger.getYRot() + rotationDelta);
            passenger.setYBodyRot(passenger.getYRot() + rotationDelta);
        }
    }

    public void setComeSailAway(boolean sail) {
        entityData.set(COMESAILAWAY, sail ? Byte.valueOf((byte)1) : Byte.valueOf((byte)0));
    }

    public boolean getComeSailAway() {
        return entityData.get(COMESAILAWAY) == (byte)1;
    }

    @Override
    public ItemStack getPickedResult(HitResult target) {
        return new ItemStack(TropicraftItems.CHAIRS.get(DyeColor.byId(getColor().getId())).get());
    }
}
