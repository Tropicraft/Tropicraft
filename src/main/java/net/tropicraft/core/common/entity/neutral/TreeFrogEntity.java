package net.tropicraft.core.common.entity.neutral;

import net.minecraft.entity.*;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.entity.projectile.PoisonBlotEntity;
import net.tropicraft.core.common.item.TropicraftItems;
import net.tropicraft.core.common.sound.Sounds;

import javax.annotation.Nullable;

public class TreeFrogEntity extends CreatureEntity implements IMob, IRangedAttackMob {

    private static final DataParameter<Integer> TYPE = EntityDataManager.createKey(TreeFrogEntity.class, DataSerializers.VARINT);

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
        entityCollisionReduction = 0.8F;
        experienceValue = 5;
    }

    @Override
    public void registerGoals() {
        goalSelector.addGoal(0, new SwimGoal(this));
        goalSelector.addGoal(1, new RangedAttackGoal(this, 1.0D, 60, 10.0F));
        goalSelector.addGoal(2, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();

        getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5.0D);
        getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25);
    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();
        if (!getNavigator().noPath() || this.getAttackTarget() != null) {
            if (onGround || isInWater()) {
                if (jumpDelay > 0)
                    jumpDelay--;
                if (jumpDelay <= 0) {
                    jumpDelay = 5 + rand.nextInt(4);

                    // this.jump();
                    // this.motionY += -0.01D + rand.nextDouble() * 0.1D;
                    final Vec3d motion = getMotion();

                    double speed = Math.sqrt(motion.x * motion.x + motion.z * motion.z);
                    if (speed > 0.02D) {
                        final double motionY = motion.y + 0.4d;
                        final double motionX = motion.x * 1.1d;
                        final double motionZ = motion.z * 1.1d;
                        setMotion(motionX, motionY, motionZ);
                    }
                }
            }
        }

        if (attackTime > 0)
            attackTime--;
    }

    @Override
    public void registerData() {
        super.registerData();
        getDataManager().register(TYPE, 0);
    }

    @Override
    public void writeAdditional(CompoundNBT nbt) {
        super.writeAdditional(nbt);
        nbt.putInt("Type", getFrogType());
    }

    @Override
    public void readAdditional(CompoundNBT nbt) {
        super.readAdditional(nbt);
        setFrogType(nbt.getInt("Type"));
    }

    @Override
    @Nullable
    public ILivingEntityData onInitialSpawn(IWorld world, DifficultyInstance difficultyInstance, SpawnReason spawnReason, @Nullable ILivingEntityData entityData, @Nullable CompoundNBT nbt) {
        final int type = rand.nextInt(Type.values().length);
        setFrogType(type);

        if (type != 0) {
            hostileAI = new NearestAttackableTargetGoal<PlayerEntity>(this, PlayerEntity.class, true);
            targetSelector.addGoal(1, hostileAI);
        }

        return super.onInitialSpawn(world, difficultyInstance, spawnReason, entityData, nbt);
    }

    public void setFrogType(int i) {
        dataManager.set(TYPE, i);
    }

    public int getFrogType() {
        return dataManager.get(TYPE);
    }

    public String getColor() {
        return Type.values()[getFrogType()].getColor();
    }

    @Override
    public void attackEntityWithRangedAttack(final LivingEntity entity, float dist) {
        if (dist < 4F && !world.isRemote && attackTime == 0 && world.getDifficulty() != Difficulty.PEACEFUL) {
            double d = entity.getPosX() - getPosX();
            double d1 = entity.getPosZ() - getPosZ();

            final PoisonBlotEntity entitypoisonblot = new PoisonBlotEntity(TropicraftEntities.POISON_BLOT.get(), this, world);
            entitypoisonblot.setPosition(entitypoisonblot.getPosX(), entitypoisonblot.getPosY() + 1.3999999761581421D, entitypoisonblot.getPosZ());
            final double shotHeight = (entity.getPosY() + (double) entity.getEyeHeight()) - 0.20000000298023224D - entitypoisonblot.getPosY();
            float f1 = MathHelper.sqrt(d * d + d1 * d1) * 0.2F;
            entity.getEntityWorld().playSound(null, entity.getPosition(), Sounds.FROG_SPIT, SoundCategory.HOSTILE, 1, 1);
            world.addEntity(entitypoisonblot);
            entitypoisonblot.shoot(d, shotHeight + (double) f1, d1, 0.6F, 12F);
            attackTime = 50;
            rotationYaw = (float) ((Math.atan2(d1, d) * 180D) / 3.1415927410125732D) - 90F;
        }
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        // TODO - add one egg per type
        return new ItemStack(TropicraftItems.TREE_FROG_SPAWN_EGG.get());
    }
}
