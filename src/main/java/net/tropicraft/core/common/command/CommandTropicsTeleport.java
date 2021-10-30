package net.tropicraft.core.common.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.tropicraft.core.common.dimension.TropicraftDimension;

import static net.minecraft.command.Commands.literal;

public class CommandTropicsTeleport {

    public static void register(final CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                literal("tropics")
                        .requires(s -> s.hasPermission(2))
                        .executes(c -> teleport(c.getSource()))
        );
    }

    private static int teleport(final CommandSource source) {
        if (source.getEntity().getType() != EntityType.PLAYER) {
            source.sendFailure(new StringTextComponent("Cannot teleport non-players!"));
        }
        TropicraftDimension.teleportPlayer((ServerPlayerEntity) source.getEntity(), TropicraftDimension.WORLD);
        return 1;
    }
}
