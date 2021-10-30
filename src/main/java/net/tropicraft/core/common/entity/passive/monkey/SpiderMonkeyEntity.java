package net.tropicraft.core.common.entity.passive.monkey;

import com.google.common.base.Suppliers;
import net.minecraft.world.entity.AgableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.tropicraft.core.common.Easings;
import net.tropicraft.core.common.TropicraftTags;
import net.tropicraft.core.common.item.TropicraftItems;

import java.util.function.Supplier;

import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;

public class SpiderMonkeyEntity extends Animal {
    private static final Supplier<Ingredient> BREEDING_ITEMS = Suppliers.memoize(() -> Ingredient.of(TropicraftTags.Items.FRUITS));

    private static final int STAND_ANIMATION_LENGTH = 15;

    private static final EntityDataAccessor<Boolean> STANDING = SynchedEntityData.defineId(SpiderMonkeyEntity.class, EntityDataSerializers.BOOLEAN);

    private int standAnimation;

    public SpiderMonkeyEntity(EntityType<? extends SpiderMonkeyEntity> type, Level world) {
        super(type, world);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 2.0));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25, BREEDING_ITEMS.get(), false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.MOVEMENT_SPEED, 0.2F);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(STANDING, false);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level.isClientSide) {
            this.tickStandingState();
        } else {
            this.tickStandingAnimation();
        }
    }

    private void tickStandingState() {
        if (this.getLastHurtByMob() != null) {
            this.setStanding(false);
            return;
        }

        if (this.level.random.nextInt(200) == 0) {
            boolean standing = this.level.random.nextInt(3) == 0;
            this.setStanding(standing);
        }
    }

    private void tickStandingAnimation() {
        if (this.isStanding()) {
            if (this.standAnimation < STAND_ANIMATION_LENGTH) {
                this.standAnimation++;
            }
        } else {
            if (this.standAnimation > 0) {
                this.standAnimation--;
            }
        }
    }

    public void setStanding(boolean standing) {
        this.entityData.set(STANDING, standing);
        this.standAnimation = standing ? STAND_ANIMATION_LENGTH : 0;
    }

    public boolean isStanding() {
        return this.entityData.get(STANDING);
    }

    public float getStandAnimation(float partialTicks) {
        float animation = (this.standAnimation + (this.isStanding() ? partialTicks : -partialTicks)) / STAND_ANIMATION_LENGTH;
        return Easings.inOutSine(Mth.clamp(animation, 0.0F, 1.0F));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.setStanding(nbt.getBoolean("standing"));
    }

    @Override
    public void addAdditionalSaveData(final CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("standing", this.isStanding());
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Override
    public SpiderMonkeyEntity getBreedOffspring(ServerLevel world, AgableMob mate) {
        return null;
    }

    @Override
    public ItemStack getPickedResult(HitResult target) {
        return new ItemStack(TropicraftItems.SPIDER_MONKEY_SPAWN_EGG.get());
    }

    @Override
    public int getMaxFallDistance() {
        return 5;
    }

    @Override
    protected int calculateFallDamage(float distance, float damageMultiplier) {
        return super.calculateFallDamage(distance, damageMultiplier) / 2;
    }
}
