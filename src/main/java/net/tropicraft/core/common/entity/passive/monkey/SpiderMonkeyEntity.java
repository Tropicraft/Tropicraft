package net.tropicraft.core.common.entity.passive.monkey;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.tropicraft.core.common.BinaryAnimation;
import net.tropicraft.core.common.TropicraftTags;

import javax.annotation.Nullable;

public class SpiderMonkeyEntity extends Animal {
    private static final EntityDataAccessor<Boolean> STANDING = SynchedEntityData.defineId(SpiderMonkeyEntity.class, EntityDataSerializers.BOOLEAN);

    private final BinaryAnimation standAnimation = new BinaryAnimation(15, Mth::easeInOutSine);

    public SpiderMonkeyEntity(EntityType<? extends SpiderMonkeyEntity> type, Level world) {
        super(type, world);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new PanicGoal(this, 2.0));
        goalSelector.addGoal(2, new BreedGoal(this, 1.0));
        goalSelector.addGoal(3, new TemptGoal(this, 1.25, item -> item.is(TropicraftTags.Items.FRUITS), false));
        goalSelector.addGoal(4, new FollowParentGoal(this, 1.25));
        goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
        goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0f));
        goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes()
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
            standAnimation.tick(isStanding());
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

    public void setStanding(boolean standing) {
        entityData.set(STANDING, standing);
        standAnimation.setImmediate(standing);
    }

    public boolean isStanding() {
        return entityData.get(STANDING);
    }

    public float getStandAnimation(float partialTicks) {
        return standAnimation.get(partialTicks);
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        setStanding(input.getBooleanOr("standing", false));
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("standing", isStanding());
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Override
    @Nullable
    public SpiderMonkeyEntity getBreedOffspring(ServerLevel world, AgeableMob mate) {
        return null;
    }

    @Override
    public int getMaxFallDistance() {
        return 5;
    }

    @Override
    protected int calculateFallDamage(double distance, float damageMultiplier) {
        return super.calculateFallDamage(distance, damageMultiplier) / 2;
    }
}
