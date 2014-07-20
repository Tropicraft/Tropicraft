package net.tropicraft.block.tileentity;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.tropicraft.registry.TCBlockRegistry;

public class TileEntitySifter extends TileEntity {

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

    public ItemStack siftItem;

    public TileEntitySifter() {
        rand = new Random();
        currentSiftTime = SIFT_TIME;
    }

    public void updateEntity() {
        // If sifter is sifting, decrement sift time
        if(currentSiftTime > 0 && isSifting) {
            currentSiftTime--;
        }

        // Rotation animation
        if(worldObj.isRemote) {
            this.yaw2 = this.yaw % 360.0D;
            this.yaw += 4.545454502105713D;
        }

        // Done sifting
        if (!worldObj.isRemote && isSifting && currentSiftTime <= 0) {
            double x = this.xCoord + worldObj.rand.nextDouble()*1.4;
            double y = this.yCoord + worldObj.rand.nextDouble()*1.4;
            double z = this.zCoord + worldObj.rand.nextDouble()*1.4;

            // TODO determine what to spawn

            if (isHeatedSifter()) {
                dumpResults(x, y, z, SiftType.HEATED);
            }

            currentSiftTime = SIFT_TIME;
            this.setSifting(false);
        }
    }

    public void dumpResults(double x, double y, double z, SiftType type) {
        float refinedAmt = getTagCompound(siftItem).getFloat("AmtRefined");

        refinedAmt = refine(refinedAmt);

        // If this is an unrefined ore
        if (refinedAmt > 0) {
            getTagCompound(siftItem).setFloat("AmtRefined", refinedAmt);
            spawn(siftItem, x, y, z);
        } else {
            dumpBeachResults(x, y, z);
        }
    }

    private void dumpBeachResults(double x, double y, double z) {
        int dumpCount = rand.nextInt(3) + 3;
        ItemStack stack;
        
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
        if (worldObj.isRemote) return;
        
        EntityItem eitem = new EntityItem(worldObj, x, y, z, stack);
        eitem.setLocationAndAngles(x, y, z, 0, 0);
        worldObj.spawnEntityInWorld(eitem);
    }

    private ItemStack getCommonItem() {
        int dmg = rand.nextInt(8);  //damage of shell

        switch (dmg) {
        case 0:
        case 1:
        case 2:
        case 4:
            //     return new ItemStack(TCItemRegistry.shells, 1, dmg);
        default:
            return new ItemStack(TCBlockRegistry.purifiedSand, 1);
        }
    }

    private ItemStack getRareItem() {
        int dmg = rand.nextInt(6);

        switch (dmg) {
        case 0:
            //TODO  return new ItemStack(TCItemRegistry.shells, 1, 3); //rube nautilus
        case 1:
            return new ItemStack(Items.gold_nugget, 1);
        case 2:
            return new ItemStack(Items.bucket, 1);
        case 3:
            return new ItemStack(Items.wooden_shovel, 1);
        case 4:
            return new ItemStack(Items.glass_bottle, 1);
        default:
            //TODO       return new ItemStack(TCItemRegistry.shells, 1, 3); //rube nautilus

            return new ItemStack(Items.diamond, 1);
        }
    }

    private float refine(float refinedAmt) {
        if (refinedAmt == 0) {
            return 33.333F;
        }

        if (refinedAmt > 32 && refinedAmt < 34) {
            return 66.667F;
        }

        if (refinedAmt > 65 && refinedAmt < 67) {
            return 100.000F;
        }

        return 0F;
    }

    /**
     * If the block below this sifter is a heat source, return true
     * @return If the block below the sifter should turn this sifter into a heated sifter
     */
    public boolean isHeatedSifter() {
        Block block = worldObj.getBlock(xCoord, yCoord - 1, zCoord);

        return block.getMaterial() == Material.fire || block.getMaterial() == Material.lava;
    }

    public void setSifting(boolean flag) {
        this.isSifting = flag;
        sync();
    }
    
    public boolean isSifting() {
        return this.isSifting;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        isSifting = nbt.getBoolean("isSifting");
        currentSiftTime = nbt.getInteger("currentSiftTime");

        NBTTagList itemtaglist = nbt.getTagList("Item", 10);

        NBTTagCompound itemtagcompound = itemtaglist.getCompoundTagAt(0);
        this.siftItem = ItemStack.loadItemStackFromNBT(itemtagcompound);
    }

    public void writeToNBT(NBTTagCompound nbt) {
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
}
