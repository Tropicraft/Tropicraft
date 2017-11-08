package net.tropicraft.core.common.block.tileentity;

import javax.annotation.Nullable;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.tropicraft.core.common.block.scuba.BlockAirCompressor;
import net.tropicraft.core.common.block.tileentity.message.MessageAirCompressorInventory;
import net.tropicraft.core.common.item.scuba.ItemScubaTank;
import net.tropicraft.core.common.item.scuba.ScubaCapabilities;
import net.tropicraft.core.common.item.scuba.api.IScubaTank;
import net.tropicraft.core.common.network.TCPacketHandler;

public class TileEntityAirCompressor extends TileEntity implements ITickable, IMachineTile {

	/** Is the compressor currently giving air */
	public boolean compressing;

	/** Number of ticks compressed so far */
	private int ticks;

	/** Amount of PSI to fill per tick */
	private static final float fillRate = 0.10F;

	/** The stack that is currently being filled */
	private ItemStack stack;
	
	private IScubaTank tank;

	public TileEntityAirCompressor() {

	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.compressing = nbt.getBoolean("Compressing");
		this.ticks = nbt.getInteger("Ticks");

		if (nbt.hasKey("Tank")) {
			setTank(ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("Tank")));
		} else {
			setTank(null);
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setBoolean("Compressing", compressing);
		nbt.setInteger("Ticks", ticks);

		if (this.stack != null) {
			NBTTagCompound var4 = new NBTTagCompound();
			this.stack.writeToNBT(var4);
			nbt.setTag("Tank", var4);
		}
		return nbt;
	}
	
	public void setTank(ItemStack tankItemStack) {
	    this.stack = tankItemStack;
        this.tank = stack == null ? null : stack.getCapability(ScubaCapabilities.getTankCapability(), null);
	}
	
    public ItemStack getTankStack() {
        return stack;
    }

	@Override
	public void update() {
		if (tank == null)
			return;

		float airContained = tank.getPressure();

		if (compressing && airContained < tank.getAirType().getMaxCapacity()) {
			if (airContained + fillRate >= tank.getAirType().getMaxCapacity()) {
				tank.setPressure(tank.getAirType().getMaxCapacity());
				ticks++;
				finishCompressing();                    
			} else {
				tank.setPressure(airContained + fillRate);
				ticks++;
			}
		}
	}

	/**
	 * Retrives an existing nbt tag compound or creates a new one if it is null
	 * @param stack
	 */
	public NBTTagCompound getTagCompound(ItemStack stack) {
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());

		return stack.getTagCompound();
	}

	public boolean addTank(ItemStack stack) {
		if (tank == null && stack.getItem() != null && stack.getItem() instanceof ItemScubaTank) {
			setTank(stack);
			this.compressing = true;
			syncInventory();
			return true;
		}

		return false;
	}

	public void ejectTank() {
		if (stack != null) {
			EntityItem tankItem = new EntityItem(world, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), stack);
			world.spawnEntity(tankItem);
			setTank(null);
		}

		this.ticks = 0;
		this.compressing = false;
		syncInventory();
	}

	public boolean isDoneCompressing() {
		return this.ticks > 0 && !this.compressing;
	}

	public float getTickRatio() {
		return this.ticks / (tank.getAirType().getMaxCapacity() * fillRate);
	}

	public boolean isCompressing() {
		return this.compressing;
	}

	public void startCompressing() {
		this.compressing = true;
		syncInventory();
	}

	public void finishCompressing() {
		this.compressing = false;
		syncInventory();
	}
	
	/* == IMachineTile == */
	
	@Override
	public boolean isActive() {
	    return getTankStack() != null;
	}
	
	@Override
	public int getProgress() {
	    return ticks;
	}
	
	@Override
	public EnumFacing getFacing() {
	    return getWorld().getBlockState(getPos()).getValue(BlockAirCompressor.FACING);
	}

	/**
	 * Called when you receive a TileEntityData packet for the location this
	 * TileEntity is currently in. On the client, the NetworkManager will always
	 * be the remote server. On the server, it will be whomever is responsible for
	 * sending the packet.
	 *
	 * @param net The NetworkManager the packet originated from
	 * @param pkt The data packet
	 */
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.getNbtCompound());
	}

	protected void syncInventory() {
		TCPacketHandler.INSTANCE.sendToDimension(new MessageAirCompressorInventory(this), getWorld().provider.getDimension());
	}

	@Override
	@Nullable
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.pos, 1, this.getUpdateTag());
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound nbttagcompound = this.writeToNBT(new NBTTagCompound());
		return nbttagcompound;
	}
}
