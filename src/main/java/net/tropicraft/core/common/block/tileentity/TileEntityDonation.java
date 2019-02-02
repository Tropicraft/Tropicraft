package net.tropicraft.core.common.block.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.tropicraft.core.common.donations.FireworkUtil;
import net.tropicraft.core.common.donations.TickerDonation;

public class TileEntityDonation extends TileEntity implements ITickable {

    private boolean registered;
    
    private int queued = 0;
    
	@Override
	public void update() {
	    if (!getWorld().isRemote) {
	        if (!registered) {
	            TickerDonation.addCallback(this);
	            registered = true;
	        }
	        if (queued > 0 && getWorld().getTotalWorldTime() % 20 == 0) {
	            BlockPos pos = getPos().up();
	            while (!getWorld().isAirBlock(pos) && pos.getY() < getPos().getY() + 10) {
	                pos = pos.up();
	            }
	            FireworkUtil.spawnFirework(pos, getWorld().provider.getDimension());
	            queued--;
	            markDirty();
	        }
	    }
	}
	
	@Override
	public void invalidate() {
	    super.invalidate();
	    TickerDonation.removeCallback(this);
	}
	
    public void triggerDonation() {
        queued++;
        markDirty();
    }

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.queued = nbt.getInteger("queuedDonations");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("queuedDonations", queued);
		return nbt;
	}
}
