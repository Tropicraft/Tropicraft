package net.tropicraft.core.common.block.tileentity;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tropicraft.core.common.block.BambooDoubleChestItemHandler;
import net.tropicraft.core.common.block.BlockBambooChest;

public class TileEntityBambooChest extends TileEntityChest {

    /** Is this chest unbreakble (Koa chest) */
    private boolean unbreakable = false;

    public TileEntityBambooChest() {
        super();
    }
    
    /**
     * Called from Chunk.setBlockIDWithMetadata and Chunk.fillChunk, determines if this tile entity should be re-created when the ID, or Metadata changes.
     * Use with caution as this will leave straggler TileEntities, or create conflicts with other TileEntities if not used properly.
     *
     * @param world Current world
     * @param pos Tile's world position
     * @param oldState The old ID of the block
     * @param newState The new ID of the block (May be the same)
     * @return true forcing the invalidation of the existing TE, false not to invalidate the existing TE
     */
    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return oldState.getBlock() != newSate.getBlock();
    }

    @Override
    public String getName() {
        return "Bamboo Chest";
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        unbreakable = nbttagcompound.getBoolean("unbreakable");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setBoolean("unbreakable", unbreakable);
        
        return nbttagcompound;
    }
    
    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        BlockPos pos = getPos();
        return new AxisAlignedBB(pos.add(-1, 0, -1), pos.add(2, 2, 2));
    }

    /**
     *
     * @return Returns if this chest is unbreakable
     */
    public boolean isUnbreakable() {
        return unbreakable;
    }

    /**
     * Sets whether this chest is unbreakable or not
     * @param flag Value to set the unbreakable flag to
     */
    public void setIsUnbreakable(boolean flag) {
        unbreakable = flag;
    }

    @Nullable
    @Override
    protected TileEntityChest getAdjacentChest(EnumFacing side)
    {
        BlockPos blockpos = this.pos.offset(side);

        if (this.isChestAt(blockpos))
        {
            TileEntity tileentity = this.world.getTileEntity(blockpos);

            if (tileentity instanceof TileEntityBambooChest)
            {
                TileEntityBambooChest tileentitychest = (TileEntityBambooChest)tileentity;
                tileentitychest.setNeighbor(this, side.getOpposite());
                return tileentitychest;
            }
        }

        return null;
    }
    
    @SuppressWarnings("incomplete-switch")
    private void setNeighbor(TileEntityChest chestTe, EnumFacing side)
    {
        if (chestTe.isInvalid())
        {
            this.adjacentChestChecked = false;
        }
        else if (this.adjacentChestChecked)
        {
            switch (side)
            {
                case NORTH:

                    if (this.adjacentChestZNeg != chestTe)
                    {
                        this.adjacentChestChecked = false;
                    }

                    break;
                case SOUTH:

                    if (this.adjacentChestZPos != chestTe)
                    {
                        this.adjacentChestChecked = false;
                    }

                    break;
                case EAST:

                    if (this.adjacentChestXPos != chestTe)
                    {
                        this.adjacentChestChecked = false;
                    }

                    break;
                case WEST:

                    if (this.adjacentChestXNeg != chestTe)
                    {
                        this.adjacentChestChecked = false;
                    }
            }
        }
    }
    
    private boolean isChestAt(BlockPos posIn)
    {
        if (this.world == null)
        {
            return false;
        }
        else
        {
            Block block = this.world.getBlockState(posIn).getBlock();
            return block instanceof BlockBambooChest && ((BlockBambooChest)block).chestType == this.getChestType();
        }
    }
    
    public BambooDoubleChestItemHandler doubleChestHandler;

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable net.minecraft.util.EnumFacing facing)
    {
        if (capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            if(doubleChestHandler == null || doubleChestHandler.needsRefresh())
                doubleChestHandler = BambooDoubleChestItemHandler.get(this);
            if (doubleChestHandler != null && doubleChestHandler != BambooDoubleChestItemHandler.NO_ADJACENT_CHESTS_INSTANCE)
                return (T) doubleChestHandler;
        }
        return super.getCapability(capability, facing);
    }

    public net.minecraftforge.items.IItemHandler getSingleChestHandler()
    {
        return super.getCapability(net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
    }
}
