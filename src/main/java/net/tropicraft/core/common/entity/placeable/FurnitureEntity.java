package net.tropicraft.core.common.entity.placeable;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraftforge.network.NetworkHooks;

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
    
    protected FurnitureEntity(EntityType<?> entityTypeIn, Level worldIn, Map<DyeColor, ? extends RegistryEntry<? extends Item>> items) {
        this(entityTypeIn, worldIn, c -> items.get(c).get());        
    }

    protected FurnitureEntity(EntityType<?> entityTypeIn, Level worldIn, Function<DyeColor, Item> itemLookup) {
        super(entityTypeIn, worldIn);
        this.itemLookup = itemLookup;
        this.blocksBuilding = true;
        //TODO this will result in pushing acting weird - but the variable is gone in 1.17 (apparently)
        // this.pushthrough = .95F;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource pSource) {
        return entityData.get(GLUED_DOWN) || super.isInvulnerableTo(pSource);
    }

    public void setRotation(float yaw) {
        this.lerpYaw = Mth.wrapDegrees(yaw);
        this.setYRot((float) this.lerpYaw);
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void defineSynchedData() {
        this.getEntityData().define(COLOR, 0);
        this.getEntityData().define(DAMAGE, (float) 0);
        this.getEntityData().define(FORWARD_DIRECTION, 1);
        this.getEntityData().define(TIME_SINCE_HIT, 0);
        this.getEntityData().define(GLUED_DOWN, false);
    }

    @Override
    public void tick() {
        final int timeSinceHit = getTimeSinceHit();
        if (timeSinceHit > 0) {
            setTimeSinceHit(timeSinceHit - 1);
        }
    
        final float damage = getDamage();
        if (damage > 0) {
            setDamage(damage - 1);
        }

        final Vec3 currentPos = position();
        xo = currentPos.x;
        yo = currentPos.y;
        zo = currentPos.z;
    
        super.tick();
    
        tickLerp();
    
        if (preventMotion()) {
            setDeltaMovement(Vec3.ZERO);
        }
    
        //updateRocking();
    
        this.checkInsideBlocks();
        List<Entity> list = this.level.getEntities(this, this.getBoundingBox().inflate((double)0.2F, (double)-0.01F, (double)0.2F), EntitySelector.pushableBy(this));
        if (!list.isEmpty()) {
            for (Entity entity : list) {
                if (!entity.hasPassenger(this)) {
                    this.push(entity);
                }
            }
        }
    }
    
    protected boolean preventMotion() {
        return true;
    }
    
    /* Following two methods mostly copied from EntityBoat interpolation code */
    @Override
    public void lerpTo(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
        if (teleport) {
            super.lerpTo(x, y, z, yaw, pitch, posRotationIncrements, teleport);
        } else {
            this.lerpX = x;
            this.lerpY = y;
            this.lerpZ = z;
            // Avoid "jumping" back to the client's rotation due to vanilla's dumb incomplete packets
            if (yaw != getYRot() || Double.isNaN(lerpYaw)) {
                this.lerpYaw = Mth.wrapDegrees((double) yaw);
            }
            this.lerpSteps = 10;
            this.setXRot(pitch);
        }
    }

    private void tickLerp() {
        if (this.lerpSteps > 0) {
            double d0 = this.getX() + (this.lerpX - this.getX()) / (double)this.lerpSteps;
            double d1 = this.getY() + (this.lerpY - this.getY()) / (double)this.lerpSteps;
            double d2 = this.getZ() + (this.lerpZ - this.getZ()) / (double)this.lerpSteps;
            double d3 = Mth.wrapDegrees(this.lerpYaw - (double)this.getYRot());
            this.setYRot((float)((double)this.getYRot() + d3 / (double)this.lerpSteps));
            this.setXRot((float)((double)this.getXRot() + (this.lerpPitch - (double)this.getXRot()) / (double)this.lerpSteps));
            --this.lerpSteps;
            this.setPos(d0, d1, d2);
            this.setRot(this.getYRot(), this.getXRot());
        }
    }

    @Override
    public InteractionResult interact(Player pPlayer, InteractionHand pHand) {
        if(invulnerablityCheck(pPlayer, pHand) == InteractionResult.SUCCESS) {
            return InteractionResult.SUCCESS;
        }

        return super.interact(pPlayer, pHand);
    }

    public InteractionResult invulnerablityCheck(Player pPlayer, InteractionHand pHand){
        if(pPlayer.getItemInHand(pHand).is(Items.DEBUG_STICK)){
            if(!this.level.isClientSide) {
                this.entityData.set(GLUED_DOWN, !this.entityData.get(GLUED_DOWN));
                ((ServerPlayer)pPlayer).sendMessage(new TranslatableComponent("Invulnerability Mode: " + (this.entityData.get(GLUED_DOWN) ? "On" : "Off")), ChatType.GAME_INFO, Util.NIL_UUID);
            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }


    @Override
    public boolean hurt(DamageSource damageSource, float amount) {
        if (this.isInvulnerableTo(damageSource)) {
            if(damageSource.getEntity() instanceof Player player){
                return player.getMainHandItem().is(Items.DEBUG_STICK);
            }

            return false;
        }

        if (!this.level.isClientSide && isAlive()) {
            this.setForwardDirection(-this.getForwardDirection());
            this.setTimeSinceHit(10);
            this.setDamage(this.getDamage() + amount * 10.0F);
            this.markHurt();
            boolean flag = damageSource.getEntity() instanceof Player && ((Player)damageSource.getEntity()).getAbilities().instabuild;
    
            if (flag || this.getDamage() > DAMAGE_THRESHOLD) {
                Entity rider = this.getControllingPassenger();
                if (rider != null) {
                    rider.startRiding(this);
                }
    
                if (!flag) {
                    this.spawnAtLocation(getItemStack(), 0.0F);
                }
    
                this.remove(RemovalReason.KILLED);
            }
        }
    
        return true;
    }

    private ItemStack getItemStack() {
        return new ItemStack(itemLookup.apply(getColor()));
    }

    @Override
    public double getPassengersRidingOffset() {
        return 0.0D;
    }

    @Override
    public void animateHurt() {
        this.setForwardDirection(-1 * this.getForwardDirection());
        this.setTimeSinceHit(10);
        this.setDamage(this.getDamage() * 10.0F);
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
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt) {
        setColor(DyeColor.byId(nbt.getInt("Color")));

        if(nbt.contains("GluedDown")){
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
