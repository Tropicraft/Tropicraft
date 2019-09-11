package net.tropicraft.core.common.block.tileentity;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.tropicraft.Constants;
import net.tropicraft.core.common.item.TropicraftItems;
import net.tropicraft.core.common.network.TropicraftPackets;
import net.tropicraft.core.common.network.message.MessageSifterInventory;
import net.tropicraft.core.common.network.message.MessageSifterStart;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class SifterTileEntity extends TileEntity implements ITickableTileEntity {

    /** Number of seconds to sift multiplied by the number of ticks per second */
    private static final int SIFT_TIME = 4 * 20;

    /** Is this machine currently sifting? */
    private boolean isSifting;

    /** Current progress in sifting; -1 if not sifting */
    private int currentSiftTime;

    private Random rand;

    public double yaw;
    public double yaw2 = 0.0D;

    @Nonnull
    private ItemStack siftItem = ItemStack.EMPTY;

    public SifterTileEntity() {
        super(TropicraftTileEntityTypes.SIFTER);
        rand = new Random();
        currentSiftTime = SIFT_TIME;
    }

    @Nonnull
    public ItemStack getSiftItem() {
        return siftItem;
    }

    @Override
    public void tick() {
        // If sifter is sifting, decrement sift time
        if (currentSiftTime > 0 && isSifting) {
            currentSiftTime--;
        }

        // Rotation animation
        if (world.isRemote) {
            yaw2 = yaw % 360.0D;
            yaw += 4.545454502105713D;
        }

        // Done sifting
        if (isSifting && currentSiftTime <= 0) {
            stopSifting();
        }
    }

    public void dumpResults(final BlockPos pos) {
        // NOTE: Removed check and drop for heated sifter in 1.12
        dumpBeachResults(pos);
        syncInventory();
    }

    // TODO replace with loot table
    private void dumpBeachResults(final BlockPos pos) {
        int dumpCount = rand.nextInt(3) + 1;
        ItemStack stack;

        while (dumpCount > 0) {
            dumpCount--;

            if (rand.nextInt(10) == 0) {
                stack = getRareItem();
            } else if (rand.nextInt(10) < 3) {
                String name;
                if (rand.nextBoolean()) {
                    name = Constants.LT17_NAMES[rand.nextInt(Constants.LT17_NAMES.length)];
                } else {
                    name = Constants.LT18_NAMES[rand.nextInt(Constants.LT18_NAMES.length)];
                }
                final CompoundNBT nameTag = new CompoundNBT();
                nameTag.putString("Name", name);
                stack = new ItemStack(TropicraftItems.LOVE_TROPICS_SHELL);
                stack.setTag(nameTag);
            } else {
                stack = getCommonItem();
            }

            spawnItem(stack, pos);
        }
    }

    private void spawnItem(ItemStack stack, BlockPos pos) {
        if (world.isRemote) {
            return;
        }

        final ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack);
        world.addEntity(itemEntity);
    }

    private ItemStack getCommonItem() {
        // Random from -1 to size-1
        final int shellIndex = rand.nextInt(TropicraftItems.SHELLS.length + 1) - 1;
        if (shellIndex < 0) {
            return getRareItem();
        }
        return new ItemStack(TropicraftItems.SHELLS[shellIndex]);
    }

    private ItemStack getRareItem() {
        final int dmg = rand.nextInt(12);

        switch (dmg) {
            case 1:
                return new ItemStack(Items.GOLD_NUGGET, 1);
            case 2:
                return new ItemStack(Items.BUCKET, 1);
            case 3:
                return new ItemStack(Items.WOODEN_SHOVEL, 1);
            case 4:
                return new ItemStack(Items.GLASS_BOTTLE, 1);
            case 5:
                return new ItemStack(TropicraftItems.WHITE_PEARL, 1);
            case 6:
                return new ItemStack(TropicraftItems.BLACK_PEARL, 1);
            case 7:
                return new ItemStack(Items.STONE_SHOVEL, 1);
            case 0:
            default:
                return new ItemStack(TropicraftItems.RUBE_NAUTILUS);
        }
    }

    public void addItemToSifter(ItemStack stack) {
        siftItem = stack.copy().split(1);
        syncInventory();
    }

    public void startSifting() {
        isSifting = true;
        currentSiftTime = SIFT_TIME;

        if (!world.isRemote) {
            TropicraftPackets.sendToDimension(new MessageSifterStart(this), world.getDimension().getType());
        }
    }

    private void stopSifting() {
        final double x = pos.getX() + world.rand.nextDouble() * 1.4;
        final double y = pos.getY() + world.rand.nextDouble() * 1.4;
        final double z = pos.getZ() + world.rand.nextDouble() * 1.4;

        if (!world.isRemote) {
            dumpResults(new BlockPos(x, y, z));
        }
        currentSiftTime = SIFT_TIME;
        isSifting = false;
        siftItem = ItemStack.EMPTY;
        syncInventory();
    }

    public void setSifting(boolean flag) {
        this.isSifting = flag;
    }

    public boolean isSifting() {
        return this.isSifting;
    }

    @Override
    public void read(CompoundNBT nbt) {
        super.read(nbt);
        isSifting = nbt.getBoolean("isSifting");
        currentSiftTime = nbt.getInt("currentSiftTime");

        if (nbt.contains("Item", 10)) {
            siftItem = ItemStack.read(nbt.getCompound("Item"));
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        super.write(nbt);
        nbt.putBoolean("isSifting", isSifting);
        nbt.putInt("currentSiftTime", currentSiftTime);
        if (!siftItem.isEmpty()) {
            nbt.put("Item", siftItem.write(new CompoundNBT()));
        }
        return nbt;
    }

    public CompoundNBT getTagCompound(ItemStack stack) {
        if (!stack.hasTag())
            stack.setTag(new CompoundNBT());

        return stack.getTag();
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        read(pkt.getNbtCompound());
    }

    protected void syncInventory() {
        TropicraftPackets.sendToDimension(new MessageSifterInventory(this), world.getDimension().getType());
    }

    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, 1, this.getUpdateTag());
    }

    public CompoundNBT getUpdateTag() {
        return writeItems(new CompoundNBT());
    }

    private CompoundNBT writeItems(final CompoundNBT nbt) {
        super.write(nbt);
        ItemStackHelper.saveAllItems(nbt, NonNullList.from(siftItem), true);
        return nbt;
    }

    public void setSiftItem(final ItemStack siftItem) {
        this.siftItem = siftItem.copy().split(1);
    }
}
