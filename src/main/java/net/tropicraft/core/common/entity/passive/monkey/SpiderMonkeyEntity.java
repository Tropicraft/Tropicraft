package net.tropicraft.core.common.entity.passive.monkey;

import com.google.common.base.Suppliers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.tropicraft.core.common.Easings;
import net.tropicraft.core.common.TropicraftTags;

import java.util.function.Supplier;

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
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new PanicGoal(this, 2.0));
        goalSelector.addGoal(2, new BreedGoal(this, 1.0));
        goalSelector.addGoal(3, new TemptGoal(this, 1.25, BREEDING_ITEMS.get(), false));
        goalSelector.addGoal(4, new FollowParentGoal(this, 1.25));
        goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
        goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0f));
        goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.MOVEMENT_SPEED, 0.2f);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(STANDING, false);
    }

    @Override
    public void tick() {
        super.tick();

        if (!level().isClientSide) {
            tickStandingState();
        } else {
            tickStandingAnimation();
        }
    }

    private void tickStandingState() {
        if (getLastHurtByMob() != null) {
            setStanding(false);
            return;
        }

        if (level().random.nextInt(200) == 0) {
            boolean standing = level().random.nextInt(3) == 0;
            setStanding(standing);
        }
    }

    private void tickStandingAnimation() {
        if (isStanding()) {
            if (standAnimation < STAND_ANIMATION_LENGTH) {
                standAnimation++;
            }
        } else {
            if (standAnimation > 0) {
                standAnimation--;
            }
        }
    }

    public void setStanding(boolean standing) {
        entityData.set(STANDING, standing);
        standAnimation = standing ? STAND_ANIMATION_LENGTH : 0;
    }

    public boolean isStanding() {
        return entityData.get(STANDING);
    }

    public float getStandAnimation(float partialTicks) {
        float animation = (standAnimation + (isStanding() ? partialTicks : -partialTicks)) / STAND_ANIMATION_LENGTH;
        return Easings.inOutSine(Mth.clamp(animation, 0.0f, 1.0f));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        setStanding(nbt.getBoolean("standing"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("standing", isStanding());
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Override
    public SpiderMonkeyEntity getBreedOffspring(ServerLevel world, AgeableMob mate) {
        return null;
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
