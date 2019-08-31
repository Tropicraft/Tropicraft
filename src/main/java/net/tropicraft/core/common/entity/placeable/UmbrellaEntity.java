package net.tropicraft.core.common.entity.placeable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import net.tropicraft.core.common.ColorHelper;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.item.UmbrellaItem;

import javax.annotation.Nullable;
import java.util.List;

public class UmbrellaEntity extends Entity {

    private static final DataParameter<Integer> COLOR = EntityDataManager.createKey(UmbrellaEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Float> DAMAGE = EntityDataManager.createKey(UmbrellaEntity.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> FORWARD_DIRECTION = EntityDataManager.createKey(UmbrellaEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> TIME_SINCE_HIT = EntityDataManager.createKey(UmbrellaEntity.class, DataSerializers.VARINT);

    private static final int DAMAGE_THRESHOLD = 40;

    private int lerpSteps;
    private double lerpX;
    private double lerpY;
    private double lerpZ;
    private double lerpYaw;
    private double lerpPitch;

    public UmbrellaEntity(EntityType<?> type, World world) {
        super(type, world);
        this.ignoreFrustumCheck = true;
        this.preventEntitySpawning = true;
        this.entityCollisionReduction = .95F;
    }
    
    public UmbrellaEntity(World world) {
    	this(TropicraftEntities.UMBRELLA, world);
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
    @OnlyIn(Dist.CLIENT)
    public void setVelocity(double x, double y, double z) {
        setMotion(x, y, z);
    }

    /**
     * For vehicles, the first passenger is generally considered the controller and "drives" the vehicle. For example,
     * Pigs, Horses, and Boats are generally "steered" by the controlling passenger.
     */
    @Override
    @Nullable
    public Entity getControllingPassenger() {
        List<Entity> list = this.getPassengers();
        return list.isEmpty() ? null : list.get(0);
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

        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;

        super.tick();

        tickLerp();

        setMotion(Vec3d.ZERO);

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

    private void tickLerp() {
        if (this.lerpSteps > 0) {
            double d0 = this.posX + (this.lerpX - this.posX) / (double)this.lerpSteps;
            double d1 = this.posY + (this.lerpY - this.posY) / (double)this.lerpSteps;
            double d2 = this.posZ + (this.lerpZ - this.posZ) / (double)this.lerpSteps;
            double d3 = MathHelper.wrapDegrees(this.lerpYaw - (double)this.rotationYaw);
            this.rotationYaw = (float)((double)this.rotationYaw + d3 / (double)this.lerpSteps);
            this.rotationPitch = (float)((double)this.rotationPitch + (this.lerpPitch - (double)this.rotationPitch) / (double)this.lerpSteps);
            --this.lerpSteps;
            this.setPosition(d0, d1, d2);
            this.setRotation(this.rotationYaw, this.rotationPitch);
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource damagesource, float amount) {
        if (this.isInvulnerableTo(damagesource)) {
            return false;
        }
        else if (!this.world.isRemote && isAlive()) {
            this.setForwardDirection(-this.getForwardDirection());
            this.setTimeSinceHit(10);
            this.setDamage(this.getDamage() + amount * 10.0F);
            this.markVelocityChanged();
            boolean flag = damagesource.getTrueSource() instanceof PlayerEntity && ((PlayerEntity)damagesource.getTrueSource()).abilities.isCreativeMode;

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
        return UmbrellaItem.getItemFromColor(ColorHelper.getColorObject(getColor()));
    }

    @Override
    public AxisAlignedBB getCollisionBox(Entity entity) {
        return entity.getBoundingBox();
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

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
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
        setColor(nbt.getInt("Color"));
    }

    @Override
    protected void writeAdditional(CompoundNBT nbt) {
        nbt.putInt("Color", getColor());
    }

    public void setColor(int color) {
        dataManager.set(COLOR, color);
    }

    public void setColor(float red, float green, float blue) {
        dataManager.set(COLOR, ColorHelper.getColor(red, green, blue));
    }

    public int getColor() {
        return dataManager.get(COLOR);
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
