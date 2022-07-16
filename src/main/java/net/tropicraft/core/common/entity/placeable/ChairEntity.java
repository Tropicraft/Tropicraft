package net.tropicraft.core.common.entity.placeable;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
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
        byte b0 = 5;
        double d0 = 0.0D;

        if (this.getComeSailAway()) {
            for (int i = 0; i < b0; ++i) {
                double d1 = this.getBoundingBox().minY + (this.getBoundingBox().maxY - this.getBoundingBox().minY) * (double)(i + 0) / (double)b0 - 0.125D;
                double d3 = this.getBoundingBox().minY + (this.getBoundingBox().maxY - this.getBoundingBox().minY) * (double)(i + 1) / (double)b0 - 0.125D;
                AABB axisalignedbb = new AABB(this.getBoundingBox().minX, d1, this.getBoundingBox().minZ, this.getBoundingBox().maxX, d3, this.getBoundingBox().maxZ);

                if (this.level.containsAnyLiquid(axisalignedbb)) {
                    d0 += 1.0D / (double)b0;
                }
            }
        }

        double d10 = Math.sqrt(this.getDeltaMovement().x * this.getDeltaMovement().x + this.getDeltaMovement().z * this.getDeltaMovement().z);
        double d2;
        double d4;
        int j;

        if (/*this.getComeSailAway() && */d10 > 0.26249999999999996D) {
            d2 = Math.cos((double) this.getYRot() * Math.PI / 180.0D);
            d4 = Math.sin((double) this.getYRot() * Math.PI / 180.0D);

            if (this.getComeSailAway())
                for (j = 0; (double)j < 1.0D + d10 * 60.0D; ++j) {
                    double d5 = random.nextFloat() * 2.0F - 1.0F;
                    double d6 = (double)(random.nextInt(2) * 2 - 1) * 0.7D;
                    double particleX;
                    double particleZ;

                    if (random.nextBoolean()) {
                        particleX = getX() - d2 * d5 * 0.8D + d4 * d6;
                        particleZ = getZ() - d4 * d5 * 0.8D - d2 * d6;
                    } else {
                        particleX = getX() + d2 + d4 * d5 * 0.7D;
                        particleZ = getZ() + d4 - d2 * d5 * 0.7D;
                    }
                    level.addParticle(ParticleTypes.SPLASH, particleX, getY() - 0.125D, particleZ, getDeltaMovement().x, getDeltaMovement().y, getDeltaMovement().z);
                }
        }

        double d11;
        double d12;

        if (!this.level.isClientSide || this.getComeSailAway()) {
            if (d0 < 1.0D) {
                d2 = d0 * 2.0D - 1.0D;
                setDeltaMovement(getDeltaMovement().add(0, 0.03999999910593033D * d2, 0));
            } else {
                if (this.getDeltaMovement().y < 0.0D) {
                    setDeltaMovement(getDeltaMovement().multiply(1, 0.5, 1));
                }

                setDeltaMovement(getDeltaMovement().add(0, 0.007000000216066837D, 0));
            }

            if (this.getComeSailAway() && this.getControllingPassenger() != null && this.getControllingPassenger() instanceof LivingEntity) {
                LivingEntity entitylivingbase = (LivingEntity)this.getControllingPassenger();
                float f = this.getControllingPassenger().getYRot() + -entitylivingbase.xxa * 90.0F;
                double moveX = -Math.sin((double)(f * (float)Math.PI / 180.0F)) * this.speedMultiplier * (double)entitylivingbase.zza * 0.05000000074505806D;
                double moveZ = Math.cos((double)(f * (float)Math.PI / 180.0F)) * this.speedMultiplier * (double)entitylivingbase.zza * 0.05000000074505806D;
                setDeltaMovement(getDeltaMovement().add(moveX, 0, moveZ));
            }

            d2 = Math.sqrt(this.getDeltaMovement().x * this.getDeltaMovement().x + this.getDeltaMovement().z * this.getDeltaMovement().z);

            if (d2 > 0.45D) {
                d4 = 0.45D / d2;
                setDeltaMovement(getDeltaMovement().multiply(d4, 1, d4));
                d2 = 0.45D;
            }

            if (d2 > d10 && this.speedMultiplier < 0.45D) {
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

            int l;

            if (this.getComeSailAway())
                for (l = 0; l < 4; ++l) {
                    int i1 = Mth.floor(this.getX() + ((double)(l % 2) - 0.5D) * 0.8D);
                    j = Mth.floor(this.getZ() + ((double)(l / 2) - 0.5D) * 0.8D);

                    for (int j1 = 0; j1 < 2; ++j1) {
                        int k = Mth.floor(this.getY()) + j1;
                        BlockPos pos = new BlockPos(i1, k, j);
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

            if (this.getComeSailAway() && this.onGround) {
                setDeltaMovement(getDeltaMovement().multiply(0.5, 0.5, 0.5));
            } else if (this.onGround) {
                setDeltaMovement(Vec3.ZERO);
            }

            this.move(MoverType.SELF, getDeltaMovement());

            // This will never trigger since d10 will only ever get up to 0.45 >:D *evil laugh*
            // In other words, when come sail away, there is no stopping this sucker
            if (this.getComeSailAway()) {
                setDeltaMovement(getDeltaMovement().multiply(0.9900000095367432D, 0.949999988079071D, 0.9900000095367432D));
            }

            this.setXRot(0.0F);
            d4 = this.getYRot();
            d11 = this.xo - this.getX();
            d12 = this.zo - this.getZ();

            if (d11 * d11 + d12 * d12 > 0.001D) {
                d4 = (double)((float)(Math.atan2(d12, d11) * 180.0D / Math.PI));
            }

            double d7 = Mth.wrapDegrees(d4 - (double)this.getYRot());

            if (d7 > 20.0D) {
                d7 = 20.0D;
            }

            if (d7 < -20.0D) {
                d7 = -20.0D;
            }

            this.setYRot((float)((double)this.getYRot() + d7));
            this.setRot(this.getYRot(), this.getXRot());

            if (!this.level.isClientSide) {
                List<?> list = this.level.getEntities(this, this.getBoundingBox().inflate(0.20000000298023224D, 0.0D, 0.20000000298023224D));

                if (list != null && !list.isEmpty()) {
                    for (int k1 = 0; k1 < list.size(); ++k1) {
                        Entity entity = (Entity)list.get(k1);

                        if (entity != this.getControllingPassenger() && entity.isPushable() && entity instanceof ChairEntity) {
                            entity.push(this);
                        }
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
        Entity rider = getControllingPassenger();
        if (level.isClientSide && rider instanceof Player) {
            Player controller = (Player) rider;
            this.rotationDelta += -controller.xxa * ROTATION_SPEED;
            setYRot(getYRot() + rotationDelta);
        }
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
