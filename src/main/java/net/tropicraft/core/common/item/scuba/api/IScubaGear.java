package net.tropicraft.core.common.item.scuba.api;

import java.util.Optional;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.tropicraft.core.common.item.scuba.ScubaCapabilities;

public interface IScubaGear extends IItemHandler, INBTSerializable<NBTTagCompound> {

    Pair<IScubaTank, IScubaTank> getTanks();
    
    default void markDirty() {}
    
    // These are not necessary yet, current impl just uses ItemStackHandler's serialization
    
    @Override
    default NBTTagCompound serializeNBT() { return new NBTTagCompound(); }

    @Override
    default void deserializeNBT(NBTTagCompound nbt) {}
    
    public class ScubaGear extends ItemStackHandler implements IScubaGear {
        
        public ScubaGear() {
            super(2);
        }
        
        @Override
        public void setSize(int size) {} // It will always be 2
        
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            markDirty();
        }

        @Override
        public Pair<IScubaTank, IScubaTank> getTanks() {
            return Pair.of(getTank(getStackInSlot(0)), getTank(getStackInSlot(1)));
        }

        private @Nullable IScubaTank getTank(ItemStack stack) {
            return Optional.ofNullable(stack).map(s -> s.getCapability(ScubaCapabilities.getTankCapability(), null)).orElse(null);
        }
    }
}
