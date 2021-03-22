package net.tropicraft.core.common.entity.placeable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class FurnitureEntity extends Entity {

    private static final DataParameter<Integer> COLOR = EntityDataManager.createKey(FurnitureEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Float> DAMAGE = EntityDataManager.createKey(FurnitureEntity.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> FORWARD_DIRECTION = EntityDataManager.createKey(FurnitureEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> TIME_SINCE_HIT = EntityDataManager.createKey(FurnitureEntity.class, DataSerializers.VARINT);
    
    private static final int DAMAGE_THRESHOLD = 40;
    
    private final Function<DyeColor, Item> itemLookup;
    
    protected int lerpSteps;
    protected double lerpX;
    protected double lerpY;
    protected double lerpZ;
    protected double lerpYaw = Double.NaN; // Force first-time sync even if packet is incomplete
    protected double lerpPitch;
    
    protected FurnitureEntity(EntityType<?> entityTypeIn, World worldIn, Map<DyeColor, ? extends RegistryObject<? extends Item>> items) {
        this(entityTypeIn, worldIn, c -> items.get(c).get());        
    }

    protected FurnitureEntity(EntityType<?> entityTypeIn, World worldIn, Function<DyeColor, Item> itemLookup) {
        super(entityTypeIn, worldIn);
        this.itemLookup = itemLookup;
        this.ignoreFrustumCheck = true;
        this.preventEntitySpawning = true;
        this.entityCollisionReduction = .95F;
    }
    
    public void setRotation(float yaw) {
        this.lerpYaw = this.rotationYaw = MathHelper.wrapDegrees(yaw);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void registerData() {
        this.getDataManager().register(COLOR, 0);
        this.getDataManager().register(DAMAGE, (float) 0);
        this.getDataManager().register(FORWARD_DIRECTION, 1);
        this.getDataManager().register(TIME_SINCE_HIT, 0);
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

        final Vector3d currentPos = getPositionVec();
        prevPosX = currentPos.x;
        prevPosY = currentPos.y;
        prevPosZ = currentPos.z;
    
        super.tick();
    
        tickLerp();
    
        if (preventMotion()) {
            setMotion(Vector3d.ZERO);
        }
    
        //updateRocking();
    
        this.doBlockCollisions();
        List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getBoundingBox().grow((double)0.2F, (double)-0.01F, (double)0.2F), EntityPredicates.pushableBy(this));
        if (!list.isEmpty()) {
            for (Entity entity : list) {
                if (!entity.isPassenger(this)) {
                    this.applyEntityCollision(entity);
                }
            }
        }
    }
    
    protected boolean preventMotion() {
        return true;
    }
    
    /* Following two methods mostly copied from EntityBoat interpolation code */
    @Override
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
        if (teleport) {
            super.setPositionAndRotationDirect(x, y, z, yaw, pitch, posRotationIncrements, teleport);
        } else {
            this.lerpX = x;
            this.lerpY = y;
            this.lerpZ = z;
            // Avoid "jumping" back to the client's rotation due to vanilla's dumb incomplete packets
            if (yaw != rotationYaw || Double.isNaN(lerpYaw)) {
                this.lerpYaw = MathHelper.wrapDegrees((double) yaw);
            }
            this.lerpSteps = 10;
            this.rotationPitch = pitch;
        }
    }

    private void tickLerp() {
        if (this.lerpSteps > 0) {
            double d0 = this.getPosX() + (this.lerpX - this.getPosX()) / (double)this.lerpSteps;
            double d1 = this.getPosY() + (this.lerpY - this.getPosY()) / (double)this.lerpSteps;
            double d2 = this.getPosZ() + (this.lerpZ - this.getPosZ()) / (double)this.lerpSteps;
            double d3 = MathHelper.wrapDegrees(this.lerpYaw - (double)this.rotationYaw);
            this.rotationYaw = (float)((double)this.rotationYaw + d3 / (double)this.lerpSteps);
            this.rotationPitch = (float)((double)this.rotationPitch + (this.lerpPitch - (double)this.rotationPitch) / (double)this.lerpSteps);
            --this.lerpSteps;
            this.setPosition(d0, d1, d2);
            this.setRotation(this.rotationYaw, this.rotationPitch);
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float amount) {
        if (this.isInvulnerableTo(damageSource)) {
            return false;
        }
        else if (!this.world.isRemote && isAlive()) {
            this.setForwardDirection(-this.getForwardDirection());
            this.setTimeSinceHit(10);
            this.setDamage(this.getDamage() + amount * 10.0F);
            this.markVelocityChanged();
            boolean flag = damageSource.getTrueSource() instanceof PlayerEntity && ((PlayerEntity)damageSource.getTrueSource()).abilities.isCreativeMode;
    
            if (flag || this.getDamage() > DAMAGE_THRESHOLD) {
                Entity rider = this.getControllingPassenger();
                if (rider != null) {
                    rider.startRiding(this);
                }
    
                if (!flag) {
                    this.entityDropItem(getItemStack(), 0.0F);
                }
    
                this.remove();
            }
        }
    
        return true;
    }

    private ItemStack getItemStack() {
        return new ItemStack(itemLookup.apply(getColor()));
    }

    @Override
    public double getMountedYOffset() {
        return 0.0D;
    }

    @Override
    public void performHurtAnimation() {
        this.setForwardDirection(-1 * this.getForwardDirection());
        this.setTimeSinceHit(10);
        this.setDamage(this.getDamage() * 10.0F);
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    protected void readAdditional(CompoundNBT nbt) {
        setColor(DyeColor.byId(nbt.getInt("Color")));
    }

    @Override
    protected void writeAdditional(CompoundNBT nbt) {
        nbt.putInt("Color", getColor().ordinal());
    }

    public void setColor(DyeColor color) {
        dataManager.set(COLOR, color.ordinal());
    }

    public DyeColor getColor() {
        return DyeColor.byId(dataManager.get(COLOR));
    }

    /**
     * Sets the forward direction of the entity.
     */
    public void setForwardDirection(int dir) {
        dataManager.set(FORWARD_DIRECTION, dir);
    }

    /**
     * Gets the forward direction of the entity.
     */
    public int getForwardDirection() {
        return dataManager.get(FORWARD_DIRECTION);
    }

    /**
     * Sets the damage taken from the last hit.
     */
    public void setDamage(float damageTaken) {
        dataManager.set(DAMAGE, damageTaken);
    }

    /**
     * Gets the damage taken from the last hit.
     */
    public float getDamage() {
        return dataManager.get(DAMAGE);
    }

    /**
     * Sets the time to count down from since the last time entity was hit.
     */
    public void setTimeSinceHit(int timeSinceHit) {
        dataManager.set(TIME_SINCE_HIT, timeSinceHit);
    }

    /**
     * Gets the time since the last hit.
     */
    public int getTimeSinceHit() {
        return dataManager.get(TIME_SINCE_HIT);
    }
}
