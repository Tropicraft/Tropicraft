package net.tropicraft.core.common.block.tileentity;

import javax.annotation.Nullable;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.tropicraft.core.common.block.tileentity.message.MessageAirCompressorInventory;
import net.tropicraft.core.common.item.scuba.ItemScubaGear.AirType;
import net.tropicraft.core.common.item.scuba.ItemScubaTank;
import net.tropicraft.core.common.network.TCPacketHandler;

public class TileEntityAirCompressor extends TileEntity implements ITickable  {

	/** Is the compressor currently giving air */
	public boolean compressing;

	/** Number of ticks compressed so far */
	private int ticks;

	/** Amount of PSI to fill per tick */
	private static final float fillRate = 0.10F;

	/** The tank that is currently being filled */
	public ItemStack tank;

	/** Max air capacity of the tank */
	private float maxCapacity;

	public TileEntityAirCompressor() {

	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.compressing = nbt.getBoolean("Compressing");
		this.ticks = nbt.getInteger("Ticks");

		if (nbt.hasKey("Tank")) {
			this.tank = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("Tank"));
			maxCapacity = this.tank.getItemDamage() == 1 ? AirType.TRIMIX.getMaxCapacity() : AirType.REGULAR.getMaxCapacity();
		} else {
			this.tank = null;
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setBoolean("Compressing", compressing);
		nbt.setInteger("Ticks", ticks);

		if (this.tank != null) {
			NBTTagCompound var4 = new NBTTagCompound();
			this.tank.writeToNBT(var4);
			nbt.setTag("Tank", var4);
		}
		return nbt;
	}
	
	public void setTank(ItemStack tankItemStack) {
	    this.tank = tankItemStack;
	}

	@Override
	public void update() {
		if (tank == null)
			return;

		float airContained = getTagCompound(tank).getFloat("AirContained");

		if (compressing && airContained < AirType.REGULAR.getMaxCapacity()) {
			if (airContained + fillRate >= AirType.REGULAR.getMaxCapacity()) {
				tank.getTagCompound().setFloat("AirContained", AirType.REGULAR.getMaxCapacity());
				ticks++;
				finishCompressing();                    
			} else {
				tank.getTagCompound().setFloat("AirContained", airContained + fillRate);
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
			this.tank = stack;
			this.compressing = true;
			syncInventory();
			return true;
		}

		return false;
	}

	public void ejectTank() {
		if (tank != null) {
			EntityItem tankItem = new EntityItem(world, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), tank);
			world.spawnEntity(tankItem);
			tank = null;
		}

		this.ticks = 0;
		this.compressing = false;
		syncInventory();
	}

	public boolean isDoneCompressing() {
		return this.ticks > 0 && !this.compressing;
	}

	public float getTickRatio() {
		return this.ticks / (AirType.REGULAR.getMaxCapacity() * fillRate);
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
