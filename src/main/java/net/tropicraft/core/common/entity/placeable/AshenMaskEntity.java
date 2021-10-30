package net.tropicraft.core.common.entity.placeable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.tropicraft.core.common.entity.hostile.AshenEntity;
import net.tropicraft.core.common.item.AshenMasks;
import net.tropicraft.core.common.item.TropicraftItems;

public class AshenMaskEntity extends Entity {

    private static final DataParameter<Byte> MASK_TYPE = EntityDataManager.defineId(AshenEntity.class, DataSerializers.BYTE);
    public static final int MAX_TICKS_ALIVE = 24000;

    public AshenMaskEntity(EntityType<?> type, World world) {
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
    protected void readAdditionalSaveData(CompoundNBT nbt) {
        setMaskType(nbt.getByte("MaskType"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT nbt) {
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
        if (!level.isClientSide) {
            // Remove masks that have been on the ground abandoned for over a day
            if (tickCount >= MAX_TICKS_ALIVE) {
                remove();
            }
        }

        final Vector3d motion = getDeltaMovement();

        if (onGround) {
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
            if (isAlive() && !level.isClientSide) {
                remove();
                markHurt();
                dropItemStack();
            }

            return true;
        }
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return new ItemStack(TropicraftItems.ASHEN_MASKS.get(AshenMasks.VALUES[getMaskType()]).get());
    }
}
