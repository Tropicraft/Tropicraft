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
import net.tropicraft.core.common.item.TropicraftItems;

import javax.annotation.Nullable;
import java.util.List;

public class ChairEntity extends FurnitureEntity {
    // TODO add drips after being wet
    // TODO make it so monkies can sit in the chair ouo
    private static final EntityDataAccessor<Byte> COMESAILAWAY = SynchedEntityData.defineId(ChairEntity.class, EntityDataSerializers.BYTE);

    /**
     * Is any entity sitting in the chair?
     */
    public boolean isChairEmpty = true;

    /**
     * Acceleration
     */
    private double speedMultiplier = 0.1;

    private float rotationDelta = 0;

    private static final float ROTATION_SPEED = 2.5f;
    private static final float FRICTION = 0.05f;

    public ChairEntity(EntityType<ChairEntity> type, Level world) {
        super(type, world, TropicraftItems.CHAIRS);
    }

    @Override
    public void tick() {
        super.tick();
        double waterHeight = 0.0;

        if (getComeSailAway()) {
            waterHeight = findWaterHeight(waterHeight);
        }

        double speed = getDeltaMovement().length();
        if (getComeSailAway() && speed > 0.2625) {
            double forwardX = Math.cos(getYRot() * Mth.DEG_TO_RAD);
            double forwardZ = Math.sin(getYRot() * Mth.DEG_TO_RAD);

            for (int i = 0; i < 1.0 + speed * 60.0; ++i) {
                double d5 = random.nextFloat() * 2.0f - 1.0f;
                double d6 = (double) (random.nextInt(2) * 2 - 1) * 0.7;
                double particleX;
                double particleZ;

                if (random.nextBoolean()) {
                    particleX = getX() - forwardX * d5 * 0.8 + forwardZ * d6;
                    particleZ = getZ() - forwardZ * d5 * 0.8 - forwardX * d6;
                } else {
                    particleX = getX() + forwardX + forwardZ * d5 * 0.7;
                    particleZ = getZ() + forwardZ - forwardX * d5 * 0.7;
                }
                level().addParticle(ParticleTypes.SPLASH, particleX, getY() - 0.125, particleZ, getDeltaMovement().x, getDeltaMovement().y, getDeltaMovement().z);
            }
        }

        LivingEntity passenger = getControllingPassenger();
        if (!level().isClientSide || getComeSailAway()) {
            if (waterHeight < 1.0) {
                double d2 = waterHeight * 2.0 - 1.0;
                setDeltaMovement(getDeltaMovement().add(0, 0.04 * d2, 0));
            } else {
                if (getDeltaMovement().y < 0.0) {
                    setDeltaMovement(getDeltaMovement().multiply(1, 0.5, 1));
                }

                setDeltaMovement(getDeltaMovement().add(0, 0.007, 0));
            }

            if (getComeSailAway() && passenger != null) {
                float yRot = passenger.getYRot() + -passenger.xxa * 90.0f;
                double moveX = -Math.sin(yRot * Mth.DEG_TO_RAD) * speedMultiplier * passenger.zza * 0.05;
                double moveZ = Math.cos(yRot * Mth.DEG_TO_RAD) * speedMultiplier * passenger.zza * 0.05;
                setDeltaMovement(getDeltaMovement().add(moveX, 0, moveZ));
            }

            double newSpeed = getDeltaMovement().length();
            if (newSpeed > 0.45) {
                double friction = 0.45 / newSpeed;
                setDeltaMovement(getDeltaMovement().multiply(friction, 1, friction));
                newSpeed = 0.45;
            }

            if (newSpeed > speed && speedMultiplier < 0.45) {
                speedMultiplier += (0.45 - speedMultiplier) / 45.0;

                if (speedMultiplier > 0.45) {
                    speedMultiplier = 0.45;
                }
            } else {
                speedMultiplier -= (speedMultiplier - 0.10) / 45.0;

                if (speedMultiplier < 0.10) {
                    speedMultiplier = 0.10;
                }
            }

            if (getComeSailAway())
                for (int i = 0; i < 4; ++i) {
                    int x = Mth.floor(getX() + ((double) (i % 2) - 0.5) * 0.8);
                    int z = Mth.floor(getZ() + ((double) (i / 2) - 0.5) * 0.8);
                    for (int j1 = 0; j1 < 2; ++j1) {
                        int k = Mth.floor(getY()) + j1;
                        BlockPos pos = new BlockPos(x, k, z);
                        Block block = level().getBlockState(pos).getBlock();
                        if (block == Blocks.SNOW) {
                            level().destroyBlock(pos, true);
                            horizontalCollision = false;
                        } else if (block == Blocks.LILY_PAD) {
                            level().destroyBlock(pos, true);
                            horizontalCollision = false;
                        }
                    }
                }

            if (onGround()) {
                setDeltaMovement(getDeltaMovement().multiply(0.5, 1.0, 0.5));
            }

            move(MoverType.SELF, getDeltaMovement());

            // This will never trigger since d10 will only ever get up to 0.45 >:D *evil laugh*
            // In other words, when come sail away, there is no stopping this sucker
            if (getComeSailAway()) {
                setDeltaMovement(getDeltaMovement().multiply(0.99, 0.95, 0.99));
            }

            setXRot(0.0f);
            float targetYRot = getYRot();
            double deltaX = xo - getX();
            double deltaZ = zo - getZ();

            if (deltaX * deltaX + deltaZ * deltaZ > 0.001) {
                targetYRot = (float) (Math.atan2(deltaZ, deltaX) * 180.0 / Math.PI);
            }

            double yRotStep = Mth.wrapDegrees(targetYRot - (double) getYRot());
            yRotStep = Mth.clamp(yRotStep, -20.0, 20.0);

            setYRot((float) (getYRot() + yRotStep));
            setRot(getYRot(), getXRot());

            if (!level().isClientSide) {
                List<Entity> entities = level().getEntities(this, getBoundingBox().inflate(0.2, 0.0, 0.2));
                for (Entity entity : entities) {
                    if (entity != passenger && entity.isPushable() && entity instanceof ChairEntity) {
                        entity.push(this);
                    }
                }

                if (passenger != null && !passenger.isAlive()) {
                    ejectPassengers();
                }
            }
        } else {
            move(MoverType.SELF, getDeltaMovement());
        }

        rotationDelta *= FRICTION;

        if (level().isClientSide && passenger instanceof Player controller) {
            rotationDelta += -controller.xxa * ROTATION_SPEED;
            setYRot(getYRot() + rotationDelta);
        }
    }

