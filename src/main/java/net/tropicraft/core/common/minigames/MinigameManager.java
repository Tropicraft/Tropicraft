package net.tropicraft.core.common.minigames;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.tropicraft.core.client.data.TropicraftLangKeys;
import net.tropicraft.core.common.Util;
import net.tropicraft.core.common.dimension.TropicraftWorldUtils;
import net.tropicraft.core.common.minigames.definitions.survive_the_tide.SurviveTheTideMinigameDefinition;
import net.tropicraft.core.common.minigames.definitions.SignatureRunMinigameDefinition;
import net.tropicraft.core.common.minigames.definitions.UnderwaterTrashHuntMinigameDefinition;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * Standard implementation of a minigame manager. Would prefer to do something other
 * than singleton-style implementation to allow for multiple managers to run multiple
 * minigames at once.
 */
public class MinigameManager implements IMinigameManager
{
    /**
     * Singleton instance that persists throughout server lifecycle.
     */
    private static IMinigameManager INSTANCE;

    /**
     * Registry map for minigame definitions. Used to fetch and start requested minigames.
     */
    private Map<ResourceLocation, IMinigameDefinition> registeredMinigames = Maps.newHashMap();

    /**
     * Current instance of the minigame. Agnostic from minigame definition and used to store
     * the players and spectators that are a part of the current minigame.
     *
     * Is null when there is no minigame running. Isn't set until a game has finished polling
     * and has started.
     */
    private IMinigameInstance currentInstance;

    /**
     * Currently polling game. Is null when there is no minigame polling or a minigame is running.
     */
    private IMinigameDefinition polling;

    /**
     * A list of players that are currently registered for the currently polling minigame.
     * Is empty when a minigame has started or stopped polling.
     */
    private List<UUID> registeredForMinigame = Lists.newArrayList();

    /**
     * Cache used to know what state the player was in before teleporting into a minigame.
     */
    private Map<UUID, MinigamePlayerCache> playerCache = Maps.newHashMap();

    /**
     * Server reference to fetch players from player list.
     */
    private MinecraftServer server;

    private MinigameManager(MinecraftServer server) {
        this.server = server;
    }

    /**
     * Initialize the MinigameManager singleton. Registers itself for forge events
     * and registers some minigame definitions.
     * @param server The minecraft server used for fetching player list.
     */
    public static void init(MinecraftServer server) {
        INSTANCE = new MinigameManager(server);
        MinecraftForge.EVENT_BUS.register(INSTANCE);

        INSTANCE.register(new SurviveTheTideMinigameDefinition(server));
        INSTANCE.register(new SignatureRunMinigameDefinition());
        INSTANCE.register(new UnderwaterTrashHuntMinigameDefinition(server));
    }

    /**
     * Returns null if init() has not been called yet. Shouldn't be called
     * before the server has started.
     * @return The global instance of the minigame manager.
     */
    public static IMinigameManager getInstance() {
        return INSTANCE;
    }

    @Override
    public void register(IMinigameDefinition minigame) {
        if (this.registeredMinigames.containsKey(minigame.getID())) {
            throw new IllegalArgumentException("Minigame already registered with the following ID: " + minigame.getID());
        }

        this.registeredMinigames.put(minigame.getID(), minigame);
    }

    @Override
    public void unregister(ResourceLocation minigameID) {
        if (!this.registeredMinigames.containsKey(minigameID)) {
            TranslationTextComponent msg = new TranslationTextComponent(TropicraftLangKeys.COMMAND_MINIGAME_NOT_REGISTERED, minigameID);
            throw new IllegalArgumentException(msg.getFormattedText());
        }

        this.registeredMinigames.remove(minigameID);
    }
    
    @Override
    public Collection<IMinigameDefinition> getAllMinigames() {
        return Collections.unmodifiableCollection(this.registeredMinigames.values());
    }

    @Override
    public IMinigameInstance getCurrentMinigame() {
        return this.currentInstance;
    }

