package net.tropicraft.core.common.entity.placeable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.tropicraft.core.common.entity.hostile.AshenEntity;
import net.tropicraft.core.common.item.AshenMasks;
import net.tropicraft.core.common.item.TropicraftItems;

public class AshenMaskEntity extends Entity {

    private static final DataParameter<Byte> MASK_TYPE = EntityDataManager.createKey(AshenEntity.class, DataSerializers.BYTE);
    public static final int MAX_TICKS_ALIVE = 24000;

    public AshenMaskEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    public void dropItemStack() {
        entityDropItem(new ItemStack(TropicraftItems.ASHEN_MASKS.get(AshenMasks.VALUES[getMaskType()]).get()), 1.0F);
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    protected void registerData() {
        dataManager.register(MASK_TYPE, (byte) 0);
    }

    @Override
    protected void readAdditional(CompoundNBT nbt) {
        setMaskType(nbt.getByte("MaskType"));
    }

    @Override
    protected void writeAdditional(CompoundNBT nbt) {
        nbt.putByte("MaskType", getMaskType());
    }

    public void setMaskType(byte type) {
        dataManager.set(MASK_TYPE, type);
    }

    public byte getMaskType() {
        return dataManager.get(MASK_TYPE);
    }

    @Override
    public void tick() {
        if (!world.isRemote) {
            // Remove masks that have been on the ground abandoned for over a day
            if (ticksExisted >= MAX_TICKS_ALIVE) {
                remove();
            }
        }

        final Vec3d motion = getMotion();

        if (onGround) {
            setMotion(motion.mul(0.5, 0, 0.5));
        }

        if (isInWater()) {
            setMotion(motion.x * 0.95f, 0.02f, motion.z * 0.95f);
        } else {
            setMotion(motion.subtract(0, 0.05f, 0));
        }

        move(MoverType.SELF, motion);
    }

    @Override
    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
        if (isInvulnerableTo(par1DamageSource)) {
            return false;
        } else {
            if (isAlive() && !world.isRemote) {
                remove();
                markVelocityChanged();
                dropItemStack();
            }

            return true;
        }
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
