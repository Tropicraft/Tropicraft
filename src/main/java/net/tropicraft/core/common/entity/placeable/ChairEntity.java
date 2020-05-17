package net.tropicraft.core.common.entity.placeable;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.tropicraft.core.common.item.TropicraftItems;

import javax.annotation.Nullable;
import java.util.List;

public class ChairEntity extends FurnitureEntity {
    // TODO add drips after being wet
    // TODO make it so monkies can sit in the chair ouo
    private static final DataParameter<Byte> COMESAILAWAY = EntityDataManager.createKey(ChairEntity.class, DataSerializers.BYTE);
    
    /** Is any entity sitting in the chair? */
    public boolean isChairEmpty = true;

    /** Acceleration */
    private double speedMultiplier = 0.1;

    public ChairEntity(EntityType<ChairEntity> type, World world) {
        super(type, world, TropicraftItems.CHAIRS);
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox() {
        return this.getBoundingBox();
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
                AxisAlignedBB axisalignedbb = new AxisAlignedBB(this.getBoundingBox().minX, d1, this.getBoundingBox().minZ, this.getBoundingBox().maxX, d3, this.getBoundingBox().maxZ);

                if (this.world.isMaterialInBB(axisalignedbb, Material.WATER)) {
                    d0 += 1.0D / (double)b0;
                }
            }
        }

        double d10 = Math.sqrt(this.getMotion().x * this.getMotion().x + this.getMotion().z * this.getMotion().z);
        double d2;
        double d4;
        int j;

        if (/*this.getComeSailAway() && */d10 > 0.26249999999999996D) {
            d2 = Math.cos((double) this.rotationYaw * Math.PI / 180.0D);
            d4 = Math.sin((double) this.rotationYaw * Math.PI / 180.0D);

            if (this.getComeSailAway())
                for (j = 0; (double)j < 1.0D + d10 * 60.0D; ++j) {
                    double d5 = rand.nextFloat() * 2.0F - 1.0F;
                    double d6 = (double)(rand.nextInt(2) * 2 - 1) * 0.7D;
                    double particleX;
                    double particleZ;

                    if (rand.nextBoolean()) {
                        particleX = getPosX() - d2 * d5 * 0.8D + d4 * d6;
                        particleZ = getPosZ() - d4 * d5 * 0.8D - d2 * d6;
                    } else {
                        particleX = getPosX() + d2 + d4 * d5 * 0.7D;
                        particleZ = getPosZ() + d4 - d2 * d5 * 0.7D;
                    }
                    world.addParticle(ParticleTypes.SPLASH, particleX, getPosY() - 0.125D, particleZ, getMotion().x, getMotion().y, getMotion().z);
                }
        }

        double d11;
        double d12;

