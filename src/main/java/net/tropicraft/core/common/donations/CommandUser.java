package net.tropicraft.core.common.donations;

import org.apache.logging.log4j.LogManager;

import net.minecraft.command.ICommandSource;
import net.minecraft.util.text.ITextComponent;

public class CommandUser implements ICommandSource {

    @Override
    public void sendMessage(ITextComponent component) {
        LogManager.getLogger().info(component.getString());
    }

    @Override
    public boolean shouldReceiveFeedback() {
        return true;
    }

    @Override
    public boolean shouldReceiveErrors() {
        return true;
    }

    @Override
    public boolean allowLogging() {
        return true;
    }
}
