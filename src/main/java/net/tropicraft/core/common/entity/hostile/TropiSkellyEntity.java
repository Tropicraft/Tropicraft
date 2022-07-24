package net.tropicraft.core.common.entity.hostile;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.HitResult;
import net.tropicraft.core.common.entity.passive.EntityKoaBase;
import net.tropicraft.core.common.entity.projectile.SpearEntity;
import net.tropicraft.core.common.item.TropicraftItems;

import javax.annotation.Nullable;

public class TropiSkellyEntity extends Monster implements RangedAttackMob {
    public TropiSkellyEntity(EntityType<? extends Monster> type, Level world) {
        super(type, world);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(2, new SkellyTridentAttackGoal(this, 1.0D, 40, 10.0F));
        goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0D));
        goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        
        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        //TODO targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, EntityAshen.class, false));
        targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, EntityKoaBase.class, false));
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide && this.level.getDifficulty() == Difficulty.PEACEFUL) {
            this.remove(RemovalReason.KILLED);
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.23)
                .add(Attributes.ARMOR, 2.0)
                .add(Attributes.ATTACK_DAMAGE, 2.5)
                .add(Attributes.FOLLOW_RANGE, 35.0);
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(TropicraftItems.BAMBOO_SPEAR.get()));
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    private boolean isValidLightLevel() {
        BlockPos blockpos = new BlockPos(getX(), getBoundingBox().minY, getZ());
        if (level.getBrightness(LightLayer.SKY, blockpos) > random.nextInt(32)) {
            return false;
        } else {
            int i = level.isThundering() ? level.getMaxLocalRawBrightness(blockpos, 10) : level.getMaxLocalRawBrightness(blockpos);
            return i <= random.nextInt(8);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SKELETON_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.SKELETON_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SKELETON_DEATH;
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return worldIn.getDifficulty() != Difficulty.PEACEFUL && this.isValidLightLevel() && super.checkSpawnRules(worldIn, spawnReasonIn);
    }

    @Override
    public ItemStack getPickedResult(HitResult target) {
        return new ItemStack(TropicraftItems.TROPISKELLY_SPAWN_EGG.get());
    }

    @Override
    public void performRangedAttack(LivingEntity pTarget, float pDistanceFactor) {
        SpearEntity throwntrident = new SpearEntity(this.level, this, new ItemStack(TropicraftItems.BAMBOO_SPEAR.get()));
        double d0 = pTarget.getX() - this.getX();
        double d1 = pTarget.getY(0.3333333333333333D) - throwntrident.getY();
        double d2 = pTarget.getZ() - this.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        throwntrident.shoot(d0, d1 + d3 * (double)0.2F, d2, 1.6F, (float)(14 - this.level.getDifficulty().getId() * 4));
        this.playSound(SoundEvents.DROWNED_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(throwntrident);
    }

    static class SkellyTridentAttackGoal extends RangedAttackGoal {
        private final TropiSkellyEntity skelly;

        public SkellyTridentAttackGoal(RangedAttackMob rangedMob, double pSpeedModifier, int pAttackInterval, float pAttackRadius) {
            super(rangedMob, pSpeedModifier, pAttackInterval, pAttackRadius);
            this.skelly = (TropiSkellyEntity) rangedMob;
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            return super.canUse() && this.skelly.getMainHandItem().is(TropicraftItems.BAMBOO_SPEAR.get());
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            super.start();
            this.skelly.setAggressive(true);
            this.skelly.startUsingItem(InteractionHand.MAIN_HAND);
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void stop() {
            super.stop();
            this.skelly.stopUsingItem();
            this.skelly.setAggressive(false);
        }
    }
}
