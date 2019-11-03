package net.tropicraft.core.common.minigames.definitions;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraft.world.dimension.DimensionType;
import net.tropicraft.core.client.data.TropicraftLangKeys;
import net.tropicraft.core.common.Util;
import net.tropicraft.core.common.dimension.TropicraftWorldUtils;
import net.tropicraft.core.common.minigames.IMinigameDefinition;
import net.tropicraft.core.common.minigames.IMinigameInstance;

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
    private int minimumPlayerCount = 3;

    public IslandRoyaleMinigameDefinition() {

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
        return this.minimumPlayerCount;
    }

    @Override
    public int getMaximumParticipantCount() {
        return this.maximumPlayerCount;
    }

    @Override
    public void worldUpdate(IMinigameInstance instance) {

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

    }

    @Override
    public void onStart() {

    }
}
