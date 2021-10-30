package net.tropicraft.core.common.entity.underdasea;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.WaterMobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;
import net.tropicraft.core.common.entity.ai.fishies.AvoidWallsGoal;
import net.tropicraft.core.common.entity.ai.fishies.RandomSwimGoal;
import net.tropicraft.core.common.entity.ai.fishies.SwimToAvoidEntityGoal;
import net.tropicraft.core.common.entity.ai.fishies.TargetPreyGoal;
import net.tropicraft.core.common.item.TropicraftItems;

import java.util.ArrayList;
import java.util.EnumSet;

public class SharkEntity extends TropicraftFishEntity {

    private static final DataParameter<Boolean> IS_BOSS = EntityDataManager.defineId(SharkEntity.class, DataSerializers.BOOLEAN);

    private final ServerBossInfo bossInfo = new ServerBossInfo(getDisplayName(), BossInfo.Color.BLUE, BossInfo.Overlay.PROGRESS);
    private ArrayList<ServerPlayerEntity> bossTargets = new ArrayList<>();
    private boolean hasSetBoss = false;

    public SharkEntity(EntityType<? extends WaterMobEntity> type, World world) {
        super(type, world);
        xpReward = 20;
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
    public void defineSynchedData() {
        super.defineSynchedData();
        getEntityData().define(IS_BOSS, false);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return WaterMobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.ATTACK_DAMAGE, 4.0);
    }

    public void setBoss() {
        getEntityData().set(IS_BOSS, true);
    }

    public boolean isBoss() {
        return this.getEntityData().get(IS_BOSS);
    }

    private void setBossTraits() {
        getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(8);
        //TODO this.setDropStack(ItemRegistry.yellowFlippers, 1);
        setCustomName(new StringTextComponent("Elder Hammerhead"));
        setCustomNameVisible(true);
        setSwimSpeeds(1.1f, 2.2f, 1.5f, 3f, 5f);
        getAttribute(Attributes.MAX_HEALTH).setBaseValue(20);
        // TODO in renderer - this.setTexture("hammerhead4");
        if (!level.isClientSide) {
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

            if (!level.isClientSide) {
                // Search for suitable target
                PlayerEntity nearest = level.getNearestPlayer(this, 64D);
                if (nearest != null) {
                    if (canSee(nearest) && nearest.isInWater() && !nearest.isCreative() && nearest.isAlive()) {
                        aggressTarget = nearest;
                        setTargetHeading(aggressTarget.getX(), aggressTarget.getY() + 1, aggressTarget.getZ(), true);
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
                if (this.getHealth() < this.getMaxHealth() && this.tickCount % 80 == 0 && this.aggressTarget == null) {
                    this.heal(1f);
                    this.spawnAnim();
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
    public void addAdditionalSaveData(CompoundNBT n) {
        n.putBoolean("isBoss", isBoss());
        super.addAdditionalSaveData(n);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT n) {
        if (n.getBoolean("isBoss")) {
            setBoss();
        }
        super.readAdditionalSaveData(n);
    }

    @Override
    public boolean removeWhenFarAway(double p) {
        return !isBoss() && super.removeWhenFarAway(p);
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return new ItemStack(TropicraftItems.HAMMERHEAD_SPAWN_EGG.get());
    }
}
