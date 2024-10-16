package net.tropicraft.core.common.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;
import net.tropicraft.core.common.entity.passive.EntityKoaBase;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;

public class EntityAIPlayKoa extends Goal {
    private final EntityKoaBase villagerObj;
    @Nullable
    private LivingEntity targetVillager;
    private final double speed;
    private int playTime;

    public EntityAIPlayKoa(EntityKoaBase villagerObjIn, double speedIn) {
        villagerObj = villagerObjIn;
        speed = speedIn;
        setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (villagerObj.getAge() >= 0) {
            return false;
        } else if (villagerObj.getRandom().nextInt(200) != 0) {
            return false;
        } else {
            List<EntityKoaBase> list = villagerObj.level().getEntitiesOfClass(EntityKoaBase.class, villagerObj.getBoundingBox().inflate(6.0, 3.0, 6.0));
            double d0 = Double.MAX_VALUE;

            for (EntityKoaBase entityvillager : list) {
                if (entityvillager != villagerObj && !entityvillager.isPlaying() && entityvillager.getAge() < 0) {
                    double d1 = entityvillager.distanceToSqr(villagerObj);

                    if (d1 <= d0) {
                        d0 = d1;
                        targetVillager = entityvillager;
                    }
                }
            }

            if (targetVillager == null) {
                Vec3 vec = DefaultRandomPos.getPos(villagerObj, 16, 3);
                return vec != null;
            }

            return true;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return playTime > 0;
    }

    @Override
    public void start() {
        if (targetVillager != null) {
            villagerObj.setPlaying(true);
        }

        playTime = 1000;
    }

    @Override
    public void stop() {
        villagerObj.setPlaying(false);
        targetVillager = null;
    }

    @Override
    public void tick() {
        --playTime;

        if (villagerObj.onGround() && villagerObj.level().random.nextInt(30) == 0) {
            villagerObj.getJumpControl().jump();
        }

        if (targetVillager != null) {
            if (villagerObj.distanceToSqr(targetVillager) > 4.0) {
                villagerObj.getNavigation().moveTo(targetVillager, speed);
            }
        } else if (villagerObj.getNavigation().isDone()) {
            Vec3 vec = DefaultRandomPos.getPos(villagerObj, 16, 3);
            if (vec == null) {
                return;
            }

            villagerObj.getNavigation().moveTo(vec.x, vec.y, vec.z, speed);
        }
    }
}


