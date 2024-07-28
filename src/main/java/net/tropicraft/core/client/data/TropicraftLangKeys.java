package net.tropicraft.core.client.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.tropicraft.Tropicraft;

public enum TropicraftLangKeys {
    NA("general", "na", "N/A"),

    SCUBA_AIR_TIME("scuba", "air_time", "Air Remaining: %s"),
    SCUBA_DIVE_TIME("scuba", "dive_time", "Dive Time: %s"),
    SCUBA_DEPTH("scuba", "depth", "Current Depth: %s"),
    SCUBA_MAX_DEPTH("scuba", "max_depth", "Max Depth: %s"),
    SCUBA_VISIBILITY_STAT("scuba", "scuba.visibility", "Underwater Fog Reduction"),

    EXPLODING_COCONUT_WARNING("item", "exploding_coconut_warning", "You do not have permission to throw this. Change the config option for exploding coconuts to allow this"),

    LEMONADE("drink", "lemonade", "Lemonade"),
    LIMEADE("drink", "limeade", "Limeade"),
    ORANGEADE("drink", "orangeade", "Orangeade"),
    CAIPIRINHA("drink", "caipirinha", "Caipirinha"),
    BLACK_COFFEE("drink", "black_coffee", "Black Coffee"),
    PINA_COLADA("drink", "pina_colada", "Piña Colada"),
    COCONUT_WATER("drink", "coconut_water", "Coconut Water"),
    MAI_TAI("drink", "mai_tai", "Mai Tai"),
    ;

    private final String key;
    private final String value;
    private final Component component;

    TropicraftLangKeys(String type, String key, String value) {
        this.key = Util.makeDescriptionId(type, Tropicraft.location(key));
        this.value = value;
        component = Component.translatable(this.key);
    }

    public String key() {
        return key;
    }

    public Component component() {
        return component;
    }

    public MutableComponent component(ChatFormatting formatting) {
        return component.copy().withStyle(formatting);
    }

    public Component format(Object... args) {
        return Component.translatable(key, args);
    }

    public static void generate(RegistrateLangProvider prov) {
        for (TropicraftLangKeys lang : values()) {
            prov.add(lang.key, lang.value);
        }
    }
}
