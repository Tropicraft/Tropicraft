package net.tropicraft.core.common.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class SeaTurtleEntity extends TurtleEntity {

    private static final DataParameter<Boolean> IS_MATURE = EntityDataManager.createKey(SeaTurtleEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> TURTLE_TYPE = EntityDataManager.createKey(SeaTurtleEntity.class, DataSerializers.VARINT);

    private static final int NUM_TYPES = 6;

    public SeaTurtleEntity(EntityType<? extends TurtleEntity> type, World world) {
        super(type, world);
    }

    @Nullable
    public ILivingEntityData onInitialSpawn(IWorld world, DifficultyInstance difficultyInstance, SpawnReason spawnReason, @Nullable ILivingEntityData data, @Nullable CompoundNBT nbt) {
        setTurtleType(rand.nextInt(NUM_TYPES) + 1);
        return super.onInitialSpawn(world, difficultyInstance, spawnReason, data, nbt);
    }

    @Override
    public void registerData() {
        super.registerData();
        getDataManager().register(IS_MATURE, false);
        getDataManager().register(TURTLE_TYPE, 1);
    }

    public void writeAdditional(CompoundNBT nbt) {
        super.writeAdditional(nbt);
        nbt.putInt("TurtleType", getTurtleType());
        nbt.putBoolean("IsMature", isMature());
    }

    public void readAdditional(CompoundNBT nbt) {
        super.readAdditional(nbt);
        setTurtleType(nbt.getInt("TurtleType"));
        setIsMature(nbt.getBoolean("IsMature"));
    }

    public boolean isMature() {
        return getDataManager().get(IS_MATURE);
    }

    public void setIsMature(final boolean mature) {
        getDataManager().set(IS_MATURE, mature);
    }

    public int getTurtleType() {
        return getDataManager().get(TURTLE_TYPE);
    }

    public void setTurtleType(final int type) {
        getDataManager().set(TURTLE_TYPE, type);
    }

    @Override
    @Nullable
    public Entity getControllingPassenger() {
        final List<Entity> passengers = getPassengers();
        return passengers.isEmpty() ? null : passengers.get(0);
    }

    @Override
    public boolean canBeSteered() {
        return getControllingPassenger() instanceof LivingEntity;
    }

    @Override
    public boolean processInteract(final PlayerEntity player, final Hand hand) {
        if (!world.isRemote && !player.isSneaking() && canFitPassenger(player)/* && this.isMature() */&& isInWater()) {
            player.startRiding(this);
        }
        return super.processInteract(player, hand);
    }

    @Override
    public void travel(Vec3d input) {
        if (isAlive()) {
            if (isBeingRidden() && canBeSteered()) {
                LivingEntity livingentity = (LivingEntity)this.getControllingPassenger();
                rotationYaw = livingentity.rotationYaw;
                prevRotationYaw = rotationYaw;
                rotationPitch = livingentity.rotationPitch * 0.5F;
                setRotation(rotationYaw, rotationPitch);
                renderYawOffset = rotationYaw;
                rotationYawHead = renderYawOffset;
                float xAcceleration = livingentity.moveStrafing * 0.5F;
                float zAcceleration = livingentity.moveForward;
                if (zAcceleration <= 0.0F) {
                    zAcceleration *= 0.25F;
                }
//
//                if (onGround) {
//                    xAcceleration = 0.0F;
//                    zAcceleration = 0.0F;
//                }

                if (canPassengerSteer()) {
                    setAIMoveSpeed((float) getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue());
                    setMotion(this.getMotion().add(MathHelper.sin(-this.rotationYaw * ((float)Math.PI / 180F)) * xAcceleration, input.y, MathHelper.cos(this.rotationYaw * ((float)Math.PI / 180F)) * zAcceleration));
                } else if (livingentity instanceof PlayerEntity) {
                    setMotion(Vec3d.ZERO);
                }

                this.prevLimbSwingAmount = this.limbSwingAmount;
                double d2 = this.posX - this.prevPosX;
                double d3 = this.posZ - this.prevPosZ;
                float f4 = MathHelper.sqrt(d2 * d2 + d3 * d3) * 4.0F;
                if (f4 > 1.0F) {
                    f4 = 1.0F;
                }

                this.limbSwingAmount += (f4 - this.limbSwingAmount) * 0.4F;
                this.limbSwing += this.limbSwingAmount;
            } else {
                this.jumpMovementFactor = 0.02F;
                super.travel(input);
            }
        }
    }
}
