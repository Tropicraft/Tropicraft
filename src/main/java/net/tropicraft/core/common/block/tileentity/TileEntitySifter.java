package net.tropicraft.core.common.block.tileentity;

import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.tropicraft.core.common.block.tileentity.message.MessageSifterInventory;
import net.tropicraft.core.common.block.tileentity.message.MessageSifterStart;
import net.tropicraft.core.common.enums.TropicraftShells;
import net.tropicraft.core.common.network.TCPacketHandler;
import net.tropicraft.core.registry.BlockRegistry;
import net.tropicraft.core.registry.ItemRegistry;

public class TileEntitySifter extends TileEntity implements ITickable {

	public static enum SiftType {
		REGULAR,
		HEATED,
		;
	}

	/** Number of seconds to sift multiplied by the number of ticks per second */
	public static final int SIFT_TIME = 4 * 20;

	/** Is this machine currently sifting? */
	public boolean isSifting;

	/** Current progress in sifting; -1 if not sifting */
	public int currentSiftTime;

	private Random rand;    

	public double yaw;
	public double yaw2 = 0.0D;

	@Nonnull
	public ItemStack siftItem = ItemStack.EMPTY;

	public TileEntitySifter() {
		rand = new Random();
		currentSiftTime = SIFT_TIME;
	}

	@Override
	public void update() {
		// If sifter is sifting, decrement sift time
		if (currentSiftTime > 0 && isSifting) {
			currentSiftTime--;
		}

		// Rotation animation
		if (getWorld().isRemote) {
			this.yaw2 = this.yaw % 360.0D;
			this.yaw += 4.545454502105713D;
		}

		// Done sifting
		if (isSifting && currentSiftTime <= 0) {
			this.stopSifting();
		}
	}

	/**
	 * Drop all the necessary blocks/items from the sifter after sifting is complete
	 */
	public void dumpResults(double x, double y, double z, SiftType type) {
		if (type == SiftType.HEATED) {
			//spawn(siftItem, x, y, z);            
			spawn(new ItemStack(BlockRegistry.sands, 1, 0), x, y, z);
		} else {
			dumpBeachResults(x, y, z);
		}

		this.syncInventory();
	}

	/**
	 * Dump the items involved in regular sifting
	 */
	private void dumpBeachResults(double x, double y, double z) {
		int dumpCount = rand.nextInt(3) + 1;
		ItemStack stack;

		spawn(new ItemStack(BlockRegistry.sands, 1, 0), x, y, z);

		while (dumpCount > 0) {
			dumpCount--;

			if (rand.nextInt(10) == 0) {
				stack = getRareItem();
			} else {
				stack = getCommonItem();
			}

			spawn(stack, x, y, z);
		}
	}

	/**
	 * Spawns an EntityItem with the given ItemStack at the given coordinates
	 */
	private void spawn(ItemStack stack, double x, double y, double z) {
		if (getWorld().isRemote) return;

		EntityItem eitem = new EntityItem(getWorld(), x, y, z, stack);
		eitem.setLocationAndAngles(x, y, z, 0, 0);
		getWorld().spawnEntity(eitem);
	}

	private ItemStack getCommonItem() {
		// Random from -1 to size-1
		int dmg = rand.nextInt(TropicraftShells.values().length + 1) - 1;
		if (dmg < 0) {
		    return getRareItem();
		}
		return new ItemStack(ItemRegistry.shell, 1, dmg);
	}

	private ItemStack getRareItem() {
		int dmg = rand.nextInt(12);

		switch (dmg) {
		case 0:
			return new ItemStack(ItemRegistry.shell, 1, TropicraftShells.RUBE.getMeta()); //rube nautilus
		case 1:
			return new ItemStack(Items.GOLD_NUGGET, 1);
		case 2:
			return new ItemStack(Items.BUCKET, 1);
		case 3:
			return new ItemStack(Items.WOODEN_SHOVEL, 1);
		case 4:
			return new ItemStack(Items.GLASS_BOTTLE, 1);
		case 5:
			return new ItemStack(ItemRegistry.whitePearl, 1);
		case 6:
			return new ItemStack(ItemRegistry.blackPearl, 1);
		case 7:
			return new ItemStack(Items.STONE_SHOVEL, 1);
		default:
			return new ItemStack(ItemRegistry.shell, 1, TropicraftShells.RUBE.getMeta()); //rube nautilus
		}
	}

	/**
	 * If the block below this sifter is a heat source, return true
	 * @return If the block below the sifter should turn this sifter into a heated sifter
	 */
	public boolean isHeatedSifter() {
		IBlockState stateBelow = getWorld().getBlockState(this.getPos().down());

		return stateBelow.getMaterial() == Material.FIRE || stateBelow.getMaterial() == Material.LAVA;
	}
	
	public void addItemToSifter(ItemStack stack) {
		this.siftItem = stack.copy();
		this.siftItem.setCount(1);
		this.syncInventory();
	}
	
	public void startSifting() {
		this.isSifting = true;

		if (!getWorld().isRemote) {
			TCPacketHandler.INSTANCE.sendToDimension(new MessageSifterStart(this), getWorld().provider.getDimension());
		}
	}
	
	private void stopSifting() {
		double x = this.pos.getX() + getWorld().rand.nextDouble() * 1.4;
		double y = this.pos.getY() + getWorld().rand.nextDouble() * 1.4;
		double z = this.pos.getZ() + getWorld().rand.nextDouble() * 1.4;

		if (!this.getWorld().isRemote) {
			dumpResults(x, y, z, isHeatedSifter() ? SiftType.HEATED : SiftType.REGULAR);	
		}
		currentSiftTime = SIFT_TIME;
		this.isSifting = false;
		this.siftItem = ItemStack.EMPTY;
		this.syncInventory();		
	}

	public void setSifting(boolean flag) {
		this.isSifting = flag;
	}

	public boolean isSifting() {
		return this.isSifting;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		isSifting = nbt.getBoolean("isSifting");
		currentSiftTime = nbt.getInteger("currentSiftTime");

		NBTTagList itemtaglist = nbt.getTagList("Item", 10);

		NBTTagCompound itemtagcompound = itemtaglist.getCompoundTagAt(0);
		this.siftItem = new ItemStack(itemtagcompound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setBoolean("isSifting", isSifting);
		nbt.setInteger("currentSiftTime", currentSiftTime);

		NBTTagList nbttaglist = new NBTTagList();

		if (this.siftItem != null) {
			NBTTagCompound siftItemTagCompound = new NBTTagCompound();
			this.siftItem.writeToNBT(siftItemTagCompound);
			nbttaglist.appendTag(siftItemTagCompound);
		}

		nbt.setTag("Item", nbttaglist);

		return nbt;
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
		TCPacketHandler.INSTANCE.sendToDimension(new MessageSifterInventory(this), getWorld().provider.getDimension());
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