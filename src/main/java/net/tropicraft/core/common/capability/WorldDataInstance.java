package net.tropicraft.core.common.capability;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.tropicraft.core.common.town.ISimulationTickable;

import java.util.concurrent.ConcurrentHashMap;

public class WorldDataInstance {

    private World world;

    public int test = 0;

    public ConcurrentHashMap<Integer, ISimulationTickable> lookupTickingManagedLocations;

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
        test = nbt.getInteger("test");
    }
    
    public void writeNBT(NBTTagCompound nbt) {
        nbt.setInteger("test", test);
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
}