    @Override
    public void finishCurrentMinigame() {
        if (this.currentInstance == null) {
            throw new IllegalStateException("Attempted to finish a current minigame when none are running.");
        }

        IMinigameDefinition def = this.currentInstance.getDefinition();
        def.onFinish(this.currentInstance.getCommandSource(), this.currentInstance);

        for (UUID uuid : this.currentInstance.getAllPlayerUUIDs()) {
            ServerPlayerEntity player = this.server.getPlayerList().getPlayerByUUID(uuid);

            if (player != null) {
                this.teleportBack(player);
            }
        }

        // Send all players a message letting them know the minigame has finished
        for (ServerPlayerEntity player : this.server.getPlayerList().getPlayers()) {
            player.sendMessage(new TranslationTextComponent(TropicraftLangKeys.COMMAND_FINISHED_MINIGAME,
                    new TranslationTextComponent(def.getUnlocalizedName()).applyTextStyle(TextFormatting.ITALIC).applyTextStyle(TextFormatting.AQUA))
                    .applyTextStyle(TextFormatting.GOLD), ChatType.CHAT);
        }

        def.onPostFinish(this.currentInstance.getCommandSource());

        this.currentInstance = null;
    }

    @Override
    public ActionResult<ITextComponent> startPolling(ResourceLocation minigameId) {
        // Make sure minigame is registered with provided id
        if (!this.registeredMinigames.containsKey(minigameId)) {
            return new ActionResult<>(ActionResultType.FAIL, new TranslationTextComponent(TropicraftLangKeys.COMMAND_MINIGAME_ID_INVALID));
        }

        // Make sure there isn't a currently running minigame
        if (this.currentInstance != null) {
            return new ActionResult<>(ActionResultType.FAIL, new TranslationTextComponent(TropicraftLangKeys.COMMAND_MINIGAME_ALREADY_STARTED));
        }

        // Make sure another minigame isn't already polling
        if (this.polling != null) {
            return new ActionResult<>(ActionResultType.FAIL, new TranslationTextComponent(TropicraftLangKeys.COMMAND_ANOTHER_MINIGAME_POLLING));
        }

        IMinigameDefinition definition = this.registeredMinigames.get(minigameId);
        this.polling = definition;

        for (ServerPlayerEntity player : this.server.getPlayerList().getPlayers()) {
            player.sendMessage(new TranslationTextComponent(TropicraftLangKeys.COMMAND_MINIGAME_POLLING,
                new TranslationTextComponent(definition.getUnlocalizedName()).applyTextStyle(TextFormatting.ITALIC).applyTextStyle(TextFormatting.AQUA),
                new StringTextComponent("/minigame register").applyTextStyle(TextFormatting.ITALIC).applyTextStyle(TextFormatting.GRAY))
                .applyTextStyle(TextFormatting.GOLD), ChatType.CHAT);
        }

        return new ActionResult<>(ActionResultType.SUCCESS, new TranslationTextComponent(TropicraftLangKeys.COMMAND_MINIGAME_POLLED));
    }

    @Override
    public ActionResult<ITextComponent> stopPolling() {
        // Check if a minigame is polling
        if (this.polling == null) {
            return new ActionResult<>(ActionResultType.FAIL, new TranslationTextComponent(TropicraftLangKeys.COMMAND_NO_MINIGAME_POLLING));
        }

        // Cache before killing the currently polled game
        String minigameName = this.polling.getUnlocalizedName();

        this.polling = null;
        this.registeredForMinigame.clear();

        for (ServerPlayerEntity player : this.server.getPlayerList().getPlayers()) {
            player.sendMessage(new TranslationTextComponent(TropicraftLangKeys.COMMAND_MINIGAME_STOPPED_POLLING,
                    new TranslationTextComponent(minigameName).applyTextStyle(TextFormatting.ITALIC).applyTextStyle(TextFormatting.AQUA))
                    .applyTextStyle(TextFormatting.RED), ChatType.CHAT);
        }

        return new ActionResult<>(ActionResultType.SUCCESS,
            new TranslationTextComponent(TropicraftLangKeys.COMMAND_STOP_POLL,
                new TranslationTextComponent(minigameName).applyTextStyle(TextFormatting.AQUA)).applyTextStyle(TextFormatting.RED));
    }

