package net.tropicraft.core.common.entity.neutral;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.entity.hostile.TropicraftCreatureEntity;
import net.tropicraft.core.common.entity.projectile.PoisonBlotEntity;
import net.tropicraft.core.common.item.TropicraftItems;
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
            this.color = s;
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
        //TODO 1.17 fix - pushthrough = 0.8F;
        xpReward = 5;
    }

    @Override
    public void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new RangedAttackGoal(this, 1.0D, 60, 10.0F));
        goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 5.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (!getNavigation().isDone() || this.getTarget() != null) {
            if (onGround() || isInWater()) {
                if (jumpDelay > 0)
                    jumpDelay--;
                if (jumpDelay <= 0) {
                    jumpDelay = 5 + random.nextInt(4);

                    // this.jump();
                    // this.motionY += -0.01D + rand.nextDouble() * 0.1D;
                    final Vec3 motion = getDeltaMovement();

                    double speed = Math.sqrt(motion.x * motion.x + motion.z * motion.z);
                    if (speed > 0.02D) {
                        final double motionY = motion.y + 0.4d;
                        final double motionX = motion.x * 1.1d;
                        final double motionZ = motion.z * 1.1d;
                        setDeltaMovement(motionX, motionY, motionZ);
                    }
                }
            }
        }

        if (attackTime > 0)
            attackTime--;
    }

    @Override
    public void defineSynchedData() {
        super.defineSynchedData();
        getEntityData().define(TYPE, 0);
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
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        final int type = random.nextInt(Type.values().length);
        setFrogType(type);

        if (type != 0) {
            hostileAI = new NearestAttackableTargetGoal<>(this, Player.class, true);
            targetSelector.addGoal(1, hostileAI);
        }

        return super.finalizeSpawn(world, difficulty, reason, spawnData, dataTag);
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
    public void performRangedAttack(final LivingEntity entity, float dist) {
        if (dist < 4F && !level().isClientSide && attackTime == 0 && level().getDifficulty() != Difficulty.PEACEFUL) {
            double d = entity.getX() - getX();
            double d1 = entity.getZ() - getZ();

            final PoisonBlotEntity poison = new PoisonBlotEntity(TropicraftEntities.POISON_BLOT.get(), this, level());
            poison.setPos(poison.getX(), poison.getY() + 1.3999999761581421D, poison.getZ());
            final double shotHeight = (entity.getY() + (double) entity.getEyeHeight()) - 0.20000000298023224D - poison.getY();
            float f1 = Mth.sqrt((float) (d * d + d1 * d1)) * 0.2F;
            entity.getCommandSenderWorld().playSound(null, entity.blockPosition(), Sounds.FROG_SPIT.get(), SoundSource.HOSTILE, 1, 1);
            level().addFreshEntity(poison);
            poison.shoot(d, shotHeight + (double) f1, d1, 0.6F, 12F);
            attackTime = 50;
            setYRot((float) ((Math.atan2(d1, d) * 180D) / 3.1415927410125732D) - 90F);
        }
    }

    @Override
    public ItemStack getPickedResult(HitResult target) {
        // TODO - add one egg per type
        return new ItemStack(TropicraftItems.TREE_FROG_SPAWN_EGG.get());
    }
}
