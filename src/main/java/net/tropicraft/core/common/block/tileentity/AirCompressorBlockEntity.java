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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AirCompressorBlockEntity extends BlockEntity implements IMachineBlock {

    /** Is the compressor currently giving air */
    private boolean compressing;

    /** Number of ticks compressed so far */
    private int ticks;

    /** Amount of PSI (ticks of air time) to fill per tick */
    private static final int fillRate = 5; // fills 5x faster than it's used

    /** The stack that is currently being filled */
    @Nonnull
    private ItemStack stack;

    private ScubaArmorItem tank;

    public AirCompressorBlockEntity(final BlockEntityType<AirCompressorBlockEntity> type, final BlockPos pos, final BlockState state) {
        super(type, pos, state);
        this.stack = ItemStack.EMPTY;
    }

    @Override
    public void loadAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
        super.loadAdditional(nbt, registries);
        this.compressing = nbt.getBoolean("Compressing");

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

    public void setTank(@Nonnull ItemStack tankItemStack) {
        this.stack = tankItemStack;
        this.tank = !(stack.getItem() instanceof ScubaArmorItem) ? null : (ScubaArmorItem) stack.getItem();
    }

    @Nonnull
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
        if (tank == null && stack.getItem() instanceof ScubaArmorItem && ((ScubaArmorItem)stack.getItem()).providesAir()) {
            setTank(stack);
            this.compressing = true;
            syncInventory();
            return true;
        }

        return false;
    }

    public void ejectTank() {
        if (!stack.isEmpty()) {
            if (!level.isClientSide) {
                ItemEntity tankItem = new ItemEntity(level, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), stack);
                level.addFreshEntity(tankItem);
            }
        }

        setTank(ItemStack.EMPTY);
        syncInventory();
        this.ticks = 0;
        this.compressing = false;
    }

    public boolean isDoneCompressing() {
        return this.ticks > 0 && !this.compressing;
    }

    public float getTickRatio(float partialTicks) {
        if (tank != null) {
            return (this.ticks + partialTicks) / (tank.getMaxAir(getTankStack()) * fillRate);
        }
        return 0;
    }

    public boolean isCompressing() {
        return this.compressing;
    }

    public void startCompressing() {
        this.compressing = true;
        syncInventory();
    }

    public void finishCompressing() {
        this.compressing = false;
        this.ticks = 0;
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
        this.loadAdditional(pkt.getTag(), registries);
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
        this.saveAdditional(nbt, registries);
        return nbt;
    }
}
