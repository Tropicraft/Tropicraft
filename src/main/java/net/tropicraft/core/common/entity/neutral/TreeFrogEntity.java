package net.tropicraft.core.common.entity.neutral;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.entity.hostile.TropicraftCreatureEntity;
import net.tropicraft.core.common.entity.projectile.PoisonBlotEntity;
import net.tropicraft.core.common.sound.Sounds;

import javax.annotation.Nullable;

public class TreeFrogEntity extends TropicraftCreatureEntity implements Enemy, RangedAttackMob {

    private static final EntityDataAccessor<Integer> TYPE = SynchedEntityData.defineId(TreeFrogEntity.class, EntityDataSerializers.INT);

    public enum Type {
        GREEN("green"),
        RED("red"),
        BLUE("blue"),
        YELLOW("yellow");

        final String color;

        Type(String s) {
            color = s;
        }

        public String getColor() {
            return color;
        }
    }

    private NearestAttackableTargetGoal<Player> hostileAI;

    public int jumpDelay = 0;
    private int attackTime;

    public TreeFrogEntity(EntityType<? extends PathfinderMob> type, Level world) {
        super(type, world);
        //TODO 1.17 fix - pushthrough = 0.8f;
        xpReward = 5;
    }

    @Override
    public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
        return false;
    }

    @Override
    public void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new RangedAttackGoal(this, 1.0, 60, 10.0f));
        goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 5.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (!getNavigation().isDone() || getTarget() != null) {
            if (onGround() || isInWater()) {
                if (jumpDelay > 0)
                    jumpDelay--;
                if (jumpDelay <= 0) {
                    jumpDelay = 5 + random.nextInt(4);

                    // this.jump();
                    // this.motionY += -0.01 + rand.nextDouble() * 0.1;
                    Vec3 motion = getDeltaMovement();

                    double speed = Math.sqrt(motion.x * motion.x + motion.z * motion.z);
                    if (speed > 0.02) {
                        double motionY = motion.y + 0.4d;
                        double motionX = motion.x * 1.1d;
                        double motionZ = motion.z * 1.1d;
                        setDeltaMovement(motionX, motionY, motionZ);
                    }
                }
            }
        }

        if (attackTime > 0)
            attackTime--;
    }

    @Override
    public void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(TYPE, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("Type", getFrogType());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        setFrogType(nbt.getInt("Type"));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData) {
        int type = random.nextInt(Type.values().length);
        setFrogType(type);

        if (type != 0) {
            hostileAI = new NearestAttackableTargetGoal<>(this, Player.class, true);
            targetSelector.addGoal(1, hostileAI);
        }

        return super.finalizeSpawn(world, difficulty, reason, spawnData);
    }

    public void setFrogType(int i) {
        entityData.set(TYPE, i);
    }

    public int getFrogType() {
        return entityData.get(TYPE);
    }

    public String getColor() {
        return Type.values()[getFrogType()].getColor();
    }

    @Override
    public void performRangedAttack(LivingEntity entity, float dist) {
        if (dist < 4.0f && !level().isClientSide && attackTime == 0 && level().getDifficulty() != Difficulty.PEACEFUL) {
            double d = entity.getX() - getX();
            double d1 = entity.getZ() - getZ();

            PoisonBlotEntity poison = new PoisonBlotEntity(TropicraftEntities.POISON_BLOT.get(), this, level());
            poison.setPos(poison.getX(), poison.getY() + 1.3999999761581421, poison.getZ());
            double shotHeight = (entity.getY() + (double) entity.getEyeHeight()) - 0.20000000298023224 - poison.getY();
            float f1 = Mth.sqrt((float) (d * d + d1 * d1)) * 0.2f;
            entity.getCommandSenderWorld().playSound(null, entity.blockPosition(), Sounds.FROG_SPIT.get(), SoundSource.HOSTILE, 1, 1);
            level().addFreshEntity(poison);
            poison.shoot(d, shotHeight + (double) f1, d1, 0.6f, 12.0f);
            attackTime = 50;
            setYRot((float) ((Math.atan2(d1, d) * 180) / 3.1415927410125732) - 90.0f);
        }
    }
}
