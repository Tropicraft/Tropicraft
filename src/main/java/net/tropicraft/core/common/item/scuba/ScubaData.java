package net.tropicraft.core.common.item.scuba;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.network.PacketDistributor;
import net.tropicraft.Constants;
import net.tropicraft.core.common.network.TropicraftPackets;
import net.tropicraft.core.common.network.message.MessageUpdateScubaData;

@EventBusSubscriber(modid = Constants.MODID, bus = Bus.FORGE)
public class ScubaData implements INBTSerializable<CompoundNBT> {
    
    @CapabilityInject(ScubaData.class)
    public static final Capability<ScubaData> CAPABILITY = null;

    public static void registerCapability() {
        CapabilityManager.INSTANCE.register(ScubaData.class, new IStorage<ScubaData>() {

            @Override
            @Nullable
            public INBT writeNBT(Capability<ScubaData> capability, ScubaData instance, Direction side) {
                return instance.serializeNBT();
            }

            @Override
            public void readNBT(Capability<ScubaData> capability, ScubaData instance, Direction side, INBT nbt) {
                instance.deserializeNBT((CompoundNBT) nbt);
            }
            
        }, ScubaData::new);
    }
    
    @SubscribeEvent
    public static void onCapabilityAttach(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(new ResourceLocation(Constants.MODID, "scuba_data"), new ICapabilitySerializable<CompoundNBT>() {
                
                LazyOptional<ScubaData> data = LazyOptional.of(ScubaData::new);
                
                @Override
                @Nonnull
                public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
                    return CAPABILITY.orEmpty(cap, data);
                }
                
                @Override
                public CompoundNBT serializeNBT() {
                    return data.orElseThrow(IllegalStateException::new).serializeNBT();
                }
                
                @Override
                public void deserializeNBT(CompoundNBT nbt) {
                    data.orElseThrow(IllegalStateException::new).deserializeNBT(nbt);
                }
            });
        }
    }
    
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent event) {
        World world = event.player.world;
        if (event.phase == Phase.END && !world.isRemote) {
            // TODO support more than chest slot?
            ItemStack chestStack = event.player.getItemStackFromSlot(EquipmentSlotType.CHEST);
            Item chestItem = chestStack.getItem();
            if (chestItem instanceof ScubaArmorItem) {
                LazyOptional<ScubaData> data = event.player.getCapability(CAPABILITY);
                if (isUnderWater(event.player)) {
                    data.ifPresent(d -> {
                        d.tick((ServerPlayerEntity) event.player);
                    });
                    ((ScubaArmorItem)chestItem).tickAir((ServerPlayerEntity) event.player, EquipmentSlotType.CHEST, chestStack);
                }
            }
        }
    }

    private long diveTime;
    private double maxDepth;
    
    private boolean dirty;
    
    public static boolean isUnderWater(PlayerEntity player) {
        BlockPos headPos = new BlockPos(player.getEyePosition(0));
        return player.world.getFluidState(headPos).isTagged(FluidTags.WATER);
    }
    
    public static double getDepth(PlayerEntity player) {
        if (isUnderWater(player)) {
            int surface = player.world.getSeaLevel();
            double depth = surface - (player.getEyePosition(0).getY());
            return depth;
        }
        return 0;
    }
    
    void tick(ServerPlayerEntity player) {
        this.diveTime++;
        dirty = true;
        updateMaxDepth(getDepth(player));
        updateClient(player);
    }

    public long getDiveTime() {
        return diveTime;
    }
    
    void updateMaxDepth(double depth) {
        if (depth > maxDepth) {
            this.maxDepth = depth;
            dirty = true;
        }
    }

    public double getMaxDepth() {
        return maxDepth;
    }
    
    void updateClient(ServerPlayerEntity target) {
        if (dirty) {
            TropicraftPackets.INSTANCE.send(PacketDistributor.PLAYER.with(() -> target), new MessageUpdateScubaData(this));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void copyFrom(ScubaData data) {
        this.diveTime = data.getDiveTime();
        this.maxDepth = data.getMaxDepth();
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT ret = new CompoundNBT();
        ret.putLong("diveTime", diveTime);
        ret.putDouble("maxDepth", maxDepth);
        return ret;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.diveTime = nbt.getLong("diveTime");
        this.maxDepth = nbt.getDouble("maxDepth");
    }

    public void serializeBuffer(PacketBuffer buf) {
        buf.writeLong(diveTime);
        buf.writeDouble(maxDepth);
    }
    
    public void deserializeBuffer(PacketBuffer buf) {
        this.diveTime = buf.readLong();
        this.maxDepth = buf.readDouble();
    }
}
