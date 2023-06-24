package net.tropicraft.core.common.entity.underdasea;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.HitResult;
import net.tropicraft.core.common.item.TropicraftItems;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class SharkEntity extends WaterAnimal {

    private static final EntityDataAccessor<Boolean> IS_BOSS = SynchedEntityData.defineId(SharkEntity.class, EntityDataSerializers.BOOLEAN);

    private final ServerBossEvent bossInfo = new ServerBossEvent(getDisplayName(), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.PROGRESS);
    private final ArrayList<ServerPlayer> bossTargets = new ArrayList<>();
    private boolean hasSetBoss = false;

    public SharkEntity(EntityType<? extends WaterAnimal> type, Level world) {
        super(type, world);
        xpReward = 20;
        this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.02F, 0.1F, true);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
//        this.setApproachesPlayers(true);
        // TODO - this.setDropStack(ItemRegistry.fertilizer, 3);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        //goalSelector.addGoal(0, new EntityAISwimAvoidPredator(0, this, 2D));
//        goalSelector.addGoal(0, new AvoidWallsGoal(EnumSet.of(Goal.Flag.MOVE), this));
        this.goalSelector.addGoal(0, new BreathAirGoal(this));
        this.goalSelector.addGoal(0, new TryFindWaterGoal(this));
        this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1.0D, 10));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));

//        if (fleeFromPlayers) {
//            goalSelector.addGoal(0, new SwimToAvoidEntityGoal(EnumSet.of(Goal.Flag.MOVE), this, 5F, new Class[] {Player.class}));
//        }
//        goalSelector.addGoal(2, new TargetPreyGoal(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK), this));
//        goalSelector.addGoal(2, new RandomSwimGoal(EnumSet.of(Goal.Flag.MOVE), this));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        this.setAirSupply(this.getMaxAirSupply());
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    @Override
    public void defineSynchedData() {
        super.defineSynchedData();
        getEntityData().define(IS_BOSS, false);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return WaterAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.MOVEMENT_SPEED, 1.2)
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
        setCustomName(new TextComponent("Elder Hammerhead"));
        setCustomNameVisible(true);
//        setSwimSpeeds(1.1f, 2.2f, 1.5f, 3f, 5f);
        getAttribute(Attributes.MAX_HEALTH).setBaseValue(20);
        // TODO in renderer - this.setTexture("hammerhead4");
        if (!level.isClientSide) {
            bossInfo.setName(new TextComponent("Elder Hammerhead"));
        }
        hasSetBoss = true;
    }

    @Override
    public void tick() {
        super.tick();
//        if (isBoss()) {
//            // Set state to boss
//            if (!hasSetBoss) {
//                setBossTraits();
//            }
//
//            if (!level.isClientSide) {
//                // Search for suitable target
//                Player nearest = level.getNearestPlayer(this, 64D);
//                if (nearest != null) {
//                    if (hasLineOfSight(nearest) && nearest.isInWater() && !nearest.isCreative() && nearest.isAlive()) {
//                        aggressTarget = nearest;
//                        setTargetHeading(aggressTarget.getX(), aggressTarget.getY() + 1, aggressTarget.getZ(), true);
//                        // Show health bar to target player
//                        if (nearest instanceof ServerPlayer) {
//                            if (!bossInfo.getPlayers().contains(nearest)) {
//                                bossTargets.add((ServerPlayer) nearest);
//                                bossInfo.addPlayer((ServerPlayer) nearest);
//                            }
//                        }
//                    } else {
//                        clearBossTargets();
//                    }
//                } else {
//                    clearBossTargets();
//                }
//
//                // Heal if no target
//                if (this.getHealth() < this.getMaxHealth() && this.tickCount % 80 == 0 && this.aggressTarget == null) {
//                    this.heal(1f);
//                    this.spawnAnim();
//                }
//                // Update health bar
//                this.bossInfo.setProgress(this.rangeMap(this.getHealth(), 0, this.getMaxHealth(), 0, 1));
//            }
//        }
    }

    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        return new WaterBoundPathNavigation(this, pLevel);
    }

    private void clearBossTargets() {
        if (bossTargets.size() > 0) {
            for (ServerPlayer p : bossTargets) {
                bossInfo.removePlayer(p);
            }
            bossTargets.clear();
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag n) {
        n.putBoolean("isBoss", isBoss());
        super.addAdditionalSaveData(n);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag n) {
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
    public ItemStack getPickedResult(HitResult target) {
        return new ItemStack(TropicraftItems.HAMMERHEAD_SPAWN_EGG.get());
    }
}
