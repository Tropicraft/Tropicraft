package net.tropicraft.core.common.entity.placeable;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.InterpolationHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class FurnitureEntity extends Entity {

    private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(FurnitureEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> DAMAGE = SynchedEntityData.defineId(FurnitureEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> FORWARD_DIRECTION = SynchedEntityData.defineId(FurnitureEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TIME_SINCE_HIT = SynchedEntityData.defineId(FurnitureEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> GLUED_DOWN = SynchedEntityData.defineId(FurnitureEntity.class, EntityDataSerializers.BOOLEAN);

    private static final int DAMAGE_THRESHOLD = 40;

    private final Function<DyeColor, Item> itemLookup;

    private final InterpolationHandler interpolation = new InterpolationHandler(this, 10);

    protected FurnitureEntity(EntityType<?> entityTypeIn, Level worldIn, Map<DyeColor, ? extends RegistryEntry<? extends Item, ? extends Item>> items) {
        this(entityTypeIn, worldIn, c -> items.get(c).get());
    }

    protected FurnitureEntity(EntityType<?> entityTypeIn, Level worldIn, Function<DyeColor, Item> itemLookup) {
        super(entityTypeIn, worldIn);
        this.itemLookup = itemLookup;
        blocksBuilding = true;
        //TODO this will result in pushing acting weird - but the variable is gone in 1.17 (apparently)
        // this.pushthrough = 0.95f;
    }

    public boolean isInvulnerableTo(DamageSource source) {
        return entityData.get(GLUED_DOWN) || super.isInvulnerableToBase(source);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(COLOR, 0);
        builder.define(DAMAGE, (float) 0);
        builder.define(FORWARD_DIRECTION, 1);
        builder.define(TIME_SINCE_HIT, 0);
        builder.define(GLUED_DOWN, false);
    }

    @Override
    public void tick() {
        int timeSinceHit = getTimeSinceHit();
        if (timeSinceHit > 0) {
            setTimeSinceHit(timeSinceHit - 1);
        }

        float damage = getDamage();
        if (damage > 0) {
            setDamage(damage - 1);
        }

        Vec3 currentPos = position();
        xo = currentPos.x;
        yo = currentPos.y;
        zo = currentPos.z;

        super.tick();

        interpolation.interpolate();

        if (preventMotion()) {
            setDeltaMovement(Vec3.ZERO);
        }

        //updateRocking();

        applyEffectsFromBlocks();
        List<Entity> list = level().getEntities(this, getBoundingBox().inflate((double) 0.2f, (double) -0.01f, (double) 0.2f), EntitySelector.pushableBy(this));
        if (!list.isEmpty()) {
            for (Entity entity : list) {
                if (!entity.hasPassenger(this)) {
                    push(entity);
                }
            }
        }
    }

    protected boolean preventMotion() {
        return true;
    }

    @Override
    public InterpolationHandler getInterpolation() {
        return interpolation;
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
        if (invulnerablityCheck(player, hand) == InteractionResult.SUCCESS) {
            return InteractionResult.SUCCESS;
        }

        return super.interact(player, hand, location);
    }

    public InteractionResult invulnerablityCheck(Player pPlayer, InteractionHand pHand) {
        if (pPlayer.getItemInHand(pHand).is(Items.DEBUG_STICK)) {
            if (pPlayer instanceof ServerPlayer serverPlayer) {
                entityData.set(GLUED_DOWN, !entityData.get(GLUED_DOWN));
                serverPlayer.sendSystemMessage(Component.translatable("Invulnerability Mode: " + (entityData.get(GLUED_DOWN) ? "On" : "Off")), true);
            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float amount) {
        if (isInvulnerableTo(damageSource)) {
            if (damageSource.getEntity() instanceof Player player) {
                return player.getMainHandItem().is(Items.DEBUG_STICK);
            }

            return false;
        }

        if (!level().isClientSide() && isAlive()) {
            setForwardDirection(-getForwardDirection());
            setTimeSinceHit(10);
            setDamage(getDamage() + amount * 10.0f);
            markHurt();
            boolean flag = damageSource.getEntity() instanceof Player && ((Player) damageSource.getEntity()).getAbilities().instabuild;

            if (flag || getDamage() > DAMAGE_THRESHOLD) {
                Entity rider = getControllingPassenger();
                if (rider != null) {
                    rider.startRiding(this);
                }

                if (!flag) {
                    spawnAtLocation(serverLevel, getItemStack(), 0.0f);
                }

                remove(RemovalReason.KILLED);
            }
        }

        return true;
    }

    private ItemStack getItemStack() {
        return new ItemStack(itemLookup.apply(getColor()));
    }

    @Override
    public void animateHurt(float direction) {
        setForwardDirection(-1 * getForwardDirection());
        setTimeSinceHit(10);
        setDamage(getDamage() * 10.0f);
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.NONE;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        setColor(input.read("Color", DyeColor.LEGACY_ID_CODEC).orElse(DyeColor.WHITE));
        entityData.set(GLUED_DOWN, input.getBooleanOr("GluedDown", false));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        output.store("Color", DyeColor.LEGACY_ID_CODEC, getColor());

        output.putBoolean("GluedDown", entityData.get(GLUED_DOWN));
    }

    public void setColor(DyeColor color) {
        entityData.set(COLOR, color.ordinal());
    }

    public DyeColor getColor() {
        return DyeColor.byId(entityData.get(COLOR));
    }

    /**
     * Sets the forward direction of the entity.
     */
    public void setForwardDirection(int dir) {
        entityData.set(FORWARD_DIRECTION, dir);
    }

    /**
     * Gets the forward direction of the entity.
     */
    public int getForwardDirection() {
        return entityData.get(FORWARD_DIRECTION);
    }

    /**
     * Sets the damage taken from the last hit.
     */
    public void setDamage(float damageTaken) {
        entityData.set(DAMAGE, damageTaken);
    }

    /**
     * Gets the damage taken from the last hit.
     */
    public float getDamage() {
        return entityData.get(DAMAGE);
    }

    /**
     * Sets the time to count down from since the last time entity was hit.
     */
    public void setTimeSinceHit(int timeSinceHit) {
        entityData.set(TIME_SINCE_HIT, timeSinceHit);
    }

    /**
     * Gets the time since the last hit.
     */
    public int getTimeSinceHit() {
        return entityData.get(TIME_SINCE_HIT);
    }
}
