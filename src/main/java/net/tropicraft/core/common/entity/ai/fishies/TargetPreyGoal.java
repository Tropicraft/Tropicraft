package net.tropicraft.core.common.entity.ai.fishies;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.AABB;
import net.tropicraft.core.common.entity.underdasea.TropicraftFishEntity;

import java.util.EnumSet;
import java.util.List;

public class TargetPreyGoal extends Goal {
    public TropicraftFishEntity entity;
    public RandomSource rand;

    public TargetPreyGoal(EnumSet<Flag> flags, TropicraftFishEntity entityObjIn) {
        entity = entityObjIn;
        rand = entity.getRandom();
        setFlags(flags);
    }

    @Override
    public boolean canUse() {
        return entity.isInWater() && entity.canAggress && entity.eatenFishAmount < entity.maximumEatAmount;
    }

    @Override
    public void tick() {
        super.tick();
        
        // Target selection
        AABB entityBB = entity.getBoundingBox();
        if (entity.tickCount % 80 == 0 && entity.aggressTarget == null || entity.getCommandSenderWorld().getEntity(entity.aggressTarget.getId()) == null) {
                List<Entity> list = entity.level().getEntities(entity, entityBB.inflate(20D, 20D, 20D).move(0.0D, -8.0D, 0.0D), e -> e.isAlive());
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
                    if(!entity.hasLineOfSight(ent)) skip = true;
                    
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
            if(entity.distanceToSqr(entity.aggressTarget) <= entity.getBbWidth()) {
                if(entity.aggressTarget instanceof LivingEntity) {
                    entity.aggressTarget.hurt(entity.damageSources().mobAttack(entity), (float) entity.getAttribute(Attributes.ATTACK_DAMAGE).getValue());
                }
                if(entity.aggressTarget instanceof TropicraftFishEntity) {
                    // Was eaten, cancel smoke
                    AABB aggressBB = entity.aggressTarget.getBoundingBox();
                    if(entityBB.maxY - entityBB.minY > aggressBB.maxY - aggressBB.minY) {
                        entity.aggressTarget.remove(Entity.RemovalReason.KILLED);
                        entity.heal(1);
                        entity.eatenFishAmount++;
                    }
                }
                entity.setRandomTargetHeading();
            }else {
                if(entity.hasLineOfSight(entity.aggressTarget) && entity.tickCount % 20 == 0) {
                    entity.setTargetHeading(entity.aggressTarget.getX(), entity.aggressTarget.getY(), entity.aggressTarget.getZ(), true);
                }
            }
            if(entity.aggressTarget != null) {
                if(!entity.hasLineOfSight(entity.aggressTarget) || !entity.aggressTarget.isInWater()) {
                    entity.aggressTarget = null;
                    entity.setRandomTargetHeading();
                }
            }
        }

        if(entity.aggressTarget == null || entity.level().getEntity(entity.aggressTarget.getId()) == null || !entity.aggressTarget.isAlive()) {
            entity.aggressTarget = null;
            entity.setRandomTargetHeading();
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }
}
