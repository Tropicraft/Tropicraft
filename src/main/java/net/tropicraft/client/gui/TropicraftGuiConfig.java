package net.tropicraft.client.gui;

import java.util.stream.Collectors;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.tropicraft.Info;
import net.tropicraft.core.common.config.TropicsConfigs;

public class TropicraftGuiConfig extends GuiConfig {

    public TropicraftGuiConfig(GuiScreen parentScreen) {
        super(
                parentScreen, 
                TropicsConfigs.getConfig().getCategoryNames().stream()
                        .map(TropicsConfigs.getConfig()::getCategory)
                        .map(ConfigElement::new)
                        .collect(Collectors.toList()),
                Info.MODID,
                false, 
                false, 
                I18n.format(Info.MODID + ".config.title")
             );
    }

}
