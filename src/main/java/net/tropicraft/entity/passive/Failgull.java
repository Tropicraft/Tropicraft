package net.tropicraft.entity.passive;

import java.util.List;

import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class Failgull extends EntityFlying {

	public int courseChangeCooldown = 0;
	public double waypointX;
	public double waypointY;
	public double waypointZ;

	public boolean inFlock;

	public Failgull leader;

	public int flockCount = 0;

	public int flockPosition = 0;

	public Failgull(World par1World) {
		super(par1World);
		setSize(0.4F, 0.6F);
		this.experienceValue = 1;
	}
	
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(3.0D);
	}

	@Override
	public void entityInit() {
		super.entityInit();
	}

	protected void updateEntityActionState()
	{
		
		++this.entityAge;
		this.despawnEntity();
		double d0 = this.waypointX - this.posX;
		double d1 = this.waypointY - this.posY;
		double d2 = this.waypointZ - this.posZ;
		double d3 = d0 * d0 + d1 * d1 + d2 * d2;

		if (d3 < 1.0D || d3 > 3600.0D)
		{
			this.waypointX = this.posX + (double)((this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
			this.waypointY = this.posY + (double)((this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
			this.waypointZ = this.posZ + (double)((this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
		}

		if (this.courseChangeCooldown-- <= 0)
		{
			this.courseChangeCooldown += this.rand.nextInt(5) + 2;
			d3 = (double)MathHelper.sqrt_double(d3);

			if (this.isCourseTraversable(this.waypointX, this.waypointY, this.waypointZ, d3))
			{
				this.motionX += d0 / d3 * 0.1D;
				this.motionY += d1 / d3 * 0.1D;
				this.motionZ += d2 / d3 * 0.1D;
			}
			else
			{
				this.waypointX = this.posX;
				this.waypointY = this.posY;
				this.waypointZ = this.posZ;
			}
		}

		if (leader != null) {
			if (flockPosition % 2 == 0) {
				this.waypointX = leader.waypointX;
				this.waypointY = leader.waypointY;
				this.waypointZ = leader.waypointZ;
			} else {
				this.waypointX = leader.waypointX;
				this.waypointY = leader.waypointY;
				this.waypointZ = leader.waypointZ;
			}
		}

		if (!inFlock) {
			List list = worldObj.getEntitiesWithinAABB(Failgull.class, this.boundingBox.expand(10D, 10D, 10D));

			int lowest = this.getEntityId();
			Failgull f = null;

			for (Object o : list) {
				f = (Failgull) o;

				if (f.leader != null) {
					this.flockPosition = ++f.leader.flockCount;
					f.inFlock = true;
					leader = f.leader;
					break;
				}
				//break;
			}
		}

		if (!inFlock && leader == null) {
			leader = this;
			inFlock = true;
		}

		//poop();		
	}

	private void poop() {
		if (!worldObj.isRemote && worldObj.rand.nextInt(20) == 0) {
			EntitySnowball s = new EntitySnowball(worldObj, posX, posY, posZ);
			s.setThrowableHeading(0, 0, 0, 0, 0);
			worldObj.spawnEntityInWorld(s);
		}
	}

	/**
	 * True if the failgull has an unobstructed line of travel to the waypoint.
	 */
	private boolean isCourseTraversable(double par1, double par3, double par5, double par7)
	{
		double d4 = (this.waypointX - this.posX) / par7;
		double d5 = (this.waypointY - this.posY) / par7;
		double d6 = (this.waypointZ - this.posZ) / par7;
		AxisAlignedBB axisalignedbb = this.boundingBox.copy();

		for (int i = 1; (double)i < par7; ++i)
		{
			axisalignedbb.offset(d4, d5, d6);

			if (!this.worldObj.getCollidingBoundingBoxes(this, axisalignedbb).isEmpty())
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * Checks if the entity's current position is a valid location to spawn this entity.
	 */
	public boolean getCanSpawnHere() {
		return super.getCanSpawnHere();
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	protected String getLivingSound()
	{
		return "";//"mine";
	}

}