package net.tropicraft.core.common.block.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.PacketDistributor;
import net.tropicraft.core.common.block.AirCompressorBlock;
import net.tropicraft.core.common.item.scuba.ScubaArmorItem;
import net.tropicraft.core.common.network.TropicraftPackets;
import net.tropicraft.core.common.network.message.MessageAirCompressorInventory;

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

    public AirCompressorBlockEntity(final BlockPos pos, final BlockState state) {
        super(TropicraftBlockEntityTypes.AIR_COMPRESSOR.get(), pos, state);
        this.stack = ItemStack.EMPTY;
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.compressing = nbt.getBoolean("Compressing");

        if (nbt.contains("Tank")) {
            setTank(ItemStack.of(nbt.getCompound("Tank")));
        } else {
            setTank(ItemStack.EMPTY);
        }
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putBoolean("Compressing", compressing);

        CompoundTag var4 = new CompoundTag();
        this.stack.save(var4);
        nbt.put("Tank", var4);
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
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.load(pkt.getTag());
    }

    protected void syncInventory() {
        if (!level.isClientSide) {
            TropicraftPackets.INSTANCE.send(PacketDistributor.DIMENSION.with(level::dimension), new MessageAirCompressorInventory(this));
        }
    }

    @Override
    @Nullable
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @Nonnull CompoundTag getUpdateTag() {
        CompoundTag nbt = new CompoundTag();
        this.saveAdditional(nbt);
        return nbt;
    }
}