    @Override
    public ActionResult<ITextComponent> start() {
        // Check if any minigame is polling, can only start if so
        if (this.polling == null) {
            return new ActionResult<>(ActionResultType.FAIL, new TranslationTextComponent(TropicraftLangKeys.COMMAND_NO_MINIGAME_POLLING));
        }

        // Check that have enough players to start minigame.
        if (this.registeredForMinigame.size() < this.polling.getMinimumParticipantCount()) {
            return new ActionResult<>(ActionResultType.FAIL, new TranslationTextComponent(TropicraftLangKeys.COMMAND_NOT_ENOUGH_PLAYERS, this.polling.getMinimumParticipantCount()));
        }

        ActionResult<ITextComponent> canStart = this.polling.canStartMinigame();

        if (canStart.getType() == ActionResultType.FAIL) {
            return canStart;
        }

        this.polling.onPreStart();

        ServerWorld world = this.server.getWorld(this.polling.getDimension());
        this.currentInstance = new MinigameInstance(this.polling, world);

        int playersAvailable = Math.min(this.registeredForMinigame.size(), this.polling.getMaximumParticipantCount());
        List<UUID> chosenPlayers = Util.extractRandomElements(new Random(), this.registeredForMinigame, playersAvailable);

        for (int i = 0; i < chosenPlayers.size(); i++) {
            UUID playerUUID = chosenPlayers.get(i);
            ServerPlayerEntity player = this.server.getPlayerList().getPlayerByUUID(playerUUID);

            if (player != null) {
                this.currentInstance.addParticipant(player);
                MinigamePlayerCache cache = new MinigamePlayerCache(player);
                this.playerCache.put(player.getUniqueID(), cache);

                cache.resetPlayerStats(player);

                this.teleportPlayerIntoInstance(this.currentInstance, player, i);
            }
        }

        for (UUID spectatorUUID : this.registeredForMinigame) {
            ServerPlayerEntity spectator = this.server.getPlayerList().getPlayerByUUID(spectatorUUID);

            if (spectator != null) {
                this.currentInstance.addSpectator(spectator);
                MinigamePlayerCache cache = new MinigamePlayerCache(spectator);
                this.playerCache.put(spectator.getUniqueID(), cache);

                cache.resetPlayerStats(spectator);

                spectator.inventory.clear();
                this.teleportSpectatorIntoInstance(this.currentInstance, spectator);
            }
        }

        this.polling = null;
        this.registeredForMinigame.clear();

        this.currentInstance.getDefinition().onStart(this.currentInstance.getCommandSource(), this.currentInstance);

        return new ActionResult<>(ActionResultType.SUCCESS, new TranslationTextComponent(TropicraftLangKeys.COMMAND_MINIGAME_STARTED).applyTextStyle(TextFormatting.GREEN));
    }

    @Override
    public ActionResult<ITextComponent> stop() {
        // Can't stop a current minigame if doesn't exist
        if (this.currentInstance == null) {
            return new ActionResult<>(ActionResultType.FAIL, new TranslationTextComponent(TropicraftLangKeys.COMMAND_NO_MINIGAME));
        }

        ITextComponent minigameName = new TranslationTextComponent(this.currentInstance.getDefinition().getUnlocalizedName()).applyTextStyle(TextFormatting.AQUA);

        this.finishCurrentMinigame();

        return new ActionResult<>(ActionResultType.SUCCESS, new TranslationTextComponent(TropicraftLangKeys.COMMAND_STOPPED_MINIGAME, minigameName).applyTextStyle(TextFormatting.GREEN));
    }

