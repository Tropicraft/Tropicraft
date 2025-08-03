package net.tropicraft.core.common.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.tropicraft.core.common.item.component.TropicraftDataComponents;

public class LoveTropicsShellItem extends ShellItem {
    public LoveTropicsShellItem(Properties properties) {
        super(properties);
    }

    @Override
    public Component getName(ItemStack stack) {
        String name = stack.get(TropicraftDataComponents.SHELL_NAME);
        if (name == null) {
            return super.getName(stack);
        }
        String type = name.endsWith("s") ? "with_s" : "normal";
        return Component.translatable("item.tropicraft.shell.owned." + type, name);
    }
}
