package net.tropicraft.core.common.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.tropicraft.core.common.command.debug.MapBiomesCommand;
import net.tropicraft.core.common.dimension.TropicraftDimension;

import static net.minecraft.commands.Commands.literal;

public final class TropicraftCommands {
    public static void register(final CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                literal("tropics")
                        .requires(s -> s.hasPermission(2))
                        .executes(c -> teleport(c.getSource()))
        );

        // Dev only debug!
        if (!FMLEnvironment.production) {
            MapBiomesCommand.register(dispatcher);

            //Used for testing the creation of portals
            dispatcher.register(literal("tropics")
                    .then(literal("portal")
                            .requires(s -> s.hasPermission(2))
                            .executes(c -> teleportWithPortal(c.getSource()))
                    )
            );
        }
    }

    private static int teleport(final CommandSourceStack source) throws CommandSyntaxException {
        TropicraftDimension.teleportPlayer(source.getPlayerOrException(), TropicraftDimension.WORLD);
        return Command.SINGLE_SUCCESS;
    }

    private static int teleportWithPortal(final CommandSourceStack source) throws CommandSyntaxException {
        TropicraftDimension.teleportPlayerWithPortal(source.getPlayerOrException(), TropicraftDimension.WORLD);
        return Command.SINGLE_SUCCESS;
    }
}
