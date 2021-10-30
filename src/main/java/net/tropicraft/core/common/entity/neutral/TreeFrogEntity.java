package net.tropicraft.core.common.entity.neutral;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RangedAttackGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.entity.hostile.TropicraftCreatureEntity;
import net.tropicraft.core.common.entity.projectile.PoisonBlotEntity;
import net.tropicraft.core.common.item.TropicraftItems;
import net.tropicraft.core.common.sound.Sounds;

import javax.annotation.Nullable;

public class TreeFrogEntity extends TropicraftCreatureEntity implements IMob, IRangedAttackMob {

    private static final DataParameter<Integer> TYPE = EntityDataManager.defineId(TreeFrogEntity.class, DataSerializers.INT);

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

    private NearestAttackableTargetGoal<PlayerEntity> hostileAI;

    public int jumpDelay = 0;
    private int attackTime;

    public TreeFrogEntity(EntityType<? extends CreatureEntity> type, World world) {
        super(type, world);
        pushthrough = 0.8F;
        xpReward = 5;
    }

    @Override
    public void registerGoals() {
        goalSelector.addGoal(0, new SwimGoal(this));
        goalSelector.addGoal(1, new RangedAttackGoal(this, 1.0D, 60, 10.0F));
        goalSelector.addGoal(2, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return CreatureEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 5.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (!getNavigation().isDone() || this.getTarget() != null) {
            if (onGround || isInWater()) {
                if (jumpDelay > 0)
                    jumpDelay--;
                if (jumpDelay <= 0) {
                    jumpDelay = 5 + random.nextInt(4);

                    // this.jump();
                    // this.motionY += -0.01D + rand.nextDouble() * 0.1D;
                    final Vector3d motion = getDeltaMovement();

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
    public void addAdditionalSaveData(CompoundNBT nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("Type", getFrogType());
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT nbt) {
        super.readAdditionalSaveData(nbt);
        setFrogType(nbt.getInt("Type"));
    }

    @Nullable
    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld world, DifficultyInstance difficulty, SpawnReason reason, @Nullable ILivingEntityData spawnData, @Nullable CompoundNBT dataTag) {
        final int type = random.nextInt(Type.values().length);
        setFrogType(type);

        if (type != 0) {
            hostileAI = new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true);
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
        if (dist < 4F && !level.isClientSide && attackTime == 0 && level.getDifficulty() != Difficulty.PEACEFUL) {
            double d = entity.getX() - getX();
            double d1 = entity.getZ() - getZ();

            final PoisonBlotEntity poison = new PoisonBlotEntity(TropicraftEntities.POISON_BLOT.get(), this, level);
            poison.setPos(poison.getX(), poison.getY() + 1.3999999761581421D, poison.getZ());
            final double shotHeight = (entity.getY() + (double) entity.getEyeHeight()) - 0.20000000298023224D - poison.getY();
            float f1 = MathHelper.sqrt(d * d + d1 * d1) * 0.2F;
            entity.getCommandSenderWorld().playSound(null, entity.blockPosition(), Sounds.FROG_SPIT, SoundCategory.HOSTILE, 1, 1);
            level.addFreshEntity(poison);
            poison.shoot(d, shotHeight + (double) f1, d1, 0.6F, 12F);
            attackTime = 50;
            yRot = (float) ((Math.atan2(d1, d) * 180D) / 3.1415927410125732D) - 90F;
        }
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        // TODO - add one egg per type
        return new ItemStack(TropicraftItems.TREE_FROG_SPAWN_EGG.get());
    }
}
