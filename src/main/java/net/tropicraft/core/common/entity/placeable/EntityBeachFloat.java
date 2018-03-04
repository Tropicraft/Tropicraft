package net.tropicraft.core.common.entity.placeable;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.tropicraft.ColorHelper;

public class EntityBeachFloat extends Entity {

	private static final DataParameter<Integer> COLOR = EntityDataManager.<Integer>createKey(EntityBeachFloat.class, DataSerializers.VARINT);

	public EntityBeachFloat(World worldIn) {
		super(worldIn);
		setSize(2F, 0.5F);
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
	 * Given a player, get the angle that the chair should be at to face the player
	 * @param player
	 * @return The angle the chair should be at to face the players
	 */
	private float getAngleToPlayer(EntityPlayer player) {
		return MathHelper.wrapDegrees(player.rotationYaw);
	}

	@Override
	protected void entityInit() {
		this.getDataManager().register(COLOR, Integer.valueOf(ColorHelper.DEFAULT_VALUE));
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
		this.setColor(Integer.valueOf(nbt.getInteger("COLOR")));
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {
		nbt.setInteger("COLOR", Integer.valueOf(this.getColor()));
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

    /**
	 * @return Returns the damage value associated with the color of this chair
	 */
	public int getDamageFromColor() {
		return ColorHelper.getDamageFromColor(this.getColor());
	}

	public void setColor(int color) {
		this.dataManager.set(COLOR, Integer.valueOf(color));
	}

	public void setColor(float red, float green, float blue) {
		this.dataManager.set(COLOR, Integer.valueOf(ColorHelper.getColor(red, green, blue)));
	}

	public int getColor() {
		return ((Integer)this.dataManager.get(COLOR)).intValue();
	}
}
