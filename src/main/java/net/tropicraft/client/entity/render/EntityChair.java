package net.tropicraft.client.entity.render;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.tropicraft.util.TropicraftUtils;

public class EntityChair extends Entity {
	// TODO add drips after being wet

	/** Combined rgba of the chair */
	private static final int DATAWATCHER_COLOR = 2;
	
	/** Current damage to the chair (from punching it) */
	private static final int DATAWATCHER_DAMAGE = 3;
	
	/** Is come sail away mode? */
	private static final int DATAWATCHER_COMESAILAWAY = 4;
	
	/** Which direction is this chair facing? */
	private static final int DATAWATCHER_FORWARD_DIRECTION = 5;
	
	/** The time to count down from since the last time entity was hit. */
	private static final int DATAWATCHER_TIME_SINCE_HIT = 6;

	public boolean isChairEmpty;

	public EntityChair(World par1World) {
		super(par1World);
		this.isChairEmpty = true;
		this.setSize(1F, 1F);
	}

	public EntityChair(World world, double d, double d1, double d2, float f1, int color, EntityPlayer player) {
		this(world);
		setPosition(d, d1, d2);
		motionX = 0.0D;
		motionY = 0.0D;
		motionZ = 0.0D;
		prevPosX = d;
		prevPosY = d1;
		prevPosZ = d2;

		//    rotationYaw = this.getAngleToPlayer(player);
	}

	public EntityChair(World par1World, double par2, double par4, double par6)
	{
		this(par1World);
		this.setPosition(par2, par4 + (double)this.yOffset, par6);
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;
		this.prevPosX = par2;
		this.prevPosY = par4;
		this.prevPosZ = par6;

	}

	public void onUpdate() {
		
	}

	@Override
	protected void entityInit() {
		this.dataWatcher.addObject(DATAWATCHER_COLOR, new Integer(TropicraftUtils.getColor(0F, 0F, 1F)));
		this.dataWatcher.addObject(DATAWATCHER_DAMAGE, new Integer(0));
		this.dataWatcher.addObject(DATAWATCHER_COMESAILAWAY, new Byte((byte)0));
		this.dataWatcher.addObject(DATAWATCHER_FORWARD_DIRECTION, new Integer(1));
		this.dataWatcher.addObject(DATAWATCHER_TIME_SINCE_HIT, new Integer(0));
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
		this.setColor(Integer.valueOf(nbt.getInteger("COLOR")));
		this.setComeSailAway(Boolean.valueOf(nbt.getBoolean("COME_SAIL_AWAY")));
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {
		nbt.setInteger("COLOR", Integer.valueOf(this.getColor()));
		nbt.setBoolean("COME_SAIL_AWAY", Boolean.valueOf(this.getComeSailAway()));
	}

	/**
	 * Returns a boundingBox used to collide the entity with other entities and blocks. This enables the entity to be
	 * pushable on contact, like chairs or minecarts.
	 */
	@Override
	public AxisAlignedBB getCollisionBox(Entity par1Entity) {
		return par1Entity.boundingBox;
	}

	/**
	 * returns the bounding box for this entity
	 */
	@Override
	public AxisAlignedBB getBoundingBox() {
		return this.boundingBox;
	}

	/**
	 * Returns true if this entity should push and be pushed by other entities when colliding.
	 */
	@Override
	public boolean canBePushed() {
		return true;
	}

	/**
	 * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
	 * prevent them from trampling crops
	 */
	@Override
	protected boolean canTriggerWalking() {
		return false;
	}
	
	public void setColor(int color) {
		this.dataWatcher.updateObject(DATAWATCHER_COLOR, Integer.valueOf(color));
	}
	
	public void setColor(float red, float green, float blue) {
		this.dataWatcher.updateObject(DATAWATCHER_COLOR, Integer.valueOf(TropicraftUtils.getColor(red, green, blue)));
	}

	public int getColor() {
		return this.dataWatcher.getWatchableObjectInt(DATAWATCHER_COLOR);
	}
	
	public void setDamage(int damage) {
		this.dataWatcher.updateObject(DATAWATCHER_DAMAGE, Integer.valueOf(damage));
	}
	
	public int getDamage() {
		return this.dataWatcher.getWatchableObjectInt(DATAWATCHER_DAMAGE);
	}
	
	public void setComeSailAway(boolean sail) {
		this.dataWatcher.updateObject(DATAWATCHER_COMESAILAWAY, sail ? Byte.valueOf((byte)1) : Byte.valueOf((byte)0));
	}
	
	public boolean getComeSailAway() {
		return this.dataWatcher.getWatchableObjectByte(DATAWATCHER_COMESAILAWAY) == (byte)1;
	}
	
    /**
     * Sets the forward direction of the entity.
     */
    public void setForwardDirection(int dir) {
        this.dataWatcher.updateObject(DATAWATCHER_FORWARD_DIRECTION, Integer.valueOf(dir));
    }

    /**
     * Gets the forward direction of the entity.
     */
    public int getForwardDirection() {
        return this.dataWatcher.getWatchableObjectInt(DATAWATCHER_FORWARD_DIRECTION);
    }
    
    /**
     * Sets the time to count down from since the last time entity was hit.
     */
    public void setTimeSinceHit(int time) {
        this.dataWatcher.updateObject(DATAWATCHER_TIME_SINCE_HIT, Integer.valueOf(time));
    }

    /**
     * Gets the time since the last hit.
     */
    public int getTimeSinceHit() {
        return this.dataWatcher.getWatchableObjectInt(DATAWATCHER_TIME_SINCE_HIT);
    }
}
