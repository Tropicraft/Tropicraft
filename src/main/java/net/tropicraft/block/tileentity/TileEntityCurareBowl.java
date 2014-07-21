package net.tropicraft.block.tileentity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.tropicraft.curare.CurareMixRegistry;
import net.tropicraft.curare.CurareType;
import net.tropicraft.registry.TCItemRegistry;

public class TileEntityCurareBowl extends TileEntity {

    public List<ItemStack> ingredients;

    /**
     * Number of clicks required to fully mix the ingredients in the bowl to get a result
     */
    private static final int REQUIRED_NUM_CLICKS = 30;
    public static final int TICKS_PER_PESTLE_CLICK = 5;

    public int pestleTicks;
    public int numClicks;
    
    public TileEntityCurareBowl() {
        
    }

    @Override
    public void readFromNBT(NBTTagCompound par1nbtTagCompound) {
        super.readFromNBT(par1nbtTagCompound);

        ingredients = new ArrayList<ItemStack>();

        int length = par1nbtTagCompound.getInteger("ContentLength");
        
        for (int i = 0; i < length; i++) {
            ItemStack stack = ItemStack.loadItemStackFromNBT(par1nbtTagCompound.getCompoundTag("Contents" + i));
            if (stack != null) {
                ingredients.add(stack);
            }
        }

        numClicks = par1nbtTagCompound.getInteger("Clicks");
        pestleTicks = par1nbtTagCompound.getShort("PestleTicks");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);

        if (ingredients != null) {
            for (int i = 0; i < ingredients.size(); i++) {
                if (ingredients.get(i) != null) {
                    NBTTagCompound var4 = new NBTTagCompound();
                    ingredients.get(i).writeToNBT(var4);
                    nbt.setTag("Contents" + i, var4);
                }
            }
            
            nbt.setInteger("ContentLength", ingredients.size());
        } else
            ingredients = new ArrayList<ItemStack>();

        nbt.setInteger("Clicks", numClicks);
        nbt.setShort("PestleTicks", (short)pestleTicks);
    }

    public void incrementNumClicks() {
        this.numClicks++;
        sync();
    }

    @Override
    public void updateEntity() {
        if (pestleTicks < numClicks*TICKS_PER_PESTLE_CLICK) {
            pestleTicks++;
        }
    }

    public void resetClicks() {
        this.numClicks = 0;
        this.pestleTicks = 0;
        sync();
    }

    public void addIngredient(ItemStack stack) {
        if (ingredients == null) {
            ingredients = new ArrayList<ItemStack>();
        }

        ingredients.add(new ItemStack(stack.getItem(), 1, stack.getItemDamage()));
        sync();
    }

    public void dropResult() {
        ItemStack[] stacks = new ItemStack[ingredients.size()];

        int c = 0;

        for (ItemStack i : ingredients) {
            stacks[c] = i;
            c++;
        }
        
        ItemStack stack = getCurareResult(getResult(stacks));
        
        EntityItem result = new EntityItem(worldObj, xCoord, yCoord + (new Random().nextInt(1) + 0.3), zCoord, stack);

        if (!worldObj.isRemote) {
            worldObj.spawnEntityInWorld(result);
        }
        
        ingredients.clear();
        sync();
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

    public boolean isBowlFull() {
        if (ingredients == null) {
            return false;
        }

        for (ItemStack i : ingredients) {
            if (i.getItem() == null) {
                return false;
            }
        }

        if (ingredients.size() < 1)
            return false;

        return true;
    }

    public ItemStack[] getIngredients() {
        if (ingredients == null)
            return new ItemStack[]{};
        
        ItemStack[] temp = new ItemStack[ingredients.size()];
        
        for (int i = 0; i < ingredients.size(); i++) {
            temp[i] = ingredients.get(i);
        }
        
        return temp;
    }
    
    public List<ItemStack> getIngredientList() {
        return ingredients;
    }
    
    public boolean hasMetMaxNumClicks() {
        return numClicks >= REQUIRED_NUM_CLICKS;
    }

    private ItemStack getCurareResult(CurareType curare) {
        return new ItemStack(TCItemRegistry.curare, 1, curare.curareId);   //the id is also the damage value 8)
    }

    private CurareType getResult(ItemStack[] ingredients1) {
        return CurareMixRegistry.getInstance().getCurareFromIngredients(ingredients1);
    }
}
