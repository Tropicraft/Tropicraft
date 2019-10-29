package net.tropicraft.core.common.item;

import com.google.common.collect.Maps;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.tropicraft.Constants;
import org.apache.commons.lang3.ArrayUtils;

import java.awt.*;
import java.util.Map;
import java.util.Random;

public class LoveTropicsShellItem extends ShellItem implements IColoredItem {

    public static class LTUtil {
        private static final Map<String, Integer> colors = Maps.newHashMap();
        private static final Random rand = new Random();

        static {
            for (final String name : ArrayUtils.addAll(Constants.LT17_NAMES, Constants.LT18_NAMES)) {
                rand.setSeed(name.hashCode());
                colors.put(name, Color.HSBtoRGB(rand.nextFloat(), (rand.nextFloat() * 0.2f) + 0.7f, 1));
            }
        }
    }

    public LoveTropicsShellItem(final Properties properties) {
        super(properties);
    }

    @Override
    public int getColor(ItemStack itemstack, int pass) {
        final CompoundNBT tag = itemstack.getTag();
        if (tag != null && !tag.isEmpty() && tag.contains("Name")) {
            return pass == 0 ? 0xFFFFFFFF : LTUtil.colors.get(tag.getString("Name"));
        }
        return pass == 0 ? 0xFFFFFFFF : LTUtil.colors.get(Constants.LT17_NAMES[0]);
    }

    @Override
    public ITextComponent getDisplayName(final ItemStack stack) {
        if (!stack.hasTag() || !stack.getTag().contains("Name")) {
            return super.getDisplayName(stack);
        }
        final String name = stack.getTag().getString("Name");
        final String type = name.endsWith("s") ? "with_s" : "normal";
        return new TranslationTextComponent("item.tropicraft.shell.owned." + type, name);
    }
}
