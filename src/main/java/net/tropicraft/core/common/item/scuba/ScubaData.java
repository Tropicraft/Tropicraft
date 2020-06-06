package net.tropicraft.core.common.item.scuba;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
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
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.network.PacketDistributor;
import net.tropicraft.Constants;
import net.tropicraft.core.common.network.TropicraftPackets;
import net.tropicraft.core.common.network.message.MessageUpdateScubaData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

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
    
    private static final Set<ServerPlayerEntity> underwaterPlayers = Collections.newSetFromMap(new WeakHashMap<>());
    
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent event) {
        World world = event.player.world;
        if (event.phase == Phase.END) {
            // TODO support more than chest slot?
            ItemStack chestStack = event.player.getItemStackFromSlot(EquipmentSlotType.CHEST);
            Item chestItem = chestStack.getItem();
            if (chestItem instanceof ScubaArmorItem) {
                LazyOptional<ScubaData> data = event.player.getCapability(CAPABILITY);
                if (!world.isRemote) {
                    underwaterPlayers.add((ServerPlayerEntity) event.player);
                }
                if (isUnderWater(event.player)) {
                    data.ifPresent(d -> {
                        d.tick(event.player);
                        if (!world.isRemote) {
                            d.updateClient((ServerPlayerEntity) event.player, false);
                        }
                    });
                    ((ScubaArmorItem)chestItem).tickAir(event.player, EquipmentSlotType.CHEST, chestStack);
                    if (!world.isRemote && world.getGameTime() % 60 == 0) {
                        // TODO this effect could be better, custom packet?
                        Vec3d eyePos = event.player.getEyePosition(0);
                        Vec3d motion = event.player.getMotion();
                        Vec3d particlePos = eyePos.add(motion.inverse());
                        ((ServerWorld) world).spawnParticle(ParticleTypes.BUBBLE,
                                particlePos.getX(), particlePos.getY(), particlePos.getZ(),
                                4 + world.rand.nextInt(3),
                                0.25, 0.25, 0.25, motion.length());
                    }
                } else if (!world.isRemote && underwaterPlayers.remove(event.player)) { // Update client state as they leave the water
                    data.ifPresent(d -> d.updateClient((ServerPlayerEntity) event.player, false));
                }
            }
        }
    }
    
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            event.getOriginal().getCapability(CAPABILITY).ifPresent(d -> {
                event.getPlayer().getCapability(CAPABILITY).ifPresent(d2 -> d2.copyFrom(d));
            });
        }
    }
    
    @SubscribeEvent 
    public static void onPlayerRespawn(PlayerRespawnEvent event) {
        updateClient(event);
    }
    
    @SubscribeEvent
    public static void onPlayerLogIn(PlayerLoggedInEvent event) {
        updateClient(event);
    }
    
    @SubscribeEvent
    public static void onPlayerChangeDimension(PlayerChangedDimensionEvent event) {
        updateClient(event);
    }
    
    private static void updateClient(PlayerEvent event) {
        if (!event.getPlayer().world.isRemote) {
            event.getPlayer().getCapability(CAPABILITY).ifPresent(d -> d.updateClient((ServerPlayerEntity) event.getPlayer(), true));
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
    
    void tick(PlayerEntity player) {
        this.diveTime++;
        if (player.world.getGameTime() % 100 == 0) {
            dirty = true;
        }
        updateMaxDepth(getDepth(player));
    }

    public long getDiveTime() {
        return diveTime;
    }
    
    void updateMaxDepth(double depth) {
        if (depth > maxDepth) {
            this.maxDepth = depth;
        }
    }

    public double getMaxDepth() {
        return maxDepth;
    }
    
    void updateClient(ServerPlayerEntity target, boolean force) {
        if (dirty || force) {
            TropicraftPackets.INSTANCE.send(PacketDistributor.PLAYER.with(() -> target), new MessageUpdateScubaData(this));
        }
    }

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
