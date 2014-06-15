package net.tropicraft.block.tileentity;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.tropicraft.item.scuba.ItemScubaGear.AirType;
import net.tropicraft.item.scuba.ItemScubaTank;
import net.tropicraft.util.TropicraftUtils;

public class TileEntityAirCompressor extends TileEntity {

    /** Is the compressor currently giving air */
    public boolean compressing;
    
    /** Number of ticks compressed so far */
    private int ticks;

    /** Amount of PSI to fill per tick */
    private static final float fillRate = 0.10F;

    /** The tank that is currently being filled */
    private ItemStack tank;

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
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setBoolean("Compressing", compressing);
        nbt.setInteger("Ticks", ticks);

        if (this.tank != null) {
            NBTTagCompound var4 = new NBTTagCompound();
            this.tank.writeToNBT(var4);
            nbt.setTag("Tank", var4);
        }
    }

    @Override
    public void updateEntity() {
        if (tank == null)
            return;
        
       // System.out.println(compressing);
        
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
    
    public boolean addTank(ItemStack stack) {
        if (tank == null && stack.getItem() != null && stack.getItem() instanceof ItemScubaTank) {
            this.tank = stack;
            this.compressing = true;
            sync();
            return true;
        }
        
        return false;
    }
    
    public void ejectTank() {
        if (tank != null) {
            EntityItem tankItem = new EntityItem(worldObj, xCoord, yCoord, zCoord, tank);
            worldObj.spawnEntityInWorld(tankItem);
            tank = null;
        }
        
        this.ticks = 0;
        this.compressing = false;
        sync();
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
        sync();
    }
    
    public void finishCompressing() {
        this.compressing = false;
        sync();
    }
}
