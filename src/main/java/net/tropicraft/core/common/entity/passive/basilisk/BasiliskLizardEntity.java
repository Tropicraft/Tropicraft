package net.tropicraft.core.common.entity.passive.basilisk;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.item.TropicraftItems;

public final class BasiliskLizardEntity extends Animal {
    private static final EntityDataAccessor<Boolean> RUNNING = SynchedEntityData.defineId(BasiliskLizardEntity.class, EntityDataSerializers.BOOLEAN);

    private static final float WATER_WALK_SPEED_BOOST = 1.6F;
    private static final int WATER_WALK_TIME = 10;

    private static final int RUNNING_ANIMATION_LENGTH = 10;

    private int movingTimer;
    private boolean onWaterSurface;

    private int runningAnimation;
    private int prevRunningAnimation;

    public BasiliskLizardEntity(EntityType<? extends BasiliskLizardEntity> type, Level world) {
        super(type, world);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 0.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 6.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25F);
    }

    @Override
    protected PathNavigation createNavigation(Level world) {
        return new WaterWalking.Navigator(this, world);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.25));
        this.goalSelector.addGoal(1, new RandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(RUNNING, false);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Override
    public BasiliskLizardEntity getBreedOffspring(ServerLevel world, AgeableMob mate) {
        return null;
    }

    @Override
    public ItemStack getPickedResult(HitResult target) {
        if (getType() == TropicraftEntities.BROWN_BASILISK_LIZARD.get()) {
            return new ItemStack(TropicraftItems.BROWN_BASILISK_LIZARD_SPAWN_EGG.get());
        } else {
            return new ItemStack(TropicraftItems.GREEN_BASILISK_LIZARD_SPAWN_EGG.get());
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level.isClientSide()) {
            this.tickMovementTimer();
            this.tickSwimming();

            this.entityData.set(RUNNING, this.onWaterSurface);
        } else {
            this.tickRunningAnimation();
        }
    }

    private void tickRunningAnimation() {
        this.prevRunningAnimation = this.runningAnimation;

        if (this.entityData.get(RUNNING)) {
            if (this.runningAnimation < RUNNING_ANIMATION_LENGTH) {
                this.runningAnimation++;
            }
        } else {
            if (this.runningAnimation > 0) {
                this.runningAnimation--;
            }
        }
    }

    private void tickMovementTimer() {
        if (this.zza != 0.0F) {
            this.movingTimer = WATER_WALK_TIME;
        } else {
            if (this.movingTimer > 0) {
                this.movingTimer--;
            }
        }
    }

    private void tickSwimming() {
        if (!this.onWaterSurface && this.isInWater() && this.getFluidHeight(FluidTags.WATER) > this.getFluidJumpThreshold()) {
            boolean shouldWaterWalk = this.shouldWaterWalk();
            if (shouldWaterWalk || this.random.nextFloat() < 0.8F) {
                this.setDeltaMovement(this.getDeltaMovement().scale(0.5).add(0.0, 0.1, 0.0));
            }
        }
    }

    @Override
    protected Vec3 maybeBackOffFromEdge(Vec3 offset, MoverType mover) {
        if (this.shouldWaterWalk()) {
            Vec3 result = WaterWalking.collide(this.level, this.getBoundingBox(), offset);
            this.onWaterSurface = offset.y < 0.0 && result.y != offset.y;
            return result;
        } else {
            this.onWaterSurface = false;
            return offset;
        }
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
        this.onGround |= this.onWaterSurface;
        super.checkFallDamage(y, this.onGround, state, pos);
    }

    @Override
    public float getSpeed() {
        float speed = super.getSpeed();
        return this.onWaterSurface ? speed * WATER_WALK_SPEED_BOOST : speed;
    }

    @Override
    public boolean canStandOnFluid(Fluid fluid) {
        return WaterWalking.canWalkOn(fluid);
    }

    private boolean shouldWaterWalk() {
        return this.movingTimer > 0;
    }

    public float getRunningAnimation(float partialTicks) {
        float animation = Mth.lerp(partialTicks, prevRunningAnimation, runningAnimation);
        return animation / RUNNING_ANIMATION_LENGTH;
    }
}
