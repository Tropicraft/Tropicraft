package net.tropicraft.core.common.entity.neutral;

import com.google.common.base.Predicate;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.tropicraft.core.common.drinks.Drink;
import net.tropicraft.core.common.entity.ai.vmonkey.*;
import net.tropicraft.core.common.item.CocktailItem;
import net.tropicraft.core.common.item.TropicraftItems;

import javax.annotation.Nullable;

public class VMonkeyEntity extends TameableEntity {

    private static final DataParameter<Byte> DATA_FLAGS = EntityDataManager.createKey(VMonkeyEntity.class, DataSerializers.BYTE);
    private static final int FLAG_CLIMBING = 1 << 0;

    public static final Predicate<LivingEntity> FOLLOW_PREDICATE = ent -> {
        if (ent == null) return false;
        if (!(ent instanceof PlayerEntity)) return false;

        PlayerEntity player = (PlayerEntity) ent;
        ItemStack heldMain = player.getHeldItemMainhand();
        ItemStack heldOff = player.getHeldItemOffhand();

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

    public VMonkeyEntity(EntityType<? extends TameableEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected void registerData() {
        super.registerData();
        dataManager.register(DATA_FLAGS, (byte) 0);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return TameableEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MAX_HEALTH, 20.0)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 2.0);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(1, new SwimGoal(this));
        goalSelector.addGoal(3, new MonkeyFollowNearestPinaColadaHolderGoal(this, 1.0D, 2.0F, 10.0F));
        goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.4F));
        goalSelector.addGoal(3, new MonkeyPickUpPinaColadaGoal(this));
        goalSelector.addGoal(2, new MonkeyStealDrinkGoal(this));
        goalSelector.addGoal(2, new MonkeySitAndDrinkGoal(this));
        goalSelector.addGoal(2, new MonkeyAngryThrowGoal(this));
        goalSelector.addGoal(4, new MonkeySitInChairGoal(this));
        goalSelector.addGoal(4, new SitGoal(this));
        goalSelector.addGoal(6, new MeleeAttackGoal(this, 1.0D, true));
        goalSelector.addGoal(7, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        goalSelector.addGoal(8, new RandomWalkingGoal(this, 1.0D));
        goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        goalSelector.addGoal(9, new LookRandomlyGoal(this));
        targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        targetSelector.addGoal(3, new HurtByTargetGoal(this));
    }

    @Override
    public void writeAdditional(final CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putByte("MonkeyFlags", getMonkeyFlags());
    }

    @Override
    public void readAdditional(final CompoundNBT compound) {
        super.readAdditional(compound);
        setMonkeyFlags(compound.getByte("MonkeyFlags"));
    }

    public LivingEntity getFollowing() {
        return following;
    }

    public void setFollowing(@Nullable final LivingEntity following) {
        this.following = following;
    }

    public boolean selfHoldingDrink(Drink drink) {
        ItemStack heldItem = getHeldItemMainhand();
        if (heldItem.getItem() instanceof CocktailItem) {
            return CocktailItem.getDrink(heldItem) == drink;
        }
        return false;
    }

    private void setMonkeyFlags(final byte flags) {
        getDataManager().set(DATA_FLAGS, flags);
    }

    private byte getMonkeyFlags() {
        return getDataManager().get(DATA_FLAGS);
    }

    public boolean isClimbing() {
        return getMonkeyFlag(FLAG_CLIMBING);
    }

    private void setClimbing(boolean state) {
        setMonkeyFlag(FLAG_CLIMBING, state);
    }

    public void setMonkeyFlag(int id, boolean flag) {
        if (flag) {
            dataManager.set(DATA_FLAGS, (byte)(dataManager.get(DATA_FLAGS) | id));
        } else {
            dataManager.set(DATA_FLAGS, (byte)(dataManager.get(DATA_FLAGS) & ~id));
        }
    }

    private boolean getMonkeyFlag(int flag) {
        return (dataManager.get(DATA_FLAGS) & flag) != 0;
    }

    @Override
    public ActionResultType getEntityInteractionResult(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (isTamed()) {
            if (isOwner(player) && !world.isRemote) {
                this.setSitting(!isQueuedToSit());
                isJumping = false;
                navigator.clearPath();
                setAttackTarget(null);
                setAggroed(false);
            }
        } else if (!stack.isEmpty() && isBreedingItem(stack)) {
            if (!player.abilities.isCreativeMode) {
                stack.shrink(1);
            }

            if (!world.isRemote) {
                if (rand.nextInt(3) == 0) {
                    setTamed(true);
                    navigator.clearPath();
                    setAttackTarget(null);
                    this.setSitting(true);
                    setHealth(20.0F);
                    setOwnerId(player.getUniqueID());
                    world.setEntityState(this, (byte) 7);
                } else {
                    world.setEntityState(this, (byte) 6);
                }
            }

            return ActionResultType.PASS;
        }

        return super.getEntityInteractionResult(player, hand);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return CocktailItem.getDrink(stack) == Drink.PINA_COLADA;
    }

    @Nullable
    @Override
    public AgeableEntity createChild(ServerWorld world, AgeableEntity entity) {
        return null;
    }

    @Override
    public boolean shouldAttackEntity(LivingEntity target, LivingEntity owner) {
        // Only attack players, and only when not tamed
        // NOTE: Maybe we want to attack other players though?
        return !isTamed() && target.getType() == EntityType.PLAYER;
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        boolean damaged = entity.attackEntityFrom(DamageSource.causeMobDamage(this), (float) getAttribute(Attributes.ATTACK_DAMAGE).getValue());

        if (damaged) {
            applyEnchantments(this, entity);
        }

        return damaged;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (isInvulnerableTo(source)) {
            return false;
        } else {
            Entity entity = source.getTrueSource();
            this.setSitting(false);

            if (entity != null && entity.getType() != EntityType.PLAYER && !(entity instanceof ArrowEntity)) {
                amount = (amount + 1.0F) / 2.0F;
            }

            return super.attackEntityFrom(source, amount);
        }
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return new ItemStack(TropicraftItems.V_MONKEY_SPAWN_EGG.get());
    }

    public boolean isMadAboutStolenAlcohol() {
        return madAboutStolenAlcohol;
    }

    public void setMadAboutStolenAlcohol(boolean madAboutStolenAlcohol) {
        this.madAboutStolenAlcohol = madAboutStolenAlcohol;
    }
}
