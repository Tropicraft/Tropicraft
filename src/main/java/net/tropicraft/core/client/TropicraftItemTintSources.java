package net.tropicraft.core.client;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.ARGB;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.tropicraft.Constants;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.item.CocktailItem;
import net.tropicraft.core.common.item.component.TropicraftDataComponents;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = Tropicraft.ID, value = Dist.CLIENT)
public class TropicraftItemTintSources {
    @SubscribeEvent
    public static void register(RegisterColorHandlersEvent.ItemTintSources event) {
        event.register(Tropicraft.id("cocktail"), Cocktail.CODEC);
        event.register(Tropicraft.id("love_tropics_shell"), LoveTropicsShell.CODEC);
    }

    public record Cocktail() implements ItemTintSource {
        public static final MapCodec<Cocktail> CODEC = MapCodec.unit(Cocktail::new);

        @Override
        public int calculate(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity) {
            return ARGB.opaque(CocktailItem.getCocktail(stack).color());
        }

        @Override
        public MapCodec<Cocktail> type() {
            return CODEC;
        }
    }

    public record LoveTropicsShell() implements ItemTintSource {
        public static final MapCodec<LoveTropicsShell> CODEC = MapCodec.unit(LoveTropicsShell::new);

        private static final Map<String, Integer> COLORS_BY_NAME = new HashMap<>();

        static {
            RandomSource random = RandomSource.create();
            for (String name : ArrayUtils.addAll(Constants.LT17_NAMES, Constants.LT18_NAMES)) {
                random.setSeed(name.hashCode());
                COLORS_BY_NAME.put(name, ARGB.opaque(Color.HSBtoRGB(random.nextFloat(), (random.nextFloat() * 0.2f) + 0.7f, 1)));
            }
        }

        @Override
        public int calculate(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity) {
            String name = stack.get(TropicraftDataComponents.SHELL_NAME);
            if (name != null) {
                return COLORS_BY_NAME.get(name);
            }
            return COLORS_BY_NAME.get(Constants.LT17_NAMES[0]);
        }

        @Override
        public MapCodec<LoveTropicsShell> type() {
            return CODEC;
        }
    }
}
