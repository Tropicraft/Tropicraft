package net.tropicraft.core.common.town;

import CoroUtil.forge.CoroAI;
import CoroUtil.util.CoroUtilFile;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import java.util.*;

public class ManagedLocation implements ISimulationTickable {

	public int locationID = -1;
	public int dimID;
	public ChunkCoordinates spawn;
	public boolean hasInit = false;

	//for active entities in the world, should not contain unloaded entities, as they become invalid
	public List<EntityLivingBase> listLoadedEntities = new ArrayList<EntityLivingBase>();
	
	//for managing entities that get loaded and unloaded, dont forget that AI/BT Agent entities will attempt to (re)register themselves on reload/spawn if they have a managed location set to them
	//its expected that those in this list are still alive
	public List<UUID> listPersistantEntities = new ArrayList<UUID>();
	public HashMap<Long, Object> lookupIDToTownObject = new HashMap<Long, Object>(); //includes both StructureObjects and ResourceNodes
	public int lastTownObjectIDSet = 1; //command building uses 0, cant increment it from its init
	
	public List<SpawnLocationData> listSpawnLocations = new ArrayList<SpawnLocationData>();
	
	public ManagedLocation() {
		
	}

	public void initData(int parLocation, int parDim, ChunkCoordinates parCoords) {
		locationID = parLocation;
		dimID = parDim;
		spawn = parCoords;
	}
	
	public void initFirstTime() {
		
	}
	
	@Override
	public ChunkCoordinates getOrigin() {
		return spawn;
	}
	
	@Override
	public void tickUpdate() {
		//listPersistantEntities.clear();
		if (getWorld().getTotalWorldTime() % 100 == 0) {
			tickMonitorPersistantMembers();
		}
	}
	
	public World getWorld() {
		return DimensionManager.getWorld(dimID);
	}
	
	/*public void addTownObject(StructureObject bb) {
		lookupIDToTownObject.put(bb.ID, bb);
	}*/
	
	public void removeObject(Object obj) {
		System.out.println("removing object: " + obj);
		if (obj instanceof EntityLivingBase) {
			listLoadedEntities.remove(obj);
		}
		
		if (!(obj instanceof EntityLivingBase)) {
			lookupIDToTownObject.remove(obj);
		}
	}
	
	/* For proper death hooks */
	public void hookEntityDied(EntityLivingBase ent) {
		System.out.println("hook entity died: " + ent);
		removeObject(ent);
		listPersistantEntities.remove(ent.getPersistentID());
		
		//decided to not update spawnlocation UUIDs here, instead we compare each spawnlocation entrys UUID to listPersistantEntities in monitor method
	}
	
	/* For unloading of an entity from chunks unloading */
	public void hookEntityDestroyed(EntityLivingBase ent) {
		System.out.println("hook entity obj destroyed: " + ent);
		removeObject(ent);
	}
	
	//unitType is unused
	public void addEntity(String unitType, EntityLivingBase ent) {
		if (listLoadedEntities.contains(ent)) {
			//this might have false positives, like when an entity is unloaded from chunk while game is running... unless somewhere else trims those types out
			CoroAI.dbg("WARNING: adding already existing entity to ManagedLocation");
		} else {
			listLoadedEntities.add(ent);
		}
		if (listPersistantEntities.contains(ent.getPersistentID())) {
			//again maybe false positives
			CoroAI.dbg("WARNING: adding already existing entitys _persistant ID_ to ManagedLocation");
		} else {
			listPersistantEntities.add(ent.getPersistentID());
		}
		
		
	}
	
	public void registerSpawnLocation(SpawnLocationData parData) {
		listSpawnLocations.add(parData);
	}
	
	/* register multiple types per coord */
	public void registerSpawnLocation(ChunkCoordinates parCoords, String... types) {
		for (int i = 0; i < types.length; i++) {
			listSpawnLocations.add(new SpawnLocationData(parCoords, types[i]));
		}
	}
	
	public void tickMonitorPersistantMembers() {
		//check occasionally
		//iterate spawn locations
		//check if UUID null
		//spawn
		
		for (int i = 0; i < listSpawnLocations.size(); i++) {
			SpawnLocationData data = listSpawnLocations.get(i);
			
			//update data if entity died
			if (data.entityUUID != null && !listPersistantEntities.contains(data.entityUUID)) {
				data.entityUUID = null;
			}
			
			if (data.entityUUID == null) {
				System.out.println("detected missing entity, attempting to respawn a " + data.type + " at coords: " + data.coords);
				spawnMemberAtSpawnLocation(data);
				if (data.entityUUID == null) {
					System.out.println("spawned location failed to spawn a new entity, perhaps spawnMemberAtSpawnLocation() method not overridden properly?");
				}
			}
		}
		
	}
	
	/* Meant to be overridden to handle mod specific entities */
	public void spawnMemberAtSpawnLocation(SpawnLocationData parData) {
		
	}
	
	public void cleanup() {
		//we dont care to actually destroy entity instances here so we just clear the lists
		listLoadedEntities.clear();
		listPersistantEntities.clear();
		
		//TODO: we should possibly iterate this to cleanup location structures better
		lookupIDToTownObject.clear();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound var1)
    {
		var1.setString("classname", this.getClass().getCanonicalName());
		
		var1.setInteger("locationID", locationID);
		var1.setInteger("dimID", dimID);
		CoroUtilFile.writeCoords("spawn", spawn, var1);
		
		NBTTagCompound nbtListPersistantEntities = new NBTTagCompound();
    	int count = 0;
    	for (int i = 0; i < listPersistantEntities.size(); i++) {
    		NBTTagCompound nbtEntry = new NBTTagCompound();
    		nbtEntry.setString("UUID", listPersistantEntities.get(i).toString());
    		nbtListPersistantEntities.setTag("entry_" + count++, nbtEntry);
		}
    	var1.setTag("listPersistantEntities", nbtListPersistantEntities);
    	
    	return var1;
    }
	
	@Override
	public void readFromNBT(NBTTagCompound var1)
    {
		hasInit = true;
    	locationID = var1.getInteger("locationID");
    	dimID = var1.getInteger("dimID");
    	spawn = CoroUtilFile.readCoords("spawn", var1);
    	
    	NBTTagCompound nbtPersistantEntities = var1.getCompoundTag("listPersistantEntities");
    	Iterator it = nbtPersistantEntities.func_150296_c().iterator();
    	while (it.hasNext()) {
    		String entryName = (String) it.next();
    		NBTTagCompound entry = nbtPersistantEntities.getCompoundTag(entryName);
    		UUID uuid = UUID.fromString(entry.getString("UUID"));
    		listPersistantEntities.add(uuid);
    	}
    }

	@Override
	public boolean isThreaded() {
		return false;
	}

	@Override
	public void tickUpdateThreaded() {
		
	}

	@Override
	public String getSharedSimulationName() {
		return "";
	}

	@Override
	public void init() {
		
	}

	@Override
	public void initPost() {
		// TODO Auto-generated method stub
		
	}
}
