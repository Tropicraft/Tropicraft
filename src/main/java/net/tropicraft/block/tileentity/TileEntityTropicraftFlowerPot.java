package net.tropicraft.block.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityTropicraftFlowerPot extends TileEntity {

	/** Arbitrary unique flower ID value of the thing in this flower pot */
	private short flowerID;
	
	public TileEntityTropicraftFlowerPot() {
		
	}
	
	public short getFlowerID() {
        return this.flowerID;
    }
	
	public void setFlowerID(short id) {
		this.flowerID = id;
	}
	
	public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        flowerID = nbt.getShort("ID");
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setShort("ID", flowerID);
    }

    @Override
    /**
     * Called when you receive a TileEntityData packet for the location this
     * TileEntity is currently in. On the client, the NetworkManager will always
     * be the remote server. On the server, it will be whomever is responsible for
     * sending the packet.
     *
     * @param net The NetworkManager the packet originated from
     * @param pkt The data packet
     */
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
    	this.readFromNBT(pkt.func_148857_g());
    }

    /**
     * Overriden in a sign to provide the text.
     */
    public Packet getDescriptionPacket() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.writeToNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 5, nbttagcompound);
    }

}
