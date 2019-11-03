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
 * Definition implementation for Signature Run minigame.
 */
public class SignatureRunMinigameDefinition implements IMinigameDefinition {
    public static BlockPos RESPAWN_POS = new BlockPos(200, 200, 200);

    private ResourceLocation id = Util.resource("signature_run");
    private String displayName = TropicraftLangKeys.MINIGAME_SIGNATURE_RUN;

    private BlockPos spectatorPos = new BlockPos(500, 200, 50);

    private BlockPos[] playerPositions = new BlockPos[] {
            new BlockPos(100, 200, 5000)
    };

    private int maximumPlayerCount = 1;
    private int minimumPlayerCount = 1;

    public SignatureRunMinigameDefinition() {

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
        return TropicraftWorldUtils.SIGNATURE_RUN_DIMENSION;
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
