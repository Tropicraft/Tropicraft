package net.tropicraft.core.common.item.scuba;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.dimension.TropicraftDimension;
import net.tropicraft.core.common.network.message.ClientboundUpdateScubaDataPacket;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.function.Supplier;

@EventBusSubscriber(modid = Tropicraft.ID, bus = EventBusSubscriber.Bus.GAME)
public class ScubaData {
    public static final Codec<ScubaData> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.LONG.fieldOf("diveTime").forGetter(ScubaData::getDiveTime),
            Codec.DOUBLE.fieldOf("maxDepth").forGetter(ScubaData::getMaxDepth)
    ).apply(i, ScubaData::new));

    public static final StreamCodec<ByteBuf, ScubaData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_LONG, ScubaData::getDiveTime,
            ByteBufCodecs.DOUBLE, ScubaData::getMaxDepth,
            ScubaData::new
    );

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Tropicraft.ID);

    public static final Supplier<AttachmentType<ScubaData>> ATTACHMENT = ATTACHMENT_TYPES.register(
            "scuba_data", () -> AttachmentType.builder(ScubaData::new).serialize(CODEC).build()
    );

    private static final Set<ServerPlayer> underwaterPlayers = Collections.newSetFromMap(new WeakHashMap<>());

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        Level world = player.level();
        // TODO support more than chest slot?
        ItemStack chestStack = player.getItemBySlot(EquipmentSlot.CHEST);
        if (chestStack.getItem() instanceof ScubaArmorItem scubaItem) {
            ScubaData data = player.getData(ATTACHMENT);
            if (!world.isClientSide) {
                underwaterPlayers.add((ServerPlayer) player);
            }
            if (isUnderWater(player)) {
                data.tick(player);
                if (!world.isClientSide) {
                    data.updateClient((ServerPlayer) player, false);
                }
                scubaItem.tickAir(player, EquipmentSlot.CHEST, chestStack);
                if (!world.isClientSide && world.getGameTime() % 60 == 0) {
                    // TODO this effect could be better, custom packet?
                    Vec3 eyePos = player.getEyePosition(0);
                    Vec3 motion = player.getDeltaMovement();
                    Vec3 particlePos = eyePos.add(motion.reverse());
                    ((ServerLevel) world).sendParticles(ParticleTypes.BUBBLE,
                            particlePos.x(), particlePos.y(), particlePos.z(),
                            4 + world.random.nextInt(3),
                            0.25, 0.25, 0.25, motion.length());
                }
            } else if (!world.isClientSide && underwaterPlayers.remove(player)) { // Update client state as they leave the water
                data.updateClient((ServerPlayer) player, false);
            }
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
        if (event.getEntity() instanceof ServerPlayer player) {
            player.getData(ATTACHMENT).updateClient(player, true);
        }
    }

    private long diveTime;
    private double maxDepth;

    private boolean dirty;

    public ScubaData() {
    }

    public ScubaData(long diveTime, double maxDepth) {
        this.diveTime = diveTime;
        this.maxDepth = maxDepth;
    }

    public static boolean isUnderWater(Player player) {
        BlockPos headPos = BlockPos.containing(player.getEyePosition(0));
        return player.level().getFluidState(headPos).is(FluidTags.WATER);
    }

    public static double getDepth(Player player) {
        if (isUnderWater(player)) {
            int surface = TropicraftDimension.getSeaLevel(player.level());
            double depth = surface - (player.getEyePosition(0).y());
            return depth;
        }
        return 0;
    }

    void tick(Player player) {
        diveTime++;
        if (player.level().getGameTime() % 100 == 0) {
            dirty = true;
        }
        updateMaxDepth(getDepth(player));
    }

    public long getDiveTime() {
        return diveTime;
    }

    void updateMaxDepth(double depth) {
        if (depth > maxDepth) {
            maxDepth = depth;
        }
    }

    public double getMaxDepth() {
        return maxDepth;
    }

    void updateClient(ServerPlayer target, boolean force) {
        if (dirty || force) {
            PacketDistributor.sendToPlayer(target, new ClientboundUpdateScubaDataPacket(this));
        }
    }

    public void copyFrom(ScubaData data) {
        diveTime = data.getDiveTime();
        maxDepth = data.getMaxDepth();
    }
}
