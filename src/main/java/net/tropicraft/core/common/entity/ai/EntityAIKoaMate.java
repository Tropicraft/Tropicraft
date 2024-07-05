package net.tropicraft.core.common.entity.ai;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.tropicraft.core.common.entity.passive.EntityKoaBase;

import java.util.EnumSet;
import java.util.List;

public class EntityAIKoaMate extends Goal
{

    /**
     * TODO: potential problems from vanilla one
     * - mate isnt set isMating
     * - does not check if already mating
     */

    private final EntityKoaBase villagerObj;
    private EntityKoaBase mate;
    private final Level world;

    //counts down from 300 when mating starts, on 0 mate completes
    private int matingTimeout;

    private final long TIME_BETWEEN_POPULATION_CHECKS = 20*10;
    private final int MAX_TOWN_POPULATION = 10;
    private long lastTimeCheckedVillagePopulation = -1;
    private int cachedVillagePopulation = 0;

    public EntityAIKoaMate(EntityKoaBase villagerIn)
    {
        this.villagerObj = villagerIn;
        this.world = villagerIn.level();
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean canUse()
    {
        //adult cooldown
        if (this.villagerObj.getAge() != 0)
        {
            return false;
        }
        else if (this.villagerObj.getRandom().nextInt(500) != 0)
        {
            return false;
        }
        else
        {
            if (this.canTownHandleMoreVillagers() && this.villagerObj.getIsWillingToMate(true)) {
                List<EntityKoaBase> listEntities = this.world.getEntitiesOfClass(EntityKoaBase.class, this.villagerObj.getBoundingBox().inflate(8.0D, 3.0D, 8.0D));
                EntityKoaBase clEnt = null;
                double clDist = 9999;
                for (EntityKoaBase ent : listEntities) {
                    if (ent != villagerObj) {
                        if (ent.getIsWillingToMate(true) && !ent.isBaby() && !this.villagerObj.isBaby()) {
                            if (villagerObj.distanceTo(ent) < clDist) {
                                clEnt = ent;
                                clDist = villagerObj.distanceTo(ent);
                            }
                        }
                    }
                }
                if (clEnt != null) {
                    this.mate = clEnt;
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
        this.matingTimeout = 300;
        this.villagerObj.setMating(true);
        if (this.mate != null) {
            this.mate.setMating(true);
        }
    }

    /**
     * Resets the task
     */
    @Override
    public void stop() {
        this.mate = null;
        this.villagerObj.setMating(false);
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean canContinueToUse() {
        boolean result = this.matingTimeout >= 0 && this.canTownHandleMoreVillagers() && this.villagerObj.getAge() == 0 && this.villagerObj.getIsWillingToMate(false);
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
        --this.matingTimeout;
        this.villagerObj.getLookControl().setLookAt(this.mate, 10.0F, 30.0F);

        if (this.villagerObj.distanceToSqr(this.mate) > 2.25D)
        {
            this.villagerObj.getNavigation().moveTo(this.mate, 0.75D);
        }
        else if (this.matingTimeout == 0 && this.mate.isMating())
        {
            this.mate.setMating(false);
            //System.out.println("mate complete");
            this.giveBirth();
        }

        if (this.villagerObj.getRandom().nextInt(35) == 0)
        {
            this.world.broadcastEntityEvent(this.villagerObj, (byte)12);
        }
    }

    //TODO: for now just checks if villagers in general area, potentially prone to overpopulation due to half chunks loaded
    // fix in 1.14 by readding village object, or migrating to new vanilla villager system
    /**
     * Calculates if town can handle more villagers
     * result is cached due to active ticking of mating constantly querying this method
     * @return
     */
    private boolean canTownHandleMoreVillagers() {
        double range = 100;
        if (lastTimeCheckedVillagePopulation + TIME_BETWEEN_POPULATION_CHECKS < world.getGameTime()) {
            lastTimeCheckedVillagePopulation = world.getGameTime();
            List<EntityKoaBase> listEntities = this.world.getEntitiesOfClass(EntityKoaBase.class, this.villagerObj.getBoundingBox().inflate(range, range, range));
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
        AgeableMob entityvillager = this.villagerObj.getBreedOffspring((ServerLevel) world, this.mate);
        this.mate.setAge(6000);
        this.villagerObj.setAge(6000);
        this.mate.setIsWillingToMate(false);
        this.villagerObj.setIsWillingToMate(false);

        //final net.neoforged.event.entity.living.BabyEntitySpawnEvent event = new net.neoforged.event.entity.living.BabyEntitySpawnEvent(villagerObj, mate, entityvillager);
        //if (net.neoforged.common.MinecraftForge.EVENT_BUS.post(event) || event.getChild() == null) { return; }
        //entityvillager = event.getChild();
        entityvillager.setAge(-24000);
        entityvillager.moveTo(villagerObj.getX(), villagerObj.getY(), villagerObj.getZ(), 0.0F, 0.0F);
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



        this.world.addFreshEntity(entityvillager);
        this.world.broadcastEntityEvent(entityvillager, (byte)12);
    }
}


