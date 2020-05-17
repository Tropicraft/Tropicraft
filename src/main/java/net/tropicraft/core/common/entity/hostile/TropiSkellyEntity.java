package net.tropicraft.core.common.entity.hostile;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.tropicraft.core.common.entity.passive.EntityKoaBase;
import net.tropicraft.core.common.item.TropicraftItems;

import javax.annotation.Nullable;

public class TropiSkellyEntity extends MonsterEntity {
    public TropiSkellyEntity(EntityType<? extends MonsterEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new SwimGoal(this));
        goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0D));
        goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        goalSelector.addGoal(8, new LookRandomlyGoal(this));
        
        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        //TODO targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, EntityAshen.class, false));
        targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, EntityKoaBase.class, false));
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.world.isRemote && this.world.getDifficulty() == Difficulty.PEACEFUL) {
            this.remove();
        }
    }

    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23);
        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.5D);
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
    }

    @Override
    @Nullable
    public ILivingEntityData onInitialSpawn(IWorld p_213386_1_, DifficultyInstance p_213386_2_, SpawnReason p_213386_3_, @Nullable ILivingEntityData p_213386_4_, @Nullable CompoundNBT p_213386_5_) {
        this.setHeldItem(Hand.MAIN_HAND, new ItemStack(TropicraftItems.BAMBOO_SPEAR.get()));
        return super.onInitialSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
    }

    private boolean isValidLightLevel() {
        BlockPos blockpos = new BlockPos(getPosX(), getBoundingBox().minY, getPosZ());
        if (world.getLightFor(LightType.SKY, blockpos) > rand.nextInt(32)) {
            return false;
        } else {
            int i = world.isThundering() ? world.getNeighborAwareLightSubtracted(blockpos, 10) : world.getLight(blockpos);
            return i <= rand.nextInt(8);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SKELETON_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.ENTITY_SKELETON_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SKELETON_DEATH;
    }

    @Override
    public boolean canSpawn(IWorld worldIn, SpawnReason spawnReasonIn) {
        return worldIn.getDifficulty() != Difficulty.PEACEFUL && this.isValidLightLevel() && super.canSpawn(worldIn, spawnReasonIn);
    }
}
