package net.tropicraft.core.common.entity.underdasea.atlantoku;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public class EntitySchoolableFish extends EntityTropicraftWaterBase {

	private static final DataParameter<Boolean> SHOULD_SPAWN_SCHOOL = EntityDataManager.<Boolean>createKey(EntitySchoolableFish.class, DataSerializers.BOOLEAN);

	public EntityTropicraftWaterBase leader = null;
	public int minSchoolAmount = 5;
	public int maxSchoolAmount = 8;
	
	public EntitySchoolableFish(World world) {
		super(world);
		this.setShouldSpawnSchool(true);
		this.setIsLeader(true);
	}

	public EntitySchoolableFish setSchoolLeader(EntityTropicraftWaterBase leader) {
		this.leader = leader;
		this.setShouldSpawnSchool(false);
		this.setIsLeader(false);
		do {
			double offsetX = (new Random()).nextDouble() * 3 - 1.5D;
			double offsetY = (new Random()).nextDouble() * 1 + 1.0D;
			double offsetZ = (new Random()).nextDouble() * 3 - 1.5D;
			setLocationAndAngles(leader.posX + offsetX, leader.posY + offsetY, leader.posZ + offsetZ, leader.rotationYaw, leader.rotationPitch);
		} while (!getCanSpawnHere());
		return this;
	}
	
	public void setSchoolSizeRange(int min, int max) {
		minSchoolAmount = min;
		maxSchoolAmount = max;
	}

	@Override
	public void entityInit() {
		super.entityInit();
		this.getDataManager().register(SHOULD_SPAWN_SCHOOL, false);

	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		// Client Side
		if (world.isRemote) {
			return;
		}
		
		try {
		spawnSchool();
		}catch(Exception e) {
			//e.printStackTrace();
		}

		// Server Side
		if (this.isInWater()) {

			if (!getIsLeader() && leader == null) {
				List<Entity> ents = world.loadedEntityList;
				for (int i = 0; i < ents.size(); i++) {
					if (ents.get(i) instanceof EntityTropicraftWaterBase) {
						EntityTropicraftWaterBase f = ((EntityTropicraftWaterBase) ents.get(i));

						if (f.getClass().getName().equals(this.getClass().getName())) {
							if (f instanceof IAtlasFish && this instanceof IAtlasFish) {
								int slot = ((IAtlasFish) f).getAtlasSlot();
								if (((IAtlasFish) this).getAtlasSlot() != slot) {
									continue;
								}
							}
							if (f.getIsLeader()) {
								this.leader = f;
							}
						}
					}
				}
				if (this.ticksExisted > 200 && leader == null) {
					this.markAsLeader();
				}
			}
			if (this.ticksExisted > 200 && leader == null) {
				this.markAsLeader();
			}

			if (this.leader != null && !getIsLeader()) {
				
				if(!world.loadedEntityList.contains(leader)) {
					this.leader = null;
					this.setRandomTargetHeading();
					return;
				}
				if(this.canEntityBeSeen(leader))
				this.setTargetHeading(this.leader.posX, this.leader.posY - 5 + rand.nextInt(10), this.leader.posZ,
						true);
				if (leader.aggressTarget != null) {
					this.aggressTarget = leader.aggressTarget;
				}
			}
		}
	}

	public void setShouldSpawnSchool(boolean spawnStatus) {
		this.dataManager.set(SHOULD_SPAWN_SCHOOL, Boolean.valueOf(spawnStatus));
	}

	public boolean getShouldSpawnSchool() {
		return this.dataManager.get(SHOULD_SPAWN_SCHOOL);
	}
	
	public void spawnSchool() {
		if (getShouldSpawnSchool() && !world.isRemote) {
			// Note: min/max values include this fish
			int maxInSchool = maxSchoolAmount;
			int minInSchool = minSchoolAmount;
			int numToSpawn = rand.nextInt(1 + maxInSchool - minInSchool) + minInSchool - 1;
			for (int i = 0; i < numToSpawn; i++) {
				EntitySchoolableFish fish = (EntitySchoolableFish) EntityList.createEntityByIDFromName(this.getEntityString(), world);
				if(fish != null) {
					fish.setSchoolLeader(this);
					world.spawnEntity(fish);
				}
			}
			setShouldSpawnSchool(false);
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound n) {
		super.writeEntityToNBT(n);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound n) {
		setShouldSpawnSchool(false);
		super.readEntityFromNBT(n);
	}

}
