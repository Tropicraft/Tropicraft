package net.tropicraft.core.common.entity.passive.basilisk;

import net.minecraft.block.BlockState;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.item.TropicraftItems;

public final class BasiliskLizardEntity extends AnimalEntity {
    private static final DataParameter<Boolean> RUNNING = EntityDataManager.defineId(BasiliskLizardEntity.class, DataSerializers.BOOLEAN);

    private static final float WATER_WALK_SPEED_BOOST = 1.6F;
    private static final int WATER_WALK_TIME = 10;

    private static final int RUNNING_ANIMATION_LENGTH = 10;

    private int movingTimer;
    private boolean onWaterSurface;

    private int runningAnimation;
    private int prevRunningAnimation;

    public BasiliskLizardEntity(EntityType<? extends BasiliskLizardEntity> type, World world) {
        super(type, world);
        this.setPathfindingMalus(PathNodeType.WATER, 0.0F);
        this.setPathfindingMalus(PathNodeType.WATER_BORDER, 0.0F);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 6.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25F);
    }

    @Override
    protected PathNavigator createNavigation(World world) {
        return new WaterWalking.Navigator(this, world);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.25));
        this.goalSelector.addGoal(1, new RandomWalkingGoal(this, 1.0));
        this.goalSelector.addGoal(2, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(3, new LookRandomlyGoal(this));
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
    public BasiliskLizardEntity getBreedOffspring(ServerWorld world, AgeableEntity mate) {
        return null;
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
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
    protected Vector3d maybeBackOffFromEdge(Vector3d offset, MoverType mover) {
        if (this.shouldWaterWalk()) {
            Vector3d result = WaterWalking.collide(this.level, this.getBoundingBox(), offset);
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
        float animation = MathHelper.lerp(partialTicks, prevRunningAnimation, runningAnimation);
        return animation / RUNNING_ANIMATION_LENGTH;
    }
}
