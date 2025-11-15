package net.tropicraft.core.common.entity.underdasea;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathType;

public class PiranhaEntity extends AbstractSchoolingFish implements IAtlasFish {

    public PiranhaEntity(EntityType<? extends AbstractSchoolingFish> type, Level world) {
        super(type, world);
        this.setPathfindingMalus(PathType.WATER, 0.0F);
        this.moveControl = new SmoothSwimmingMoveControl(this, 85, 20, 0.2F, 0.5F, false);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return AbstractFish.createAttributes()
                .add(Attributes.MAX_HEALTH, 5.0)
                .add(Attributes.FOLLOW_RANGE, 12.0)
                .add(Attributes.MOVEMENT_SPEED, 0.8)
                .add(Attributes.ATTACK_DAMAGE, 2.0);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 12.0f));
        goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.3f, true));
        goalSelector.addGoal(4, new FishRandomSwimGoal(this));

        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public int getHeadRotSpeed() {
        return 30;
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        return InteractionResult.PASS;
    }

    @Override
    public int getMaxSchoolSize() {
        return 20;
    }

    @Override
    public ItemStack getBucketItemStack() {
        return ItemStack.EMPTY;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SALMON_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SALMON_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.SALMON_HURT;
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.SALMON_FLOP;
    }

    @Override
    public int getAtlasSlot() {
        return 9;
    }

    static class FishRandomSwimGoal extends RandomSwimmingGoal {
        private final AbstractFish fish;

        // params: speedModifier, interval
        public FishRandomSwimGoal(AbstractFish fish) {
            super(fish, 1.0F, 40);
            this.fish = fish;
        }

        @Override
        public boolean canUse() {
            return fish.getTarget() != null && super.canUse();
        }
    }
}
