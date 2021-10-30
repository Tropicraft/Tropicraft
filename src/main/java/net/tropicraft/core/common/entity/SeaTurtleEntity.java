package net.tropicraft.core.common.entity;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.BreedGoal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.tropicraft.core.common.dimension.TropicraftDimension;
import net.tropicraft.core.common.entity.egg.SeaTurtleEggEntity;
import net.tropicraft.core.common.item.TropicraftItems;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

public class SeaTurtleEntity extends TurtleEntity {

    private static final DataParameter<Boolean> IS_MATURE = EntityDataManager.defineId(SeaTurtleEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> TURTLE_TYPE = EntityDataManager.defineId(SeaTurtleEntity.class, DataSerializers.INT);
    private static final DataParameter<Boolean> NO_BRAKES = EntityDataManager.defineId(SeaTurtleEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CAN_FLY = EntityDataManager.defineId(SeaTurtleEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_DIGGING = EntityDataManager.defineId(SeaTurtleEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> HAS_EGG = EntityDataManager.defineId(SeaTurtleEntity.class, DataSerializers.BOOLEAN);

    private static final int NUM_TYPES = 6;
    
    private double lastPosY;
    private int digCounter;

    public SeaTurtleEntity(EntityType<? extends TurtleEntity> type, World world) {
        super(type, world);
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public CreatureAttribute getMobType() {
        return CreatureAttribute.WATER;
    }

    @Override
    protected float nextStep() {
        return this.moveDist + 0.15F;
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld world, DifficultyInstance difficultyInstance, SpawnReason spawnReason, @Nullable ILivingEntityData data, @Nullable CompoundNBT nbt) {
        setRandomTurtleType();
        this.lastPosY = getY();
        return super.finalizeSpawn(world, difficultyInstance, spawnReason, data, nbt);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        // goalSelector
        GoalSelector goalSelector = ObfuscationReflectionHelper.getPrivateValue(MobEntity.class, this, "goalSelector");
        // goals
        Set<PrioritizedGoal> goalSet = ObfuscationReflectionHelper.getPrivateValue(GoalSelector.class, goalSelector, "availableGoals");

        final Optional<PrioritizedGoal> eggGoal = goalSet.stream().filter(p -> p.getGoal().toString().contains("Egg")).findFirst();
        if (eggGoal.isPresent()) {
            goalSelector.removeGoal(eggGoal.get().getGoal());
            goalSelector.addGoal(1, new BetterLayEggGoal(this, 1.0));
        }

        final Optional<PrioritizedGoal> mateGoal = goalSet.stream().filter(p -> p.getGoal().toString().contains("Mate")).findFirst();
        if (mateGoal.isPresent()) {
            goalSelector.removeGoal(mateGoal.get().getGoal());
            goalSelector.addGoal(1, new BetterMateGoal(this, 1.0));
        }
    }

    @Override
    public void defineSynchedData() {
        super.defineSynchedData();
        getEntityData().define(IS_MATURE, true);
        getEntityData().define(TURTLE_TYPE, 1);
        getEntityData().define(NO_BRAKES, false);
        getEntityData().define(CAN_FLY, false);
        getEntityData().define(IS_DIGGING, false);
        getEntityData().define(HAS_EGG, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("TurtleType", getTurtleType());
        nbt.putBoolean("IsMature", isMature());
        nbt.putBoolean("NoBrakesOnThisTrain", getNoBrakes());
        nbt.putBoolean("LongsForTheSky", getCanFly());
        nbt.putBoolean("HasEgg", hasEgg());
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT nbt) {
        super.readAdditionalSaveData(nbt);
        if (nbt.contains("TurtleType")) {
            setTurtleType(nbt.getInt("TurtleType"));
        } else {
            setRandomTurtleType();
        }
        if (nbt.contains("IsMature")) {
            setIsMature(nbt.getBoolean("IsMature"));
        } else {
            setIsMature(true);
        }
        setNoBrakes(nbt.getBoolean("NoBrakesOnThisTrain"));
        setCanFly(nbt.getBoolean("LongsForTheSky"));
        setHasEgg(nbt.getBoolean("HasEgg"));
        this.lastPosY = this.getY();
    }

    public boolean isMature() {
        return getEntityData().get(IS_MATURE);
    }

    public SeaTurtleEntity setIsMature(final boolean mature) {
        getEntityData().set(IS_MATURE, mature);
        return this;
    }

    public int getTurtleType() {
        return getEntityData().get(TURTLE_TYPE);
    }
    
    public void setRandomTurtleType() {
        setTurtleType(random.nextInt(NUM_TYPES) + 1);
    }

    public SeaTurtleEntity setTurtleType(final int type) {
        getEntityData().set(TURTLE_TYPE, MathHelper.clamp(type, 1, NUM_TYPES));
        return this;
    }
    
    public boolean getNoBrakes() {
        return getEntityData().get(NO_BRAKES);
    }
    
    public SeaTurtleEntity setNoBrakes(final boolean noBrakes) {
        getEntityData().set(NO_BRAKES, noBrakes);
        return this;
    }

    public boolean getCanFly() {
        return getEntityData().get(CAN_FLY);
    }
    
    public SeaTurtleEntity setCanFly(final boolean canFly) {
        getEntityData().set(CAN_FLY, canFly);
        return this;
    }
    
    @Override
    @Nullable
    public Entity getControllingPassenger() {
        final List<Entity> passengers = getPassengers();
        return passengers.isEmpty() ? null : passengers.get(0);
    }

    public static boolean canSpawnOnLand(EntityType<SeaTurtleEntity> turtle, IWorld world, SpawnReason reason, BlockPos pos, Random rand) {
        return pos.getY() < TropicraftDimension.getSeaLevel(world) + 4 && world.getBlockState(pos.below()).getBlock() == Blocks.SAND && world.getRawBrightness(pos, 0) > 8;
    }

    @Override
    public boolean canBeControlledByRider() {
        return getControllingPassenger() instanceof LivingEntity;
    }
    
    @Override
    public double getPassengersRidingOffset() {
        return super.getPassengersRidingOffset() - 0.1;
    }

    @Nullable
    @Override
    public AgeableEntity getBreedOffspring(ServerWorld world, AgeableEntity partner) {
        return TropicraftEntities.SEA_TURTLE.get().create(this.level)
                .setTurtleType(random.nextBoolean() && partner instanceof SeaTurtleEntity ? ((SeaTurtleEntity)partner).getTurtleType() : getTurtleType())
                .setIsMature(false);
    }


    @Override
    public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
        ActionResultType result = super.mobInteract(player, hand);
        if (result != ActionResultType.PASS) {
            return result;
        }

        if (!level.isClientSide && !player.isShiftKeyDown() && canAddPassenger(player) && isMature()) {
            player.startRiding(this);
        }

        return ActionResultType.SUCCESS;
    }
    
    @Override
    public boolean shouldRender(double x, double y, double z) {
        Entity controller = getControllingPassenger();
        if (controller != null) {
            return controller.shouldRender(x, y, z);
        }
        return super.shouldRender(x, y, z);
    }
    
    @Override
    public void tick() {
        super.tick();
        lastPosY = getY();
    }
    
    @Override
    public void aiStep() {
        super.aiStep();
        if (this.isAlive() && this.isLayingEgg() && this.digCounter >= 1 && this.digCounter % 5 == 0) {
            BlockPos pos = this.blockPosition();
            if (this.level.getBlockState(pos.below()).getMaterial() == Material.SAND) {
                this.level.levelEvent(Constants.WorldEvents.BREAK_BLOCK_EFFECTS, pos, Block.getId(Blocks.SAND.defaultBlockState()));
            }
        }

        if (this.level.isClientSide) {
            if (isVehicle() && canBeControlledByRider()) {
                if (isInWater() || getCanFly()) {
                    Vector3d movement = new Vector3d(getX(), getY(), getZ()).subtract(xo, yo, zo);
                    double speed = movement.length();
                    Vector3d particleOffset = movement.reverse().scale(2);
                    if (speed > 0.05) {
                        int maxParticles = MathHelper.ceil(speed * 5);
                        int particlesToSpawn = random.nextInt(1 + maxParticles);
                        IParticleData particle = isInWater() ? ParticleTypes.BUBBLE : ParticleTypes.END_ROD;
                        for (int i = 0; i < particlesToSpawn; i++) {
                            Vector3d particleMotion = movement.scale(1);
                            level.addParticle(particle, true,
                                    particleOffset.x() + getX() - 0.25 + random.nextDouble() * 0.5,
                                    particleOffset.y() + getY() + 0.1 + random.nextDouble() * 0.1,
                                    particleOffset.z() + getZ() - 0.25 + random.nextDouble() * 0.5, particleMotion.x, particleMotion.y, particleMotion.z);
                        }
                    }
                }
            }
        }
    }

    public float lerp(float x1, float x2, float t) {
        return x1 + (t*0.03f) * MathHelper.wrapDegrees(x2 - x1);
    }

    private float swimSpeedCurrent;

    @Override
    public void positionRider(Entity passenger) {
        super.positionRider(passenger);
        if (this.hasPassenger(passenger)) {
            if(passenger instanceof PlayerEntity) {
                PlayerEntity p = (PlayerEntity)passenger;
                if(this.isInWater()) {
                    if(p.zza > 0f) {
                        this.xRot = this.lerp(xRot, -(passenger.xRot*0.5f), 6f);
                        this.yRot = this.lerp(yRot, -passenger.yRot, 6f);
//                        this.targetVector = null;
//                        this.targetVectorHeading = null;
                        this.swimSpeedCurrent += 0.05f;
                        if(this.swimSpeedCurrent > 4f) {
                            this.swimSpeedCurrent = 4f;
                        }
                    }
                    if(p.zza < 0f) {
                        this.swimSpeedCurrent *= 0.89f;
                        if(this.swimSpeedCurrent < 0.1f) {
                            this.swimSpeedCurrent = 0.1f;
                        }
                    }
                    if(p.zza == 0f) {
                        if(this.swimSpeedCurrent > 1f) {
                            this.swimSpeedCurrent *= 0.94f;
                            if(this.swimSpeedCurrent <= 1f) {
                                this.swimSpeedCurrent = 1f;
                            }
                        }
                        if(this.swimSpeedCurrent < 1f) {
                            this.swimSpeedCurrent *= 1.06f;
                            if(this.swimSpeedCurrent >= 1f) {
                                this.swimSpeedCurrent = 1f;
                            }
                        }
                        //this.swimSpeedCurrent = 1f;
                    }
                    //    this.swimYaw = -passenger.rotationYaw;
                }
                //p.rotationYaw = this.rotationYaw;
            } else
            if (passenger instanceof MobEntity) {
                MobEntity mobentity = (MobEntity)passenger;
                this.yBodyRot = mobentity.yBodyRot;
                this.yHeadRotO = mobentity.yHeadRotO;
            }
        }
    }
        
    @Override
    public void setPos(double x, double y, double z) {
        super.setPos(x, y, z);
    }

    @Override
    public void travel(Vector3d input) {
        if (isAlive()) {
            if (isVehicle() && canBeControlledByRider()) {
                final Entity controllingPassenger = getControllingPassenger();

                if (!(controllingPassenger instanceof LivingEntity)) {
                    return;
                }

                final LivingEntity controllingEntity = (LivingEntity) controllingPassenger;

                this.yRot = controllingPassenger.yRot;
                this.yRotO = this.yRot;
                this.xRot = controllingPassenger.xRot;
                this.setRot(this.yRot, this.xRot);
                this.yBodyRot = this.yRot;
                this.yHeadRot = this.yRot;
                this.maxUpStep = 1.0F;
                this.flyingSpeed = this.getSpeed() * 0.1F;

                float strafe = controllingEntity.xxa;
                float forward = getNoBrakes() ? 1 : controllingEntity.zza;
                float vertical = controllingEntity.yya; // Players never use this?

                double verticalFromPitch = -Math.sin(Math.toRadians(xRot)) * forward;
                forward *= MathHelper.clamp(1 - (Math.abs(xRot) / 90), 0.01f, 1);

                if (!isInWater()) {
                    if (getCanFly()) {
                        this.setDeltaMovement(this.getDeltaMovement().add(0, -this.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).getValue() * 0.05, 0));
                    } else {
                        // Lower max speed when breaching, as a penalty to uncareful driving
                        this.setDeltaMovement(this.getDeltaMovement().multiply(0.9, 0.99, 0.9).add(0, -this.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).getValue(), 0));
                    }
                }

                if (this.isControlledByLocalInstance()) {
                    Vector3d travel = new Vector3d(strafe, verticalFromPitch + vertical, forward)
                            .scale(this.getAttribute(Attributes.MOVEMENT_SPEED).getValue())
                            // This scale controls max speed. We reduce it significantly here so that the range of speed is higher
                            // This is compensated for by the high value passed to moveRelative
                            .scale(0.025F);
                    // This is the effective speed modifier, controls the post-scaling of the movement vector
                    moveRelative(1F, travel);
                    move(MoverType.SELF, getDeltaMovement());
                    // This value controls how much speed is "dampened" which effectively controls how much drift there is, and the max speed
                    this.setDeltaMovement(this.getDeltaMovement().scale(forward > 0 || !isInWater() ? 0.975 : 0.9));
                } else {
                    this.fallDistance = (float) Math.max(0, (getY() - lastPosY) * -8);
                    this.setDeltaMovement(Vector3d.ZERO);
                }
                this.animationSpeedOld = this.animationSpeed;
                double d1 = this.getX() - this.xo;
                double d0 = this.getZ() - this.zo;
                float swinger = MathHelper.sqrt(d1 * d1 + d0 * d0) * 4.0F;
                if (swinger > 1.0F) {
                    swinger = 1.0F;
                }

                this.animationSpeed += (swinger - this.animationSpeed) * 0.4F;
                this.animationPosition += this.animationSpeed;
            } else {
                this.flyingSpeed = 0.02F;
                super.travel(input);
            }
        }
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return new ItemStack(TropicraftItems.SEA_TURTLE_SPAWN_EGG.get());
    }

    @Override
    public boolean canBeRiddenInWater(Entity rider) {
        return true;
    }

    static class BetterLayEggGoal extends MoveToBlockGoal {
        private final SeaTurtleEntity turtle;

        BetterLayEggGoal(SeaTurtleEntity turtle, double speedIn) {
            super(turtle, speedIn, 16);
            this.turtle = turtle;
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        @Override
        public boolean canUse() {
            return turtle.hasEgg() && turtle.getHomePos().closerThan(turtle.position(), 9.0D) && super.canUse();
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && turtle.hasEgg() && turtle.getHomePos().closerThan(turtle.position(), 9.0D);
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        @Override
        public void tick() {
            super.tick();
            BlockPos blockpos = this.turtle.blockPosition();
            if (!this.turtle.isInWater() && this.isReachedTarget()) {
                if (!this.turtle.isLayingEgg()) {
                    this.turtle.setDigging(true);
                } else if (this.turtle.digCounter > 200) {
                    World world = this.turtle.level;
                    world.playSound(null, blockpos, SoundEvents.TURTLE_LAY_EGG, SoundCategory.BLOCKS, 0.3F, 0.9F + world.random.nextFloat() * 0.2F);
                    //world.setBlockState(this.destinationBlock.up(), Blocks.TURTLE_EGG.defaultBlockState().with(TurtleEggBlock.EGGS, Integer.valueOf(this.turtle.rand.nextInt(4) + 1)), 3);
                    final SeaTurtleEggEntity egg = TropicraftEntities.SEA_TURTLE_EGG.get().create(world);
                    final BlockPos spawnPos = blockPos.above();
                    egg.setPos(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
                    world.addFreshEntity(egg);
                    this.turtle.setHasEgg(false);
                    this.turtle.setDigging(false);
                    this.turtle.setInLoveTime(600);
                }

                if (this.turtle.isLayingEgg()) {
                    this.turtle.digCounter++;
                }
            }
        }

        @Override
        protected boolean isValidTarget(IWorldReader worldIn, BlockPos pos) {
            if (!worldIn.isEmptyBlock(pos.above())) {
                return false;
            } else {
                return worldIn.getBlockState(pos).getMaterial() == Material.SAND;
            }
        }
    }

    static class BetterMateGoal extends BreedGoal {
        private final SeaTurtleEntity turtle;

        BetterMateGoal(SeaTurtleEntity turtle, double speedIn) {
            super(turtle, speedIn);
            this.turtle = turtle;
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        @Override
        public boolean canUse() {
            return super.canUse() && !this.turtle.hasEgg();
        }

        /**
         * Spawns a baby animal of the same type.
         */
        @Override
        protected void breed() {
            ServerPlayerEntity serverplayerentity = this.animal.getLoveCause();
            if (serverplayerentity == null && this.partner.getLoveCause() != null) {
                serverplayerentity = this.partner.getLoveCause();
            }

            if (serverplayerentity != null) {
                serverplayerentity.awardStat(Stats.ANIMALS_BRED);
                CriteriaTriggers.BRED_ANIMALS.trigger(serverplayerentity, this.animal, this.partner, null);
            }

            this.turtle.setHasEgg(true);
            this.animal.resetLove();
            this.partner.resetLove();
            Random random = this.animal.getRandom();
            if (this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
                this.level.addFreshEntity(new ExperienceOrbEntity(this.level, this.animal.getX(), this.animal.getY(), this.animal.getZ(), random.nextInt(7) + 1));
            }

        }
    }

    private void setDigging(boolean digging) {
        this.digCounter = digging ? 1 : 0;
        this.entityData.set(IS_DIGGING, digging);
    }

    @Override
    public boolean isLayingEgg() {
        return digCounter > 0;
    }

    private void setHasEgg(boolean hasEgg) {
        this.entityData.set(HAS_EGG, hasEgg);
    }

    @Override
    public boolean hasEgg() {
        return entityData.get(HAS_EGG);
    }
}
