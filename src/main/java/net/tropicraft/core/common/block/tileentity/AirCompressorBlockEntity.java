package net.tropicraft.core.common.block.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;
import net.tropicraft.core.common.block.AirCompressorBlock;
import net.tropicraft.core.common.item.scuba.ScubaArmorItem;
import net.tropicraft.core.common.network.message.ClientboundAirCompressorInventoryPacket;

import javax.annotation.Nullable;

public class AirCompressorBlockEntity extends BlockEntity implements IMachineBlock {

    /**
     * Is the compressor currently giving air
     */
    private boolean compressing;

    /**
     * Number of ticks compressed so far
     */
    private int ticks;

    /**
     * Amount of PSI (ticks of air time) to fill per tick
     */
    private static final int fillRate = 5; // fills 5x faster than it's used

    /**
     * The stack that is currently being filled
     */
    private ItemStack stack;

    @Nullable
    private ScubaArmorItem tank;

    public AirCompressorBlockEntity(BlockEntityType<AirCompressorBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        stack = ItemStack.EMPTY;
    }

    @Override
    public void loadAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
        super.loadAdditional(nbt, registries);
        compressing = nbt.getBoolean("Compressing");

        if (nbt.contains("Tank")) {
            setTank(ItemStack.parse(registries, nbt.getCompound("Tank")).orElse(ItemStack.EMPTY));
        } else {
            setTank(ItemStack.EMPTY);
        }
    }

    @Override
    public void saveAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
        super.saveAdditional(nbt, registries);
        nbt.putBoolean("Compressing", compressing);

        if (!stack.isEmpty()) {
            nbt.put("Tank", stack.save(registries, new CompoundTag()));
        }
    }

    public void setTank(ItemStack tankItemStack) {
        stack = tankItemStack;
        tank = !(stack.getItem() instanceof ScubaArmorItem) ? null : (ScubaArmorItem) stack.getItem();
    }

    public ItemStack getTankStack() {
        return stack;
    }

    @Nullable
    public ScubaArmorItem getTank() {
        return tank;
    }

    public static void compressTick(Level level, BlockPos pos, BlockState state, AirCompressorBlockEntity te) {
        te.tick();
    }

    private void tick() {
        if (tank == null)
            return;

        int airContained = tank.getRemainingAir(getTankStack());
        int maxAir = tank.getMaxAir(getTankStack());

        if (compressing) {
            int overflow = tank.addAir(fillRate, getTankStack());
            ticks++;
            if (overflow > 0) {
                finishCompressing();
            }
        }
    }

    public boolean addTank(ItemStack stack) {
        if (tank == null && stack.getItem() instanceof ScubaArmorItem && ((ScubaArmorItem) stack.getItem()).providesAir()) {
            setTank(stack);
            compressing = true;
            syncInventory();
            return true;
        }

        return false;
    }

    public void ejectTank() {
        if (!stack.isEmpty()) {
            if (!level.isClientSide) {
                ItemEntity tankItem = new ItemEntity(level, getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), stack);
                level.addFreshEntity(tankItem);
            }
        }

        setTank(ItemStack.EMPTY);
        syncInventory();
        ticks = 0;
        compressing = false;
    }

    public boolean isDoneCompressing() {
        return ticks > 0 && !compressing;
    }

    public float getTickRatio(float partialTicks) {
        if (tank != null) {
            return (ticks + partialTicks) / (tank.getMaxAir(getTankStack()) * fillRate);
        }
        return 0;
    }

    public boolean isCompressing() {
        return compressing;
    }

    public void startCompressing() {
        compressing = true;
        syncInventory();
    }

    public void finishCompressing() {
        compressing = false;
        ticks = 0;
        syncInventory();
    }

    public float getBreatheProgress(float partialTicks) {
        if (isDoneCompressing()) {
            return 0;
        }
        return (float) (((((ticks + partialTicks) / 20) * Math.PI) + Math.PI) % (Math.PI * 2));
    }

    /* == IMachineTile == */

    @Override
    public boolean isActive() {
        return !getTankStack().isEmpty();
    }

    @Override
    public float getProgress(float partialTicks) {
        return getTickRatio(partialTicks);
    }

    @Override
    public Direction getDirection(BlockState state) {
        return state.getValue(AirCompressorBlock.FACING);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider registries) {
        loadAdditional(pkt.getTag(), registries);
    }

    protected void syncInventory() {
        if (level instanceof ServerLevel serverLevel) {
            PacketDistributor.sendToPlayersTrackingChunk(serverLevel, new ChunkPos(getBlockPos()), new ClientboundAirCompressorInventoryPacket(this));
        }
    }

    @Override
    @Nullable
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag nbt = new CompoundTag();
        saveAdditional(nbt, registries);
        return nbt;
    }
}
