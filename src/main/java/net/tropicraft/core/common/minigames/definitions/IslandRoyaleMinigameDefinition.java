package net.tropicraft.core.common.minigames.definitions;

import net.minecraft.block.Blocks;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.tropicraft.core.client.data.TropicraftLangKeys;
import net.tropicraft.core.common.Util;
import net.tropicraft.core.common.config.ConfigLT;
import net.tropicraft.core.common.dimension.TropicraftWorldUtils;
import net.tropicraft.core.common.minigames.IMinigameDefinition;
import net.tropicraft.core.common.minigames.IMinigameInstance;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import weather2.MinigameWeatherInstance;
import weather2.MinigameWeatherInstanceServer;

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

    private BlockPos waterLevelMin = new BlockPos(5555, 0, 7360);
    private BlockPos waterLevelMax = waterLevelMin.add(1000, 0, 1000);

    private MinecraftServer server;

    public enum MinigamePhase {
        PHASE1,
        PHASE2,
        PHASE3;
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
            //LOGGER.info("world update + " + world.getGameTime());

            minigameTime++;
            phaseTime++;

            if (phase == MinigamePhase.PHASE1) {
                if (phaseTime >= ConfigLT.MINIGAME_ISLAND_ROYALE.phase1Length.get()) {
                    nextPhase();
                }
            } else if (phase == MinigamePhase.PHASE2) {
                if (phaseTime >= ConfigLT.MINIGAME_ISLAND_ROYALE.phase2Length.get()) {
                    nextPhase();
                }
            } else if (phase == MinigamePhase.PHASE3) {
                //???
                //DOOOOOOOOOOOOOOM
            }

            minigameWeatherInstance.tick(this);

            if (minigameTime % 200 == 0) {
                this.waterLevel++;
                BlockPos min = this.waterLevelMin.add(0, this.waterLevel, 0);
                BlockPos max = this.waterLevelMax.add(0, this.waterLevel, 0);

                Iterable<BlockPos> positions = BlockPos.getAllInBoxMutable(min, max);

                System.out.println(this.waterLevel);
                for (BlockPos position : positions) {
                    if (world.getBlockState(position).isAir(world, position)) {
                        world.setBlockState(position, Blocks.WATER.getDefaultState(), 4);
                    }
                }

//                for (int x = min.getX() >> 4; x < max.getX() >> 4; x++) {
//                    for (int z = min.getZ() >> 4; z < max.getZ() >> 4; z++) {
//                        Chunk chunk = world.getChunk(x, z);
//
//                        chunk.markDirty();
//                    }
//                }
            }
        }
    }

    @Override
    public void onPlayerDeath(ServerPlayerEntity player, IMinigameInstance instance) {
        if (!instance.getSpectators().contains(player.getUniqueID())) {
            instance.removeParticipant(player);
            instance.addSpectator(player);

            player.setGameType(GameType.SPECTATOR);
        }
    }

    @Override
    public void onPlayerUpdate(ServerPlayerEntity player, IMinigameInstance instance) {

    }

    @Override
    public void onPlayerRespawn(ServerPlayerEntity player, IMinigameInstance instance) {

    }

    @Override
    public void onFinish(CommandSource commandSource) {
        minigameWeatherInstance.reset();
        phase = MinigamePhase.PHASE1;
        phaseTime = 0;
    }

    @Override
    public void onStart(CommandSource commandSource) {
        minigameTime = 0;
        ServerWorld world = this.server.getWorld(this.getDimension());
        waterLevel = world.getSeaLevel();
        phase = MinigamePhase.PHASE1;
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
            //no
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
}
