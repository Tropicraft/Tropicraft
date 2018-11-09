package net.tropicraft.core.common.donations;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nullable;

public class CommandUser implements ICommandSender {

    World world;

    public CommandUser(World world) {

        this.world = world;

    }

    @Override
    public String getName() {
        return "TCCommandSender";
    }

    @Override
    public boolean canUseCommand(int permLevel, String commandName) {
        return true;
    }

    @Override
    public World getEntityWorld() {
        return world;
    }

    @Nullable
    @Override
    public MinecraftServer getServer() {
        return FMLCommonHandler.instance().getMinecraftServerInstance();
    }
}
