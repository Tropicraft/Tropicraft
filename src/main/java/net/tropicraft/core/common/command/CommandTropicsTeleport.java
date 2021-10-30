package net.tropicraft.core.common.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.EntityType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.TextComponent;
import net.tropicraft.core.common.dimension.TropicraftDimension;

import static net.minecraft.command.Commands.literal;

public clasnet.minecraft.commands.Commands    public static void register(final CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                literal("tropics")
                        .requires(s -> s.hasPermission(2))
                        .executes(c -> teleport(c.getSource()))
        );
    }

    private static int teleport(final CommandSourceStack source) {
        if (source.getEntity().getType() != EntityType.PLAYER) {
            source.sendFailure(new TextComponent("Cannot teleport non-players!"));
        }
        TropicraftDimension.teleportPlayer((ServerPlayer) source.getEntity(), TropicraftDimension.WORLD);
        return 1;
    }
}