    @Override
    public ActionResult<ITextComponent> registerFor(ServerPlayerEntity player) {
        // Check if minigame has already started
        if (this.currentInstance != null) {
            return new ActionResult<>(ActionResultType.FAIL, new TranslationTextComponent(TropicraftLangKeys.COMMAND_MINIGAME_ALREADY_STARTED));
        }

        // Check if a minigame is polling
        if (this.polling == null) {
            return new ActionResult<>(ActionResultType.FAIL, new TranslationTextComponent(TropicraftLangKeys.COMMAND_NO_MINIGAME_POLLING));
        }

        if (this.registeredForMinigame.contains(player.getUniqueID())) {
            return new ActionResult<>(ActionResultType.FAIL, new TranslationTextComponent(TropicraftLangKeys.COMMAND_MINIGAME_ALREADY_REGISTERED));
        }

        this.registeredForMinigame.add(player.getUniqueID());

        if (this.registeredForMinigame.size() >= this.polling.getMinimumParticipantCount()) {
            for (ServerPlayerEntity p : this.server.getPlayerList().getPlayers()) {
                p.sendMessage(new TranslationTextComponent(TropicraftLangKeys.COMMAND_ENOUGH_PLAYERS).applyTextStyle(TextFormatting.AQUA));
            }
        }

        ITextComponent minigameName = new TranslationTextComponent(this.polling.getUnlocalizedName()).applyTextStyle(TextFormatting.AQUA);
        ITextComponent msg = new TranslationTextComponent(TropicraftLangKeys.COMMAND_REGISTERED_FOR_MINIGAME, minigameName).applyTextStyle(TextFormatting.GREEN);

        return new ActionResult<>(ActionResultType.SUCCESS, msg);
    }

    @Override
    public ActionResult<ITextComponent> unregisterFor(ServerPlayerEntity player) {
        // Check if a minigame is polling
        if (this.polling == null) {
            return new ActionResult<>(ActionResultType.FAIL, new TranslationTextComponent(TropicraftLangKeys.COMMAND_NO_MINIGAME_POLLING));
        }

        // Check if minigame has already started
        if (!this.registeredForMinigame.contains(player.getUniqueID())) {
            return new ActionResult<>(ActionResultType.FAIL, new TranslationTextComponent(TropicraftLangKeys.COMMAND_NOT_REGISTERED_FOR_MINIGAME));
        }

        this.registeredForMinigame.remove(player.getUniqueID());

        if (this.registeredForMinigame.size() == this.polling.getMinimumParticipantCount() - 1) {
            for (ServerPlayerEntity p : this.server.getPlayerList().getPlayers()) {
                p.sendMessage(new TranslationTextComponent(TropicraftLangKeys.COMMAND_NO_LONGER_ENOUGH_PLAYERS).applyTextStyle(TextFormatting.RED));
            }
        }

        ITextComponent minigameName = new TranslationTextComponent(this.polling.getUnlocalizedName()).applyTextStyle(TextFormatting.AQUA);
        ITextComponent msg = new TranslationTextComponent(TropicraftLangKeys.COMMAND_UNREGISTERED_MINIGAME, minigameName).applyTextStyle(TextFormatting.RED);

        return new ActionResult<>(ActionResultType.SUCCESS, msg);
    }

    private void teleportPlayerIntoInstance(IMinigameInstance instance, ServerPlayerEntity player, int playerCount) {
        BlockPos[] positions = instance.getDefinition().getParticipantPositions();

        // Ensure length of participant positions matches the maximum participant count.
        if (positions.length != instance.getDefinition().getMaximumParticipantCount()) {
            throw new IllegalStateException("The participant positions length doesn't match the" +
                    "maximum participant count defined by the following minigame definition! " + instance.getDefinition().getID());
        }

        BlockPos teleportTo = positions[playerCount];

        TropicraftWorldUtils.teleportPlayerNoPortal(player, instance.getDefinition().getDimension(), teleportTo);
        player.setGameType(instance.getDefinition().getParticipantGameType());
    }

