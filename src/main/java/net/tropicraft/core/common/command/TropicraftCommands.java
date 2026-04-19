package net.tropicraft.core.common.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.permissions.Permissions;
import net.neoforged.fml.loading.FMLEnvironment;
import net.tropicraft.core.common.command.debug.MapBiomesCommand;
import net.tropicraft.core.common.dimension.TropicraftDimension;

import static net.minecraft.commands.Commands.literal;

public final class TropicraftCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                literal("tropics")
                        .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                        .executes(c -> teleport(c.getSource()))
        );

        // Dev only debug!
        if (!FMLEnvironment.isProduction()) {
            MapBiomesCommand.register(dispatcher);

            //Used for testing the creation of portals
            dispatcher.register(literal("tropics")
                    .then(literal("portal")
                            .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                            .executes(c -> teleportWithPortal(c.getSource()))
                    )
            );
        }
    }

    private static int teleport(CommandSourceStack source) throws CommandSyntaxException {
        TropicraftDimension.teleportPlayer(source.getPlayerOrException(), TropicraftDimension.WORLD);
        return Command.SINGLE_SUCCESS;
    }

    private static int teleportWithPortal(CommandSourceStack source) throws CommandSyntaxException {
        TropicraftDimension.teleportPlayerWithPortal(source.getPlayerOrException(), TropicraftDimension.WORLD);
        return Command.SINGLE_SUCCESS;
    }
}
