package net.tropicraft.core.common.command;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.BiConsumer;

import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.tropicraft.Tropicraft;

public class CommandTropicsMiscClient extends CommandBase {
    
    private enum SubCommand {

        ENC_UNLOCK((player, args) -> {
            for (int i = 0; i < Tropicraft.encyclopedia.getPageCount(); i++) {
                Tropicraft.encyclopedia.markPageAsNewlyVisible(i);
            }
        }),
        
        ENC_RESET((player, args) -> {
            for (int i = 0; i < Tropicraft.encyclopedia.getPageCount(); i++) {
                Tropicraft.encyclopedia.hidePage(i);
            }
        }),
            
        ;
        
        private final BiConsumer<EntityPlayer, String[]> function;
        
        private SubCommand(BiConsumer<EntityPlayer, String[]> function) {
            this.function = function;
        }
    }

    @Override
    public String getName() {
        return "tc_misc_client";
    }

    @Override
    public String getUsage(ICommandSender commandSender) {
        return "";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    private static EntityPlayer getCommandSenderAsClientPlayer(ICommandSender sender) throws PlayerNotFoundException {
        if (sender instanceof EntityPlayer) {
            return (EntityPlayer) sender;
        } else {
            throw new PlayerNotFoundException("commands.generic.player.unspecified");
        }
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender commandSender,
                        String[] args) throws CommandException {
        EntityPlayer player = getCommandSenderAsClientPlayer(commandSender);

        if (args.length > 0) {
            String subcmd = args[0].toUpperCase(Locale.ROOT);
            try {
                SubCommand cmd = SubCommand.valueOf(subcmd);
                cmd.function.accept(player, args);
            } catch (IllegalArgumentException e) {
                throw new CommandException("Invalid subcommand");
            }
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, Arrays.stream(SubCommand.values()).map(Enum::name).map(s -> s.toLowerCase(Locale.ROOT)).toArray(String[]::new));
        }
        return super.getTabCompletions(server, sender, args, targetPos);
    }
}