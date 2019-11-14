package net.tropicraft.core.common.block.tileentity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tropicraft.core.common.donations.FireworkUtil;
import net.tropicraft.core.common.donations.TickerDonation;

public class DonationTileEntity extends TileEntity implements ITickableTileEntity {

    private boolean registered;
    
    private int queued = 0;
    private int randomOffset = 0;
    
    public DonationTileEntity() {
        super(TropicraftTileEntityTypes.DONATION.get());
    }
    
    @Override
    public void setWorld(World worldIn) {
        super.setWorld(worldIn);
        this.randomOffset = worldIn.getRandom().nextInt(20);
    }
    
	@Override
	public void tick() {
	    if (!getWorld().isRemote) {
	        if (!registered) {
	            TickerDonation.addCallback(this);
	            registered = true;
	        }
	        if (queued > 0 && getWorld().getGameTime() % 20 == randomOffset) {
	            BlockPos pos = getPos().up();
	            while (!getWorld().isAirBlock(pos) && pos.getY() < getPos().getY() + 10) {
	                pos = pos.up();
	            }
	            FireworkUtil.spawnFirework(pos, getWorld(), FireworkUtil.Palette.COOL_EARTH.getPalette());
	            queued--;
	            markDirty();
	        }
	    }
	}
	
	@Override
	public void remove() {
	    super.remove();
	    TickerDonation.removeCallback(this);
	}
	
    @SuppressWarnings("deprecation")
    public void triggerDonation() {
        if (world.isBlockLoaded(getPos())) {
            queued++;
            markDirty();
        }
    }

    @Override
    public void read(CompoundNBT nbt) {
		super.read(nbt);
		this.queued = nbt.getInt("queuedDonations");
	}

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
		super.write(nbt);
		nbt.putInt("queuedDonations", queued);
		return nbt;
	}
}
