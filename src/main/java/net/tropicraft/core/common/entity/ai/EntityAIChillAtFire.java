package net.tropicraft.core.common.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;
import net.tropicraft.core.common.Util;
import net.tropicraft.core.common.entity.passive.EntityKoaBase;
import net.tropicraft.core.common.item.TropicraftItems;

import java.util.EnumSet;

public class EntityAIChillAtFire extends Goal {
    private final EntityKoaBase entityObj;

    private final int walkingTimeoutMax = 20 * 10;

    private int walkingTimeout;
    private int repathPentalty = 0;

    private int lookUpdateTimer = 0;
    private int randXPos = 0;
    private int randYPos = 0;
    private int randZPos = 0;

    public EntityAIChillAtFire(EntityKoaBase entityObjIn) {
        entityObj = entityObjIn;
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {

        if ((entityObj.getWantsToParty() || entityObj.druggedTime > 0) && !entityObj.listPosDrums.isEmpty()) {
            return false;
        }

        BlockPos blockpos = entityObj.blockPosition();

        if (!entityObj.level().isDay() || entityObj.level().isRaining() && entityObj.level().getBiome(blockpos).value().getPrecipitationAt(blockpos) != Biome.Precipitation.RAIN) {
            if (!isTooClose()) {
                return entityObj.level().random.nextInt(20) == 0;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {

        if ((entityObj.getWantsToParty() || entityObj.druggedTime > 0) && !entityObj.listPosDrums.isEmpty()) {
            return false;
        }

        BlockPos blockpos = entityObj.blockPosition();
        //return !this.entityObj.getNavigation().noPath();
        if (!entityObj.level().isDay() || entityObj.level().isRaining() && entityObj.level().getBiome(blockpos).value().getPrecipitationAt(blockpos) != Biome.Precipitation.RAIN) {
            return !isTooClose();
        } else {
            return entityObj.level().random.nextInt(60) != 0;
        }
    }

    @Override
    public void tick() {
        super.tick();

        boolean isClose = false;

        BlockPos blockposGoal = null;
        if (entityObj.posLastFireplaceFound != null) {
            //path to base of fire
            blockposGoal = entityObj.posLastFireplaceFound.offset(0, -1, 0);
        } else {
            blockposGoal = entityObj.getRestrictCenter();
        }

        if (blockposGoal.equals(BlockPos.ZERO)) {
            stop();
            return;
        }

        //prevent walking into the fire
        double dist = entityObj.position().distanceTo(new Vec3(blockposGoal.getX(), blockposGoal.getY(), blockposGoal.getZ()));
        if (dist < 4D && entityObj.onGround()) {
            entityObj.setSitting(true);
            entityObj.getNavigation().stop();
            isClose = true;
            if (lookUpdateTimer <= 0) {
                lookUpdateTimer = 200 + entityObj.level().random.nextInt(100);
                int range = 2;
                randXPos = entityObj.level().random.nextInt(range) - entityObj.level().random.nextInt(range);
                //stargaze
                if (entityObj.level().random.nextInt(3) == 0) {
                    randYPos = 5 + entityObj.level().random.nextInt(5);
                } else {
                    randYPos = 0;
                }
                randZPos = entityObj.level().random.nextInt(range) - entityObj.level().random.nextInt(range);

                if (entityObj.getId() % 3 == 0) {
                    entityObj.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(TropicraftItems.BAMBOO_MUG.get()));
                } else if (entityObj.getId() % 5 == 0) {
                    entityObj.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(TropicraftItems.COOKED_FROG_LEG.get()));
                } else {
                    entityObj.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(TropicraftItems.ORANGE.get()));
                }

                entityObj.heal(1);
            }
            entityObj.getLookControl().setLookAt(blockposGoal.getX() + randXPos, blockposGoal.getY() + randYPos + 1D, blockposGoal.getZ() + randZPos,
                    8.0f, 8.0f);
        } else {
            entityObj.setSitting(false);
        }

        if (!isClose) {
            if ((entityObj.getNavigation().isDone() || walkingTimeout <= 0) && repathPentalty <= 0) {

                int i = blockposGoal.getX();
                int j = blockposGoal.getY();
                int k = blockposGoal.getZ();

                boolean success = false;

                if (entityObj.distanceToSqr(Vec3.atCenterOf(blockposGoal)) > 256.0) {
                    Vec3 Vector3d = DefaultRandomPos.getPosTowards(entityObj, 14, 3, new Vec3((double) i + 0.5, (double) j, (double) k + 0.5), (float) Math.PI / 2.0f);

                    if (Vector3d != null) {
                        success = entityObj.getNavigation().moveTo(Vector3d.x, Vector3d.y, Vector3d.z, 1.0);
                    } else {
                        success = Util.tryMoveToXYZLongDist(entityObj, new BlockPos(i, j, k), 1);
                    }
                } else {
                    success = entityObj.getNavigation().moveTo((double) i + 0.5, (double) j, (double) k + 0.5, 1.0);
                }

                if (!success) {
                    repathPentalty = 40;
                } else {
                    walkingTimeout = walkingTimeoutMax;
                }
            } else {
                if (walkingTimeout > 0) {
                    walkingTimeout--;
                } else {

                }
            }
        }

        if (repathPentalty > 0) {
            repathPentalty--;
        }

        if (lookUpdateTimer > 0) {
            lookUpdateTimer--;
        }
    }

    @Override
    public void start() {
        super.start();
        //this.insidePosX = -1;
        //reset any previous path so tick can start with a fresh path
        entityObj.getNavigation().stop();
    }

    @Override
    public void stop() {
        super.stop();
        entityObj.setSitting(false);
        walkingTimeout = 0;
        /*this.insidePosX = this.doorInfo.getInsideBlockPos().getX();
        this.insidePosZ = this.doorInfo.getInsideBlockPos().getZ();
        this.doorInfo = null;*/
    }

    public boolean isTooClose() {
        BlockPos blockposGoal = null;
        if (entityObj.posLastFireplaceFound != null) {
            //path to base of fire
            blockposGoal = entityObj.posLastFireplaceFound.offset(0, -1, 0);
        } else {
            blockposGoal = entityObj.getRestrictCenter();
        }

        if (blockposGoal.equals(BlockPos.ZERO)) {
            return false;
        }

        //prevent walking into the fire
        double dist = entityObj.position().distanceTo(new Vec3(blockposGoal.getX(), blockposGoal.getY(), blockposGoal.getZ()));
        return dist <= 3D;
    }
}


