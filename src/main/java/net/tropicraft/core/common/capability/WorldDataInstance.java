package net.tropicraft.core.common.capability;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.tropicraft.core.common.town.ISimulationTickable;
import net.tropicraft.core.common.town.ManagedLocation;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class WorldDataInstance {

    private World world;

    //switching to random UUID generator is probably the best practice, int ids forever increasing for now!
    private int lastIDUsedForKoaVillage = 0;

    public ConcurrentHashMap<Integer, ISimulationTickable> lookupTickingManagedLocations = new ConcurrentHashMap<>();

    public WorldDataInstance() {

    }

    public World getWorld() {
        return world;
    }

    public WorldDataInstance setWorld(World world) {
        this.world = world;
        return this;
    }

    public void readNBT(NBTTagCompound nbt) {
        lastIDUsedForKoaVillage = nbt.getInteger("lastIDUsedForKoaVillage");

        NBTTagCompound tickingLocations = nbt.getCompoundTag("tickingLocations");

        Iterator it = tickingLocations.getKeySet().iterator();

        while (it.hasNext()) {
            String keyName = (String)it.next();
            NBTTagCompound nbt2 = tickingLocations.getCompoundTag(keyName);

            String classname = nbt2.getString("classname");

            ClassLoader classLoader = WorldDataInstance.class.getClassLoader();

            Class aClass = null;

            try {
                aClass = classLoader.loadClass(classname);
                //System.out.println("aClass.getName() = " + aClass.getName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            ISimulationTickable locationObj = null;
            if (aClass != null) {
                try {
                    locationObj = (ISimulationTickable)aClass.getConstructor(new Class[] {}).newInstance();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (locationObj != null) {
                locationObj.init();
                locationObj.readFromNBT(nbt2);
                locationObj.initPost();
                addTickingLocation(locationObj);

                //System.out.println("reading in ticking location: " + nbt.toString() + " - " + entrance.getOrigin().posX + " - " + entrance.spawn.posZ);
            }
        }
    }
    
    public void writeNBT(NBTTagCompound nbt) {
        nbt.setInteger("lastIDUsedForKoaVillage", lastIDUsedForKoaVillage);

        NBTTagCompound nbtSet = new NBTTagCompound();

        int index = 0;
        for (ISimulationTickable entry : lookupTickingManagedLocations.values()) {
            NBTTagCompound nbt2 = new NBTTagCompound();
            entry.writeToNBT(nbt2);
            nbtSet.setTag("" + index++, nbt2);
        }
        nbt.setTag("tickingLocations", nbtSet);

        nbt.setString("classname", this.getClass().getCanonicalName());
    }

    public void addTickingLocation(ISimulationTickable location) {
        addTickingLocation(location, true);
    }

    public void addTickingLocation(ISimulationTickable location, boolean init) {
        //if (lookupDungeonEntrances == null) lookupDungeonEntrances = new HashMap<Integer, DungeonEntrance>();
        if (location.getOrigin() != null) {
            //Integer hash = location.getOrigin().hashCode();
            int hash = 0;
            if (location instanceof ManagedLocation) {
                hash = ((ManagedLocation) location).locationID;
            } else {
                hash = location.getOrigin().hashCode();
            }
            if (!lookupTickingManagedLocations.containsKey(hash)) {
                lookupTickingManagedLocations.put(hash, location);
                //relocated to a ticking first time init so it can be after readnbt
                //if (init) location.init();
            } else {
                System.out.println("warning: location already exists at these coords: " + location.getOrigin());
            }
        }
        //listTickingLocations.add(location);
    }

    public void removeTickingLocation(ISimulationTickable location) {
        if (location.getOrigin() != null) {
            Integer hash = location.getOrigin().hashCode();
            if (lookupTickingManagedLocations.containsKey(hash)) {
                lookupTickingManagedLocations.remove(hash);
                location.cleanup();
            } else {
                System.out.println("Error, couldnt find location for removal");
            }
        }
        //listTickingLocations.remove(location);
    }

    public void tick() {
        for (ISimulationTickable entry : lookupTickingManagedLocations.values()) {
            entry.tickUpdate();
        }
    }

    public ISimulationTickable getLocationByID(int id) {
        return lookupTickingManagedLocations.get(id);
    }

    public int getAndIncrementKoaIDVillage() {
        return lastIDUsedForKoaVillage++;
    }
}
