package net.tropicraft.core.common.capability;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.tropicraft.core.common.town.ISimulationTickable;
import net.tropicraft.core.common.town.ManagedLocation;

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
    }
    
    public void writeNBT(NBTTagCompound nbt) {
        nbt.setInteger("lastIDUsedForKoaVillage", lastIDUsedForKoaVillage);
    }

    public void addTickingLocation(ISimulationTickable location) {
        addTickingLocation(location, true);
    }

    public void addTickingLocation(ISimulationTickable location, boolean init) {
        //if (lookupDungeonEntrances == null) lookupDungeonEntrances = new HashMap<Integer, DungeonEntrance>();
        if (location.getOrigin() != null) {
            Integer hash = location.getOrigin().hashCode();
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
