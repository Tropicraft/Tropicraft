package net.tropicraft.core.client.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.Constants;

public enum TropicraftLangKeys {
    NA("general", "na", "N/A"),

    SCUBA_AIR_TIME("scuba", "air_time", "Air Remaining: %s"),
    SCUBA_DIVE_TIME("scuba", "dive_time", "Dive Time: %s"),
    SCUBA_DEPTH("scuba", "depth", "Current Depth: %s"),
    SCUBA_MAX_DEPTH("scuba", "max_depth", "Max Depth: %s"),
    SCUBA_VISIBILITY_STAT("scuba", "scuba.visibility", "Underwater Fog Reduction");

    private final String key;
    private final String value;
    private final Component component;

    TropicraftLangKeys(String type, String key, String value) {
        this.key = Util.makeDescriptionId(type, new ResourceLocation(Constants.MODID, key));
        this.value = value;
        this.component = Component.translatable(key);
    }

    public String key() {
        return key;
    }

    public Component component() {
        return component;
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
