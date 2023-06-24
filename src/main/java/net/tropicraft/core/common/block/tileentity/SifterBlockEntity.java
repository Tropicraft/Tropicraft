package net.tropicraft.core.common.block.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.tropicraft.Constants;
import net.tropicraft.core.common.TropicraftTags;
import net.tropicraft.core.common.item.TropicraftItems;
import net.tropicraft.core.common.network.TropicraftPackets;
import net.tropicraft.core.common.network.message.MessageSifterInventory;
import net.tropicraft.core.common.network.message.MessageSifterStart;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class SifterBlockEntity extends BlockEntity {

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

    public SifterBlockEntity(final BlockEntityType<SifterBlockEntity> type, final BlockPos pos, final BlockState state) {
        super(type, pos, state);
        rand = new Random();
        currentSiftTime = SIFT_TIME;
    }

    @Nonnull
    public ItemStack getSiftItem() {
        return siftItem;
    }

    public static void siftTick(Level level, BlockPos pos, BlockState state, SifterBlockEntity sifter) {
        sifter.tick();
    }

    private void tick() {
        // If sifter is sifting, decrement sift time
        if (currentSiftTime > 0 && isSifting) {
            currentSiftTime--;
        }

        // Rotation animation
        if (level.isClientSide) {
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
                final CompoundTag nameTag = new CompoundTag();
                nameTag.putString("Name", name);
                stack = new ItemStack(TropicraftItems.LOVE_TROPICS_SHELL.get());
                stack.setTag(nameTag);
            } else {
                stack = getCommonItem();
            }

            spawnItem(stack, pos);
        }
    }

    private void spawnItem(ItemStack stack, BlockPos pos) {
        if (level.isClientSide) {
            return;
        }

        final ItemEntity itemEntity = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), stack);
        level.addFreshEntity(itemEntity);
    }

    private ItemStack getCommonItem() {
        // Random from -1 to size-1

        HolderSet.Named<Item> tag = Registry.ITEM.getOrCreateTag(TropicraftTags.Items.SHELLS);

        final int shellIndex = rand.nextInt(tag.size() + 1) - 1;
        if (shellIndex < 0) {
            return getRareItem();
        }
        return new ItemStack(tag.getRandomElement(rand).get().value());
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
                return new ItemStack(TropicraftItems.WHITE_PEARL.get(), 1);
            case 6:
                return new ItemStack(TropicraftItems.BLACK_PEARL.get(), 1);
            case 7:
                return new ItemStack(Items.STONE_SHOVEL, 1);
            case 0:
            default:
                return new ItemStack(TropicraftItems.RUBE_NAUTILUS.get());
        }
    }

    public void addItemToSifter(ItemStack stack) {
        siftItem = stack.copy().split(1);
        syncInventory();
    }

    public void startSifting() {
        isSifting = true;
        currentSiftTime = SIFT_TIME;

        if (!level.isClientSide) {
            TropicraftPackets.sendToDimension(new MessageSifterStart(this), level);
        }
    }

    private void stopSifting() {
        final double x = worldPosition.getX() + level.random.nextDouble() * 1.4;
        final double y = worldPosition.getY() + level.random.nextDouble() * 1.4;
        final double z = worldPosition.getZ() + level.random.nextDouble() * 1.4;

        if (!level.isClientSide) {
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
    public void load(CompoundTag nbt) {
        super.load(nbt);
        isSifting = nbt.getBoolean("isSifting");
        currentSiftTime = nbt.getInt("currentSiftTime");

        if (nbt.contains("Item", 10)) {
            siftItem = ItemStack.of(nbt.getCompound("Item"));
        }
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putBoolean("isSifting", isSifting);
        nbt.putInt("currentSiftTime", currentSiftTime);
        if (!siftItem.isEmpty()) {
            nbt.put("Item", siftItem.save(new CompoundTag()));
        }
    }

    public CompoundTag getTagCompound(ItemStack stack) {
        if (!stack.hasTag())
            stack.setTag(new CompoundTag());

        return stack.getTag();
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        load(pkt.getTag());
    }

    protected void syncInventory() {
        if (!level.isClientSide()) {
            TropicraftPackets.sendToDimension(new MessageSifterInventory(this), level);
        }
    }

    @Override
    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return writeItems(new CompoundTag());
    }

    private CompoundTag writeItems(final CompoundTag nbt) {
        this.saveAdditional(nbt);
        return nbt;
    }

    public void setSiftItem(final ItemStack siftItem) {
        this.siftItem = siftItem.copy().split(1);
    }
}
