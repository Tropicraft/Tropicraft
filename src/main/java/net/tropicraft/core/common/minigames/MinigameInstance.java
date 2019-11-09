package net.tropicraft.core.common.minigames;

import CoroUtil.util.Vec3;
import com.google.common.collect.Sets;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ICommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;

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

    private CommandSource commandSource;

    private ServerWorld world;

    public MinigameInstance(IMinigameDefinition definition, ServerWorld world) {
        this.definition = definition;
        this.world = world;
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

    @Override
    public CommandSource getCommandSource() {
        if (this.commandSource == null) {
            String s = this.getDefinition().getUnlocalizedName();
            ITextComponent text = new StringTextComponent(s);
            this.commandSource = new CommandSource(ICommandSource.field_213139_a_, Vec3d.ZERO, Vec2f.ZERO, this.world, 2, s, text, this.world.getServer(), null);
        }

        return this.commandSource;
    }
}
