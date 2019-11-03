package net.tropicraft.core.common.minigames;

import com.google.common.collect.Sets;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.Set;
import java.util.UUID;

/**
 * Default implementation of a minigame instance. Simple and naive
 * solution to holding state of players that are currently part
 * of the minigame instance, as well as the definition that is being
 * used to specify the rulesets for the minigame.
 */
public class MinigameInstance implements IMinigameInstance
{
    private IMinigameDefinition definition;

    private Set<UUID> players = Sets.newHashSet();

    private Set<UUID> spectators = Sets.newHashSet();

    public MinigameInstance(IMinigameDefinition definition) {
        this.definition = definition;
    }

    @Override
    public IMinigameDefinition getDefinition() {
        return this.definition;
    }

    @Override
    public void addParticipant(ServerPlayerEntity player) {
        if (this.spectators.contains(player.getUniqueID())) {
            throw new IllegalArgumentException("Player already exists in this minigame instance as a spectator! "
                    + player.getDisplayName().getFormattedText());
        }

        if (this.players.contains(player.getUniqueID())) {
            throw new IllegalArgumentException("Player already exists in this minigame instance! "
                    + player.getDisplayName().getFormattedText());
        }

        this.players.add(player.getUniqueID());
    }

    @Override
    public void removeParticipant(ServerPlayerEntity player) {
        if (!this.players.contains(player.getUniqueID())) {
            throw new IllegalArgumentException("Player doesn't exist in this minigame instance! "
                    + player.getDisplayName().getFormattedText());
        }

        this.players.remove(player.getUniqueID());
    }

    @Override
    public void addSpectator(ServerPlayerEntity player) {
        if (this.players.contains(player.getUniqueID())) {
            throw new IllegalArgumentException("Player already exists in this minigame instance as a non-spectator! "
                    + player.getDisplayName().getFormattedText());
        }

        if (this.spectators.contains(player.getUniqueID())) {
            throw new IllegalArgumentException("Player already exists in this minigame instance as a spectator! "
                    + player.getDisplayName().getFormattedText());
        }

        this.spectators.add(player.getUniqueID());
    }

    @Override
    public void removeSpectator(ServerPlayerEntity player) {
        if (!this.spectators.contains(player.getUniqueID())) {
            throw new IllegalArgumentException("Player doesn't exist in this minigame instance as a spectator! "
                    + player.getDisplayName().getFormattedText());
        }

        this.spectators.remove(player.getUniqueID());
    }

    @Override
    public Set<UUID> getParticipants() {
        return this.players;
    }

    @Override
    public Set<UUID> getSpectators() {
        return this.spectators;
    }
}