    /**
     * Teleports the spectator into the dimension specified by the minigame definition.
     * Will set the position of the player to the location specified by the definition
     * for spectators. Sets player GameType to SPECTATOR.
     * @param instance The instance of the currently running minigame.
     * @param player The spectator to teleport into the instance.
     */
    private void teleportSpectatorIntoInstance(IMinigameInstance instance, ServerPlayerEntity player) {
        BlockPos teleportTo = instance.getDefinition().getSpectatorPosition();

        TropicraftWorldUtils.teleportPlayerNoPortal(player, instance.getDefinition().getDimension(), teleportTo);
        player.setGameType(instance.getDefinition().getSpectatorGameType());
    }

    /**
     * Teleports a player or spectator out from a currently running minigame instance.
     * Will use the MinigamePlayerCache to reset them to their previous state before
     * entering the instance.
     * @param player The player being teleported back out of the minigame instance.
     */
    private void teleportBack(ServerPlayerEntity player) {
        if (!this.playerCache.containsKey(player.getUniqueID())) {
            throw new IllegalStateException("Player attempting to teleport back was not cached!");
        }

        MinigamePlayerCache cache = this.playerCache.get(player.getUniqueID());
        cache.teleportBack(player);

        this.playerCache.remove(player.getUniqueID());
    }

    @SubscribeEvent
    public void onPlayerHurt(LivingHurtEvent event) {
        if (this.ifPlayerInInstance(event.getEntity())) {
            this.currentInstance.getDefinition().onPlayerHurt(event, this.currentInstance);
        }
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (this.ifPlayerInInstance(event.getEntity())) {
            this.currentInstance.getDefinition().onPlayerUpdate((ServerPlayerEntity) event.getEntity(), this.currentInstance);
        }

        if (this.ifEntityInDimension(event.getEntity())) {
            this.currentInstance.getDefinition().onLivingEntityUpdate(event.getEntityLiving(), this.currentInstance);
        }
    }

    /**
     * Funnel into minigame definition onPlayerDeath() for convenience
     */
    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        if (this.ifPlayerInInstance(event.getEntity())) {
            this.currentInstance.getDefinition().onPlayerDeath((ServerPlayerEntity) event.getEntity(), this.currentInstance);
        }
    }

    /**
     * Funnel into definition onPlayerRespawn() for convenience.
     *
     * Also set player's respawn position defined by the minigame definition.
     */
    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (this.ifPlayerInInstance(event.getPlayer())) {
            IMinigameDefinition def = this.currentInstance.getDefinition();
            def.onPlayerRespawn((ServerPlayerEntity) event.getPlayer(), this.currentInstance);

            BlockPos respawn = def.getPlayerRespawnPosition(this.currentInstance);

            ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
            player.connection.setPlayerLocation(respawn.getX(), respawn.getY(), respawn.getZ(), 0, 0);
        }
    }

    /**
     * Funnel into definition worldUpdate() for convenience
     */
    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (this.currentInstance != null && event.world.getDimension().getType() == this.currentInstance.getDefinition().getDimension()) {
                this.currentInstance.getDefinition().worldUpdate(event.world, this.currentInstance);
            }
        }
    }

    /**
     * When a player logs out, remove them from the currently running minigame instance
     * if they are inside, and teleport back them to their original state.
     *
     * Also if they have registered for a minigame poll, they will be removed from the
     * list of registered players.
     */
    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();

        if (this.currentInstance != null) {
            if (this.currentInstance.getParticipants().contains(player.getUniqueID())) {
                this.currentInstance.removeParticipant(player);
                this.teleportBack(player);
            }

            if (this.currentInstance.getSpectators().contains(player.getUniqueID())) {
                this.currentInstance.removeSpectator(player);
                this.teleportBack(player);
            }
        }

        if (this.polling != null) {
            if (this.registeredForMinigame.contains(player.getUniqueID())) {
                this.unregisterFor((ServerPlayerEntity) event.getPlayer());
            }
        }
    }

    private boolean ifPlayerInInstance(Entity entity) {
        return entity instanceof ServerPlayerEntity && this.currentInstance != null && this.currentInstance.getAllPlayerUUIDs().contains(entity.getUniqueID());
    }

    private boolean ifEntityInDimension(Entity entity) {
        return this.currentInstance != null && entity.dimension == this.currentInstance.getDefinition().getDimension();
    }
}
