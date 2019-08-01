package net.tropicraft.core.common.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;

public class TropicraftCommands {
    private final CommandDispatcher<CommandSource> dispatcher = new CommandDispatcher<>();

    public TropicraftCommands() {
        CommandTropicsTeleport.register(dispatcher);
    }
}