    private double findWaterHeight(double waterHeight) {
        AABB bounds = getBoundingBox();
        int steps = 5;
        for (int i = 0; i < steps; i++) {
            double start = Mth.lerp((double) i / steps, bounds.minY, bounds.maxY) - 0.125;
            double end = Mth.lerp((double) (i + 1) / steps, bounds.minY, bounds.maxY) - 0.125;
            AABB testBounds = new AABB(bounds.minX, start, bounds.minZ, bounds.maxX, end, bounds.maxZ);
            if (level().containsAnyLiquid(testBounds)) {
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
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(COMESAILAWAY, (byte) 0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        setComeSailAway(nbt.getBoolean("COME_SAIL_AWAY"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("COME_SAIL_AWAY", getComeSailAway());
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (invulnerablityCheck(player, hand) == InteractionResult.SUCCESS) {
            return InteractionResult.SUCCESS;
        } else if (!level().isClientSide && !player.isShiftKeyDown()) {
            player.startRiding(this);
            return InteractionResult.SUCCESS;
        }

        return !player.isPassengerOfSameVehicle(this) ? InteractionResult.SUCCESS : InteractionResult.PASS;
    }

    @Override
    public boolean isPickable() {
        return isAlive();
    }

    @Override
    @Nullable
    public LivingEntity getControllingPassenger() {
        return getFirstPassenger() instanceof LivingEntity passenger ? passenger : null;
    }

    @Override
    protected void positionRider(Entity passenger, Entity.MoveFunction move) {
        super.positionRider(passenger, move);
        passenger.setYRot(passenger.getYRot() + rotationDelta);
        passenger.setYHeadRot(passenger.getYHeadRot() + rotationDelta);
    }

    public void setComeSailAway(boolean sail) {
        entityData.set(COMESAILAWAY, sail ? Byte.valueOf((byte) 1) : Byte.valueOf((byte) 0));
    }

    public boolean getComeSailAway() {
        return entityData.get(COMESAILAWAY) == (byte) 1;
    }

    @Override
    public ItemStack getPickedResult(HitResult target) {
        return new ItemStack(TropicraftItems.CHAIRS.get(DyeColor.byId(getColor().getId())).get());
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        return getBoundingBox().expandTowards(0.0, 1.0, 0.0);
    }
}
