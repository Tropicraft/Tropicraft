package net.tropicraft.core.common.item.component;

import com.mojang.serialization.Codec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.data.TropicraftLangKeys;
import net.tropicraft.core.client.scuba.ScubaHUD;
import net.tropicraft.core.common.drinks.Cocktail;
import net.tropicraft.core.common.entity.projectile.ExplodingCoconutEntity;

@EventBusSubscriber(modid = Tropicraft.ID)
public class TropicraftDataComponents {
    public static final DeferredRegister.DataComponents REGISTER = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Tropicraft.ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> SCUBA_AIR = REGISTER.registerComponentType(
            "scuba_air",
            builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT)
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Float>> EXPLOSION_RADIUS = REGISTER.registerComponentType(
            "explosion_radius",
            builder -> builder.persistent(Codec.floatRange(0.0f, ExplodingCoconutEntity.MAX_EXPLOSION_RADIUS)).networkSynchronized(ByteBufCodecs.FLOAT)
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> SHELL_NAME = REGISTER.registerComponentType(
            "shell_name",
            builder -> builder.persistent(Codec.sizeLimitedString(64)).networkSynchronized(ByteBufCodecs.STRING_UTF8)
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Cocktail>> COCKTAIL = REGISTER.registerComponentType(
            "cocktail",
            builder -> builder.persistent(Cocktail.CODEC).networkSynchronized(Cocktail.STREAM_CODEC)
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> DESTROYS_BLOCKS = REGISTER.registerComponentType(
            "destroys_blocks",
            builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL)
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<DamageModifier>> INCOMING_DAMAGE_MODIFIER = REGISTER.registerComponentType(
            "incoming_damage_modifier",
            builder -> builder.persistent(DamageModifier.CODEC)
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> SCIENTIFIC_NAME = REGISTER.registerComponentType(
            "scientific_name",
            builder -> builder.persistent(Codec.sizeLimitedString(128)).networkSynchronized(ByteBufCodecs.stringUtf8(128))
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> HAS_DESCRIPTION = REGISTER.registerComponentType(
            "has_description",
            builder -> builder.persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC)
    );

    @SubscribeEvent
    public static void addToItemTooltip(ItemTooltipEvent event) {
        ItemStack itemStack = event.getItemStack();
        Integer airRemaining = itemStack.get(SCUBA_AIR);
        if (airRemaining != null) {
            event.getToolTip().add(TropicraftLangKeys.SCUBA_AIR_TIME
                    .format(Component.literal(ScubaHUD.formatTime(airRemaining))
                            .withStyle(ScubaHUD.getAirTimeColor(airRemaining)))
                    .copy()
                    .withStyle(ChatFormatting.GRAY));
        }

        Cocktail cocktail = itemStack.get(COCKTAIL);
        if (cocktail != null) {
            cocktail.ingredients().forEach(ingredient -> event.getToolTip().add(ingredient.value().getDisplayName()));
        }

        String scientificName = itemStack.get(SCIENTIFIC_NAME);
        if (scientificName != null) {
            event.getToolTip().add(Component.literal(scientificName).withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC));
        }

        if (itemStack.has(HAS_DESCRIPTION)) {
            event.getToolTip().add(Component.translatable(itemStack.getItem().getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY));
        }
    }
}
