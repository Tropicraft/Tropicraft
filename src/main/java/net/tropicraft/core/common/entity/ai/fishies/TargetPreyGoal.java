package net.tropicraft.core.common.entity.ai.fishies;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.tropicraft.core.common.entity.underdasea.TropicraftFishEntity;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.ai.goal.Goal.Flag;

public class TargetPreyGoal extends Goal {
    public TropicraftFishEntity entity;
    public Random rand;

    public TargetPreyGoal(EnumSet<Flag> flags, TropicraftFishEntity entityObjIn) {
        entity = entityObjIn;
        rand = entity.getRNG();
        setMutexFlags(flags);
    }

    @Override
    public boolean shouldExecute() {
        return entity.isInWater() && entity.canAggress && entity.eatenFishAmount < entity.maximumEatAmount;
    }

    @Override
    public void tick() {
        super.tick();
        
        // Target selection
        AxisAlignedBB entityBB = entity.getBoundingBox();
        if (entity.ticksExisted % 80 == 0 && entity.aggressTarget == null || entity.getEntityWorld().getEntityByID(entity.aggressTarget.getEntityId()) == null) {
                List<Entity> list = entity.world.getEntitiesInAABBexcluding(entity, entityBB.grow(20D, 20D, 20D).offset(0.0D, -8.0D, 0.0D), e -> e.isAlive());
                if(list.size() > 0) {
                    Entity ent = list.get(rand.nextInt(list.size()));
                    boolean skip = false;
                    if(ent.equals(entity)) skip = true;    
                    if(ent.getClass().getName().equals(entity.getClass().getName())) skip = true;    
//                    if(entity instanceof IPredatorDiet) {
//                        Class[] prey = ((IPredatorDiet)entity).getPreyClasses();
//                        boolean contains = false;
//                        for(int i =0; i < prey.length; i++) {
//                            if(prey[i].getName().equals(ent.getClass().getName())) {
//                                contains = true;
//                            }
//                        }
//                        if(!contains) {
//                            skip = true;
//                        }
//                    }
                    if(!ent.isInWater()) skip = true;                
                    if(!entity.canEntityBeSeen(ent)) skip = true;
                    
                    if(!skip) {
                        if (ent instanceof LivingEntity){
                            if (ent.isInWater()) {
                                entity.aggressTarget = ent;
                            }
                        }
                    }
                }
            }
            if (rand.nextInt(200) == 0) {
                entity.aggressTarget = null;
                entity.setRandomTargetHeading();
            }

        // Hunt Target and/or Do damage
        if(entity.aggressTarget != null) {
            if(entity.getDistanceSq(entity.aggressTarget) <= entity.getWidth()) {
                if(entity.aggressTarget instanceof LivingEntity) {
                    entity.aggressTarget.attackEntityFrom(DamageSource.causeMobDamage(entity), (float) entity.getAttribute(Attributes.ATTACK_DAMAGE).getValue());
                }
                if(entity.aggressTarget instanceof TropicraftFishEntity) {
                    // Was eaten, cancel smoke
                    AxisAlignedBB aggressBB = entity.aggressTarget.getBoundingBox();
                    if(entityBB.maxY - entityBB.minY > aggressBB.maxY - aggressBB.minY) {
                        entity.aggressTarget.remove();
                        entity.heal(1);
                        entity.eatenFishAmount++;
                    }
                }
                entity.setRandomTargetHeading();
            }else {
                if(entity.canEntityBeSeen(entity.aggressTarget) && entity.ticksExisted % 20 == 0) {
                    entity.setTargetHeading(entity.aggressTarget.getPosX(), entity.aggressTarget.getPosY(), entity.aggressTarget.getPosZ(), true);
                }
            }
            if(entity.aggressTarget != null) {
                if(!entity.canEntityBeSeen(entity.aggressTarget) || !entity.aggressTarget.isInWater()) {
                    entity.aggressTarget = null;
                    entity.setRandomTargetHeading();
                }
            }
        }

        if(entity.aggressTarget == null || entity.world.getEntityByID(entity.aggressTarget.getEntityId()) == null || !entity.aggressTarget.isAlive()) {
            entity.aggressTarget = null;
            entity.setRandomTargetHeading();
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.shouldExecute();
    }
}
