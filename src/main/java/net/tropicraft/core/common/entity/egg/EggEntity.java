package net.tropicraft.core.common.entity.egg;

import com.google.common.collect.ImmutableList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class EggEntity extends LivingEntity {

    private static final EntityDataAccessor<Integer> HATCH_DELAY = SynchedEntityData.defineId(EggEntity.class, EntityDataSerializers.INT);

    public double rotationRand;
   
    public EggEntity(final EntityType<? extends EggEntity> type, Level w) {
        super(type, w);
        rotationRand = 0;
        noCulling = true;
       
        setYRot(random.nextInt(360));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.MAX_HEALTH, 2.0);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        tickCount = compound.getInt("ticks");
        setHatchDelay(compound.getInt("hatchDelay"));
        super.readAdditionalSaveData(compound);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("ticks", tickCount);
        compound.putInt("hatchDelay", getHatchDelay());
        super.addAdditionalSaveData(compound);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(HATCH_DELAY, 0);
        setHatchDelay(-60 + random.nextInt(120));
    }

    public abstract boolean shouldEggRenderFlat();
    
    public abstract String getEggTexture();
    
    /**
     * Create and return a Entity here
     */
    public abstract Entity onHatch();
    
    /**
     * The amount of time in ticks it will take for the egg to hatch
     *     eg. hatch on tick n
     * @return a positive number
     */
    public abstract int getHatchTime();
    
    /**
     * The amount of time in ticks the egg will move around before it hatches
     *  eg. start rolling n ticks before hatch
     * @return a positive number lower than getHatchTime()
     */
    public abstract int getPreHatchMovement();
    
    public int getRandomHatchDelay() {
        return this.getEntityData().get(HATCH_DELAY);
    }
     
    public boolean isHatching() {
        return this.tickCount > (getHatchTime() + getRandomHatchDelay());
    }
    
    public boolean isNearHatching() {
        return this.tickCount > (getHatchTime() + getRandomHatchDelay()) - getPreHatchMovement();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        
        if (isNearHatching()) {
            rotationRand += 0.1707F * level.random.nextFloat();
            
            // Hatch time!
            if (tickCount >= this.getHatchTime()) {
                if (!level.isClientSide) {
                    final Entity ent = onHatch();
                    ent.moveTo(getX(), getY(), getZ(), 0.0F, 0.0F);
                    level.addFreshEntity(ent);
                    remove(RemovalReason.DISCARDED);
                }
            }
        } 
    }
    
    public void setHatchDelay(int i) {
        this.getEntityData().set(HATCH_DELAY, -60 + random.nextInt(120));
    }
    
    public int getHatchDelay() {
        return this.getEntityData().get(HATCH_DELAY);
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return ImmutableList.of();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot slotIn) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot slotIn, ItemStack stack) {
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.LEFT;
    }
}
