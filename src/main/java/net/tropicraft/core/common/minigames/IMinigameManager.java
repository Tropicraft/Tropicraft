package net.tropicraft.core.common.minigames;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

/**
 * Specification for a minigame manager. Used to register minigame definitions
 * as well as hold the currently running minigame instance if applicable.
 *
 * Implementations get to define the logic for polling, starting, stopping and
 * registering for polling minigames. Each of these actions return an ActionResult,
 * which are fed into Minecraft Commands to send these messages back to players
 * which execute the commands.
 */
public interface IMinigameManager
{
    /**
     * Register a minigame definition, allowing it to be polled by a poll command.
     * The getID() method of the definition is what is used within the poll command
     * to discriminate which minigame to fetch.
     *
     * @param minigame The minigame definition to register.
     * @throws IllegalArgumentException When the ID used by the minigame being registered
     * is already taken by another registered minigame.
     */
    void register(IMinigameDefinition minigame) throws IllegalArgumentException;

    /**
     * Unregisters a minigame definiton, meaning the minigame commands will no longer
     * recognise it.
     * @param minigameID The ID of the minigame definition to unregister.
     * @throws IllegalArgumentException When the ID used by the minigame being unregistered
     * has not been used by any registered minigames.
     */
    void unregister(ResourceLocation minigameID) throws IllegalArgumentException;

    /**
     * Holds metadata for which players
     * are participants and which are spectators.
     * @return The actively running minigame instance.
     */
    IMinigameInstance getCurrentMinigame();

    /**
     * Finishes the actively running minigame, teleporting players back to
     * their original state before joining the minigame.
     *
     * @throws IllegalStateException When there is no actively running
     * minigame instance. This method should never be called unless
     * it's certain a minigame is actively running.
     */
    void finishCurrentMinigame() throws IllegalStateException;

    /**
     * Starts polling the minigame.
     * @param minigameId The unique ID of the minigame being polled.
     * @param pollingPlayer The player that attempted to start polling this minigame.
     * @return The ActionResult of the polling attempt.
     */
    ActionResult<ITextComponent> startPolling(ResourceLocation minigameId, ServerPlayerEntity pollingPlayer);

    /**
     * Stops polling an actively polling minigame.
     * @param requestingPlayer The player that attempted to stop polling the actively polling minigame.
     * @return The ActionResult of stopping the polling of an actively polling minigame.
     */
    ActionResult<ITextComponent> stopPolling(ServerPlayerEntity requestingPlayer);

    /**
     * Starts an actively polling minigame if it has at least the minimum amount of
     * participants registered to the minigame, specified by the minigame definition.
     * @param startingPlayer The player attempting to start the polling minigame.
     * @return The ActionResult of the start attempt.
     */
    ActionResult<ITextComponent> start(ServerPlayerEntity startingPlayer);

    /**
     * Stops an actively running minigame.
     * @param stoppingPlayer The player attempting to stop the actively running minigame.
     * @return The ActionResult of the stop attempt.
     */
    ActionResult<ITextComponent> stop(ServerPlayerEntity stoppingPlayer);

    /**
     * Registers a player for the currently polling minigame. Puts them in a queue
     * to be selected as either a participant or a spectator when the minigame starts.
     * @param player The player being registered for the currently polling minigame.
     * @return The ActionResult of the register attempt.
     */
    ActionResult<ITextComponent> registerFor(ServerPlayerEntity player);

    /**
     * Unregisters a player for a currently polling minigame if they've registered
     * previously. Removes them from the queue for the minigame if they don't want
     * to be a part of it when it starts.
     * @param player The player being unregistered for the currently polling minigame/
     * @return The ActionResult of the unregister attempt.
     */
    ActionResult<ITextComponent> unregisterFor(ServerPlayerEntity player);
}
