package net.tropicraft.core.common.minigames.definitions;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.tropicraft.core.client.data.TropicraftLangKeys;
import net.tropicraft.core.common.Util;
import net.tropicraft.core.common.dimension.TropicraftWorldUtils;
import net.tropicraft.core.common.minigames.IMinigameDefinition;
import net.tropicraft.core.common.minigames.IMinigameInstance;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import weather2.MinigameWeatherInstance;
import weather2.MinigameWeatherInstanceServer;
import weather2.util.WeatherUtil;

/**
 * Definition implementation for the Island Royale minigame.
 * Has some hardcoded block positions for player positions and
 * respawn points for now.
 *
 * Will resolve minigame features and logic in worldUpdate() method
 * later on.
 */
public class IslandRoyaleMinigameDefinition implements IMinigameDefinition {
    public static BlockPos RESPAWN_POS = new BlockPos(5780, 141, 6955);

    private ResourceLocation id = Util.resource("island_royale");
    private String displayName = TropicraftLangKeys.MINIGAME_ISLAND_ROYALE;

    private BlockPos spectatorPos = new BlockPos(5780, 141, 6955);

    private BlockPos[] playerPositions = new BlockPos[] {
            new BlockPos(5780, 141, 6955), // Player 1
            new BlockPos(5780, 141, 6955), // Player 2
            new BlockPos(5780, 141, 6955), // Player 3
            new BlockPos(5780, 141, 6955), // Player 4
            new BlockPos(5780, 141, 6955), // Player 5
            new BlockPos(5780, 141, 6955), // Player 6
            new BlockPos(5780, 141, 6955), // Player 7
            new BlockPos(5780, 141, 6955), // Player 8
            new BlockPos(5780, 141, 6955), // Player 9
            new BlockPos(5780, 141, 6955), // Player 10
            new BlockPos(5780, 141, 6955), // Player 11
            new BlockPos(5780, 141, 6955), // Player 12
            new BlockPos(5780, 141, 6955), // Player 13
            new BlockPos(5780, 141, 6955), // Player 14
            new BlockPos(5780, 141, 6955), // Player 15
            new BlockPos(5780, 141, 6955), // Player 16
    };

    private int maximumPlayerCount = 16;
    private int minimumPlayerCount = 1;

    public static final Logger LOGGER = LogManager.getLogger();

    public static boolean debug = true;

    private MinigameWeatherInstanceServer minigameWeatherInstance;

    private MinigamePhase phase;

    private long minigameTime = 0;
    private long phaseTime = 0;

    private long phase1Length = 20*10;
    private long phase2Length = 20*10;
    //shouldnt end until game ends afaik
    private long phase3Length = 20*10;

    public enum MinigamePhase {
        PHASE1,
        PHASE2,
        PHASE3;
    }

    public IslandRoyaleMinigameDefinition() {
        minigameWeatherInstance = new MinigameWeatherInstanceServer();
    }

    @Override
    public ResourceLocation getID() {
        return this.id;
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
        return this.spectatorPos;
    }

    @Override
    public BlockPos getPlayerRespawnPosition(IMinigameInstance instance) {
        return RESPAWN_POS;
    }

    @Override
    public BlockPos[] getParticipantPositions() {
        return this.playerPositions;
    }

    @Override
    public int getMinimumParticipantCount() {
        return debug ? 1 : this.minimumPlayerCount;
    }

    @Override
    public int getMaximumParticipantCount() {
        return this.maximumPlayerCount;
    }

    @Override
    public void worldUpdate(World world, IMinigameInstance instance) {
        if (world.getDimension().getType() == getDimension()) {
            //LOGGER.info("world update + " + world.getGameTime());

            minigameTime++;
            phaseTime++;

            if (phase == MinigamePhase.PHASE1) {
                if (phaseTime >= phase1Length) {
                    nextPhase();
                }
            } else if (phase == MinigamePhase.PHASE2) {
                if (phaseTime >= phase2Length) {
                    nextPhase();
                }
            } else if (phase == MinigamePhase.PHASE3) {
                //???
            }

            minigameWeatherInstance.tick(this);
        }
    }

    @Override
    public void onPlayerDeath(ServerPlayerEntity player, IMinigameInstance instance) {
        if (!instance.getSpectators().contains(player)) {
            instance.removeParticipant(player);
            instance.addSpectator(player);

            player.setGameType(GameType.SPECTATOR);
        }
    }

    @Override
    public void onPlayerRespawn(ServerPlayerEntity player, IMinigameInstance instance) {

    }

    @Override
    public void onFinish() {
        minigameWeatherInstance.reset();
        phase = MinigamePhase.PHASE1;
        phaseTime = 0;
    }

    @Override
    public void onStart() {
        minigameTime = 0;
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
