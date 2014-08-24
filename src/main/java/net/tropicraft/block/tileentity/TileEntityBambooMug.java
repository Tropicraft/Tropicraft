package net.tropicraft.block.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityBambooMug extends TileEntity {
    public ItemStack cocktail;

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (nbt.hasKey("Cocktail")) {
            this.cocktail = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("Cocktail"));
        } else {
            this.cocktail = null;
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt2) {
        super.writeToNBT(nbt2);
        if (this.cocktail != null) {
            NBTTagCompound nbt = new NBTTagCompound();
            this.cocktail.writeToNBT(nbt);
            nbt2.setTag("Cocktail", nbt);
        }
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
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.func_148857_g());
    }
    
    public void sync() {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.writeToNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 5, nbttagcompound);
    }

    public boolean isEmpty() {
        return this.cocktail == null;
    }

    public void setCocktail(ItemStack cocktail) {
        this.cocktail = cocktail;
        this.sync();
    }

    public int getMetadata() {
        return worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
    }

    @Override
    public boolean canUpdate() {
        return false;
    }
}