        if (!this.world.isRemote || this.getComeSailAway()) {
            if (d0 < 1.0D) {
                d2 = d0 * 2.0D - 1.0D;
                setMotion(getMotion().add(0, 0.03999999910593033D * d2, 0));
            } else {
                if (this.getMotion().y < 0.0D) {
                    setMotion(getMotion().mul(1, 0.5, 1));
                }

                setMotion(getMotion().add(0, 0.007000000216066837D, 0));
            }

            if (this.getComeSailAway() && this.getControllingPassenger() != null && this.getControllingPassenger() instanceof LivingEntity) {
                LivingEntity entitylivingbase = (LivingEntity)this.getControllingPassenger();
                float f = this.getControllingPassenger().rotationYaw + -entitylivingbase.moveStrafing * 90.0F;
                double moveX = -Math.sin((double)(f * (float)Math.PI / 180.0F)) * this.speedMultiplier * (double)entitylivingbase.moveForward * 0.05000000074505806D;
                double moveZ = Math.cos((double)(f * (float)Math.PI / 180.0F)) * this.speedMultiplier * (double)entitylivingbase.moveForward * 0.05000000074505806D;
                setMotion(getMotion().add(moveX, 0, moveZ));
            }

            d2 = Math.sqrt(this.getMotion().x * this.getMotion().x + this.getMotion().z * this.getMotion().z);

            if (d2 > 0.45D) {
                d4 = 0.45D / d2;
                setMotion(getMotion().mul(d4, 1, d4));
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
                    int i1 = MathHelper.floor(this.getPosX() + ((double)(l % 2) - 0.5D) * 0.8D);
                    j = MathHelper.floor(this.getPosZ() + ((double)(l / 2) - 0.5D) * 0.8D);

                    for (int j1 = 0; j1 < 2; ++j1) {
                        int k = MathHelper.floor(this.getPosY()) + j1;
                        BlockPos pos = new BlockPos(i1, k, j);
                        Block block = this.world.getBlockState(pos).getBlock();
                        
                        if (block == Blocks.SNOW) {
                            this.world.destroyBlock(pos, true);
                            this.collidedHorizontally = false;
                        } else if (block == Blocks.LILY_PAD) {
                            this.world.destroyBlock(pos, true);
                            this.collidedHorizontally = false;
                        }
                    }
                }

            if (this.getComeSailAway() && this.onGround) {
                setMotion(getMotion().mul(0.5, 0.5, 0.5));
            } else if (this.onGround) {
                setMotion(Vec3d.ZERO);
            }

            this.move(MoverType.SELF, getMotion());

            // This will never trigger since d10 will only ever get up to 0.45 >:D *evil laugh*
            // In other words, when come sail away, there is no stopping this sucker
            if (this.getComeSailAway()) {
                setMotion(getMotion().mul(0.9900000095367432D, 0.949999988079071D, 0.9900000095367432D));
            }

            this.rotationPitch = 0.0F;
            d4 = (double)this.rotationYaw;
            d11 = this.prevPosX - this.getPosX();
            d12 = this.prevPosZ - this.getPosZ();

            if (d11 * d11 + d12 * d12 > 0.001D) {
                d4 = (double)((float)(Math.atan2(d12, d11) * 180.0D / Math.PI));
            }

            double d7 = MathHelper.wrapDegrees(d4 - (double)this.rotationYaw);

            if (d7 > 20.0D) {
                d7 = 20.0D;
            }

            if (d7 < -20.0D) {
                d7 = -20.0D;
            }

            this.rotationYaw = (float)((double)this.rotationYaw + d7);
            this.setRotation(this.rotationYaw, this.rotationPitch);

            if (!this.world.isRemote) {
                List<?> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getBoundingBox().grow(0.20000000298023224D, 0.0D, 0.20000000298023224D));

                if (list != null && !list.isEmpty()) {
                    for (int k1 = 0; k1 < list.size(); ++k1) {
                        Entity entity = (Entity)list.get(k1);

                        if (entity != this.getControllingPassenger() && entity.canBePushed() && entity instanceof ChairEntity) {
                            entity.applyEntityCollision(this);
                        }
                    }
                }

                if (this.getControllingPassenger() != null && !this.getControllingPassenger().isAlive()) {
                    this.removePassengers();
                }
            }
        } else {
            this.move(MoverType.SELF, getMotion());
        }
    }
    
    @Override
    protected boolean preventMotion() {
        return !getComeSailAway();
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.getDataManager().register(COMESAILAWAY, new Byte((byte)0));
    }

    @Override
    protected void readAdditional(CompoundNBT nbt) {
        super.readAdditional(nbt);
        this.setComeSailAway(Boolean.valueOf(nbt.getBoolean("COME_SAIL_AWAY")));
    }

    @Override
    protected void writeAdditional(CompoundNBT nbt) {
        super.writeAdditional(nbt);
        nbt.putBoolean("COME_SAIL_AWAY", getComeSailAway());
    }

    @Override
    public boolean processInitialInteract(PlayerEntity player, Hand hand) {
        if (!world.isRemote && !player.isSneaking()) {
            player.startRiding(this);
            return true;
        }

        return !player.isRidingSameEntity(this);
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    @Override
    public boolean canBeCollidedWith() {
        return isAlive();
    }
    
    @Override
    @Nullable
    public Entity getControllingPassenger() {
        List<Entity> list = this.getPassengers();
        return list.isEmpty() ? null : (Entity)list.get(0);
    }

    @Override
    public double getMountedYOffset() {
        return 0.11;
    }
    
    @Override
    public void updatePassenger(Entity passenger) {
        if (this.isPassenger(passenger)) {
            Vec3d xzOffset = new Vec3d(0, 0, -0.125).rotateYaw((float) Math.toRadians(-rotationYaw));
            passenger.setPosition(getPosX() + xzOffset.x, getPosY() + getMountedYOffset() + passenger.getYOffset(), getPosZ() + xzOffset.z);
        }
    }

    public void setComeSailAway(boolean sail) {
        dataManager.set(COMESAILAWAY, sail ? Byte.valueOf((byte)1) : Byte.valueOf((byte)0));
    }

    public boolean getComeSailAway() {
        return dataManager.get(COMESAILAWAY) == (byte)1;
    }
}