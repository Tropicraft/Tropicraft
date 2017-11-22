package net.tropicraft.core.common.block.tileentity;

import javax.annotation.Nullable;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class TileEntityTropicraftFlowerPot extends TileEntity {

    private Item flowerPotItem;
    private int flowerPotData;
	
	public TileEntityTropicraftFlowerPot() {
		
	}
	
    public TileEntityTropicraftFlowerPot(Item potItem, int potData) {
        this.flowerPotItem = potItem;
        this.flowerPotData = potData;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        ResourceLocation resourcelocation = (ResourceLocation)Item.REGISTRY.getNameForObject(this.flowerPotItem);
        compound.setString("Item", resourcelocation == null ? "" : resourcelocation.toString());
        compound.setInteger("Data", this.flowerPotData);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        if (compound.hasKey("Item", 8)) {
            this.flowerPotItem = Item.getByNameOrId(compound.getString("Item"));
        } else {
            this.flowerPotItem = Item.getItemById(compound.getInteger("Item"));
        }

        this.flowerPotData = compound.getInteger("Data");
    }

    @Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 5, this.getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    public void setFlowerPotData(Item potItem, int potData) {
        this.flowerPotItem = potItem;
        this.flowerPotData = potData;
    }

    @Nullable
    public ItemStack getFlowerItemStack() {
        return this.flowerPotItem == null ? null : new ItemStack(this.flowerPotItem, 1, this.flowerPotData);
    }

    @Nullable
    public Item getFlowerPotItem() {
        return this.flowerPotItem;
    }

    public int getFlowerPotData() {
        return this.flowerPotData;
    }
}
