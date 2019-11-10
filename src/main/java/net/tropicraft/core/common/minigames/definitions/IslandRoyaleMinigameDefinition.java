package net.tropicraft.core.common.minigames.definitions;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraft.world.server.ServerWorld;
import net.tropicraft.core.client.data.TropicraftLangKeys;
import net.tropicraft.core.common.Util;
import net.tropicraft.core.common.config.ConfigLT;
import net.tropicraft.core.common.dimension.TropicraftWorldUtils;
import net.tropicraft.core.common.minigames.IMinigameDefinition;
import net.tropicraft.core.common.minigames.IMinigameInstance;
import net.tropicraft.core.common.minigames.MinigameManager;
import weather2.MinigameWeatherInstance;
import weather2.MinigameWeatherInstanceServer;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Definition implementation for the Island Royale minigame.
 *
 * Will resolve minigame features and logic in worldUpdate() method
 * later on.
 */
public class IslandRoyaleMinigameDefinition implements IMinigameDefinition {
    public static ResourceLocation ID = Util.resource("island_royale");
    private String displayName = TropicraftLangKeys.MINIGAME_ISLAND_ROYALE;

    public static final Logger LOGGER = LogManager.getLogger();

    public static boolean debug = true;

    private MinigameWeatherInstanceServer minigameWeatherInstance;

    private MinigamePhase phase = MinigamePhase.PHASE1;

    private long minigameTime = 0;
    private long phaseTime = 0;

    private int waterLevel;

    private BlockPos waterLevelMin = new BlockPos(5722, 0, 6782);
    private BlockPos waterLevelMax = new BlockPos(6102, 0, 7162);

    private MinecraftServer server;

    private boolean minigameEnded;
    private int minigameEndedTimer;
    private UUID winningPlayer;

    private WaterLevelInfo phase2WaterLevelInfo = new WaterLevelInfo(
            ConfigLT.MINIGAME_ISLAND_ROYALE.phase2TargetWaterLevel.get(),
            126,
            ConfigLT.MINIGAME_ISLAND_ROYALE.phase2Length.get());

    private WaterLevelInfo phase3WaterLevelInfo = new WaterLevelInfo(
            ConfigLT.MINIGAME_ISLAND_ROYALE.phase3TargetWaterLevel.get(),
            ConfigLT.MINIGAME_ISLAND_ROYALE.phase2TargetWaterLevel.get(),
            ConfigLT.MINIGAME_ISLAND_ROYALE.phase3Length.get()
    );

    public enum MinigamePhase {
        PHASE1,
        PHASE2,
        PHASE3,
        PHASE4,
    }

