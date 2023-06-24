package net.tropicraft.core.client.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.Constants;

public enum TropicraftLangKeys {
    
    NA("general", "na", "N/A"),

    SCUBA_AIR_TIME("scuba", "air_time", "Air Remaining: %s"),
    SCUBA_DIVE_TIME("scuba", "dive_time", "Dive Time: %s"),
    SCUBA_DEPTH("scuba", "depth", "Current Depth: %s"),
    SCUBA_MAX_DEPTH("scuba", "max_depth", "Max Depth: %s"),
    SCUBA_VISIBILITY_STAT("scuba", "scuba.visibility", "Underwater Fog Reduction")
    ;

    protected final String key, value;
    private final Component component;

    private TropicraftLangKeys(String type, String key, String value) {
        this.key = net.minecraft.Util.makeDescriptionId(type, new ResourceLocation(Constants.MODID, key));
        this.value = value;
        this.component = Component.translatable(this.key);
    }

    public String getKey() {
        return key;
    }

    public Component getComponent() {
        return component;
    }

    public Component format(Object... args) {
        return Component.translatable(key, args);
    }

    public String getLocalizedText() {
        return getComponent().getString();
    }
    
    protected void register(RegistrateLangProvider prov) {
        prov.add(key, value);
    }

    public static void generate(RegistrateLangProvider prov) {
        for (TropicraftLangKeys lang : values()) {
            lang.register(prov);
        }
    }
}
