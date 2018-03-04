package net.tropicraft.core.common.entity.placeable;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class EntityPlaceableBase extends Entity {

	private static final DataParameter<Float> DAMAGE = EntityDataManager.<Float>createKey(EntityChair.class, DataSerializers.FLOAT);
	private static final DataParameter<Integer> FORWARD_DIRECTION = EntityDataManager.<Integer>createKey(EntityChair.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> TIME_SINCE_HIT = EntityDataManager.<Integer>createKey(EntityChair.class, DataSerializers.VARINT);

	public EntityPlaceableBase(World worldIn) {
		super(worldIn);
	}

	@Override
	protected void entityInit() {
		this.getDataManager().register(DAMAGE, new Float(0));
		this.getDataManager().register(FORWARD_DIRECTION, new Integer(1));
		this.getDataManager().register(TIME_SINCE_HIT, new Integer(0));
	}

	/**
	 * Setups the entity to do the hurt animation. Only used by packets in multiplayer.
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public void performHurtAnimation() {
		this.setForwardDirection(-this.getForwardDirection());
		this.setTimeSinceHit(10);
		this.setDamage(this.getDamage() * 11.0F);
	}

	@Override
	public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
		if (!this.world.isRemote && !player.isSneaking()) {
			player.renderYawOffset = 0;
			player.startRiding(this);
		}

		return true;
	}

	/**
	 * Returns true if other Entities should be prevented from moving through this Entity.
	 */
	@Override
	public boolean canBeCollidedWith() {
		return !this.isDead;
	}

	/**
	 * Sets the forward direction of the entity.
	 */
	public void setForwardDirection(int dir) {
		this.dataManager.set(FORWARD_DIRECTION, Integer.valueOf(dir));
	}

	/**
	 * Gets the forward direction of the entity.
	 */
	public int getForwardDirection() {
		return ((Integer)this.dataManager.get(FORWARD_DIRECTION)).intValue();
	}

	/**
	 * Sets the damage taken from the last hit.
	 */
	public void setDamage(float damageTaken) {
		this.dataManager.set(DAMAGE, Float.valueOf(damageTaken));
	}

	/**
	 * Gets the damage taken from the last hit.
	 */
	public float getDamage() {
		return ((Float)this.dataManager.get(DAMAGE)).floatValue();
	}

	/**
	 * Sets the time to count down from since the last time entity was hit.
	 */
	public void setTimeSinceHit(int timeSinceHit) {
		this.dataManager.set(TIME_SINCE_HIT, Integer.valueOf(timeSinceHit));
	}

	/**
	 * Gets the time since the last hit.
	 */
	public int getTimeSinceHit() {
		return ((Integer)this.dataManager.get(TIME_SINCE_HIT)).intValue();
	}

	/**
	 * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
	 * prevent them from trampling crops
	 */
	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	/**
	 * Given a player, get the angle that the chair should be at to face the player
	 * @param player
	 * @return The angle the chair should be at to face the players
	 */
	protected float getAngleToPlayer(EntityPlayer player) {
		return MathHelper.wrapDegrees(player.rotationYaw);
	}

	/**
	 * Returns a boundingBox used to collide the entity with other entities and blocks. This enables the entity to be
	 * pushable on contact, like boats or minecarts.
	 */
	@Override
	@Nullable
	public AxisAlignedBB getCollisionBox(Entity entityIn) {
		return entityIn.getEntityBoundingBox();
	}

	/**
	 * Returns the collision bounding box for this entity
	 */
	@Nullable
	@Override
	public AxisAlignedBB getCollisionBoundingBox() {
		return this.getEntityBoundingBox();
	}
}
