package net.tropicraft.core.common.block;

import java.lang.ref.WeakReference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Objects;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.tropicraft.core.common.block.tileentity.TileEntityBambooChest;

public class BambooDoubleChestItemHandler extends WeakReference<TileEntityBambooChest> implements IItemHandlerModifiable {

    // Dummy cache value to signify that we have checked and definitely found no adjacent chests
    public static final BambooDoubleChestItemHandler NO_ADJACENT_CHESTS_INSTANCE = new BambooDoubleChestItemHandler(null, null, false);
    private final boolean mainChestIsUpper;
    private final TileEntityBambooChest mainChest;
    private final int hashCode;
    
    public BambooDoubleChestItemHandler(TileEntityBambooChest mainChest, TileEntityBambooChest other, boolean mainChestIsUpper) {
        super(other);
        this.mainChest = mainChest;
        this.mainChestIsUpper = mainChestIsUpper;
        hashCode = Objects.hashCode(mainChestIsUpper ? mainChest : other) * 31 + Objects.hashCode(!mainChestIsUpper ? mainChest : other);
    }
    
    @Nullable
    public static BambooDoubleChestItemHandler get(TileEntityBambooChest chest)
    {
        World world = chest.getWorld();
        BlockPos pos = chest.getPos();
        if (world == null || pos == null || !world.isBlockLoaded(pos))
            return null; // Still loading

        Block blockType = chest.getBlockType();

        EnumFacing[] horizontals = EnumFacing.HORIZONTALS;
        for (int i = horizontals.length - 1; i >= 0; i--)   // Use reverse order so we can return early
        {
            EnumFacing enumfacing = horizontals[i];
            BlockPos blockpos = pos.offset(enumfacing);
            Block block = world.getBlockState(blockpos).getBlock();

            if (block == blockType)
            {
                TileEntity otherTE = world.getTileEntity(blockpos);

                if (otherTE instanceof TileEntityBambooChest)
                {
                    TileEntityBambooChest otherChest = (TileEntityBambooChest) otherTE;
                    return new BambooDoubleChestItemHandler(chest, otherChest,
                            enumfacing != net.minecraft.util.EnumFacing.WEST && enumfacing != net.minecraft.util.EnumFacing.NORTH);

                }
            }
        }
        return NO_ADJACENT_CHESTS_INSTANCE; //All alone
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        BambooDoubleChestItemHandler that = (BambooDoubleChestItemHandler) o;

        if (hashCode != that.hashCode)
            return false;

        final TileEntityBambooChest otherChest = getOtherChest();
        if (mainChestIsUpper == that.mainChestIsUpper)
            return Objects.equal(mainChest, that.mainChest) && Objects.equal(otherChest, that.getOtherChest());
        else
            return Objects.equal(mainChest, that.getOtherChest()) && Objects.equal(otherChest, that.mainChest);
    }
    
    @Nullable
    private TileEntityBambooChest getOtherChest()
    {
        TileEntityBambooChest tileEntityChest = get();
        return tileEntityChest != null && !tileEntityChest.isInvalid() ? tileEntityChest : null;
    }

    @Override
    public int getSlots() {
        return 27 * 2;
    }

    @Override
    @Nonnull
    public ItemStack getStackInSlot(int slot) {
        boolean accessingUpperChest = slot < 27;
        int targetSlot = accessingUpperChest ? slot : slot - 27;
        TileEntityChest chest = getChest(accessingUpperChest);
        return chest != null ? chest.getStackInSlot(targetSlot) : ItemStack.EMPTY;
    }

    @Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        boolean accessingUpperChest = slot < 27;
        int targetSlot = accessingUpperChest ? slot : slot - 27;
        TileEntityBambooChest chest = getChest(accessingUpperChest);
        if (chest == null)
            return stack;

        int starting = stack.getCount();
        ItemStack ret = chest.getSingleChestHandler().insertItem(targetSlot, stack, simulate);
        if (ret.getCount() != starting && !simulate)
        {
            chest = getChest(!accessingUpperChest);
            if (chest != null)
                chest.markDirty();
        }

        return ret;
    }

    @Override
    @Nonnull
    public ItemStack extractItem(int slot, int amount, boolean simulate)
    {
        boolean accessingUpperChest = slot < 27;
        int targetSlot = accessingUpperChest ? slot : slot - 27;
        TileEntityBambooChest chest = getChest(accessingUpperChest);
        if (chest == null)
            return ItemStack.EMPTY;

        ItemStack ret = chest.getSingleChestHandler().extractItem(targetSlot, amount, simulate);
        if (!ret.isEmpty() && !simulate)
        {
            chest = getChest(!accessingUpperChest);
            if (chest != null)
                chest.markDirty();
        }

        return ret;
    }

    @Override
    public int getSlotLimit(int slot) {
        boolean accessingUpperChest = slot < 27;
        return getChest(accessingUpperChest).getInventoryStackLimit();
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        boolean accessingUpperChest = slot < 27;
        int targetSlot = accessingUpperChest ? slot : slot - 27;
        TileEntityBambooChest chest = getChest(accessingUpperChest);
        if (chest != null) {
            IItemHandler singleHandler = chest.getSingleChestHandler();
            if (singleHandler instanceof IItemHandlerModifiable) {
                ((IItemHandlerModifiable) singleHandler).setStackInSlot(targetSlot, stack);
            }
        }

        chest = getChest(!accessingUpperChest);
        if (chest != null)
            chest.markDirty();
    }
    
    @Nullable
    public TileEntityBambooChest getChest(boolean accessingUpper) {
        if (accessingUpper == mainChestIsUpper)
            return mainChest;
        else {
            return getOtherChest();
        }
    }

    public boolean needsRefresh() {
        if (this == NO_ADJACENT_CHESTS_INSTANCE)
            return false;
        TileEntityBambooChest tileEntityChest = get();
        return tileEntityChest == null || tileEntityChest.isInvalid();
    }
}
