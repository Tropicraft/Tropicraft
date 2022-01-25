package net.tropicraft.core.common.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.tropicraft.core.common.command.debug.MapBiomesCommand;
import net.tropicraft.core.common.dimension.TropicraftDimension;

import static net.minecraft.commands.Commands.literal;

public class CommandTropics {

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
            dispatcher.register(
                    literal("tfportal")
                            .requires(s -> s.hasPermission(2))
                            .executes(c -> teleportPortalTest(c.getSource()))
            );
        }
    }

    private static int teleport(final CommandSourceStack source) {
        if (source.getEntity().getType() != EntityType.PLAYER) {
            source.sendFailure(new TextComponent("Cannot teleport non-players!"));
        }
        TropicraftDimension.teleportPlayer((ServerPlayer) source.getEntity(), TropicraftDimension.WORLD, false);
        return 1;
    }

    private static int teleportPortalTest(final CommandSourceStack source) {
        if (source.getEntity().getType() != EntityType.PLAYER) {
            source.sendFailure(new TextComponent("Cannot make a portal on a non-player nor teleport non-players!"));
        }
        TropicraftDimension.teleportPlayer((ServerPlayer) source.getEntity(), TropicraftDimension.WORLD, true);
        return 1;
    }
}
