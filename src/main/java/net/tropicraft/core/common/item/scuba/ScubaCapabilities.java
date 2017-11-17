package net.tropicraft.core.common.item.scuba;

import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.tropicraft.core.common.item.scuba.api.IScubaGear;
import net.tropicraft.core.common.item.scuba.api.IScubaGear.ScubaGear;
import net.tropicraft.core.common.item.scuba.api.IScubaTank;
import net.tropicraft.core.common.item.scuba.api.IScubaTank.ScubaTank;

public class ScubaCapabilities {
    
    @SuppressWarnings("null")
	@CapabilityInject(IScubaTank.class)
    @Nonnull
    private static Capability<IScubaTank> tankCapability;
    
    @SuppressWarnings("null")
	@CapabilityInject(IScubaGear.class)
    @Nonnull
    private static Capability<IScubaGear> gearCapability;
    
    @Nonnull
    public static Capability<IScubaTank> getTankCapability() {
        return tankCapability;
    }
    
    @Nonnull
    public static Capability<IScubaGear> getGearCapability() {
        return gearCapability;
    }
    
    public static void register() {
        
        CapabilityManager.INSTANCE.register(IScubaTank.class, new Capability.IStorage<IScubaTank>() {

            @Override
            public NBTBase writeNBT(Capability<IScubaTank> capability, IScubaTank instance, EnumFacing side) {
                return instance.serializeNBT();
            }

            @Override
            public void readNBT(Capability<IScubaTank> capability, IScubaTank instance, EnumFacing side, NBTBase nbt) {
                instance.deserializeNBT((NBTTagCompound) nbt);
            }
            
        }, ScubaTank::new);
        
        CapabilityManager.INSTANCE.register(IScubaGear.class, new Capability.IStorage<IScubaGear>() {

            @Override
            public NBTBase writeNBT(Capability<IScubaGear> capability, IScubaGear instance, EnumFacing side) {
                return instance.serializeNBT();
            }

            @Override
            public void readNBT(Capability<IScubaGear> capability, IScubaGear instance, EnumFacing side, NBTBase nbt) {
                instance.deserializeNBT((NBTTagCompound) nbt);
            }
            
        }, ScubaGear::new);
    }

    public static <C> ICapabilityProvider getProvider(final Capability<C> cap, final Supplier<C> factory) {
        return new ICapabilityProvider() {

            @Override
            public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
                return capability == cap;
            }

            @Override
            public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
                return hasCapability(capability, facing) ? cap.cast(factory.get()) : null;
            }
        };
    }
}
