package net.tropicraft.core.common.entity.placeable;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
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

    protected int lerpSteps;
    protected double lerpX;
    protected double lerpY;
    protected double lerpZ;
    protected double lerpYaw = Double.NaN; // Force first-time sync even if packet is incomplete
    protected double lerpPitch;

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

    @Override
    public boolean isInvulnerableTo(DamageSource pSource) {
        return entityData.get(GLUED_DOWN) || super.isInvulnerableTo(pSource);
    }

    public void setRotation(float yaw) {
        lerpYaw = Mth.wrapDegrees(yaw);
        setYRot((float) lerpYaw);
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

        tickLerp();

        if (preventMotion()) {
            setDeltaMovement(Vec3.ZERO);
        }

        //updateRocking();

        checkInsideBlocks();
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

    /* Following two methods mostly copied from EntityBoat interpolation code */
    @Override
    public void lerpTo(double x, double y, double z, float yaw, float pitch, int posRotationIncrements) {
        lerpX = x;
        lerpY = y;
        lerpZ = z;
        // Avoid "jumping" back to the client's rotation due to vanilla's dumb incomplete packets
        if (yaw != getYRot() || Double.isNaN(lerpYaw)) {
            lerpYaw = Mth.wrapDegrees((double) yaw);
        }
        lerpSteps = 10;
        setXRot(pitch);
    }

    private void tickLerp() {
        if (lerpSteps > 0) {
            double d0 = getX() + (lerpX - getX()) / (double) lerpSteps;
            double d1 = getY() + (lerpY - getY()) / (double) lerpSteps;
            double d2 = getZ() + (lerpZ - getZ()) / (double) lerpSteps;
            double d3 = Mth.wrapDegrees(lerpYaw - (double) getYRot());
            setYRot((float) ((double) getYRot() + d3 / (double) lerpSteps));
            setXRot((float) ((double) getXRot() + (lerpPitch - (double) getXRot()) / (double) lerpSteps));
            --lerpSteps;
            setPos(d0, d1, d2);
            setRot(getYRot(), getXRot());
        }
    }

    @Override
    public InteractionResult interact(Player pPlayer, InteractionHand pHand) {
        if (invulnerablityCheck(pPlayer, pHand) == InteractionResult.SUCCESS) {
            return InteractionResult.SUCCESS;
        }

        return super.interact(pPlayer, pHand);
    }

    public InteractionResult invulnerablityCheck(Player pPlayer, InteractionHand pHand) {
        if (pPlayer.getItemInHand(pHand).is(Items.DEBUG_STICK)) {
            if (!level().isClientSide) {
                entityData.set(GLUED_DOWN, !entityData.get(GLUED_DOWN));
                pPlayer.sendSystemMessage(Component.translatable("Invulnerability Mode: " + (entityData.get(GLUED_DOWN) ? "On" : "Off")));
            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }

    @Override
    public boolean hurt(DamageSource damageSource, float amount) {
        if (isInvulnerableTo(damageSource)) {
            if (damageSource.getEntity() instanceof Player player) {
                return player.getMainHandItem().is(Items.DEBUG_STICK);
            }

            return false;
        }

        if (!level().isClientSide && isAlive()) {
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
                    spawnAtLocation(getItemStack(), 0.0f);
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
    protected void readAdditionalSaveData(CompoundTag nbt) {
        setColor(DyeColor.byId(nbt.getInt("Color")));

        if (nbt.contains("GluedDown")) {
            entityData.set(GLUED_DOWN, nbt.getBoolean("GluedDown"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt) {
        nbt.putInt("Color", getColor().ordinal());

        nbt.putBoolean("GluedDown", entityData.get(GLUED_DOWN));
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
