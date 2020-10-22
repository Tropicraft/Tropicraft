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
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
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
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.GameRules;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.tropicraft.core.common.entity.egg.SeaTurtleEggEntity;
import net.tropicraft.core.common.item.TropicraftItems;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

public class SeaTurtleEntity extends TurtleEntity {

    private static final DataParameter<Boolean> IS_MATURE = EntityDataManager.createKey(SeaTurtleEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> TURTLE_TYPE = EntityDataManager.createKey(SeaTurtleEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> NO_BRAKES = EntityDataManager.createKey(SeaTurtleEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CAN_FLY = EntityDataManager.createKey(SeaTurtleEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_DIGGING = EntityDataManager.createKey(SeaTurtleEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> HAS_EGG = EntityDataManager.createKey(SeaTurtleEntity.class, DataSerializers.BOOLEAN);

    private static final int NUM_TYPES = 6;
    
    private double lastPosY;
    private int digCounter;

    public SeaTurtleEntity(EntityType<? extends TurtleEntity> type, World world) {
        super(type, world);
    }

    protected void registerAttributes() {
        super.registerAttributes();
    }

    public boolean isPushedByWater() {
        return false;
    }

    public boolean canBreatheUnderwater() {
        return true;
    }

    public CreatureAttribute getCreatureAttribute() {
        return CreatureAttribute.WATER;
    }

    protected float determineNextStepDistance() {
        return this.distanceWalkedOnStepModified + 0.15F;
    }

    @Nullable
    public ILivingEntityData onInitialSpawn(IWorld world, DifficultyInstance difficultyInstance, SpawnReason spawnReason, @Nullable ILivingEntityData data, @Nullable CompoundNBT nbt) {
        setRandomTurtleType();
        this.lastPosY = getPosY();
        return super.onInitialSpawn(world, difficultyInstance, spawnReason, data, nbt);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        // goalSelector
        GoalSelector goalSelector = ObfuscationReflectionHelper.getPrivateValue(MobEntity.class, this, "field_70714_bg");
        // goals
        Set<PrioritizedGoal> goalSet = ObfuscationReflectionHelper.getPrivateValue(GoalSelector.class, goalSelector, "field_220892_d");

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
    public void registerData() {
        super.registerData();
        getDataManager().register(IS_MATURE, true);
        getDataManager().register(TURTLE_TYPE, 1);
        getDataManager().register(NO_BRAKES, false);
        getDataManager().register(CAN_FLY, false);
        getDataManager().register(IS_DIGGING, false);
        getDataManager().register(HAS_EGG, false);
    }

    public void writeAdditional(CompoundNBT nbt) {
        super.writeAdditional(nbt);
        nbt.putInt("TurtleType", getTurtleType());
        nbt.putBoolean("IsMature", isMature());
        nbt.putBoolean("NoBrakesOnThisTrain", getNoBrakes());
        nbt.putBoolean("LongsForTheSky", getCanFly());
        nbt.putBoolean("HasEgg", hasEgg());
    }

    public void readAdditional(CompoundNBT nbt) {
        super.readAdditional(nbt);
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
        this.lastPosY = this.getPosY();
    }

    public boolean isMature() {
        return getDataManager().get(IS_MATURE);
    }

    public SeaTurtleEntity setIsMature(final boolean mature) {
        getDataManager().set(IS_MATURE, mature);
        return this;
    }

    public int getTurtleType() {
        return getDataManager().get(TURTLE_TYPE);
    }
    
    public void setRandomTurtleType() {
        setTurtleType(rand.nextInt(NUM_TYPES) + 1);
    }

    public SeaTurtleEntity setTurtleType(final int type) {
        getDataManager().set(TURTLE_TYPE, MathHelper.clamp(type, 1, NUM_TYPES));
        return this;
    }
    
    public boolean getNoBrakes() {
        return getDataManager().get(NO_BRAKES);
    }
    
    public SeaTurtleEntity setNoBrakes(final boolean noBrakes) {
        getDataManager().set(NO_BRAKES, noBrakes);
        return this;
    }

    public boolean getCanFly() {
        return getDataManager().get(CAN_FLY);
    }
    
    public SeaTurtleEntity setCanFly(final boolean canFly) {
        getDataManager().set(CAN_FLY, canFly);
        return this;
    }
    
    @Override
    @Nullable
    public Entity getControllingPassenger() {
        final List<Entity> passengers = getPassengers();
        return passengers.isEmpty() ? null : passengers.get(0);
    }

    public static boolean canSpawnOnLand(EntityType<SeaTurtleEntity> turtle, IWorld world, SpawnReason reason, BlockPos pos, Random rand) {
        return pos.getY() < world.getSeaLevel() + 4 && world.getBlockState(pos.down()).getBlock() == Blocks.SAND && world.getLightSubtracted(pos, 0) > 8;
    }

    @Override
    public boolean canBeSteered() {
        return getControllingPassenger() instanceof LivingEntity;
    }
    
    @Override
    public double getMountedYOffset() {
        return super.getMountedYOffset() - 0.1;
    }

    @Override
    @Nullable
    public AgeableEntity createChild(AgeableEntity ent) {
        return TropicraftEntities.SEA_TURTLE.get().create(this.world)
                .setTurtleType(rand.nextBoolean() && ent instanceof SeaTurtleEntity ? ((SeaTurtleEntity)ent).getTurtleType() : getTurtleType())
                .setIsMature(false);
    }

    @Override
    public boolean processInteract(final PlayerEntity player, final Hand hand) {
        if (super.processInteract(player, hand)) {
            return true;
        }

        if (!world.isRemote && !player.isSneaking() && canFitPassenger(player) && isMature()) {
            player.startRiding(this);
        }

        return true;
    }
    
    @Override
    public boolean isInRangeToRender3d(double x, double y, double z) {
        Entity controller = getControllingPassenger();
        if (controller != null) {
            return controller.isInRangeToRender3d(x, y, z);
        }
        return super.isInRangeToRender3d(x, y, z);
    }
    
    @Override
    public void tick() {
        super.tick();
        lastPosY = getPosY();
    }
    
    @Override
    public void livingTick() {
        super.livingTick();
        if (this.isAlive() && this.isDigging() && this.digCounter >= 1 && this.digCounter % 5 == 0) {
            BlockPos blockpos = new BlockPos(this);
            if (this.world.getBlockState(blockpos.down()).getMaterial() == Material.SAND) {
                this.world.playEvent(2001, blockpos, Block.getStateId(Blocks.SAND.getDefaultState()));
            }
        }

        if (this.world.isRemote) {
            if (isBeingRidden() && canBeSteered()) {
                if (isInWater() || getCanFly()) {
                    Vec3d movement = new Vec3d(getPosX(), getPosY(), getPosZ()).subtract(prevPosX, prevPosY, prevPosZ);
                    double speed = movement.length();
                    Vec3d particleOffset = movement.inverse().scale(2);
                    if (speed > 0.05) {
                        int maxParticles = MathHelper.ceil(speed * 5);
                        int particlesToSpawn = rand.nextInt(1 + maxParticles);
                        IParticleData particle = isInWater() ? ParticleTypes.BUBBLE : ParticleTypes.END_ROD;
                        for (int i = 0; i < particlesToSpawn; i++) {
                            Vec3d particleMotion = movement.scale(1);
                            world.addParticle(particle, true,
                                    particleOffset.getX() + getPosX() - 0.25 + rand.nextDouble() * 0.5,
                                    particleOffset.getY() + getPosY() + 0.1 + rand.nextDouble() * 0.1,
                                    particleOffset.getZ() + getPosZ() - 0.25 + rand.nextDouble() * 0.5, particleMotion.x, particleMotion.y, particleMotion.z);
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
    public void updatePassenger(Entity passenger) {
        super.updatePassenger(passenger);
        if (this.isPassenger(passenger)) {
            if(passenger instanceof PlayerEntity) {
                PlayerEntity p = (PlayerEntity)passenger;
                if(this.isInWater()) {
                    if(p.moveForward > 0f) {
                        this.rotationPitch = this.lerp(rotationPitch, -(passenger.rotationPitch*0.5f), 6f);
                        this.rotationYaw = this.lerp(rotationYaw, -passenger.rotationYaw, 6f);
//                        this.targetVector = null;
//                        this.targetVectorHeading = null;
                        this.swimSpeedCurrent += 0.05f;
                        if(this.swimSpeedCurrent > 4f) {
                            this.swimSpeedCurrent = 4f;
                        }
                    }
                    if(p.moveForward < 0f) {
                        this.swimSpeedCurrent *= 0.89f;
                        if(this.swimSpeedCurrent < 0.1f) {
                            this.swimSpeedCurrent = 0.1f;
                        }
                    }
                    if(p.moveForward == 0f) {
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
                    //	this.swimYaw = -passenger.rotationYaw;
                }
                //p.rotationYaw = this.rotationYaw;
            } else
            if (passenger instanceof MobEntity) {
                MobEntity mobentity = (MobEntity)passenger;
                this.renderYawOffset = mobentity.renderYawOffset;
                this.prevRotationYawHead = mobentity.prevRotationYawHead;
            }
        }
    }
        
    @Override
    public void setPosition(double x, double y, double z) {
        super.setPosition(x, y, z);
    }

    @Override
    public void travel(Vec3d input) {
        if (isAlive()) {
            if (isBeingRidden() && canBeSteered()) {
                final Entity controllingPassenger = getControllingPassenger();

                if (!(controllingPassenger instanceof LivingEntity)) {
                    return;
                }

                final LivingEntity controllingEntity = (LivingEntity) controllingPassenger;

                this.rotationYaw = controllingPassenger.rotationYaw;
                this.prevRotationYaw = this.rotationYaw;
                this.rotationPitch = controllingPassenger.rotationPitch;
                this.setRotation(this.rotationYaw, this.rotationPitch);
                this.renderYawOffset = this.rotationYaw;
                this.rotationYawHead = this.rotationYaw;
                this.stepHeight = 1.0F;
                this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1F;

                float strafe = controllingEntity.moveStrafing;
                float forward = getNoBrakes() ? 1 : controllingEntity.moveForward;
                float vertical = controllingEntity.moveVertical; // Players never use this?

                double verticalFromPitch = -Math.sin(Math.toRadians(rotationPitch)) * forward;
                forward *= MathHelper.clamp(1 - (Math.abs(rotationPitch) / 90), 0.01f, 1);

                if (!isInWater()) {
                    if (getCanFly()) {
                        this.setMotion(this.getMotion().add(0, -this.getAttribute(ENTITY_GRAVITY).getValue() * 0.05, 0));
                    } else {
                        // Lower max speed when breaching, as a penalty to uncareful driving
                        this.setMotion(this.getMotion().mul(0.9, 0.99, 0.9).add(0, -this.getAttribute(ENTITY_GRAVITY).getValue(), 0));
                    }
                }

                if (this.canPassengerSteer()) {
                    Vec3d travel = new Vec3d(strafe, verticalFromPitch + vertical, forward)
                    		.scale(this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue())
                    		// This scale controls max speed. We reduce it significantly here so that the range of speed is higher
                    		// This is compensated for by the high value passed to moveRelative
                    		.scale(0.025F);
                    // This is the effective speed modifier, controls the post-scaling of the movement vector
                    moveRelative(1F, travel);
                    move(MoverType.SELF, getMotion());
                    // This value controls how much speed is "dampened" which effectively controls how much drift there is, and the max speed
                    this.setMotion(this.getMotion().scale(forward > 0 || !isInWater() ? 0.975 : 0.9));
                } else {
                    this.fallDistance = (float) Math.max(0, (getPosY() - lastPosY) * -8);
                    this.setMotion(Vec3d.ZERO);
                }
                this.prevLimbSwingAmount = this.limbSwingAmount;
                double d1 = this.getPosX() - this.prevPosX;
                double d0 = this.getPosZ() - this.prevPosZ;
                float swinger = MathHelper.sqrt(d1 * d1 + d0 * d0) * 4.0F;
                if (swinger > 1.0F) {
                    swinger = 1.0F;
                }

                this.limbSwingAmount += (swinger - this.limbSwingAmount) * 0.4F;
                this.limbSwing += this.limbSwingAmount;
            } else {
                this.jumpMovementFactor = 0.02F;
                super.travel(input);
            }
        }
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return new ItemStack(TropicraftItems.SEA_TURTLE_SPAWN_EGG.get());
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
        public boolean shouldExecute() {
            return turtle.hasEgg() && turtle.getHome().withinDistance(turtle.getPositionVec(), 9.0D) ? super.shouldExecute() : false;
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting() {
            return super.shouldContinueExecuting() && turtle.hasEgg() && turtle.getHome().withinDistance(turtle.getPositionVec(), 9.0D);
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        @Override
        public void tick() {
            super.tick();
            BlockPos blockpos = new BlockPos(this.turtle);
            if (!this.turtle.isInWater() && this.getIsAboveDestination()) {
                if (!this.turtle.isDigging()) {
                    this.turtle.setDigging(true);
                } else if (this.turtle.digCounter > 200) {
                    World world = this.turtle.world;
                    world.playSound((PlayerEntity)null, blockpos, SoundEvents.ENTITY_TURTLE_LAY_EGG, SoundCategory.BLOCKS, 0.3F, 0.9F + world.rand.nextFloat() * 0.2F);
                    //world.setBlockState(this.destinationBlock.up(), Blocks.TURTLE_EGG.getDefaultState().with(TurtleEggBlock.EGGS, Integer.valueOf(this.turtle.rand.nextInt(4) + 1)), 3);
                    final SeaTurtleEggEntity egg = TropicraftEntities.SEA_TURTLE_EGG.get().create(world);
                    final BlockPos spawnPos = destinationBlock.up();
                    egg.setPosition(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
                    world.addEntity(egg);
                    this.turtle.setHasEgg(false);
                    this.turtle.setDigging(false);
                    this.turtle.setInLove(600);
                }

                if (this.turtle.isDigging()) {
                    this.turtle.digCounter++;
                }
            }
        }

        @Override
        protected boolean shouldMoveTo(IWorldReader worldIn, BlockPos pos) {
            if (!worldIn.isAirBlock(pos.up())) {
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
        public boolean shouldExecute() {
            return super.shouldExecute() && !this.turtle.hasEgg();
        }

        /**
         * Spawns a baby animal of the same type.
         */
        protected void spawnBaby() {
            ServerPlayerEntity serverplayerentity = this.animal.getLoveCause();
            if (serverplayerentity == null && this.targetMate.getLoveCause() != null) {
                serverplayerentity = this.targetMate.getLoveCause();
            }

            if (serverplayerentity != null) {
                serverplayerentity.addStat(Stats.ANIMALS_BRED);
                CriteriaTriggers.BRED_ANIMALS.trigger(serverplayerentity, this.animal, this.targetMate, (AgeableEntity)null);
            }

            this.turtle.setHasEgg(true);
            this.animal.resetInLove();
            this.targetMate.resetInLove();
            Random random = this.animal.getRNG();
            if (this.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
                this.world.addEntity(new ExperienceOrbEntity(this.world, this.animal.getPosX(), this.animal.getPosY(), this.animal.getPosZ(), random.nextInt(7) + 1));
            }

        }
    }

    private void setDigging(boolean digging) {
        this.digCounter = digging ? 1 : 0;
        this.dataManager.set(IS_DIGGING, digging);
    }

    @Override
    public boolean isDigging() {
        return digCounter > 0;
    }

    private void setHasEgg(boolean hasEgg) {
        this.dataManager.set(HAS_EGG, hasEgg);
    }

    @Override
    public boolean hasEgg() {
        return dataManager.get(HAS_EGG);
    }
}
