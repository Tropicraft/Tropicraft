package net.tropicraft.core.common.entity.underdasea;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.BreathAirGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ManateeEntity extends WaterAnimal {
    private float xBodyRot;
    private float xBodyRotO;

    public ManateeEntity(EntityType<? extends ManateeEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        moveControl = new SmoothSwimmingMoveControl(this, 85, 1, 0.025f, 0.1F, false);
        lookControl = new SmoothSwimmingLookControl(this, 5);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(0, new BreathAirGoal(this));
        goalSelector.addGoal(0, new TryFindWaterGoal(this));
        goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1.0D, 10));
        goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
    }

    @Override
    public void tick() {
        super.tick();

        xBodyRotO = xBodyRot;
        float targetXBodyRot = onGround() ? 0.0f : Mth.clamp(getXRot() * 0.5f, -10.0f, 10.0f);
        xBodyRot += Mth.clamp(targetXBodyRot - xBodyRot, -0.5f, 0.5f);
    }

    @Override
    public void travel(Vec3 vector) {
        if (isControlledByLocalInstance() && isInWater()) {
            moveRelative(getSpeed(), vector);
            move(MoverType.SELF, getDeltaMovement());
            setDeltaMovement(getDeltaMovement().scale(0.9));
        } else {
            super.travel(vector);
        }
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new WaterBoundPathNavigation(this, level);
    }

    public float getXBodyRot(float partialTicks) {
        return Mth.lerp(partialTicks, xBodyRotO, xBodyRot);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return AbstractFish.createAttributes()
                .add(Attributes.MAX_HEALTH, 50.0)
                .add(Attributes.MOVEMENT_SPEED, 0.4);
    }
}
