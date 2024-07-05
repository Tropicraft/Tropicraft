package net.tropicraft.core.common.entity.ai;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.tropicraft.core.common.entity.passive.EntityKoaBase;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;

public class EntityAIKoaMate extends Goal {

    /**
     * TODO: potential problems from vanilla one
     * - mate isnt set isMating
     * - does not check if already mating
     */

    private final EntityKoaBase villagerObj;
    @Nullable
    private EntityKoaBase mate;
    private final Level world;

    //counts down from 300 when mating starts, on 0 mate completes
    private int matingTimeout;

    private final long TIME_BETWEEN_POPULATION_CHECKS = 20 * 10;
    private final int MAX_TOWN_POPULATION = 10;
    private long lastTimeCheckedVillagePopulation = -1;
    private int cachedVillagePopulation = 0;

    public EntityAIKoaMate(EntityKoaBase villagerIn) {
        villagerObj = villagerIn;
        world = villagerIn.level();
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean canUse() {
        //adult cooldown
        if (villagerObj.getAge() != 0) {
            return false;
        } else if (villagerObj.getRandom().nextInt(500) != 0) {
            return false;
        } else {
            if (canTownHandleMoreVillagers() && villagerObj.getIsWillingToMate(true)) {
                List<EntityKoaBase> listEntities = world.getEntitiesOfClass(EntityKoaBase.class, villagerObj.getBoundingBox().inflate(8.0, 3.0, 8.0));
                EntityKoaBase clEnt = null;
                double clDist = 9999;
                for (EntityKoaBase ent : listEntities) {
                    if (ent != villagerObj) {
                        if (ent.getIsWillingToMate(true) && !ent.isBaby() && !villagerObj.isBaby()) {
                            if (villagerObj.distanceTo(ent) < clDist) {
                                clEnt = ent;
                                clDist = villagerObj.distanceTo(ent);
                            }
                        }
                    }
                }
                if (clEnt != null) {
                    mate = clEnt;
                    //System.out.println("start mate");
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void start() {
        matingTimeout = 300;
        villagerObj.setMating(true);
        if (mate != null) {
            mate.setMating(true);
        }
    }

    /**
     * Resets the task
     */
    @Override
    public void stop() {
        mate = null;
        villagerObj.setMating(false);
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean canContinueToUse() {
        boolean result = matingTimeout >= 0 && canTownHandleMoreVillagers() && villagerObj.getAge() == 0 && villagerObj.getIsWillingToMate(false);
        if (!result) {
            //System.out.println("mating reset");
        }
        return result;
    }

    /**
     * Updates the task
     */
    @Override
    public void tick() {
        --matingTimeout;
        villagerObj.getLookControl().setLookAt(mate, 10.0f, 30.0f);

        if (villagerObj.distanceToSqr(mate) > 2.25) {
            villagerObj.getNavigation().moveTo(mate, 0.75);
        } else if (matingTimeout == 0 && mate.isMating()) {
            mate.setMating(false);
            //System.out.println("mate complete");
            giveBirth();
        }

        if (villagerObj.getRandom().nextInt(35) == 0) {
            world.broadcastEntityEvent(villagerObj, (byte) 12);
        }
    }

    //TODO: for now just checks if villagers in general area, potentially prone to overpopulation due to half chunks loaded
    // fix in 1.14 by readding village object, or migrating to new vanilla villager system

    /**
     * Calculates if town can handle more villagers
     * result is cached due to active ticking of mating constantly querying this method
     *
     * @return
     */
    private boolean canTownHandleMoreVillagers() {
        double range = 100;
        if (lastTimeCheckedVillagePopulation + TIME_BETWEEN_POPULATION_CHECKS < world.getGameTime()) {
            lastTimeCheckedVillagePopulation = world.getGameTime();
            List<EntityKoaBase> listEntities = world.getEntitiesOfClass(EntityKoaBase.class, villagerObj.getBoundingBox().inflate(range, range, range));
            cachedVillagePopulation = listEntities.size();
            //System.out.println("update cached koa population to: " + cachedVillagePopulation);
            return listEntities.size() < MAX_TOWN_POPULATION;
        } else {
            //System.out.println("return cached koa population: " + cachedVillagePopulation);
            return cachedVillagePopulation < MAX_TOWN_POPULATION;
        }
    }

    /*private boolean canTownHandleMoreVillagers112()
    {
        TownKoaVillage village = villagerObj.getVillage();

        if (village == null) {
            if (villagerObj.findAndSetTownID(true)) {
                village = villagerObj.getVillage();

                //just in case
                if (village == null) return false;
            } else {
                return false;
            }
        }

        return village.getPopulationSize() < village.getMaxPopulationSize();
    }*/

    //TODO: 1.14 readd
    private void giveBirth() {
        AgeableMob entityvillager = villagerObj.getBreedOffspring((ServerLevel) world, mate);
        mate.setAge(6000);
        villagerObj.setAge(6000);
        mate.setIsWillingToMate(false);
        villagerObj.setIsWillingToMate(false);

        //final net.neoforged.event.entity.living.BabyEntitySpawnEvent event = new net.neoforged.event.entity.living.BabyEntitySpawnEvent(villagerObj, mate, entityvillager);
        //if (net.neoforged.common.MinecraftForge.EVENT_BUS.post(event) || event.getChild() == null) { return; }
        //entityvillager = event.getChild();
        entityvillager.setAge(-24000);
        entityvillager.moveTo(villagerObj.getX(), villagerObj.getY(), villagerObj.getZ(), 0.0f, 0.0f);
        if (entityvillager instanceof EntityKoaBase) {
            ((EntityKoaBase) entityvillager).setVillageAndDimID(villagerObj.getVillageID(), villagerObj.getVillageDimension());
            entityvillager.restrictTo(villagerObj.getRestrictCenter(), EntityKoaBase.MAX_HOME_DISTANCE);

            //TODO: 1.14 readd
            /*TownKoaVillage village = villagerObj.getVillage();
            if (village != null) {
                ((EntityKoaBase) entityvillager).postSpawnGenderFix();

                village.addEntity(entityvillager);
            }*/

            ((EntityKoaBase) entityvillager).updateUniqueEntityAI();

            entityvillager.level().playSound(null, entityvillager.blockPosition(), SoundEvents.CHICKEN_EGG, SoundSource.AMBIENT, 1, 1);
        }

        world.addFreshEntity(entityvillager);
        world.broadcastEntityEvent(entityvillager, (byte) 12);
    }
}


