package net.tropicraft.core.common.item.scuba;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
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
import net.minecraftforge.network.PacketDistributor;
import net.tropicraft.Constants;
import net.tropicraft.core.common.dimension.TropicraftDimension;
import net.tropicraft.core.common.network.TropicraftPackets;
import net.tropicraft.core.common.network.message.MessageUpdateScubaData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

@EventBusSubscriber(modid = Constants.MODID, bus = Bus.FORGE)
public class ScubaData implements INBTSerializable<CompoundTag> {

    // TODO 1.17 this is 100000% wrong
   // @CapabilityInject(ScubaData.class)
   public static final Capability<ScubaData> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
       @Override
       public String toString() {
           return super.toString();
       }
   });

//    public static void registerCapability() {
//        CapabilityManager.INSTANCE.register(ScubaData.class, new IStorage<ScubaData>() {
//
//            @Override
//            @Nullable
//            public Tag writeNBT(Capability<ScubaData> capability, ScubaData instance, Direction side) {
//                return instance.serializeNBT();
//            }
//
//            @Override
//            public void readNBT(Capability<ScubaData> capability, ScubaData instance, Direction side, Tag nbt) {
//                instance.deserializeNBT((CompoundTag) nbt);
//            }
//
//        }, ScubaData::new);
//    }
//
    @SubscribeEvent
    public static void onCapabilityAttach(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(new ResourceLocation(Constants.MODID, "scuba_data"), new ICapabilitySerializable<CompoundTag>() {
                
                LazyOptional<ScubaData> data = LazyOptional.of(ScubaData::new);
                
                @Override
                @Nonnull
                public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
                    return CAPABILITY.orEmpty(cap, data);
                }
                
                @Override
                public CompoundTag serializeNBT() {
                    return data.orElseThrow(IllegalStateException::new).serializeNBT();
                }
                
                @Override
                public void deserializeNBT(CompoundTag nbt) {
                    data.orElseThrow(IllegalStateException::new).deserializeNBT(nbt);
                }
            });
        }
    }
    
    private static final Set<ServerPlayer> underwaterPlayers = Collections.newSetFromMap(new WeakHashMap<>());
    
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent event) {
        Level world = event.player.level;
        if (event.phase == Phase.END) {
            // TODO support more than chest slot?
            ItemStack chestStack = event.player.getItemBySlot(EquipmentSlot.CHEST);
            Item chestItem = chestStack.getItem();
            if (chestItem instanceof ScubaArmorItem) {
                LazyOptional<ScubaData> data = event.player.getCapability(CAPABILITY);
                if (!world.isClientSide) {
                    underwaterPlayers.add((ServerPlayer) event.player);
                }
                if (isUnderWater(event.player)) {
                    data.ifPresent(d -> {
                        d.tick(event.player);
                        if (!world.isClientSide) {
                            d.updateClient((ServerPlayer) event.player, false);
                        }
                    });
                    ((ScubaArmorItem)chestItem).tickAir(event.player, EquipmentSlot.CHEST, chestStack);
                    if (!world.isClientSide && world.getGameTime() % 60 == 0) {
                        // TODO this effect could be better, custom packet?
                        Vec3 eyePos = event.player.getEyePosition(0);
                        Vec3 motion = event.player.getDeltaMovement();
                        Vec3 particlePos = eyePos.add(motion.reverse());
                        ((ServerLevel) world).sendParticles(ParticleTypes.BUBBLE,
                                particlePos.x(), particlePos.y(), particlePos.z(),
                                4 + world.random.nextInt(3),
                                0.25, 0.25, 0.25, motion.length());
                    }
                } else if (!world.isClientSide && underwaterPlayers.remove(event.player)) { // Update client state as they leave the water
                    data.ifPresent(d -> d.updateClient((ServerPlayer) event.player, false));
                }
            }
        }
    }
    
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            event.getOriginal().getCapability(CAPABILITY).ifPresent(d -> {
                event.getEntity().getCapability(CAPABILITY).ifPresent(d2 -> d2.copyFrom(d));
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
        if (!event.getEntity().level.isClientSide) {
            event.getEntity().getCapability(CAPABILITY).ifPresent(d -> d.updateClient((ServerPlayer) event.getEntity(), true));
        }
    }

    private long diveTime;
    private double maxDepth;
    
    private boolean dirty;
    
    public static boolean isUnderWater(Player player) {
        BlockPos headPos = new BlockPos(player.getEyePosition(0));
        return player.level.getFluidState(headPos).is(FluidTags.WATER);
    }
    
    public static double getDepth(Player player) {
        if (isUnderWater(player)) {
            int surface = TropicraftDimension.getSeaLevel(player.level);
            double depth = surface - (player.getEyePosition(0).y());
            return depth;
        }
        return 0;
    }
    
    void tick(Player player) {
        this.diveTime++;
        if (player.level.getGameTime() % 100 == 0) {
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
    
    void updateClient(ServerPlayer target, boolean force) {
        if (dirty || force) {
            TropicraftPackets.CHANNEL.send(PacketDistributor.PLAYER.with(() -> target), new MessageUpdateScubaData(this));
        }
    }

    public void copyFrom(ScubaData data) {
        this.diveTime = data.getDiveTime();
        this.maxDepth = data.getMaxDepth();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag ret = new CompoundTag();
        ret.putLong("diveTime", diveTime);
        ret.putDouble("maxDepth", maxDepth);
        return ret;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.diveTime = nbt.getLong("diveTime");
        this.maxDepth = nbt.getDouble("maxDepth");
    }

    public void serializeBuffer(FriendlyByteBuf buf) {
        buf.writeLong(diveTime);
        buf.writeDouble(maxDepth);
    }
    
    public void deserializeBuffer(FriendlyByteBuf buf) {
        this.diveTime = buf.readLong();
        this.maxDepth = buf.readDouble();
    }
}
