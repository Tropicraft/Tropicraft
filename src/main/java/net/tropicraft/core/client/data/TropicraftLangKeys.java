package net.tropicraft.core.client.data;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.tropicraft.Constants;
import net.tropicraft.core.common.Util;

public enum TropicraftLangKeys {

    SCUBA_AIR_TIME("scuba", "air_time", "Air Remaining: %s"),
    SCUBA_DIVE_TIME("scuba", "dive_time", "Dive Time: %s"),
    SCUBA_DEPTH("scuba", "depth", "Current Depth: %s"),
    SCUBA_MAX_DEPTH("scuba", "max_depth", "Max Depth: %s"),
    ;

    private final String key, value;
    private final TranslationTextComponent component;

    private TropicraftLangKeys(String type, String key) {
        this(type, key, Util.toEnglishName(key));
    }

    private TropicraftLangKeys(String type, String key, String value) {
        this.key = net.minecraft.util.Util.makeTranslationKey(type, new ResourceLocation(Constants.MODID, key));
        this.value = value;
        this.component = new TranslationTextComponent(this.key);
    }
    
    public String getKey() {
        return key;
    }

    public TranslationTextComponent getComponent() {
        return component;
    }

    public TranslationTextComponent format(Object... args) {
        return new TranslationTextComponent(getComponent().getKey(), args);
    }

    public String getLocalizedText() {
        return getComponent().getFormattedText();
    }

    public static void generate(TropicraftLangProvider prov) {
        for (TropicraftLangKeys lang : values()) {
            prov.add(lang.key, lang.value);
        }
    }
}
