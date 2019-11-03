package net.tropicraft.core.common.minigames;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraft.world.dimension.DimensionType;
import net.tropicraft.core.common.dimension.TropicraftWorldUtils;

/**
 * Used to cache previous game type, dimension and position of player
 * before teleporting into minigame instance.
 *
 * Can use teleportBack() to reset them back to their previous state.
 */
public class MinigamePlayerCache {
    private GameType gameType;
    private DimensionType dimensionType;
    private BlockPos blockPos;
    private PlayerInventory inventory;

    public MinigamePlayerCache(ServerPlayerEntity player) {
        this.gameType = player.interactionManager.getGameType();
        this.dimensionType = player.dimension;
        this.blockPos = new BlockPos(player.posX, player.posY, player.posZ);

        this.inventory = new PlayerInventory(player);
        this.inventory.copyInventory(player.inventory);
    }

    /**
     * Resets the player back to their previous state when this cache
     * was created.
     * @param player The player being reset.
     */
    public void teleportBack(ServerPlayerEntity player) {
        player.inventory.copyInventory(this.inventory);
        player.setGameType(this.gameType);
        TropicraftWorldUtils.teleportPlayerNoPortal(player, this.dimensionType, this.blockPos);
    }
}