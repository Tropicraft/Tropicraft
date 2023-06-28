package net.tropicraft.core.common.entity.placeable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.tropicraft.core.common.item.AshenMasks;
import net.tropicraft.core.common.item.TropicraftItems;

public class AshenMaskEntity extends Entity {

    private static final EntityDataAccessor<Byte> MASK_TYPE = SynchedEntityData.defineId(AshenMaskEntity.class, EntityDataSerializers.BYTE);
    public static final int MAX_TICKS_ALIVE = 24000;

    public AshenMaskEntity(EntityType<?> type, Level world) {
        super(type, world);
    }

    public void dropItemStack() {
        spawnAtLocation(new ItemStack(TropicraftItems.ASHEN_MASKS.get(AshenMasks.VALUES[getMaskType()]).get()), 1.0F);
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(MASK_TYPE, (byte) 0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt) {
        setMaskType(nbt.getByte("MaskType"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt) {
        nbt.putByte("MaskType", getMaskType());
    }

    public void setMaskType(byte type) {
        entityData.set(MASK_TYPE, type);
    }

    public byte getMaskType() {
        return entityData.get(MASK_TYPE);
    }

    @Override
    public void tick() {
        if (!level().isClientSide) {
            // Remove masks that have been on the ground abandoned for over a day
            if (tickCount >= MAX_TICKS_ALIVE) {
                remove(RemovalReason.DISCARDED);
            }
        }

        final Vec3 motion = getDeltaMovement();

        if (onGround()) {
            setDeltaMovement(motion.multiply(0.5, 0, 0.5));
        }

        if (isInWater()) {
            setDeltaMovement(motion.x * 0.95f, 0.02f, motion.z * 0.95f);
        } else {
            setDeltaMovement(motion.subtract(0, 0.05f, 0));
        }

        move(MoverType.SELF, motion);
    }

    @Override
    public boolean hurt(DamageSource damageSource, float par2) {
        if (isInvulnerableTo(damageSource)) {
            return false;
        } else {
            if (isAlive() && !level().isClientSide) {
                remove(RemovalReason.KILLED);
                markHurt();
                dropItemStack();
            }

            return true;
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public ItemStack getPickedResult(HitResult target) {
        return new ItemStack(TropicraftItems.ASHEN_MASKS.get(AshenMasks.VALUES[getMaskType()]).get());
    }
}
