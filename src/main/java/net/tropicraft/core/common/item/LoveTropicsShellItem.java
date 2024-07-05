package net.tropicraft.core.common.item;

import com.google.common.collect.Maps;
import net.minecraft.network.chat.Component;
import net.minecraft.util.CommonColors;
import net.minecraft.util.FastColor;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.tropicraft.Constants;
import org.apache.commons.lang3.ArrayUtils;

import java.awt.*;
import java.util.Map;

public class LoveTropicsShellItem extends ShellItem {

    public static class LTUtil {
        private static final Map<String, Integer> colors = Maps.newHashMap();
        private static final RandomSource rand = RandomSource.create();

        static {
            for (final String name : ArrayUtils.addAll(Constants.LT17_NAMES, Constants.LT18_NAMES)) {
                rand.setSeed(name.hashCode());
                colors.put(name, FastColor.ARGB32.opaque(Color.HSBtoRGB(rand.nextFloat(), (rand.nextFloat() * 0.2f) + 0.7f, 1)));
            }
        }
    }

    public LoveTropicsShellItem(final Properties properties) {
        super(properties);
    }

    public static int getColor(ItemStack itemstack, int pass) {
        final String name = itemstack.get(TropicraftDataComponents.SHELL_NAME);
        if (name != null) {
            return pass == 0 ? CommonColors.WHITE : LTUtil.colors.get(name);
        }
        return pass == 0 ? CommonColors.WHITE : LTUtil.colors.get(Constants.LT17_NAMES[0]);
    }

    @Override
    public Component getName(final ItemStack stack) {
        final String name = stack.get(TropicraftDataComponents.SHELL_NAME);
        if (name == null) {
            return super.getName(stack);
        }
        final String type = name.endsWith("s") ? "with_s" : "normal";
        return Component.translatable("item.tropicraft.shell.owned." + type, name);
    }
}
