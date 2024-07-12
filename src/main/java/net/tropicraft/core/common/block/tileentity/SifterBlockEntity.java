package net.tropicraft.core.common.block.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;
import net.tropicraft.Constants;
import net.tropicraft.core.common.TropicraftTags;
import net.tropicraft.core.common.item.TropicraftDataComponents;
import net.tropicraft.core.common.item.TropicraftItems;
import net.tropicraft.core.common.network.message.ClientboundSifterInventoryPacket;
import net.tropicraft.core.common.network.message.ClientboundSifterStartPacket;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SifterBlockEntity extends BlockEntity {

    /**
     * Number of seconds to sift multiplied by the number of ticks per second
     */
    private static final int SIFT_TIME = 4 * 20;

    /**
     * Is this machine currently sifting?
     */
    private boolean isSifting;

    /**
     * Current progress in sifting; -1 if not sifting
     */
    private int currentSiftTime;

    private final RandomSource rand;

    public double yaw;
    public double yaw2 = 0.0;

    @Nonnull
    private ItemStack siftItem = ItemStack.EMPTY;

    public SifterBlockEntity(BlockEntityType<SifterBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        rand = RandomSource.create();
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
            yaw2 = yaw % 360.0;
            yaw += 45.45454502105713;
        }

        // Done sifting
        if (isSifting && currentSiftTime <= 0) {
            stopSifting();
        }
    }

    public void dumpResults(BlockPos pos) {
        // NOTE: Removed check and drop for heated sifter in 1.12
        dumpBeachResults(pos);
        syncInventory();
    }

    // TODO replace with loot table
    private void dumpBeachResults(BlockPos pos) {
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
                stack = new ItemStack(TropicraftItems.LOVE_TROPICS_SHELL.get());
                stack.set(TropicraftDataComponents.SHELL_NAME, name);
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

        ItemEntity itemEntity = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), stack);
        level.addFreshEntity(itemEntity);
    }

    private ItemStack getCommonItem() {
        // Random from -1 to size-1

        HolderSet.Named<Item> tag = level.registryAccess().registryOrThrow(Registries.ITEM).getOrCreateTag(TropicraftTags.Items.SHELLS);

        int shellIndex = rand.nextInt(tag.size() + 1) - 1;
        if (shellIndex < 0) {
            return getRareItem();
        }
        return new ItemStack(tag.getRandomElement(rand).get().value());
    }

    private ItemStack getRareItem() {
        return switch (rand.nextInt(12)) {
            case 1 -> new ItemStack(Items.GOLD_NUGGET, 1);
            case 2 -> new ItemStack(Items.BUCKET, 1);
            case 3 -> new ItemStack(Items.WOODEN_SHOVEL, 1);
            case 4 -> new ItemStack(Items.GLASS_BOTTLE, 1);
            case 5 -> new ItemStack(TropicraftItems.WHITE_PEARL.get(), 1);
            case 6 -> new ItemStack(TropicraftItems.BLACK_PEARL.get(), 1);
            case 7 -> new ItemStack(Items.STONE_SHOVEL, 1);
            default -> new ItemStack(TropicraftItems.RUBE_NAUTILUS.get());
        };
    }

    public void addItemToSifter(ItemStack stack) {
        siftItem = stack.copy().split(1);
        syncInventory();
    }

    public void startSifting() {
        isSifting = true;
        currentSiftTime = SIFT_TIME;

        if (level instanceof ServerLevel serverLevel) {
            PacketDistributor.sendToPlayersTrackingChunk(serverLevel, new ChunkPos(getBlockPos()), new ClientboundSifterStartPacket(getBlockPos()));
        }
    }

    private void stopSifting() {
        double x = worldPosition.getX() + level.random.nextDouble() * 1.4;
        double y = worldPosition.getY() + level.random.nextDouble() * 1.4;
        double z = worldPosition.getZ() + level.random.nextDouble() * 1.4;

        if (!level.isClientSide) {
            dumpResults(BlockPos.containing(x, y, z));
        }
        currentSiftTime = SIFT_TIME;
        isSifting = false;
        siftItem = ItemStack.EMPTY;
        syncInventory();
    }

    public void setSifting(boolean flag) {
        isSifting = flag;
    }

    public boolean isSifting() {
        return isSifting;
    }

    @Override
    public void loadAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
        super.loadAdditional(nbt, registries);
        isSifting = nbt.getBoolean("isSifting");
        currentSiftTime = nbt.getInt("currentSiftTime");

        if (nbt.contains("Item", 10)) {
            siftItem = ItemStack.parse(registries, nbt.getCompound("Item")).orElse(ItemStack.EMPTY);
        } else {
            siftItem = ItemStack.EMPTY;
        }
    }

    @Override
    public void saveAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
        super.saveAdditional(nbt, registries);
        nbt.putBoolean("isSifting", isSifting);
        nbt.putInt("currentSiftTime", currentSiftTime);
        if (!siftItem.isEmpty()) {
            nbt.put("Item", siftItem.save(registries, new CompoundTag()));
        }
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider registries) {
        loadAdditional(pkt.getTag(), registries);
    }

    protected void syncInventory() {
        if (level instanceof ServerLevel serverLevel) {
            PacketDistributor.sendToPlayersTrackingChunk(serverLevel, new ChunkPos(getBlockPos()), new ClientboundSifterInventoryPacket(this));
        }
    }

    @Override
    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return writeItems(new CompoundTag(), registries);
    }

    private CompoundTag writeItems(CompoundTag nbt, HolderLookup.Provider registries) {
        saveAdditional(nbt, registries);
        return nbt;
    }

    public void setSiftItem(ItemStack siftItem) {
        this.siftItem = siftItem.copy().split(1);
    }
}
