package net.tropicraft.core.common.entity.neutral;

import com.google.common.base.Predicate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.tropicraft.core.common.drinks.Drink;
import net.tropicraft.core.common.entity.ai.vmonkey.*;
import net.tropicraft.core.common.item.CocktailItem;
import net.tropicraft.core.common.item.TropicraftItems;

import javax.annotation.Nullable;

public class VMonkeyEntity extends TamableAnimal {

    private static final EntityDataAccessor<Byte> DATA_FLAGS = SynchedEntityData.defineId(VMonkeyEntity.class, EntityDataSerializers.BYTE);
    private static final int FLAG_CLIMBING = 1 << 0;

    public static final Predicate<LivingEntity> FOLLOW_PREDICATE = ent -> {
        if (ent == null) return false;
        if (!(ent instanceof Player)) return false;

        Player player = (Player) ent;
        ItemStack heldMain = player.getMainHandItem();
        ItemStack heldOff = player.getOffhandItem();

        if (heldMain.getItem() instanceof CocktailItem) {
            if (CocktailItem.getDrink(heldMain) == Drink.PINA_COLADA) {
                return true;
            }
        }

        if (heldOff.getItem() instanceof CocktailItem) {
            return CocktailItem.getDrink(heldOff) == Drink.PINA_COLADA;
        }

        return false;
    };

    /** Entity this monkey is following around */
    private LivingEntity following;
    private boolean madAboutStolenAlcohol;

    public VMonkeyEntity(EntityType<? extends TamableAnimal> type, Level world) {
        super(type, world);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DATA_FLAGS, (byte) 0);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ATTACK_DAMAGE, 2.0);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(1, new FloatGoal(this));
        goalSelector.addGoal(3, new MonkeyFollowNearestPinaColadaHolderGoal(this, 1.0D, 2.0F, 10.0F));
        goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.4F));
        goalSelector.addGoal(3, new MonkeyPickUpPinaColadaGoal(this));
        goalSelector.addGoal(2, new MonkeyStealDrinkGoal(this));
        goalSelector.addGoal(2, new MonkeySitAndDrinkGoal(this));
        goalSelector.addGoal(2, new MonkeyAngryThrowGoal(this));
        goalSelector.addGoal(4, new MonkeySitInChairGoal(this));
        goalSelector.addGoal(4, new SitWhenOrderedToGoal(this));
        goalSelector.addGoal(6, new MeleeAttackGoal(this, 1.0D, true));
        goalSelector.addGoal(7, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        goalSelector.addGoal(8, new RandomStrollGoal(this, 1.0D));
        goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        targetSelector.addGoal(3, new HurtByTargetGoal(this));
    }

    @Override
    public void addAdditionalSaveData(final CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putByte("MonkeyFlags", getMonkeyFlags());
    }

    @Override
    public void readAdditionalSaveData(final CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        setMonkeyFlags(compound.getByte("MonkeyFlags"));
    }

    public LivingEntity getFollowing() {
        return following;
    }

    public void setFollowing(@Nullable final LivingEntity following) {
        this.following = following;
    }

    public boolean selfHoldingDrink(Drink drink) {
        ItemStack heldItem = getMainHandItem();
        if (heldItem.getItem() instanceof CocktailItem) {
            return CocktailItem.getDrink(heldItem) == drink;
        }
        return false;
    }

    private void setMonkeyFlags(final byte flags) {
        getEntityData().set(DATA_FLAGS, flags);
    }

    private byte getMonkeyFlags() {
        return getEntityData().get(DATA_FLAGS);
    }

    public boolean isClimbing() {
        return getMonkeyFlag(FLAG_CLIMBING);
    }

    private void setClimbing(boolean state) {
        setMonkeyFlag(FLAG_CLIMBING, state);
    }

    public void setMonkeyFlag(int id, boolean flag) {
        if (flag) {
            entityData.set(DATA_FLAGS, (byte)(entityData.get(DATA_FLAGS) | id));
        } else {
            entityData.set(DATA_FLAGS, (byte)(entityData.get(DATA_FLAGS) & ~id));
        }
    }

    private boolean getMonkeyFlag(int flag) {
        return (entityData.get(DATA_FLAGS) & flag) != 0;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (isTame()) {
            if (isOwnedBy(player) && !level().isClientSide) {
                this.setOrderedToSit(!isOrderedToSit());
                jumping = false;
                navigation.stop();
                setTarget(null);
                setAggressive(false);
            }
        } else if (!stack.isEmpty() && isFood(stack)) {
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }

            if (!level().isClientSide) {
                if (random.nextInt(3) == 0) {
                    setTame(true);
                    navigation.stop();
                    setTarget(null);
                    this.setOrderedToSit(true);
                    setHealth(20.0F);
                    setOwnerUUID(player.getUUID());
                    level().broadcastEntityEvent(this, (byte) 7);
                } else {
                    level().broadcastEntityEvent(this, (byte) 6);
                }
            }

            return InteractionResult.PASS;
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return CocktailItem.getDrink(stack) == Drink.PINA_COLADA;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
        return null;
    }

    @Override
    public boolean wantsToAttack(LivingEntity target, LivingEntity owner) {
        // Only attack players, and only when not tamed
        // NOTE: Maybe we want to attack other players though?
        return !isTame() && target.getType() == EntityType.PLAYER;
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        boolean damaged = entity.hurt(damageSources().mobAttack(this), (float) getAttribute(Attributes.ATTACK_DAMAGE).getValue());

        if (damaged) {
            doEnchantDamageEffects(this, entity);
        }

        return damaged;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (isInvulnerableTo(source)) {
            return false;
        } else {
            Entity entity = source.getEntity();
            this.setOrderedToSit(false);

            if (entity != null && entity.getType() != EntityType.PLAYER && !(entity instanceof Arrow)) {
                amount = (amount + 1.0F) / 2.0F;
            }

            return super.hurt(source, amount);
        }
    }

    public boolean isMadAboutStolenAlcohol() {
        return madAboutStolenAlcohol;
    }

    public void setMadAboutStolenAlcohol(boolean madAboutStolenAlcohol) {
        this.madAboutStolenAlcohol = madAboutStolenAlcohol;
    }
}