    public IslandRoyaleMinigameDefinition(MinecraftServer server) {
        this.minigameWeatherInstance = new MinigameWeatherInstanceServer();
        this.server = server;
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public String getUnlocalizedName() {
        return this.displayName;
    }

    @Override
    public DimensionType getDimension() {
        return TropicraftWorldUtils.ISLAND_ROYALE_DIMENSION;
    }

    @Override
    public GameType getParticipantGameType() {
        return GameType.ADVENTURE;
    }

    @Override
    public GameType getSpectatorGameType() {
        return GameType.SPECTATOR;
    }

    @Override
    public BlockPos getSpectatorPosition() {
        return ConfigLT.minigame_IslandRoyale_spectatorPosition;
    }

    @Override
    public BlockPos getPlayerRespawnPosition(IMinigameInstance instance) {
        return ConfigLT.minigame_IslandRoyale_respawnPosition;
    }

    @Override
    public BlockPos[] getParticipantPositions() {
        return ConfigLT.minigame_IslandRoyale_playerPositions;
    }

    @Override
    public int getMinimumParticipantCount() {
        return ConfigLT.MINIGAME_ISLAND_ROYALE.minimumPlayerCount.get();
    }

    @Override
    public int getMaximumParticipantCount() {
        return ConfigLT.MINIGAME_ISLAND_ROYALE.maximumPlayerCount.get();
    }

    @Override
    public void worldUpdate(World world, IMinigameInstance instance) {
        if (world.getDimension().getType() == getDimension()) {
            this.checkForGameEndCondition(instance);

            minigameTime++;
            phaseTime++;

            this.processWaterLevel(world);

            if (phase == MinigamePhase.PHASE1) {
                if (phaseTime >= 500) {
                    nextPhase();

                    for (UUID uuid : instance.getAllPlayerUUIDs()) {
                        ServerPlayerEntity player = this.server.getPlayerList().getPlayerByUUID(uuid);

                        if (player != null) {
                            player.sendMessage(new TranslationTextComponent(TropicraftLangKeys.ISLAND_ROYALE_PVP_ENABLED).applyTextStyle(TextFormatting.RED), ChatType.CHAT);
                        }
                    }
                }
            } else if (phase == MinigamePhase.PHASE2) {
                if (phaseTime >= ConfigLT.MINIGAME_ISLAND_ROYALE.phase2Length.get()) {
                    nextPhase();
                }
            } else if (phase == MinigamePhase.PHASE3) {
                if (phaseTime >= ConfigLT.MINIGAME_ISLAND_ROYALE.phase3Length.get()) {
                    nextPhase();
                }
            }

            minigameWeatherInstance.tick(this);
        }
    }

    @Override
    public void onPlayerDeath(ServerPlayerEntity player, IMinigameInstance instance) {
        if (!instance.getSpectators().contains(player.getUniqueID())) {
            instance.removeParticipant(player);
            instance.addSpectator(player);

            player.setGameType(GameType.SPECTATOR);
        }

        if (instance.getParticipants().size() == 1) {
            this.minigameEnded = true;

            this.winningPlayer = instance.getParticipants().iterator().next();
        }
    }

    @SubscribeEvent
    public void onPlayerHurt(LivingHurtEvent event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity)event.getEntityLiving();
            boolean isInInstance = MinigameManager.getInstance().getCurrentMinigame().getParticipants().contains(player.getUniqueID());

            if (isInInstance && event.getSource().getTrueSource() instanceof PlayerEntity && this.phase == MinigamePhase.PHASE1) {
                event.setCanceled(true);
            }
        }
    }

    @Override
    public void onPlayerUpdate(ServerPlayerEntity player, IMinigameInstance instance) {
        if (player.isInWater() && player.ticksExisted % 20 == 0) {
            player.attackEntityFrom(DamageSource.DROWN, 2.0F);
        }
    }

    @Override
    public void onPlayerRespawn(ServerPlayerEntity player, IMinigameInstance instance) {

    }

    @Override
    public void onFinish(CommandSource commandSource, IMinigameInstance instance) {
        minigameWeatherInstance.reset();
        phase = MinigamePhase.PHASE1;
        phaseTime = 0;
    }

    @Override
    public void onPostFinish(CommandSource commandSource) {
        ServerWorld world = this.server.getWorld(this.getDimension());
        DimensionManager.unloadWorld(world);
    }

    @Override
    public void onPreStart() {
        this.fetchBaseMap();
    }

    @Override
    public void onStart(CommandSource commandSource, IMinigameInstance instance) {
        minigameTime = 0;
        ServerWorld world = this.server.getWorld(this.getDimension());
        waterLevel = world.getSeaLevel();
        phase = MinigamePhase.PHASE1;

        for (UUID uuid : instance.getAllPlayerUUIDs()) {
            ServerPlayerEntity player = this.server.getPlayerList().getPlayerByUUID(uuid);

            if (player != null) {
                player.sendMessage(new TranslationTextComponent(TropicraftLangKeys.ISLAND_ROYALE_START).applyTextStyle(TextFormatting.DARK_PURPLE), ChatType.CHAT);
                player.sendMessage(new TranslationTextComponent(TropicraftLangKeys.ISLAND_ROYALE_PVP_DISABLED).applyTextStyle(TextFormatting.YELLOW), ChatType.CHAT);
            }
        }
    }

    public MinigameWeatherInstance getMinigameWeatherInstance() {
        return minigameWeatherInstance;
    }

    public void setMinigameWeatherInstance(MinigameWeatherInstanceServer minigameWeatherInstance) {
        this.minigameWeatherInstance = minigameWeatherInstance;
    }

    public MinigamePhase getPhase() {
        return phase;
    }

    public void setPhase(MinigamePhase phase) {
        this.phase = phase;
    }

    public void nextPhase() {
        if (phase == MinigamePhase.PHASE1) {
            phase = MinigamePhase.PHASE2;
        } else if (phase == MinigamePhase.PHASE2) {
            phase = MinigamePhase.PHASE3;
        } else if (phase == MinigamePhase.PHASE3) {
            phase = MinigamePhase.PHASE4;
        }
        phaseTime = 0;
    }

    public long getMinigameTime() {
        return minigameTime;
    }

    public void setMinigameTime(long minigameTime) {
        this.minigameTime = minigameTime;
    }

    public long getPhaseTime() {
        return phaseTime;
    }

    public void setPhaseTime(long phaseTime) {
        this.phaseTime = phaseTime;
    }

    public void dbg(String str) {
        if (debug) {
            LOGGER.info(str);
        }
    }

    private void checkForGameEndCondition(IMinigameInstance instance) {
        if (this.minigameEnded) {
            if (this.minigameEndedTimer == 0) {
                ServerPlayerEntity winning = this.server.getPlayerList().getPlayerByUUID(this.winningPlayer);

                if (winning != null) {
                    for (UUID uuid : instance.getAllPlayerUUIDs()) {
                        ServerPlayerEntity player = this.server.getPlayerList().getPlayerByUUID(uuid);

                        if (player != null) {
                            player.sendMessage(new TranslationTextComponent(TropicraftLangKeys.ISLAND_ROYALE_FINISH, winning.getDisplayName(), ChatType.CHAT));
                            player.sendMessage(new TranslationTextComponent(TropicraftLangKeys.MINIGAME_FINISH), ChatType.CHAT);
                        }
                    }
                }
            }

            this.minigameEndedTimer++;

            if (this.minigameEndedTimer >= 200) {
                MinigameManager.getInstance().finishCurrentMinigame();
            }
        }
    }

    private void fetchBaseMap() {
        File worldFile = this.server.getWorld(DimensionType.OVERWORLD).getSaveHandler().getWorldDirectory();

        File baseMapsFile = new File(worldFile, "minigame_base_maps");

        File islandRoyaleBase = new File(baseMapsFile, "hunger_games");
        File islandRoyaleCurrent = new File(worldFile, "tropicraft/hunger_games");

        try {
            if (islandRoyaleBase.exists()) {
                FileUtils.deleteDirectory(islandRoyaleCurrent);

                if (islandRoyaleCurrent.mkdirs()) {
                    FileUtils.copyDirectory(islandRoyaleBase, islandRoyaleCurrent);
                }
            } else {
                LOGGER.info("Island royale base map doesn't exist in " + islandRoyaleBase.getPath() + ", add first before it can copy and replace each game start.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processWaterLevel(World world) {
        if (phase == MinigamePhase.PHASE2 || phase == MinigamePhase.PHASE3) {
            WaterLevelInfo waterLevelInfo;

            if (phase == MinigamePhase.PHASE2) {
                waterLevelInfo = this.phase2WaterLevelInfo;
            }
            else {
                waterLevelInfo = this.phase3WaterLevelInfo;
            }

            if (minigameTime % waterLevelInfo.getInterval() == 0) {
                this.waterLevel++;
                BlockPos min = this.waterLevelMin.add(0, this.waterLevel, 0);
                BlockPos max = this.waterLevelMax.add(0, this.waterLevel, 0);
                ChunkPos minChunk = new ChunkPos(min);
                ChunkPos maxChunk = new ChunkPos(max);

                long startTime = System.currentTimeMillis();
                long updatedBlocks = 0;

                MutableBlockPos localStart = new MutableBlockPos();
                MutableBlockPos localEnd = new MutableBlockPos();
                MutableBlockPos realPos = new MutableBlockPos();

                for (int x = minChunk.x; x <= maxChunk.x; x++) {
                    for (int z = minChunk.z; z <= maxChunk.z; z++) {
                        ChunkPos chunkPos = new ChunkPos(x, z);
                        BlockPos chunkStart = chunkPos.asBlockPos();
                        // Extract current chunk section
                        ChunkSection[] sectionArray = world.getChunk(x, z).getSections();
                        ChunkSection section = sectionArray[this.waterLevel >> 4];
                        int localY = this.waterLevel & 0xF;
                        // Calculate start/end within the current section
                        localStart.setPos(min.subtract(chunkStart));
                        localStart.setPos(Math.max(0, localStart.getX()), localY, Math.max(0, localStart.getZ()));
                        localEnd.setPos(max.subtract(chunkStart));
                        localEnd.setPos(Math.min(15, localEnd.getX()), localY, Math.min(15, localEnd.getZ()));
                        // If this section is empty, we must add a new one
                        if (section == Chunk.EMPTY_SECTION) {
                            // This constructor expects the "base y" which means the real Y-level floored to the nearest multiple of 16
                            // This is accomplished by removing the last 4 bits of the coordinate
                            section = new ChunkSection(this.waterLevel & ~0xF);
                            sectionArray[this.waterLevel >> 4] = section;
                        }
                        for (BlockPos pos : BlockPos.getAllInBoxMutable(localStart, localEnd)) {
                            BlockState existing = section.getBlockState(pos.getX(), pos.getY(), pos.getZ());
                            realPos.setPos(chunkStart.getX() + pos.getX(), this.waterLevel, chunkStart.getZ() + pos.getZ());
                            if (existing.isAir(world, pos) || !existing.getMaterial().blocksMovement()) {
                                // If air or a replaceable block, just set to water
                                section.setBlockState(pos.getX(), pos.getY(), pos.getZ(), Blocks.WATER.getDefaultState());
                            } else if (existing.getBlock() instanceof IWaterLoggable) {
                                // If waterloggable, set the waterloggable property to true
                                section.setBlockState(pos.getX(), pos.getY(), pos.getZ(), existing.with(BlockStateProperties.WATERLOGGED, true));
                            }
                            // Tell the client about the change
                            ((ServerChunkProvider)world.getChunkProvider()).markBlockChanged(realPos);
                            updatedBlocks++;
                            // FIXES LIGHTING AT THE COST OF PERFORMANCE - TODO ask fry?
                            // world.getChunkProvider().getLightManager().checkBlock(realPos);
                        }
                    }
                }

                long endTime = System.currentTimeMillis();
                LogManager.getLogger().info("Updated {} blocks in {}ms", updatedBlocks, endTime - startTime);
            }
        }
    }
}