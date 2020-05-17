package net.tropicraft.core.common.entity.underdasea;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.WaterMobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;
import net.tropicraft.core.common.entity.ai.fishies.AvoidWallsGoal;
import net.tropicraft.core.common.entity.ai.fishies.RandomSwimGoal;
import net.tropicraft.core.common.entity.ai.fishies.SwimToAvoidEntityGoal;
import net.tropicraft.core.common.entity.ai.fishies.TargetPreyGoal;

import java.util.ArrayList;
import java.util.EnumSet;

public class SharkEntity extends TropicraftFishEntity {

    private static final DataParameter<Boolean> IS_BOSS = EntityDataManager.createKey(SharkEntity.class, DataSerializers.BOOLEAN);

    private final ServerBossInfo bossInfo = new ServerBossInfo(getDisplayName(), BossInfo.Color.BLUE, BossInfo.Overlay.PROGRESS);
    private ArrayList<ServerPlayerEntity> bossTargets = new ArrayList<>();
    private boolean hasSetBoss = false;

    public SharkEntity(EntityType<? extends WaterMobEntity> type, World world) {
        super(type, world);
        experienceValue = 20;
        this.setApproachesPlayers(true);
        // TODO - this.setDropStack(ItemRegistry.fertilizer, 3);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        //goalSelector.addGoal(0, new EntityAISwimAvoidPredator(0, this, 2D));
        goalSelector.addGoal(0, new AvoidWallsGoal(EnumSet.of(Goal.Flag.MOVE), this));
        if (fleeFromPlayers) {
            goalSelector.addGoal(0, new SwimToAvoidEntityGoal(EnumSet.of(Goal.Flag.MOVE), this, 5F, new Class[] {PlayerEntity.class}));
        }
        goalSelector.addGoal(2, new TargetPreyGoal(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK), this));
        goalSelector.addGoal(2, new RandomSwimGoal(EnumSet.of(Goal.Flag.MOVE), this));
    }

    @Override
    public void registerData() {
        super.registerData();
        getDataManager().register(IS_BOSS, false);
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
        getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4);
    }

    public void setBoss() {
        getDataManager().set(IS_BOSS, true);
    }

    public boolean isBoss() {
        return this.getDataManager().get(IS_BOSS);
    }

    private void setBossTraits() {
        getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(8);
        //TODO this.setDropStack(ItemRegistry.yellowFlippers, 1);
        setCustomName(new StringTextComponent("Elder Hammerhead"));
        setCustomNameVisible(true);
        setSwimSpeeds(1.1f, 2.2f, 1.5f, 3f, 5f);
        getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20);
        // TODO in renderer - this.setTexture("hammerhead4");
        if (!world.isRemote) {
            bossInfo.setName(new StringTextComponent("Elder Hammerhead"));
        }
        hasSetBoss = true;
    }

    @Override
    public void tick() {
        super.tick();
        if (isBoss()) {
            // Set state to boss
            if (!hasSetBoss) {
                setBossTraits();
            }

            if (!world.isRemote) {
                // Search for suitable target
                PlayerEntity nearest = world.getClosestPlayer(this, 64D);
                if (nearest != null) {
                    if (canEntityBeSeen(nearest) && nearest.isInWater() && !nearest.isCreative() && nearest.isAlive()) {
                        aggressTarget = nearest;
                        setTargetHeading(aggressTarget.getPosX(), aggressTarget.getPosY() + 1, aggressTarget.getPosZ(), true);
                        // Show health bar to target player
                        if (nearest instanceof ServerPlayerEntity) {
                            if (!bossInfo.getPlayers().contains(nearest)) {
                                bossTargets.add((ServerPlayerEntity) nearest);
                                bossInfo.addPlayer((ServerPlayerEntity) nearest);
                            }
                        }
                    } else {
                        clearBossTargets();
                    }
                } else {
                    clearBossTargets();
                }

                // Heal if no target
                if (this.getHealth() < this.getMaxHealth() && this.ticksExisted % 80 == 0 && this.aggressTarget == null) {
                    this.heal(1f);
                    this.spawnExplosionParticle();
                }
                // Update health bar
                this.bossInfo.setPercent(this.rangeMap(this.getHealth(), 0, this.getMaxHealth(), 0, 1));
            }
        }
    }

    private void clearBossTargets() {
        if (bossTargets.size() > 0) {
            for (ServerPlayerEntity p : bossTargets) {
                bossInfo.removePlayer(p);
            }
            bossTargets.clear();
        }
    }

    @Override
    public void writeAdditional(CompoundNBT n) {
        n.putBoolean("isBoss", isBoss());
        super.writeAdditional(n);
    }

    @Override
    public void readAdditional(CompoundNBT n) {
        if (n.getBoolean("isBoss")) {
            setBoss();
        }
        super.readAdditional(n);
    }

    @Override
    public boolean canDespawn(double p) {
        return !isBoss() && super.canDespawn(p);
    }
}
