package net.tropicraft.core.common.entity.placeable;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.tropicraft.core.common.item.AshenMaskItem;
import net.tropicraft.core.common.item.AshenMasks;
import net.tropicraft.core.common.item.TropicraftItems;

public class AshenMaskEntity extends Entity {

    private static final EntityDataAccessor<Byte> MASK_TYPE = SynchedEntityData.defineId(AshenMaskEntity.class, EntityDataSerializers.BYTE);
    public static final int MAX_TICKS_ALIVE = 24000;

    public AshenMaskEntity(EntityType<?> type, Level world) {
        super(type, world);
    }

    public void dropItemStack(ServerLevel level) {
        ItemEntry<AshenMaskItem> mask = TropicraftItems.ASHEN_MASKS.get(AshenMasks.VALUES[getMaskType()]);
        spawnAtLocation(level, new ItemStack(mask.get()), 1.0f);
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(MASK_TYPE, (byte) 0);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        setMaskType(input.getByteOr("MaskType", (byte) 0));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        output.putByte("MaskType", getMaskType());
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

        Vec3 motion = getDeltaMovement();

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
    public boolean hurtServer(ServerLevel level, DamageSource damageSource, float amount) {
        if (isInvulnerableToBase(damageSource)) {
            return false;
        } else {
            if (isAlive()) {
                remove(RemovalReason.KILLED);
                markHurt();
                dropItemStack(level);
            }

            return true;
        }
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(TropicraftItems.ASHEN_MASKS.get(AshenMasks.VALUES[getMaskType()]).get());
    }
}
