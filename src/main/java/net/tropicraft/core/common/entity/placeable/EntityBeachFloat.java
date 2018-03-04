package net.tropicraft.core.common.entity.placeable;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.core.registry.ItemRegistry;

public class EntityBeachFloat extends EntityPlaceableColored {

	/** Is any entity laying on the float? */
	public boolean isEmpty;

	@SideOnly(Side.CLIENT)
	private double velocityX;
	@SideOnly(Side.CLIENT)
	private double velocityY;
	@SideOnly(Side.CLIENT)
	private double velocityZ;

	/** Acceleration */
	private double speedMultiplier;
	
	
	public EntityBeachFloat(World worldIn) {
		super(worldIn);
		setSize(2F, 0.5F);
		this.ignoreFrustumCheck = true;
		this.isEmpty = true;
		this.speedMultiplier = 0.10D;
		this.preventEntitySpawning = true;
		this.entityCollisionReduction = .95F;
	}

	public EntityBeachFloat(World world, double x, double y, double z, int color, EntityPlayer player) {
		this(world);
		setPosition(x, y, z);
		motionX = 0.0D;
		motionY = 0.0D;
		motionZ = 0.0D;
		prevPosX = x;
		prevPosY = y;
		prevPosZ = z;
		setColor(color);		
		rotationYaw = this.getAngleToPlayer(player);
	}
	
	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource damageSource, float par2) {
		if (this.isEntityInvulnerable(damageSource)) {
			return false;
		} else if (!this.world.isRemote && !this.isDead) {
			this.setForwardDirection(-this.getForwardDirection());
			this.setTimeSinceHit(10);
			this.setDamage(this.getDamage() + par2 * 10.0F);
			this.markVelocityChanged();
			boolean flag = damageSource.getTrueSource() instanceof EntityPlayer && ((EntityPlayer)damageSource.getTrueSource()).capabilities.isCreativeMode;

			if (flag || this.getDamage() > 40.0F) {
				if (!flag) {
					this.entityDropItem(new ItemStack(ItemRegistry.beach_float, 1,  getDamageFromColor()), 0.0F);
				}

				this.setDead();
			}

			return true;
		} else {
			return true;
		}
	}

	/**
	 * Returns true if this entity should push and be pushed by other entities when colliding.
	 */
	@Override
	public boolean canBePushed() {
		return true;
	}

	/**
	 * Returns the Y offset from the entity's position for any entity riding this one.
	 */
	@Override
	public double getMountedYOffset() {
		return (double)this.height - 0.65D;
	}

    /**
     * For vehicles, the first passenger is generally considered the controller and "drives" the vehicle. For example,
     * Pigs, Horses, and Boats are generally "steered" by the controlling passenger.
     */
    @Override
    @Nullable
    public Entity getControllingPassenger() {
        List<Entity> list = this.getPassengers();
        return list.isEmpty() ? null : (Entity)list.get(0);
    }

	/**
	 * Sets the velocity to the args. Args: x, y, z
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public void setVelocity(double xVelocity, double yVelocity, double zVelocity) {
		this.velocityX = this.motionX = xVelocity;
		this.velocityY = this.motionY = yVelocity;
		this.velocityZ = this.motionZ = zVelocity;
	}

    /**
     * Gets the horizontal facing direction of this Entity, adjusted to take specially-treated entity types into
     * account.
     */
	@Override
    public EnumFacing getAdjustedHorizontalFacing() {
        return this.getHorizontalFacing().rotateY();
    }
	
	@Override
	public boolean shouldRiderSit() {
	    return false;
	}
}
